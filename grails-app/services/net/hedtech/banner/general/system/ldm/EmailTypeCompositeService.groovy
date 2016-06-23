/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.ldm.v4.EmailTypeDetails
import net.hedtech.banner.general.system.ldm.v6.EmailTypeEnum
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> Service to retrive the data from a email validation table </p>
 */
@Transactional
class EmailTypeCompositeService extends LdmService {

    def emailTypeService

    /**
     * GET /api/email-types
     * @return List
     */
    @Transactional(readOnly = true)
    List<EmailTypeDetails> list(Map params) {
        List<EmailTypeDetails> emailTypes = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        Map<String, String> mappedCodeInfo = getBannerEmailTypeToHEDMEmailTypeMap(EmailTypeEnum.EMAIL_TYPE)
        if (!mappedCodeInfo.isEmpty()) {
            params.offset = params.offset ?: 0
            emailTypeService.fetchByEmailTypeCodes(mappedCodeInfo.keySet(), params.max as Integer, params.offset as Integer).each { result ->
                EmailType emailType = result.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = result.getAt(1)
                emailTypes << new EmailTypeDetails(emailType.code, emailType.description, globalUniqueIdentifier.guid, mappedCodeInfo.get(emailType.code))
            }
        }
        return emailTypes
    }

    /**
     *
     * @return Long value as total count
     */
    Long count() {
        Map<String, String> mappedCodeInfo = getBannerEmailTypeToHEDMEmailTypeMap(EmailTypeEnum.EMAIL_TYPE)
        if(!mappedCodeInfo.isEmpty()){
            return emailTypeService.countByEmailTypeCodes(mappedCodeInfo.keySet())
        }else {
            return 0L
        }

    }

    /**
     * GET /api/email-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    EmailTypeDetails get(String guid) {
        if (!guid) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        EmailType emailType = emailTypeService.get(globalUniqueIdentifier.domainId)
        if (!emailType) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        IntegrationConfiguration integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME, emailType.code)
        if (!integrationConfiguration || !EmailTypeEnum.EMAIL_TYPE.contains(integrationConfiguration.translationValue)) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        return new EmailTypeDetails(emailType.code, emailType.description, globalUniqueIdentifier.guid, integrationConfiguration.translationValue)

    }

    /**
     * POST /api/email-types
     *
     * @param content Request body
     * @return EmailTypeDetails object post creating the record
     */
    def create(Map content) {
        if (!(content.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        if (EmailType.findByCode(content.code) != null) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }
        EmailType emailType = bindEmailType(new EmailType(), content)
        String emailGuid = content?.id?.trim()?.toLowerCase()
        if (emailGuid && emailGuid!=GeneralValidationCommonConstants.NIL_GUID) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            emailGuid = updateGuidValue(emailType.id, emailGuid, GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES)?.guid
        } else {
            emailGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES, emailType?.id)?.guid
        }
        return new EmailTypeDetails(emailType.code, emailType.description, emailGuid, content.emailType)
    }


    /**
     * PUT /api/email-types/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String emailGuid = content.id?.trim()?.toLowerCase()
        if (!emailGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.LDM_NAME_EMAIL_TYPES, emailGuid)

        if (!globalUniqueIdentifier) {
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }
        EmailType emailType = emailTypeService.get(globalUniqueIdentifier?.domainId)
        if (!emailType) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }
        // Should not allow to update EmailType.code as it is read-only
        if (emailType.code != content.code?.trim()) {
            content.put(GeneralValidationCommonConstants.CODE, emailType.code)
        }
        emailType = bindEmailType(emailType, content)

      return  new EmailTypeDetails(emailType.code, emailType.description, emailGuid, content.emailType)
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
     * Fetch All Email Type details and put into Map for person v6
     * @param codes
     * @return Map
     */
    public Map<String, List> fetchAllMappedEmailTypes() {
        Map emailTypeMap = [:]
        List emailTypeInfo = []
        Map<String, String> mappedCodeInfo = getBannerEmailTypeToHEDMEmailTypeMap(EmailTypeEnum.EMAIL_TYPE)
        if (!mappedCodeInfo.isEmpty()) {
            emailTypeService.fetchByEmailTypeCodes(mappedCodeInfo.keySet(),0,-1).each { result ->
                EmailType emailType = result.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = result.getAt(1)
                emailTypeInfo << emailType.code
                emailTypeInfo << globalUniqueIdentifier.guid
                emailTypeInfo << mappedCodeInfo.get(emailType.code)
                emailTypeMap.put(emailType.code, emailTypeInfo)
            }
        }
        return emailTypeMap
    }

    public Map<String,String> getBannerEmailTypeToHEDMEmailTypeMap(List<String> emailTypeEnumValues) {
        Map<String,String> mappedEmailTypeCode = [:]
        IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValues(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME, emailTypeEnumValues).each {
            if(EmailType.findByCode(it.value)){
                mappedEmailTypeCode.put(it.value, it.translationValue)
            }
        }
        return mappedEmailTypeCode
    }
}