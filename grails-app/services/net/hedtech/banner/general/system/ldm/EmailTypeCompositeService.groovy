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
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.EmailTypesView
import net.hedtech.banner.general.system.ldm.v4.ContactEntityType
import net.hedtech.banner.general.system.ldm.v4.EmailTypeDetails
import net.hedtech.banner.general.system.ldm.v4.OrganizationEmailType
import net.hedtech.banner.general.system.ldm.v4.PersonEmailType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> Service to retrive the data from a email validation table </p>
 */
@Transactional
class EmailTypeCompositeService extends LdmService {

    public static final def allowedSortFields = [GeneralValidationCommonConstants.CODE]
    public static final String LDM_NAME_EMAIL_TYPES = 'email-types'
    public static final Integer MAX_UPPER_LIMIT = 500

    def emailTypeService

    /**
     * GET /api/academic-honors
     * @return List
     */
    @Transactional(readOnly = true)
    List<EmailTypeDetails> list(Map params) {
        List emailTypes = []
        List<EmailTypesView> results = fetchEmailTypes(params)

        results.each {
            result ->
                Map<String, String> types = [:]
                verifyAndSetEntityType(ContactEntityType.ORGANIZATION, result, types)
                verifyAndSetEntityType(ContactEntityType.PERSON, result, types)
                if (types) {
                    emailTypes << new EmailTypeDetails(types, result)
                }
        }
        emailTypes
    }

    private verifyAndSetEntityType(ContactEntityType entityType, def result, Map types) {
        if (result?.entityType == entityType?.value?.toUpperCase()) {
            if (checkEmailType(result?.emailType, entityType?.value)) {
                types << ([(entityType?.value) : ["emailType": result?.emailType]])
            }
        }
    }


    private def checkEmailType(def value, String type) {
        if (type.equalsIgnoreCase(ContactEntityType.PERSON?.value)) {
            for (PersonEmailType emailType : PersonEmailType.values()) {
                if (emailType?.value == value) {
                    return true
                }
            }
        } else if (type.equalsIgnoreCase(ContactEntityType.ORGANIZATION?.value)) {
            for (OrganizationEmailType emailType : OrganizationEmailType.values()) {
                if (emailType?.value == value) {
                    return true
                }
            }
        }

        return false
    }

    private def fetchEmailTypes(params) {
        def results
        RestfulApiValidationUtility.validateSortField(params?.sort, allowedSortFields)
        RestfulApiValidationUtility.correctMaxAndOffset(params, 0, MAX_UPPER_LIMIT)
        params.sort = params?.sort ?: GeneralValidationCommonConstants.DEFAULT_SORT_FIELD_CODE
        params?.order = params?.order ?: GeneralValidationCommonConstants.DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortOrder(params.order)
        results = EmailTypesView.list(params)

        results
    }

    /**
     *
     * @return Long value as total count
     */
    Long count(params) {
        return EmailTypesView.count();
    }

    /**
     * GET /api/email-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    EmailTypeDetails get(String guid) {
        EmailTypesView emailTypesView = null
        Map<String, String> types = [:]
        if (guid) {
            emailTypesView = EmailTypesView.findByGuid(guid)
            if(!emailTypesView){
                throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
            }
            verifyAndSetEntityType(ContactEntityType.ORGANIZATION, emailTypesView, types)
            verifyAndSetEntityType(ContactEntityType.PERSON, emailTypesView, types)
            new EmailTypeDetails(types, emailTypesView)
        }
    }

    /**
     * POST /api/email-types
     *
     * @param content Request body
     * @return EmailTypeDetails object post creating the record
     */
    def create(Map content) {
        if (!(content?.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        if (EmailType.findByCode(content.code) != null) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }
        EmailType emailType = bindEmailType(new EmailType(), content)
        String emailGuid = content?.id?.trim()?.toLowerCase()
        if (emailGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            emailGuid = updateGuidValue(emailType.id, emailGuid, LDM_NAME_EMAIL_TYPES)?.guid
        } else {
            emailGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME_EMAIL_TYPES, emailType?.id)?.guid
        }
        return getDecorator(emailType, content, emailGuid)
    }


    /**
     * PUT /api/email-types/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String emailGuid = content?.id?.trim()?.toLowerCase()
        if (!emailGuid) {
            throw new ApplicationException("emailType", new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME_EMAIL_TYPES, emailGuid)

        if (!globalUniqueIdentifier) {
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }
        EmailType emailType = EmailType.findById(globalUniqueIdentifier?.domainId)
        if (!emailType) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }
        // Should not allow to update EmailType.code as it is read-only
        if (emailType?.code != content?.code?.trim()) {
            content.put(GeneralValidationCommonConstants.CODE, emailType.code)
        }
        emailType = bindEmailType(emailType, content)

        return getDecorator(emailType, content, emailGuid)
    }

    /**
     * Invoking the LDM service to bind map properties onto grails domains.
     * Invoking the ServiceBase to creates or updates a model instance provided within the supplied domainModelOrMap.
     */
    private def bindEmailType(EmailType emailType, Map content) {
        emailType.displayWebIndicator = Boolean.FALSE
        emailType.urlIndicator = Boolean.FALSE
        super.bindData(emailType, content, [:])
        emailTypeService.createOrUpdate(emailType)
    }


    /**
     * Populating the decorator class with the response as per schema.
     */
    private def getDecorator(EmailType emailType, Map content, String emailGuid = null) {
        EmailTypesView view = new EmailTypesView()
        Map<String, String> types = [:]
        view.setCode(emailType.code)
        view.setDescription(emailType.description)
        view.setGuid(emailGuid)
        for (Map.Entry<String, String> entry : content?.get(GeneralValidationCommonConstants.TYPE)?.entrySet() ){
            types << ([(entry.getKey()) : ["emailType": entry.getValue()?.get("emailType")]])
        }
        return new EmailTypeDetails(types, view)
    }
}