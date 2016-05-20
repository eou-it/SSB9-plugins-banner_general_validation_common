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
import net.hedtech.banner.general.system.EmailTypeReadOnly
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

    def emailTypeService
    def emailTypeReadOnlyService

    /**
     * GET /api/eamil-types
     * @return List
     */
    @Transactional(readOnly = true)
    List<EmailTypeDetails> list(Map params) {
        List<EmailTypeDetails> emailTypes = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
          emailTypeReadOnlyService.fetchAll(params).each {result ->
                    emailTypes << new EmailTypeDetails(result)
             }
        emailTypes
    }

    /**
     *
     * @return Long value as total count
     */
    Long count() {
        return emailTypeReadOnlyService.fetchCountAll()
    }

    /**
     * GET /api/email-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    EmailTypeDetails get(String guid) {
        EmailTypeReadOnly emailTypesView = emailTypeReadOnlyService.fetchByGuid(guid)
            if(!emailTypesView){
                throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
            }
         return   new EmailTypeDetails(emailTypesView)
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
            emailGuid = updateGuidValue(emailType.id, emailGuid, GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES)?.guid
        } else {
            emailGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES, emailType?.id)?.guid
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
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES, emailGuid)

        if (!globalUniqueIdentifier) {
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }
        EmailType emailType = emailTypeService.get(globalUniqueIdentifier?.domainId)
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
        EmailTypeReadOnly view = new EmailTypeReadOnly()
        view.setCode(emailType.code)
        view.setDescription(emailType.description)
        view.setId(emailGuid)
        view.setEmailType(content.emailType)
        return new EmailTypeDetails(view)
    }
}