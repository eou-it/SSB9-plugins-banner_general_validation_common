/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.PhoneTypeView
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.system.ldm.v4.PhoneType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Phone Type Composite Service.It will return phone types of person and organization. 
 * If phone type code was configure for both person and organization, then we are considering person as a 1st preference.</p>
 */
@Transactional
class PhoneTypeCompositeService extends LdmService {

    private static final String DEFAULT_SORT_FIELD = 'code'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List<String> allowedSortFields = [DEFAULT_SORT_FIELD]
    private static final String LDM_NAME ='phone-types'

    def telephoneTypeService

    /**
     * GET /api/phone-types
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    def  list(Map params) {
        List<PhoneType> phoneTypeList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ?: DEFAULT_SORT_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        getFetchPhoneDetails(false, params).each { phoneTypeViewRecord ->
            Map<String, String> types = [:]
            types << ([(phoneTypeViewRecord.entityType) : ["phoneType": phoneTypeViewRecord.phoneType]])
            phoneTypeList << new PhoneType(phoneTypeViewRecord,types)
        }
        return phoneTypeList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        getFetchPhoneDetails(true,[:])
    }

    /**
     * GET /api/phone-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    PhoneType get(String guid) {
        PhoneTypeView  phoneTypeViewRecord = PhoneTypeView.get(guid?.trim())
        if (phoneTypeViewRecord) {
            Map<String, String> types = [:]
            types << ([(phoneTypeViewRecord.entityType) : ["phoneType": phoneTypeViewRecord.phoneType]])
           return new PhoneType(phoneTypeViewRecord,types)
           } else {
            GlobalUniqueIdentifier globalUniqueIdentifier=GlobalUniqueIdentifier.findByGuid(guid?.trim())
            if(globalUniqueIdentifier && globalUniqueIdentifier?.ldmName!=LDM_NAME) {
                throw new RestfulApiValidationException("phoneType.invalidGuid")
            }else {
                throw new ApplicationException("phoneType", new NotFoundException())
            }
        }
    }

    /**
     * POST /api/phone-types
     *
     * @param content Request body
     * @return PhoneType detail object post creating the record
     */
    def create(Map content) {
        if (!content?.code) {
            throw new ApplicationException('phoneType', new BusinessLogicValidationException('code.required.message', null))
        }
        TelephoneType telephoneType = TelephoneType.findByCode(content?.code?.trim())
        if (telephoneType) {
            throw new ApplicationException("phoneType", new BusinessLogicValidationException("code.exists.message", null))
        }
        telephoneType = bindTelephoneType(new TelephoneType(), content)
        String telephoneTypeGuid = content?.id?.trim()?.toLowerCase()
        if (telephoneTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(telephoneType.id, telephoneTypeGuid, LDM_NAME)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, telephoneType?.id)
            telephoneTypeGuid = globalUniqueIdentifier.guid
        }
        log.debug("GUID: ${telephoneTypeGuid}")
      return getDecorator(telephoneType,content,telephoneTypeGuid)
    }

    private bindTelephoneType(TelephoneType telephoneType,Map content){
        super.bindData(telephoneType, content, [:])
        telephoneTypeService.createOrUpdate(telephoneType)
    }

    /**
     * Populating the decorator class with the response as per schema.
     */
    private def getDecorator(TelephoneType telephoneType, Map content, String telephoneTypeGuid) {
        PhoneTypeView view = new PhoneTypeView()
        Map<String, String> types = [:]
        view.setCode(telephoneType.code)
        view.setDescription(telephoneType.description)
        view.setId(telephoneTypeGuid)
        for (Map.Entry<String, String> entry : content?.get("type")?.entrySet() ){
            types << ([(entry.getKey()) : ["phoneType": entry.getValue()?.get("phoneType")]])
        }
        return new PhoneType(view, types)
    }

    private def getFetchPhoneDetails(Boolean count, Map params) {
        if (count) {
          return  PhoneTypeView.count()
        } else {
          return  PhoneTypeView.list(params)
        }
    }

}
