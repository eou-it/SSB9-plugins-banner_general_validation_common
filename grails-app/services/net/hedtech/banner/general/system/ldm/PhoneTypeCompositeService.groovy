/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.system.ldm.v4.PhoneTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Phone Type Composite Service.It will return phone types of person and organization. 
 * If phone type code was configure for both person and organization, then we are considering person as a 1st preference.</p>
 */
@Transactional
class PhoneTypeCompositeService extends LdmService {

    def telephoneTypeService

    /**
     * GET /api/phone-types
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    def list(Map params) {
        List<PhoneTypeDecorator> phoneTypeList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        Map<String, String> bannerPhoneTypeToHedmPhoneTypeMap = getBannerPhoneTypeToHedmV6PhoneTypeMap()
        if (bannerPhoneTypeToHedmPhoneTypeMap) {
            params.offset = params.offset ?: 0
            telephoneTypeService.fetchAllWithGuidByCodeInList(bannerPhoneTypeToHedmPhoneTypeMap.keySet(), params.max as Integer, params.offset as Integer).each { result ->
                TelephoneType telephoneType = result.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = result.getAt(1)
                phoneTypeList << new PhoneTypeDecorator(telephoneType.code, telephoneType.description, globalUniqueIdentifier.guid, bannerPhoneTypeToHedmPhoneTypeMap.get(telephoneType.code))
            }
        }
        return phoneTypeList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count() {
        return getBannerPhoneTypeToHedmV6PhoneTypeMap().size()
    }

    /**
     * GET /api/phone-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    PhoneTypeDecorator get(String guid) {
        if (!guid) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }

        TelephoneType telephoneType = telephoneTypeService.get(globalUniqueIdentifier.domainId)
        if (!telephoneType) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }

        Map<String, String> bannerPhoneTypeToHedmV6PhoneTypeMap = getBannerPhoneTypeToHedmV6PhoneTypeMap()
        if (!bannerPhoneTypeToHedmV6PhoneTypeMap.containsKey(telephoneType.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }
        return new PhoneTypeDecorator(telephoneType.code, telephoneType.description, globalUniqueIdentifier.guid, bannerPhoneTypeToHedmV6PhoneTypeMap.get(telephoneType.code))
    }

    /**
     * POST /api/phone-types
     *
     * @param content Request body
     * @return PhoneType detail object post creating the record
     */
    def create(Map content) {
        if (!content.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        TelephoneType telephoneType = telephoneTypeService.fetchByCode(content.code?.trim())
        if (telephoneType) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }
        telephoneType = bindTelephoneType(new TelephoneType(), content)
        String telephoneTypeGuid = content.id?.trim()?.toLowerCase()
        if (telephoneTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(telephoneType.id, telephoneTypeGuid, GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME)
        } else {
            telephoneTypeGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME, telephoneType.id).guid
        }
        log.debug("GUID: ${telephoneTypeGuid}")
        return new PhoneTypeDecorator(telephoneType.code, telephoneType.description, telephoneTypeGuid, content.phoneType)
    }

    /**
     * PUT /api/phone-types/{id}*
     * @param content Request body
     * @return PhoneType detail object put updating the record
     */
    def update(Map content) {
        String telephoneTypeGuid = content.id?.trim().toLowerCase()
        if (!telephoneTypeGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME, telephoneTypeGuid)
        if (!globalUniqueIdentifier) {
            return create(content)
        }
        TelephoneType telephoneType = telephoneTypeService.get(globalUniqueIdentifier.domainId)
        if (!telephoneType) {
            throw new ApplicationException(GeneralValidationCommonConstants.PHONE_ENTITY_TYPE, new NotFoundException())
        }
        // Should not allow to update TelephoneType.code as it is read-only
        if (telephoneType.code != content.code?.trim()) {
            content.code = telephoneType?.code
        }
        telephoneType = bindTelephoneType(telephoneType, content)
        return new PhoneTypeDecorator(telephoneType.code, telephoneType.description, telephoneTypeGuid, content.phoneType)
    }

    def getPhoneTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = telephoneTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                TelephoneType phoneType = it.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
                codeToGuidMap.put(phoneType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }

    def getBannerPhoneTypeToHedmV3PhoneTypeMap() {
        return getBannerPhoneTypeToHedmPhoneTypeMap(GeneralValidationCommonConstants.PHONE_TYPE_SETTING_NAME_V3, GeneralValidationCommonConstants.VERSION_V3)
    }

    def getBannerPhoneTypeToHedmV6PhoneTypeMap() {
        return getBannerPhoneTypeToHedmPhoneTypeMap(GeneralValidationCommonConstants.PHONE_TYPE_SETTING_NAME_V6, GeneralValidationCommonConstants.VERSION_V6)
    }

    /**
     * Binding telephoneType content into TelephoneType saving data into DB
     * @param telephoneType
     * @param content
     * @return
     */
    private bindTelephoneType(TelephoneType telephoneType, Map content) {
        super.bindData(telephoneType, content, [:])
        telephoneTypeService.createOrUpdate(telephoneType)
    }

    private def getBannerPhoneTypeToHedmPhoneTypeMap(String settingName, String version) {
        Map<String, String> bannerPhoneTypeToHedmPhoneTypeMap = [:]
        List<IntegrationConfiguration> intConfs = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (intConfs) {
            List<TelephoneType> entities = telephoneTypeService.fetchAllByCodeInList(intConfs.value)
            intConfs.each {
                HedmPhoneType hedmPhoneType = HedmPhoneType.getByString(it.translationValue, version)
                if (entities.code.contains(it.value) && hedmPhoneType) {
                    bannerPhoneTypeToHedmPhoneTypeMap.put(it.value, hedmPhoneType.versionToEnumMap[version])
                } else {
                    throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
                }
            }
        } else {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerPhoneTypeToHedmPhoneTypeMap
    }

}
