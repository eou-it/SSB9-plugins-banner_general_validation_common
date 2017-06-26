/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.service.ServiceBase


// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

/**
 * A transactional service supporting persistence of the Academic Year model.
 * */
class AcademicCredentialService extends ServiceBase {

    boolean transactional = true

    /**
     * fetching AcademicCredential data based on guid
     * @param guid
     * @return AcademicCredential
     */
    AcademicCredential fetchByGuid(String guid){
        return AcademicCredential.fetchByGuid(guid)
    }

    /**
     * get count of Academic Credential data based on filter data
     * @param filterData
     * @return
     */
    def  countAll(filterData) {
        return AcademicCredential.countAll(filterData)
    }

    /**
     * fetch Academic Credential data based on filter data
     * @param filterData
     * @param pagingAndSortParams
     * @return
     */
    def  fetchSearch(filterData, pagingAndSortParams) {
        return AcademicCredential.fetchSearch(filterData, pagingAndSortParams)

    }



    List<AcademicCredential> fetchAllByCriteria(Map content, String sortField = null, String sortDirection = null, int max = 0, int offset = -1) {
        Map params = [:]
        List criteria = []
        Map pagingAndSortingParams = [:]

        buildCriteria(content, params, criteria)

        if (max > 0) {
            pagingAndSortingParams.max = max
        }

        if (offset > -1) {
            pagingAndSortingParams.offset = offset
        }

        sortDirection = sortDirection ?: 'asc'

        if (sortField) {
            pagingAndSortingParams.sortCriteria = [
                    ["sortColumn": sortField, "sortDirection": sortDirection],
                    ["sortColumn": 'id', "sortDirection": 'asc']
            ]
        } else {
            pagingAndSortingParams.sortCriteria = [
                    ["sortColumn": 'id', "sortDirection": 'asc']
            ]
        }

        return getDynamicFinderForFetchAllByCriteria().find([params: params, criteria: criteria], pagingAndSortingParams)
    }

    long countByCriteria(Map content) {
        Map params = [:]
        List criteria = []
        buildCriteria(content, params, criteria)
        return getDynamicFinderForFetchAllByCriteria().count([params: params, criteria: criteria])
    }


    private void buildCriteria(Map content, Map params, List criteria) {
        //add custom filter criteria here
        if (content?.containsKey("type")) {
            params.put("type", content.type)
            criteria.add([key: "type", binding: "type", operator: Operators.EQUALS])
        }
    }


    private DynamicFinder getDynamicFinderForFetchAllByCriteria() {
        String query = """FROM AcademicCredential a"""
        return new DynamicFinder(AcademicCredential.class, query, "a")
    }
}
