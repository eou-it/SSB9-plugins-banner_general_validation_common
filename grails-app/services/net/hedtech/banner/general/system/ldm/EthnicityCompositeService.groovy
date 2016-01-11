/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
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
import net.hedtech.banner.general.system.ldm.v1.EthnicityParentCategory
import net.hedtech.banner.general.system.ldm.v1.Metadata
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
                                                  GeneralValidationCommonConstants.VERSION_V4 ]


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

            List<Ethnicity> ethnicityList = ethnicityService.list(params) as List
            ethnicityList.each { ethnicity ->
                ethnicityDetailList << getDecorator(ethnicity)
            }
        } else if (GeneralValidationCommonConstants.VERSION_V3.equals(getAcceptVersion(VERSIONS))) {
            def results = getUnitedStatesEthnicCodes()
            results?.each {
                ethnicityDetailList << [guid: it.guid, title: it.domainKey]
            }
        }else if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS))) {
             getUnitedStatesEthnicCodes().each { ethnicity ->
                 if(ethnicity.domainId != 0){
                ethnicityDetailList << fetchV4Data(ethnicity)
                 }
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
        } else if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS))) {
            total = GlobalUniqueIdentifier.countByLdmNameAndDomainIdGreaterThan(GeneralValidationCommonConstants.ETHNICITIES_US,0L)
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
        } else if (GeneralValidationCommonConstants.VERSION_V3.equals(getAcceptVersion(VERSIONS))) {
            GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITIES_US, guid?.trim()?.toLowerCase())
            if (!globalUniqueIdentifier) {
                throw new ApplicationException(GeneralValidationCommonConstants.ETHNICITY, new NotFoundException())
            }
            result = [guid: globalUniqueIdentifier.guid, title: globalUniqueIdentifier.domainKey]
        } else if (GeneralValidationCommonConstants.VERSION_V4.equals(getAcceptVersion(VERSIONS))) {
            GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ETHNICITIES_US, guid?.trim()?.toLowerCase())
            if (!globalUniqueIdentifier || globalUniqueIdentifier.domainId<=0) {
                throw new ApplicationException(GeneralValidationCommonConstants.ETHNICITY, new NotFoundException())
            }
            result = fetchV4Data(globalUniqueIdentifier)
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

    /**
     * STVETHN_ETHN_CDE -> HeDM enumeration (Mapping)(in LIST/GET operations)
     *
     * @param ethnicCode 1 - Not Hispanic or Latino, 2 - Hispanic or Latino, or null.
     */
    String getHeDMEnumeration(String ethnicCode) {
        String hedmEnum
        switch (ethnicCode) {
            case "1":
                hedmEnum = EthnicityParentCategory.NON_HISPANIC.value
                break
            case "2":
                hedmEnum = EthnicityParentCategory.HISPANIC.value
                break
            default:
                break
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
        switch (hedmEnum) {
            case EthnicityParentCategory.NON_HISPANIC.value:
                ethnicCode = "1"
                break
            case EthnicityParentCategory.HISPANIC.value:
                ethnicCode = "2"
                break
            default:
                break
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
                if (!EthnicityParentCategory.contains(parentCategory)) {
                    throw new ApplicationException("ethnicity", new BusinessLogicValidationException("parentCategory.invalid", null))
                }
                ethnicity.ethnic = getEthnicCode(content.parentCategory)
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
        return GlobalUniqueIdentifier.findAllByLdmName(GeneralValidationCommonConstants.ETHNICITIES_US)
    }

   def fetchV4Data(def ethnicity){
       switch (ethnicity?.domainId){
           case 1L :  return [id: ethnicity.guid, title: ethnicity.domainKey, ethnicCategory:GeneralValidationCommonConstants.NON_HISPANIC]
           case 2L  :  return [id: ethnicity.guid, title: ethnicity.domainKey, ethnicCategory:GeneralValidationCommonConstants.HISPANIC]
       }
   }

}
