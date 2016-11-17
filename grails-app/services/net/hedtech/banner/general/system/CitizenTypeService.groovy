package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.query.DynamicFinder

class CitizenTypeService extends ServiceBase {

    GlobalUniqueIdentifierService globalUniqueIdentifierService

    boolean transactional = true

    def fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {

        Map params = [:]
        List criteria = []
        Map pagingAndSortParams = [:]

        sortOrder = sortOrder ?: 'asc'
        if (sortField) {
            pagingAndSortParams.sortCriteria = [
                    ["sortColumn": sortField, "sortDirection": sortOrder],
                    ["sortColumn": "id", "sortDirection": "asc"]
            ]
        } else {
            pagingAndSortParams.sortColumn = "id"
            pagingAndSortParams.sortDirection = sortOrder
        }
        if (max > 0) {
            pagingAndSortParams.max = max
        }
        if (offset > -1) {
            pagingAndSortParams.offset = offset
        }
        return getDynamicFinderForFetchAllByCriteria().find([params: params, criteria: criteria], pagingAndSortParams)
    }

    private DynamicFinder getDynamicFinderForFetchAllByCriteria() {
        def query = """ FROM CitizenType a """
        return new DynamicFinder(CitizenType.class, query, "a")
    }

    public def fetchByGuid(String guid){
        CitizenType citizenType
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.CITIZENSHIP_STATUSES_LDM_NAME, guid?.toLowerCase()?.trim())
        if(globalUniqueIdentifier){
            citizenType = CitizenType.findByCode(globalUniqueIdentifier.domainKey)
        }
        return citizenType
    }

    public def fetchByCitizenIndicator(Boolean CitizenIndicator) {
        return CitizenType.findByCitizenIndicator(CitizenIndicator)
    }


}
