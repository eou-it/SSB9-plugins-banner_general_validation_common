/*********************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.ldm.v6.AddressTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Address Type Service.</p>
 * <p> It will return address types of person and organization.</p>
 * <p> If address type code was configure for both person and organization, then priority is given to person.</p>
 */
@Transactional
class AddressTypeCompositeService extends LdmService {


    def addressTypeService

    /**
     * GET /api/address-types
     * @param params
     * @return List
     */
    @Transactional(readOnly = true)
    List<AddressTypeDecorator> list(Map params) {
        List<AddressTypeDecorator> addressTypes = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        Map<String, String> bannerAddressTypeToHedmAddressTypeMap = getBannerAddressTypeToHedmV6AddressTypeMap()
        if (bannerAddressTypeToHedmAddressTypeMap) {
            params.offset = params.offset ?: 0
            addressTypeService.fetchAllWithGuidByCodeInList(bannerAddressTypeToHedmAddressTypeMap.keySet(), params.max as int, params.offset as int).each { result ->
                AddressType addressType = result.addressType
                GlobalUniqueIdentifier globalUniqueIdentifier = result.globalUniqueIdentifier
                addressTypes << new AddressTypeDecorator(addressType.code, addressType.description, globalUniqueIdentifier.guid, bannerAddressTypeToHedmAddressTypeMap.get(addressType.code))
            }
        }
        return addressTypes
    }

    /**
     * @return Long value as total count
     */
    @Transactional(readOnly = true)
    Long count() {
        return getBannerAddressTypeToHedmV6AddressTypeMap().size()
    }

    /**
     * GET /api/address-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AddressTypeDecorator get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new NotFoundException())
        }

        AddressType addressType = addressTypeService.get(globalUniqueIdentifier.domainId)
        if (!addressType) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new NotFoundException())
        }

        Map<String, String> bannerAddressTypeToHedmAddressTypeMap = getBannerAddressTypeToHedmV6AddressTypeMap()
        if (!bannerAddressTypeToHedmAddressTypeMap.containsKey(addressType.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new NotFoundException())
        }

        return new AddressTypeDecorator(addressType.code, addressType.description, globalUniqueIdentifier.guid, bannerAddressTypeToHedmAddressTypeMap.get(addressType.code))

    }

    /**
     * POST /api/address-types
     *
     * @param content Request body
     */
    def create(Map content) {
        if (!content?.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        AddressType addressType = addressTypeService.fetchByCode(content?.code?.trim())
        if (addressType) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }
        addressType = bindaddressType(new AddressType(), content)
        String addressTypeGuid = content?.id?.trim()?.toLowerCase()
        if (addressTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(addressType.id, addressTypeGuid, GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME)
        } else {
            addressTypeGuid = (globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME, addressType?.id)).guid
        }
        log.debug("GUID: ${addressTypeGuid}")

        return new AddressTypeDecorator(addressType.code, addressType.description, addressTypeGuid, content.addressType)
    }

    /**
     * PUT /api/address-types/<id>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String addressTypeGuid = content?.id?.trim()?.toLowerCase()
        if (!addressTypeGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME, addressTypeGuid)
        if (!globalUniqueIdentifier) {
            //Per strategy when a ID was provided, the create should happen.
            return create(content)
        }
        if (!content?.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        AddressType addressType = addressTypeService.get(globalUniqueIdentifier?.domainId)
        if (!addressType) {
            throw new ApplicationException(GeneralValidationCommonConstants.ADDRESS_TYPE, new NotFoundException())
        }
        // Should not allow to update addressType code as it is read-only
        if (addressType.code != content?.code?.trim()) {
            content.put(GeneralValidationCommonConstants.CODE, addressType.code)
        }
        addressType = bindaddressType(addressType, content)

        return new AddressTypeDecorator(addressType.code, addressType.description, addressTypeGuid, content.addressType)
    }

    /**
     * Invoking the LDM service to bind map properties onto grails domains.
     * Invoking the ServiceBase to creates or updates a model instance provided within the supplied domainModelOrMap.
     */
    def bindaddressType(AddressType addressType, Map content) {
        bindData(addressType, content, [:])
        addressTypeService.createOrUpdate(addressType)
    }


    def getAddressTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = addressTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                AddressType addressType = it.addressType
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(addressType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }


    def getBannerAddressTypeToHedmV3AddressTypeMap() {
        return getBannerAddressTypeToHEDMAddressTypeMap(GeneralValidationCommonConstants.ADDRESS_TYPE_SETTING_NAME_V3, GeneralValidationCommonConstants.VERSION_V3)
    }


    def getBannerAddressTypeToHedmV6AddressTypeMap() {
        return getBannerAddressTypeToHEDMAddressTypeMap(GeneralValidationCommonConstants.ADDRESS_TYPE_SETTING_NAME_V6, GeneralValidationCommonConstants.VERSION_V6)
    }


    private def getBannerAddressTypeToHEDMAddressTypeMap(String settingName, String version) {
        Map<String, String> bannerAddressTypeToHedmAddressTypeMap = [:]
        List<IntegrationConfiguration> integrationConfigurationList = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (integrationConfigurationList) {
            List<AddressType> addressTypeList = addressTypeService.fetchAllByCodeInList(integrationConfigurationList.value)
            integrationConfigurationList.each {
                HedmAddressType hedmAddressType = HedmAddressType.getByString(it.translationValue, version)
                if (addressTypeList.code.contains(it.value) && hedmAddressType) {
                    bannerAddressTypeToHedmAddressTypeMap.put(it.value, hedmAddressType.versionToEnumMap[version])
                }
            }
        }
        return bannerAddressTypeToHedmAddressTypeMap
    }

}
