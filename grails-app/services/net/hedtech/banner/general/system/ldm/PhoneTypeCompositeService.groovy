/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.PhoneTypeView
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v4.PhoneType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Phone Type Composite Service.It will return phone types of person and organization. 
 * If phone type code was configure for both person and organization, then we are considering person as a 1st preference.</p>
 */
class PhoneTypeCompositeService {

    //This filed is used for only to create and update of phone type data
    def  telephoneTypeService

    private static final String DEFAULT_SORT_FIELD = 'code'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List<String> allowedSortFields = [DEFAULT_SORT_FIELD]
    private static final String LDM_NAME ='phone-types'


    /**
     * GET /api/phone-types
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    def  list(Map params) {
        List<PhoneType> phoneTypeList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ? params?.sort : DEFAULT_SORT_FIELD
        params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        getDataFromDB(false, params).each { phoneTypeView ->
            phoneTypeList << new PhoneType(phoneTypeView, new Metadata(phoneTypeView.dataOrigin), phoneTypeView.phoneType, phoneTypeView.translationValue)
        }
        return phoneTypeList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        getDataFromDB(true,[:])
    }

    /**
     * GET /api/phone-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    PhoneType get(String guid) {
        PhoneTypeView  phoneTypeView = PhoneTypeView.get(guid?.trim())
        if (phoneTypeView) {
            new PhoneType(phoneTypeView, new Metadata(phoneTypeView.dataOrigin), phoneTypeView.phoneType, phoneTypeView.translationValue)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier=GlobalUniqueIdentifier.findByGuid(guid?.trim())
            if(globalUniqueIdentifier && globalUniqueIdentifier?.ldmName!=LDM_NAME) {
                throw new RestfulApiValidationException("phoneType.invalidGuid")
            }else {
                throw new ApplicationException("phoneType", new NotFoundException())
            }
        }
    }

    private def getDataFromDB(Boolean count, Map params) {
        if (count) {
            PhoneTypeView.count()
        } else {
            PhoneTypeView.list(params)
        }
    }
    
}
