/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
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

    private static final String PROCESS_CODE = 'HEDM'
    private static final String MARITAL_STATUS_LDM_NAME = 'marital-status'
    static final String MARITAL_STATUS_PARENT_CATEGORY = "MARITALSTATUS.PARENTCATEGORY"
    private static final List<String> VERSIONS = ["v1","v4"]

    /**
     * GET /api/marital-statuses
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<MaritalStatusDetail> list(Map params) {
        List maritalStatusDetailList = []

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = ("v4".equals(LdmService.getAcceptVersion(VERSIONS))? ['code', 'title']:['abbreviation', 'title'])
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)

        List<MaritalStatus> maritalStatusList = maritalStatusService.list(params) as List
        maritalStatusList.each { maritalStatus ->
            maritalStatusDetailList << getDecorator(maritalStatus)
        }
        return maritalStatusDetailList
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
    Long count() {
        return MaritalStatus.count()
    }

    /**
     * GET /api/marital-statuses/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    MaritalStatusDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        MaritalStatus maritalStatus = MaritalStatus.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        return getDecorator(maritalStatus, globalUniqueIdentifier.guid)
    }

    /**
     * POST /api/marital-statuses
     *
     * @param content Request body
     */
    def create(content) {
        validateRequest(content)

        MaritalStatus maritalStatus = MaritalStatus.findByCode(content?.code?.trim())
        if (maritalStatus) {
            throw new ApplicationException("maritalStatus", new BusinessLogicValidationException("exists.message", null))
        }

        maritalStatus = bindMaritalStatus(new MaritalStatus(), content)

        String msGuid = content?.guid?.trim()?.toLowerCase()
        if (msGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(maritalStatus.id, msGuid, MARITAL_STATUS_LDM_NAME)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus?.id)
            msGuid = globalUniqueIdentifier.guid
        }
        log.debug("GUID: ${msGuid}")

        return getDecorator(maritalStatus, msGuid)
    }

    /**
     * PUT /api/marital-statuses/<guid>
     *
     * @param content Request body
     */
    def update(content) {
        String msGuid = content?.id?.trim()?.toLowerCase()

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, msGuid)
        if (msGuid) {
            if (!globalUniqueIdentifier) {
                if (!content.get('guid')) {
                    content.put('guid', msGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        MaritalStatus maritalStatus = MaritalStatus.findById(globalUniqueIdentifier?.domainId)
        if (!maritalStatus) {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        // Should not allow to update maritalStatus.code as it is read-only
        if (maritalStatus.code != content?.code?.trim()) {
            content.put("code", maritalStatus.code)
        }
        validateRequest(content)
        maritalStatus = bindMaritalStatus(maritalStatus, content)

        return getDecorator(maritalStatus, msGuid)
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        if (null == maritalStatusId) {
            return null
        }
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        return getDecorator(maritalStatus)
    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = getDecorator(maritalStatus)
        }
        return maritalStatusDetail
    }


    private void validateRequest(content) {
        if (!content?.code) {
            throw new ApplicationException('maritalStatus', new BusinessLogicValidationException('code.required.message', null))
        }
        if (!content?.description) {
            throw new ApplicationException('maritalStatus', new BusinessLogicValidationException('description.required.message', null))
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


    private MaritalStatusDetail getDecorator(MaritalStatus maritalStatus, String msGuid = null) {
        MaritalStatusDetail decorator
        if (maritalStatus) {
            if (!msGuid) {
                msGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
            }
            decorator = new MaritalStatusDetail(maritalStatus, msGuid, getHeDMEnumeration(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
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
            IntegrationConfiguration intgConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(PROCESS_CODE, MARITAL_STATUS_PARENT_CATEGORY, maritalStatusCode)
            log.debug "Value ${intgConf?.value} - TranslationValue ${intgConf?.translationValue}"
            if (intgConf && MaritalStatusParentCategory.MARITAL_STATUS_PARENT_CATEGORY.contains(intgConf.translationValue)) {
                hedmEnum = intgConf.translationValue
            }
        }
        return hedmEnum
    }

}
