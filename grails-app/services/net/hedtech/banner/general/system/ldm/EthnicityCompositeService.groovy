/*********************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.ldm.v1.EthnicityDetail
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v6.EthnicityDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "ethnicities" resource for CDM
 */
@Transactional
class EthnicityCompositeService extends LdmService {

    def ethnicityService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1,
                                                  GeneralValidationCommonConstants.VERSION_V3,
                                                  GeneralValidationCommonConstants.VERSION_V4,
                                                  GeneralValidationCommonConstants.VERSION_V6]


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<EthnicityDetail> list(Map params) {
        log.debug "list:Begin:$params"
        List ethnicityDetailList = []

        if (GeneralValidationCommonConstants.VERSION_V1.equals(getAcceptVersion(VERSIONS))) {
            RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

            List allowedSortFields = [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE]
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            RestfulApiValidationUtility.validateSortOrder(params.order)
            params.sort = fetchBannerDomainPropertyForLdmField(params.sort)

            ethnicityService.list(params).each { ethnicity ->
                ethnicityDetailList << getDecorator(ethnicity)
            }
        } else {
            getUnitedStatesEthnicCodes().each { ethnicity ->
                EthnicityDecorator ethnicityDecorator = getEthnicityUSDecorator(ethnicity)
                if (ethnicityDecorator) ethnicityDetailList << ethnicityDecorator
            }
        }


        log.debug "list:End:${ethnicityDetailList?.size()}"
        return ethnicityDetailList
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        log.debug "count:Begin"
        int total

        if (GeneralValidationCommonConstants.VERSION_V1.equals(getAcceptVersion(VERSIONS))) {
            total = ethnicityService.count()
        } else if (GeneralValidationCommonConstants.VERSION_V3.equals(getAcceptVersion(VERSIONS))) {
            total = GlobalUniqueIdentifier.countByLdmName(GeneralValidationCommonConstants.ETHNICITIES_US)
        } else if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS)) || GeneralValidationCommonConstants.VERSION_V6.equals(getAcceptVersion(VERSIONS))) {
            total = GlobalUniqueIdentifier.countByLdmNameAndDomainIdGreaterThan(GeneralValidationCommonConstants.ETHNICITIES_US, 0L)
        }

        log.debug "count:End:$total"
        return total
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {
        log.debug "get:Begin:$guid"
        def result

        if (GeneralValidationCommonConstants.VERSION_V1.equals(getAcceptVersion(VERSIONS))) {
            GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITY_LDM_NAME, guid)
            if (!globalUniqueIdentifier) {
                throw new ApplicationException(GeneralValidationCommonConstants.ETHNICITY, new NotFoundException())
            }

            Ethnicity ethnicity = ethnicityService.get(globalUniqueIdentifier.domainId)
            if (!ethnicity) {
                throw new ApplicationException(GeneralValidationCommonConstants.ETHNICITY, new NotFoundException())
            }

            result = getDecorator(ethnicity, globalUniqueIdentifier.guid)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITIES_US, guid?.trim()?.toLowerCase())
            if (!globalUniqueIdentifier || (globalUniqueIdentifier.domainId == 0 && getAcceptVersion(VERSIONS) > GeneralValidationCommonConstants.VERSION_V3)) {
                throw new ApplicationException(GeneralValidationCommonConstants.ETHNICITY, new NotFoundException())
            }
            result = getEthnicityUSDecorator(globalUniqueIdentifier)
        }
        log.debug "get:End:$result"
        return result;
    }

    /**
     * POST /api/ethnicities
     *
     * @param content Request body
     * @return
     */
    def create(Map content) {
        validateRequest(content)

        Ethnicity ethnicity = Ethnicity.findByCode(content?.code?.trim())
        if (ethnicity) {
            throw new ApplicationException("ethnicity", new BusinessLogicValidationException("exists.message", null))
        }

        ethnicity = bindEthnicity(new Ethnicity(), content)

        String ethnicityGuid = content?.guid?.trim()?.toLowerCase()
        if (ethnicityGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(ethnicity.id, ethnicityGuid, GeneralValidationCommonConstants.ETHNICITY_LDM_NAME)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.ETHNICITY_LDM_NAME, ethnicity?.id)
            ethnicityGuid = globalUniqueIdentifier.guid
        }
        log.debug("GUID: ${ethnicityGuid}")

        return getDecorator(ethnicity, ethnicityGuid)
    }

    /**
     * PUT /api/ethnicities/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String ethnicityGuid = content?.id?.trim()?.toLowerCase()

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITY_LDM_NAME, ethnicityGuid)
        if (ethnicityGuid) {
            if (!globalUniqueIdentifier) {
                if (!content.get('guid')) {
                    content.put('guid', ethnicityGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("ethnicity", new NotFoundException())
        }

        Ethnicity ethnicity = Ethnicity.findById(globalUniqueIdentifier?.domainId)
        if (!ethnicity) {
            throw new ApplicationException("ethnicity", new NotFoundException())
        }

        // Should not allow to update ethnicity.code as it is read-only
        if (ethnicity.code != content?.code?.trim()) {
            content.put("code", ethnicity.code)
        }
        validateRequest(content)
        ethnicity = bindEthnicity(ethnicity, content)

        return getDecorator(ethnicity, ethnicityGuid)
    }


    EthnicityDetail fetchByEthnicityId(Long ethnicityId) {
        if (null == ethnicityId) {
            return null
        }
        Ethnicity ethnicity = ethnicityService.get(ethnicityId) as Ethnicity
        if (!ethnicity) {
            return null
        }
        return getDecorator(ethnicity)
    }


    EthnicityDetail fetchByEthnicityCode(String ethnicityCode) {
        EthnicityDetail ethnicityDetail = null
        if (ethnicityCode) {
            Ethnicity ethnicity = Ethnicity.findByCode(ethnicityCode)
            if (!ethnicity) {
                return ethnicityDetail
            }
            ethnicityDetail = getDecorator(ethnicity)
        }
        return ethnicityDetail
    }


    Map<String, String> fetchGUIDsForUnitedStatesEthnicCodes() {
        Map<String, String> usEthnicCodeToGuidMap = [:]
        getUnitedStatesEthnicCodes().each {
            if (it.domainId > 0) {
                usEthnicCodeToGuidMap.put(String.valueOf(it.domainId), it.guid)
            }
        }
        return usEthnicCodeToGuidMap
    }

    /**
     * STVETHN_ETHN_CDE -> HeDM enumeration (Mapping)(in LIST/GET operations)
     *
     * @param ethnicCode 1 - Not Hispanic or Latino, 2 - Hispanic or Latino, or null.
     */
    String getHeDMEnumeration(String ethnicCode) {
        String hedmEnum
        UsEthnicCategory usEthnicCategory = UsEthnicCategory.getByBannerValue(ethnicCode)
        if (usEthnicCategory) {
            hedmEnum = usEthnicCategory.versionToEnumMap[GeneralValidationCommonConstants.VERSION_V1]
        }
        return hedmEnum
    }

    /**
     * HeDM enumeration -> STVETHN_ETHN_CDE (Default)(in POST/PUT operations)
     *
     * @param hedmEnum
     * @return
     */
    private String getEthnicCode(String hedmEnum) {
        String ethnicCode
        UsEthnicCategory usEthnicCategory = UsEthnicCategory.getByDataModelValue(hedmEnum, GeneralValidationCommonConstants.VERSION_V1)
        if (usEthnicCategory) {
            ethnicCode = usEthnicCategory.bannerValue
        }
        return ethnicCode
    }


    private def getDecorator(Ethnicity ethnicity, String ethnicityGuid = null) {
        EthnicityDetail decorator
        if (ethnicity) {
            if (!ethnicityGuid) {
                ethnicityGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.ETHNICITY_LDM_NAME, ethnicity.id)?.guid
            }
            decorator = new EthnicityDetail(ethnicity, ethnicityGuid, getHeDMEnumeration(ethnicity.ethnic), new Metadata(ethnicity.dataOrigin))
        }
        return decorator
    }


    def bindEthnicity(Ethnicity ethnicity, Map content) {
        setDataOrigin(ethnicity, content)
        bindData(ethnicity, content, [:])
        if (content.containsKey("parentCategory")) {
            String parentCategory = content.get("parentCategory")
            if (parentCategory) {
                UsEthnicCategory usEthnicCategory = UsEthnicCategory.getByDataModelValue(parentCategory, GeneralValidationCommonConstants.VERSION_V1)
                if (!usEthnicCategory) {
                    throw new ApplicationException("ethnicity", new BusinessLogicValidationException("parentCategory.invalid", null))
                }
                ethnicity.ethnic = usEthnicCategory.bannerValue
            } else {
                ethnicity.ethnic = null
            }
        }
        try {
            ethnicityService.createOrUpdate(ethnicity)
        } catch (ApplicationException ae) {
            throwBusinessLogicValidationException(ae)
        }
    }


    private void validateRequest(Map content) {
        if (!content?.code) {
            throw new ApplicationException('ethnicity', new BusinessLogicValidationException('code.required.message', null))
        }
        if (!content?.description) {
            throw new ApplicationException('ethnicity', new BusinessLogicValidationException('description.required.message', null))
        }
    }

    /**
     * Ethnic codes defined by the U.S. government
     *
     * @return
     */
    List<GlobalUniqueIdentifier> getUnitedStatesEthnicCodes() {
        return globalUniqueIdentifierService.fetchByLdmName(GeneralValidationCommonConstants.ETHNICITIES_US)
    }


    EthnicityDecorator getEthnicityUSDecorator(GlobalUniqueIdentifier globalUniqIdentifier) {
        String version = getAcceptVersion(VERSIONS)
        if (GeneralValidationCommonConstants.VERSION_V3.equals(version)) {
            return new EthnicityDecorator(globalUniqIdentifier.guid, globalUniqIdentifier.domainKey, null)
        } else if (version > GeneralValidationCommonConstants.VERSION_V3 && globalUniqIdentifier.domainId > 0) {
            return createEthnicityV6(globalUniqIdentifier.guid, globalUniqIdentifier.domainKey, String.valueOf(globalUniqIdentifier.domainId))
        }
    }


    private EthnicityDecorator createEthnicityV6(String guid, String title, String usEthnicCode) {
        EthnicityDecorator decorator
        if (guid) {
            String ethnicCategory
            UsEthnicCategory usEthnicCategory = UsEthnicCategory.getByBannerValue(usEthnicCode)
            if (usEthnicCategory) {
                ethnicCategory = usEthnicCategory.versionToEnumMap[GeneralValidationCommonConstants.VERSION_V4]
            }
            decorator = new EthnicityDecorator(guid, title, ethnicCategory)
        }
        return decorator
    }


    public def fetchByGuid(String ethnicityGuid) {
        Ethnicity ethnicity
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITIES_US, ethnicityGuid?.toLowerCase()?.trim())
        if (globalUniqueIdentifier) {
            ethnicity = ethnicityService.get(globalUniqueIdentifier.domainId)
        }
        return ethnicity
    }

}
