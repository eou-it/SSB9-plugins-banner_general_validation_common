/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
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
    def list(Map params) {
        List<NameTypeDecorator> nameTypeList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        nameTypeService.fetchAll(params).each {
            nameTypeList << new NameTypeDecorator(it.getAt(0), it.getAt(1), it.getAt(2), it.getAt(3))
        }
        return nameTypeList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        return nameTypeService.count(params)
    }

    /**
     * GET /api/person-name-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    NameTypeDecorator get(String guid) {
        List nameType = nameTypeService.fetchByGuid(guid)
        if (nameType) {
            return new NameTypeDecorator(nameType.getAt(0), nameType.getAt(1), nameType.getAt(2), nameType.getAt(3))
        } else {
            throw new ApplicationException(PersonNameTypeCompositeService.class, new NotFoundException())
        }
    }


    def getBannerNameTypeToHEDMNameTypeMap() {
        def bannerNameTypeToHedmNameTypeMap = [:]
        List<IntegrationConfiguration> intConfs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING)
        if (intConfs) {
            intConfs.each {
                NameType nameType = NameType.findByCode(it.value)
                NameTypeCategory nameTypeCategory = NameTypeCategory.getByString(it.translationValue)
                if (nameType && nameTypeCategory) {
                    bannerNameTypeToHedmNameTypeMap.put(nameType.code, nameTypeCategory)
                }
            }
        }
        return bannerNameTypeToHedmNameTypeMap
    }

}
