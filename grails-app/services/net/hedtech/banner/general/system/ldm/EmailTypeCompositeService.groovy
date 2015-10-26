/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
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

    public static final def allowedSortFields = ['code']
    public static final def DEFAULT_SORTED_FIELD = 'code'
    public static final String LDM_NAME_EMAIL_TYPES = 'email-types'
    public static final String DEFAULT_ORDER_TYPE = 'ASC'
    public static final Integer MAX_UPPER_LIMIT = 500
    public final Map sortFieldMap = ['code': 'code']

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
            if (checkPersonEmailType(result?.emailType, entityType?.value)) {
                types << ([(entityType?.value) : ["emailType": result?.emailType]])
            }
        }
    }


    private def checkPersonEmailType(def value, String type) {
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
        params.sort = params?.sort ? sortFieldMap[params.sort] : DEFAULT_SORTED_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
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
            verifyAndSetEntityType(ContactEntityType.ORGANIZATION, emailTypesView, types)
            verifyAndSetEntityType(ContactEntityType.PERSON, emailTypesView, types)
            if (!emailTypesView) {
                GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByGuid(guid)
                if (globalUniqueIdentifier && globalUniqueIdentifier?.ldmName!=LDM_NAME_EMAIL_TYPES) {
                    throw new ApplicationException("emailTypeCompositeService", new BusinessLogicValidationException("invalid.guid", []))
                } else {
                    throw new ApplicationException("emailTypeCompositeService", new NotFoundException())
                }
            }
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
            throw new ApplicationException('emailType', new BusinessLogicValidationException('code.required.message', null))
        }
        if (EmailType.findByCode(content.code) != null) {
            throw new ApplicationException('emailType', new BusinessLogicValidationException('code.exists.message', null))
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
        for (Map.Entry<String, String> entry : content?.get("type")?.entrySet() ){
            types << ([(entry.getKey()) : ["emailType": entry.getValue()?.get("emailType")]])
        }
        return new EmailTypeDetails(types, view)
    }
}