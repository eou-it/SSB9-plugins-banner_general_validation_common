/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.grails.databinding.SimpleMapDataBindingSource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "marital-status" resource for LDM media types
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class MaritalStatusCompositeService extends LdmService {

    def maritalStatusService

    private static final String PROCESS_CODE = 'HEDM'
    private static final String MARITAL_STATUS_LDM_NAME = 'marital-status'
    static final String MARITAL_STATUS_PARENT_CATEGORY = "MARITALSTATUS.PARENTCATEGORY"

    List<MaritalStatusDetail> list(Map params) {
        List maritalStatusDetailList = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<MaritalStatus> maritalStatusList = maritalStatusService.list(params) as List
        maritalStatusList.each { maritalStatus ->
            maritalStatusDetailList << getDecoratedMaritalStatus(maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id))
        }
        return maritalStatusDetailList
    }


    Long count() {
        return MaritalStatus.count()
    }


    MaritalStatusDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        MaritalStatus maritalStatus = MaritalStatus.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException("maritalStatus", new NotFoundException())
        }

        return getDecoratedMaritalStatus( maritalStatus, globalUniqueIdentifier )
    }

    def create(content) {
        validateMaritalStatus(content)
        MaritalStatus undecoratedMaritalStatus = bindMaritalStatus( new MaritalStatus(), content)
        GlobalUniqueIdentifier globalUniqueIdentifier
        if( content.id ) {
            globalUniqueIdentifier = updateGuidValue(undecoratedMaritalStatus.id, content.id, MARITAL_STATUS_LDM_NAME)
        }
        else {
            globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, undecoratedMaritalStatus?.id)
        }
        getDecoratedMaritalStatus( undecoratedMaritalStatus, globalUniqueIdentifier )
    }

    def update(content) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(MARITAL_STATUS_LDM_NAME, content?.id)
        if(!globalUniqueIdentifier) {
            return create( content )
        }
        MaritalStatus undecoratedMaritalStatus = MaritalStatus.findById(globalUniqueIdentifier?.domainId)
        undecoratedMaritalStatus = bindMaritalStatus( undecoratedMaritalStatus, content)
        getDecoratedMaritalStatus( undecoratedMaritalStatus, globalUniqueIdentifier )
    }


    MaritalStatusDetail fetchByMaritalStatusId(Long maritalStatusId) {
        if (null == maritalStatusId) {
            return null
        }
        MaritalStatus maritalStatus = maritalStatusService.get(maritalStatusId) as MaritalStatus
        return getDecoratedMaritalStatus( maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatusId) )

    }


    MaritalStatusDetail fetchByMaritalStatusCode(String maritalStatusCode) {
        MaritalStatusDetail maritalStatusDetail = null
        if (maritalStatusCode) {
            MaritalStatus maritalStatus = MaritalStatus.findByCode(maritalStatusCode)
            if (!maritalStatus) {
                return maritalStatusDetail
            }
            maritalStatusDetail = getDecoratedMaritalStatus( maritalStatus, GlobalUniqueIdentifier.findByLdmNameAndDomainId(MARITAL_STATUS_LDM_NAME, maritalStatus.id) )

        }
        return maritalStatusDetail
    }

    def getLdmMaritalStatus(def maritalStatus) {
        if (maritalStatus == null) {
            return maritalStatus
        }
        else {
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, MARITAL_STATUS_PARENT_CATEGORY, maritalStatus)
            MaritalStatusParentCategory.MARITAL_STATUS_PARENT_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
        }

    }

    static void validateMaritalStatus(content) {
        if(!content?.code) {
            throw new ApplicationException( 'maritalStatus', new BusinessLogicValidationException('code.required', [] ) )
        }
        if(!content?.description) {
            throw new ApplicationException( 'maritalStatus', new BusinessLogicValidationException('description.required', [] ) )
        }
    }

    def bindMaritalStatus(MaritalStatus maritalStatus, Map content) {
        setDataOrigin(maritalStatus,content)
        bindData(maritalStatus, content, [:])
        maritalStatus.financeConversion = maritalStatus.financeConversion ?: "1" 
        maritalStatusService.createOrUpdate(maritalStatus)
    }

    def getDecoratedMaritalStatus(MaritalStatus maritalStatus, GlobalUniqueIdentifier globalUniqueIdentifier) {
        new MaritalStatusDetail(maritalStatus,
                globalUniqueIdentifier.guid, getLdmMaritalStatus(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
    }
}
