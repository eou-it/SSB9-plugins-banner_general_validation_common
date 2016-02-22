/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.PhoneType
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

    private static final String DEFAULT_SORT_FIELD = 'code'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List<String> allowedSortFields = [DEFAULT_SORT_FIELD]
    private static final String LDM_NAME ='phone-types'
    private static final String ENTITY_TYPE = 'phoneType'

    def telephoneTypeService
    def phoneTypeService


    /**
     * GET /api/phone-types
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    def  list(Map params) {
        List<PhoneTypeDecorator> phoneTypeList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ?: DEFAULT_SORT_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        getFetchPhoneDetails(false, params).each { phoneTypeViewRecord ->
            Map<String, String> types = [:]
            types << ([(phoneTypeViewRecord.entityType) : ["$ENTITY_TYPE": phoneTypeViewRecord.phoneType]])
            phoneTypeList << new PhoneTypeDecorator(phoneTypeViewRecord,types)
        }
        return phoneTypeList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
       return getFetchPhoneDetails(true,params)
    }

    /**
     * GET /api/phone-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    PhoneTypeDecorator get(String guid) {
        PhoneType  phoneTypeViewRecord = phoneTypeService.fetchByGuid(guid?.trim())
        if (phoneTypeViewRecord) {
            Map<String, String> types = [:]
            types << ([(phoneTypeViewRecord.entityType) : ["$ENTITY_TYPE": phoneTypeViewRecord.phoneType]])
           return new PhoneTypeDecorator(phoneTypeViewRecord,types)
           } else {
                throw new ApplicationException(ENTITY_TYPE, new NotFoundException())
            }
    }

    /**
     * POST /api/phone-types
     *
     * @param content Request body
     * @return PhoneType detail object post creating the record
     */
    def create(Map content) {
        if (!content.code) {
            throw new ApplicationException(ENTITY_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        TelephoneType telephoneType = telephoneTypeService.fetchByCode(content.code?.trim())
        if (telephoneType) {
            throw new ApplicationException(ENTITY_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }
        telephoneType = bindTelephoneType(new TelephoneType(), content)
        String telephoneTypeGuid = content.id?.trim()?.toLowerCase()
        if (telephoneTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(telephoneType.id, telephoneTypeGuid, LDM_NAME)
        } else {
            telephoneTypeGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(LDM_NAME, telephoneType?.id)?.guid
        }
        log.debug("GUID: ${telephoneTypeGuid}")
      return getDecorator(telephoneType,content,telephoneTypeGuid)
    }

    /**
     * PUT /api/phone-types/{id}
     *
     * @param content Request body
     * @return PhoneType detail object put updating the record
     */
    def update(Map content){
        String telephoneTypeGuid = content.id?.trim().toLowerCase()
        if (!telephoneTypeGuid) {
            throw new ApplicationException(ENTITY_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(LDM_NAME, telephoneTypeGuid)
        if (!globalUniqueIdentifier) {
            return create(content)
        }
        TelephoneType telephoneType = telephoneTypeService.get(globalUniqueIdentifier.domainId)
        if (!telephoneType) {
            throw new ApplicationException(ENTITY_TYPE, new NotFoundException())
        }
        // Should not allow to update TelephoneType.code as it is read-only
        if (telephoneType.code != content.code?.trim()) {
            content.code = telephoneType?.code
        }
        telephoneType = bindTelephoneType(telephoneType, content)
        return getDecorator(telephoneType, content, telephoneTypeGuid)
    }

    /**
     * Binding telephoneType content into TelephoneType saving data into DB
     * @param telephoneType
     * @param content
     * @return
     */
    private bindTelephoneType(TelephoneType telephoneType,Map content){
        super.bindData(telephoneType, content, [:])
        telephoneTypeService.createOrUpdate(telephoneType)
    }

    /**
     * Populating the decorator class with the response as per schema.
     */
    private def getDecorator(TelephoneType telephoneType, Map content, String telephoneTypeGuid) {
        PhoneType phoneType = new PhoneType()
        Map<String, String> types = [:]
        phoneType.setCode(telephoneType.code)
        phoneType.setDescription(telephoneType.description)
        phoneType.setId(telephoneTypeGuid)
        for (Map.Entry<String, String> entry : content?.get(GeneralValidationCommonConstants.TYPE)?.entrySet() ){
            types << ([(entry.getKey()) : ["$ENTITY_TYPE": entry.getValue()?.get(ENTITY_TYPE)]])
        }
        return new PhoneTypeDecorator(phoneType, types)
    }

    private def getFetchPhoneDetails(Boolean count, Map params) {
        if (count) {
          return  phoneTypeService.count(params)
        } else {
          return  phoneTypeService.list(params)
        }
    }

}
