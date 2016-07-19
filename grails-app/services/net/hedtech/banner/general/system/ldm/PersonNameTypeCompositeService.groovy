/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.general.system.ldm.v6.NameTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for PersonNameTypeComposite Service.</p>
 * <p> It will return person name category types of birth , legal and personal </p>
 */
@Transactional
class PersonNameTypeCompositeService extends LdmService {

    def nameTypeService

    /**
     * GET /api/person-name-types
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<NameTypeDecorator> list(Map params) {
        List<NameTypeDecorator> nameTypeList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        Map<String, String> bannerNameTypeToHedmV6NameTypeMap = getBannerNameTypeToHedmV6NameTypeMap()
        if (bannerNameTypeToHedmV6NameTypeMap) {
            params.offset = params.offset ?: 0
            nameTypeService.fetchAllWithGuidByCodeInList(bannerNameTypeToHedmV6NameTypeMap.keySet(), params.max as int, params.offset as int).each {
                NameType nameType = it.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
                nameTypeList << new NameTypeDecorator(globalUniqueIdentifier.guid, nameType.code, nameType.description, bannerNameTypeToHedmV6NameTypeMap.get(nameType.code))
            }
        }
        return nameTypeList
    }


    @Transactional(readOnly = true)
    Long count() {
        return getBannerNameTypeToHedmV6NameTypeMap().size()
    }

    /**
     * GET /api/person-name-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    NameTypeDecorator get(String guid) {
        if (!guid) {
            throw new ApplicationException(PersonNameTypeCompositeService.class, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(PersonNameTypeCompositeService.class, new NotFoundException())
        }

        NameType nameType = nameTypeService.get(globalUniqueIdentifier.domainId)
        if (!nameType) {
            throw new ApplicationException(PersonNameTypeCompositeService.class, new NotFoundException())
        }

        Map<String, String> bannerNameTypeToHedmV6NameTypeMap = getBannerNameTypeToHedmV6NameTypeMap()

        if (!bannerNameTypeToHedmV6NameTypeMap.containsKey(nameType.code)) {
            throw new ApplicationException(PersonNameTypeCompositeService.class, new NotFoundException())
        }

        return new NameTypeDecorator(globalUniqueIdentifier.guid, nameType.code, nameType.description, bannerNameTypeToHedmV6NameTypeMap.get(nameType.code))
    }


    def getBannerNameTypeToHedmV3NameTypeMap() {
        return getBannerNameTypeToHedmNameTypeMap(GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING, GeneralValidationCommonConstants.VERSION_V3)
    }


    def getBannerNameTypeToHedmV6NameTypeMap() {
        return getBannerNameTypeToHedmNameTypeMap(GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING, GeneralValidationCommonConstants.VERSION_V6)
    }


    def getNameTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = nameTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                NameType nameType = it.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
                codeToGuidMap.put(nameType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }


    private def getBannerNameTypeToHedmNameTypeMap(String settingName, String version) {
        Map<String, String> bannerNameTypeToHedmNameTypeMap = [:]
        List<IntegrationConfiguration> intConfs = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (intConfs) {
            List<NameType> entities = nameTypeService.fetchAllByCodeInList(intConfs.value)
            intConfs.each {
                NameTypeCategory nameTypeCategory = NameTypeCategory.getByString(it.translationValue, version)
                if (entities.code.contains(it.value) && nameTypeCategory) {
                    bannerNameTypeToHedmNameTypeMap.put(it.value, nameTypeCategory.versionToEnumMap[version])
                } else {
                    NameTypeCategory nameTypeCategoryInOtherVersion = NameTypeCategory.getByString(it.translationValue)
                    if ((nameTypeCategory == null && nameTypeCategoryInOtherVersion == null) || (nameTypeCategory != null && !entities.code.contains(it.value))) {
                        throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
                    }
                }
            }
        } else {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerNameTypeToHedmNameTypeMap
    }

}
