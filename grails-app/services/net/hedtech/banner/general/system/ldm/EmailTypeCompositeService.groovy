/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
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
class EmailTypeCompositeService {

    public static final def allowedSortFields = ['code']
    public static final def DEFAULT_SORTED_FIELD = 'code'
    public static final String LDM_NAME_EMAIL_TYPES = 'email-types'
    public static final String DEFAULT_ORDER_TYPE = 'ASC'
    public static final Integer MAX_UPPER_LIMIT = 500
    public final Map sortFieldMap = ['code': 'code']

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
                boolean isValid = false
                def types = [:]

                if (result.entityType == ContactEntityType.ORGANIZATION?.value?.toUpperCase()) {
                    types.put('entityType', ContactEntityType.ORGANIZATION.value)
                    if (checkPersonEmailType(result?.emailType, ContactEntityType.ORGANIZATION?.value)) {
                        types.put('emailType', result?.emailType)
                        isValid = true
                    }

                } else if (result.entityType == ContactEntityType.PERSON?.value?.toUpperCase()) {
                    types.put('entityType', ContactEntityType.PERSON?.value)
                    if (checkPersonEmailType(result?.emailType, ContactEntityType.PERSON?.value)) {
                        types.put('emailType', result?.emailType)
                        isValid = true
                    }

                }
                if (isValid) {
                    emailTypes << new EmailTypeDetails(types, result)
                }


        }
        emailTypes
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
        def types = [:]
        if (guid) {
            emailTypesView = EmailTypesView.findByGuid(guid)

            if (emailTypesView?.entityType == ContactEntityType.ORGANIZATION?.value?.toUpperCase()) {
                types.put('entityType', ContactEntityType.ORGANIZATION?.value)
                if (checkPersonEmailType(emailTypesView?.emailType, ContactEntityType.ORGANIZATION?.value)) {
                    types.put('emailType', emailTypesView?.emailType)
                }

            } else if (emailTypesView?.entityType == ContactEntityType.PERSON?.value?.toUpperCase()) {
                types.put('entityType', ContactEntityType.PERSON?.value)
                if (checkPersonEmailType(emailTypesView?.emailType, ContactEntityType.PERSON?.value)) {
                    types.put('emailType', emailTypesView.emailType)
                }

            }
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
}