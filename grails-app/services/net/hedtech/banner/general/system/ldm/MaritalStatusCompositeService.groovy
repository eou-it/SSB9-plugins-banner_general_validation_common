/*********************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for HeDM marital-statuses
 */
@Transactional
class MaritalStatusCompositeService extends LdmService {

    def maritalStatusService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1,
                                                  GeneralValidationCommonConstants.VERSION_V4]

    /**
     * GET /api/marital-statuses
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(Map params) {
        String version = LdmService.getAcceptVersion(VERSIONS)

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = (GeneralValidationCommonConstants.VERSION_V4.equals(version) ? [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE] : [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE])
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        String sortField = fetchBannerDomainPropertyForLdmField(params.sort?.trim())
        String sortOrder = params.order?.trim()
        int max = params.max?.trim()?.toInteger() ?: 0
        int offset = params.offset?.trim()?.toInteger() ?: 0

        def rows
        if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
            def bannerMaritalStatusToHedmMaritalStatusMap = getBannerMaritalStatusToHedmV4MaritalStatusMap()
            rows = maritalStatusService.fetchAllWithGuidByCodeInList(bannerMaritalStatusToHedmMaritalStatusMap.keySet(), sortField, sortOrder, max, offset)
        } else if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
            rows = maritalStatusService.fetchAllWithGuid(sortField, sortOrder, max, offset)
        }

        return createMaritalStatusDataModels(rows)
    }

    /**
     * GET /api/marital-statuses
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count(Map params) {
        String version = LdmService.getAcceptVersion(VERSIONS)
        if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
            return getBannerMaritalStatusToHedmV4MaritalStatusMap().size()
        } else if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
            return maritalStatusService.count(params)
        }
    }

    /**
     * GET /api/marital-statuses/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        if (GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS))) {
            def bannerMaritalStatusToHedmMaritalStatusMap = getBannerMaritalStatusToHedmV4MaritalStatusMap()
            if (!bannerMaritalStatusToHedmMaritalStatusMap.get(maritalStatus.code)) {
                throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
            }
        }

        return createMaritalStatusDataModels([[maritalStatus: maritalStatus, globalUniqueIdentifier: globalUniqueIdentifier]])[0]
    }

    /**
     * POST /api/marital-statuses
     *
     * @param content Request body
     */
    def create(content) {
        def version = LdmService.getAcceptVersion(VERSIONS)
        validateRequest(content, version)
        MaritalStatus maritalStatus = maritalStatusService.fetchByCode(content.code?.trim())
        if (maritalStatus) {
            def messageCode = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS : GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(messageCode, null))
        }

        maritalStatus = bindMaritalStatus(new MaritalStatus(), content)

        String msGuid = content.guid?.trim()?.toLowerCase()
        if (msGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(maritalStatus.id, msGuid, GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)
        } else {
            msGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
        }
        log.debug("GUID: ${msGuid}")
        def maritalCategory = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? content.maritalCategory : content.parentCategory
        return getDecorator(maritalStatus, msGuid, maritalCategory)
    }

    /**
     * PUT /api/marital-statuses/<guid>
     *
     * @param content Request body
     */
    def update(content) {
        String msGuid = content.id?.trim()?.toLowerCase()
        if (!msGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, msGuid)
        if (!globalUniqueIdentifier) {
            if (!content.guid) {
                content.guid = msGuid
            }
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        // Should not allow to update maritalStatus.code as it is read-only
        if (maritalStatus.code != content.code?.trim()) {
            content.code = maritalStatus.code
        }
        validateRequest(content, LdmService.getAcceptVersion(VERSIONS))
        maritalStatus = bindMaritalStatus(maritalStatus, content)
        return getDecorator(maritalStatus, msGuid, getHeDMEnumeration(maritalStatus.code))
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        if (null == maritalStatusId) {
            return null
        }
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        return getDecorator(maritalStatus, null)
    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = getDecorator(maritalStatus, null)
        }
        return maritalStatusDetail
    }


    private void validateRequest(content, version) {
        if (!content.code) {
            def parameterValue = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.CODE.capitalize() : GeneralValidationCommonConstants.ABBREVIATION.capitalize()
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, [parameterValue]))
        }
        if (!content.description) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_DESCRIPTION_REQUIRED, null))
        }
    }


    def bindMaritalStatus(MaritalStatus maritalStatus, Map content) {
        setDataOrigin(maritalStatus, content)
        bindData(maritalStatus, content, [:])
        if (content.parentCategory) {
            // No domain field to store.  So not useful for create and update operations
        }
        maritalStatus.financeConversion = maritalStatus.financeConversion ?: "1"
        maritalStatusService.createOrUpdate(maritalStatus)
    }


    private MaritalStatusDetail getDecorator(MaritalStatus maritalStatus, String msGuid = null, String martialStatusCategory) {
        MaritalStatusDetail decorator
        if (maritalStatus && !msGuid) {
            msGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
        }
        if (maritalStatus && !martialStatusCategory) {
            martialStatusCategory = getHeDMEnumeration(maritalStatus.code)
        }
        if (maritalStatus) {
            decorator = new MaritalStatusDetail(maritalStatus, msGuid, martialStatusCategory, new Metadata(maritalStatus.dataOrigin))
        }
        return decorator
    }

    /**
     * STVMRTL_CODE -> HeDM enumeration (Mapping)(in LIST/GET operations)
     *
     * @param maritalStatusCode STVMRTL_CODE
     * @return
     */
    String getHeDMEnumeration(String maritalStatusCode) {
        String hedmEnum
        if (maritalStatusCode) {
            def version = LdmService.getAcceptVersion(VERSIONS)
            String settingName = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY : GeneralValidationCommonConstants.MARITAL_STATUS_PARENT_CATEGORY
            IntegrationConfiguration intgConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, settingName, maritalStatusCode)
            log.debug "Value ${intgConf?.value} - TranslationValue ${intgConf?.translationValue}"
            MaritalStatusCategory maritalStatusCategory
            if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
                maritalStatusCategory = MaritalStatusCategory.getByString(intgConf?.translationValue, "v4")
            } else if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
                maritalStatusCategory = MaritalStatusCategory.getByString(intgConf?.translationValue, "v1")
            }
            if (maritalStatusCategory) {
                hedmEnum = intgConf?.translationValue
            }
        }
        return hedmEnum
    }


    private def createMaritalStatusDataModels(def rows) {
        def decorators = []

        def bannerMaritalStatusToHedmMaritalStatusMap
        if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS))) {
            bannerMaritalStatusToHedmMaritalStatusMap = getBannerMaritalStatusToHedmV4MaritalStatusMap()
        } else if (GeneralValidationCommonConstants.VERSION_V1.equals(getAcceptVersion(VERSIONS))) {
            bannerMaritalStatusToHedmMaritalStatusMap = getBannerMaritalStatusToHedmV1MaritalStatusMap()
        }

        rows?.each {
            MaritalStatus maritalStatus = it.maritalStatus
            GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier

            if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS))) {
                decorators << createMaritalStatusDataModelV4(globalUniqueIdentifier.guid, maritalStatus, bannerMaritalStatusToHedmMaritalStatusMap)
            } else if (GeneralValidationCommonConstants.VERSION_V1.equals(getAcceptVersion(VERSIONS))) {
                decorators << createMaritalStatusDataModelV1(globalUniqueIdentifier.guid, maritalStatus, bannerMaritalStatusToHedmMaritalStatusMap)
            }
        }

        return decorators
    }

    MaritalStatusDetail createMaritalStatusDataModelV1(String guid, MaritalStatus maritalStatus,
                                          def bannerMaritalStatusToHedmV1MaritalStatusMap) {
        return new MaritalStatusDetail(maritalStatus, guid, bannerMaritalStatusToHedmV1MaritalStatusMap.get(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
    }

    MaritalStatusDetail createMaritalStatusDataModelV4(String guid, MaritalStatus maritalStatus,
                                          def bannerMaritalStatusToHedmV4MaritalStatusMap) {
        return new MaritalStatusDetail(maritalStatus, guid, bannerMaritalStatusToHedmV4MaritalStatusMap.get(maritalStatus.code), null)
    }


    def getMaritalStatusCodeToGuidMap(Collection<String> codes) {
        Map<String, String> codeToGuidMap = [:]
        if (codes) {
            def rows = maritalStatusService.fetchAllWithGuidByCodeInList(codes)
            rows?.each {
                MaritalStatus nameType = it.maritalStatus
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                codeToGuidMap.put(nameType.code, globalUniqueIdentifier.guid)
            }
        }
        return codeToGuidMap
    }


    def getBannerMaritalStatusToHedmV1MaritalStatusMap() {
        return getBannerMaritalStatusToHedmMaritalStatusMap(GeneralValidationCommonConstants.MARITAL_STATUS_PARENT_CATEGORY, GeneralValidationCommonConstants.VERSION_V1)
    }


    def getBannerMaritalStatusToHedmV4MaritalStatusMap() {
        return getBannerMaritalStatusToHedmMaritalStatusMap(GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY, GeneralValidationCommonConstants.VERSION_V4)
    }

    private def getBannerMaritalStatusToHedmMaritalStatusMap(String settingName, String version) {
        Map<String, String> bannerMaritalStatusToHedmMaritalStatusMap = [:]
        List<IntegrationConfiguration> intConfs = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        log.debug "List of IntegrationConfiguration objects"
        log.debug intConfs
        if (intConfs) {
            List<MaritalStatus> entities = maritalStatusService.fetchAllByCodeInList(intConfs.value)
            intConfs.each {
                MaritalStatusCategory maritalStatusCategory = MaritalStatusCategory.getByString(it.translationValue, version)
                if (entities.code.contains(it.value) && maritalStatusCategory) {
                    bannerMaritalStatusToHedmMaritalStatusMap.put(it.value, maritalStatusCategory.versionToEnumMap[version])
                } else {
                    //throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
                }
            }
        } else {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerMaritalStatusToHedmMaritalStatusMap
    }

}
