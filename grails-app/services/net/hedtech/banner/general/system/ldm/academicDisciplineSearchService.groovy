/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.operators.Operators


class AcademicDisciplineSearchService {

    boolean transactional = true


    def preCreate(map) {
        throwUnsupportedException()
    }


    def preUpdate(map) {
        throwUnsupportedException()
    }


    def preDelete(map) {
        throwUnsupportedException()
    }


    def throwUnsupportedException() {
        throw new ApplicationException(AcademicDisciplineSearchService, "@@r1:unsupported.operation@@")
    }


    def fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def params = [:]
        def criteria = []
        def pagingAndSortParams = [:]

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

        buildCriteria(content, params, criteria)


        DynamicFinder dynamicFinder = new DynamicFinder(AcademicDisciplineView.class, getQueryForFetchAllByCriteria(), "a")

        def result = dynamicFinder.find([params: params, criteria: criteria], pagingAndSortParams)

        return result
    }


    def countByCriteria(Map content) {
        def params = [:]
        def criteria = []

        buildCriteria(content, params, criteria)

        DynamicFinder dynamicFinder = new DynamicFinder(AcademicDisciplineView.class, getQueryForFetchAllByCriteria(), "a")
        def result = dynamicFinder.count([params: params, criteria: criteria])
        log.debug "Count query on SVQ_SCBCRSE_SCBCGID returned $result"

        return result
    }


    private void buildCriteria(Map content, Map params, List criteria) {
        if (content.containsKey(GeneralValidationCommonConstants.TYPE)) {
            params.put(GeneralValidationCommonConstants.TYPE, content.get(GeneralValidationCommonConstants.TYPE).trim().toLowerCase())
            criteria.add([key: GeneralValidationCommonConstants.TYPE, binding: GeneralValidationCommonConstants.TYPE, operator: Operators.EQUALS_IGNORE_CASE])
        }
    }

    private String getQueryForFetchAllByCriteria() {
        def query = """
                       from AcademicDisciplineView a
                       where 1 = 1
                    """

        return query
    }


    def fetchByGuid(academicDisciplineGuid) {
        if (academicDisciplineGuid) {
            AcademicDisciplineView.withSession { session ->
                List<AcademicDisciplineView> academicDisciplines = session.getNamedQuery('AcademicDisciplineView.fetchByGuid').setString('academicDisciplineGuid', academicDisciplineGuid).list()
                return academicDisciplines
            }
        } else {
            return null
        }
    }



}
