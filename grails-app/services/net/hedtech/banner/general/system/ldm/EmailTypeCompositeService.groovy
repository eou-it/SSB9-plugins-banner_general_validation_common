/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.ldm.v4.EmailTypeDetails
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> Service to retrive the data from a email validation table </p>
 */
@Transactional
class EmailTypeCompositeService extends LdmService {

    def emailTypeService

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V6]

    /**
     * GET /api/email-types
     * @return List
     */
    @Transactional(readOnly = true)
    List<EmailTypeDetails> list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        List<EmailTypeDetails> emailTypes = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        Map<String, String> bannerEmailTypeToHedmEmailTypeMap = getBannerEmailTypeToHedmV6EmailTypeMap()
        if (bannerEmailTypeToHedmEmailTypeMap) {
            params.offset = params.offset ?: 0
            emailTypeService.fetchAllWithGuidByCodeInList(bannerEmailTypeToHedmEmailTypeMap.keySet(), params.max as int, params.offset as int).each { result ->
                EmailType emailType = result.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = result.getAt(1)
                emailTypes << new EmailTypeDetails(emailType.code, emailType.description, globalUniqueIdentifier.guid, bannerEmailTypeToHedmEmailTypeMap.get(emailType.code))
            }
        }
        return emailTypes
    }

    Long count() {
        return getBannerEmailTypeToHedmV6EmailTypeMap().size()
    }

    /**
     * GET /api/email-types/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    EmailTypeDetails get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        if (!guid) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        EmailType emailType = emailTypeService.get(globalUniqueIdentifier.domainId)
        if (!emailType) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        Map<String, String> bannerEmailTypeToHedmEmailTypeMap = getBannerEmailTypeToHedmV6EmailTypeMap()
        if (!bannerEmailTypeToHedmEmailTypeMap.containsKey(emailType.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }

        return new EmailTypeDetails(emailType.code, emailType.description, globalUniqueIdentifier.guid, bannerEmailTypeToHedmEmailTypeMap.get(emailType.code))

    }

    /**
     * POST /api/email-types
     *
     * @param content Request body
     * @return EmailTypeDetails object post creating the record
     */
    def create(Map content) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        if (!(content.code)) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        if (EmailType.findByCode(content.code) != null) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }
        EmailType emailType = bindEmailType(new EmailType(), content)
        String emailGuid = content?.id?.trim()?.toLowerCase()
        if (emailGuid && emailGuid != GeneralValidationCommonConstants.NIL_GUID) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            emailGuid = updateGuidValue(emailType.id, emailGuid, GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)?.guid
        } else {
            emailGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME, emailType?.id)?.guid
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
        String acceptVersion = getAcceptVersion(VERSIONS)
        String emailGuid = content.id?.trim()?.toLowerCase()
        if (!emailGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME, emailGuid)

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

        return new EmailTypeDetails(emailType.code, emailType.description, emailGuid, content.emailType)
    }

    def getEmailTypeCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            List entities = emailTypeService.fetchAllWithGuidByCodeInList(codes)
            entities.each {
                EmailType emailType = it.getAt(0)
                GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
                codeToGuidMap.put(emailType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }

    def getBannerEmailTypeToHedmV3EmailTypeMap() {
        return getBannerEmailTypeToHEDMEmailTypeMap(GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME_V3, GeneralValidationCommonConstants.VERSION_V3)
    }


    def getBannerEmailTypeToHedmV6EmailTypeMap() {
        return getBannerEmailTypeToHEDMEmailTypeMap(GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME_V6, GeneralValidationCommonConstants.VERSION_V6)
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

    private def getBannerEmailTypeToHEDMEmailTypeMap(String settingName, String version) {
        Map<String, String> bannerEmailTypeToHedmEmailTypeMap = [:]
        List<IntegrationConfiguration> integrationConfigurationList = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (integrationConfigurationList) {
            List<EmailType> entities = emailTypeService.fetchAllByCodeInList(integrationConfigurationList.value)
            integrationConfigurationList.each {
                HedmEmailType hedmEmailType = HedmEmailType.getByDataModelValue(it.translationValue, version)
                if (entities.code.contains(it.value) && hedmEmailType) {
                    bannerEmailTypeToHedmEmailTypeMap.put(it.value, hedmEmailType.versionToEnumMap[version])
                }
            }
        }
        if (bannerEmailTypeToHedmEmailTypeMap.isEmpty()) {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerEmailTypeToHedmEmailTypeMap
    }

}
