/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).  
// These exceptions must be caught and handled by the controller using this service.
// 
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

/**
 * A transactional service supporting persistence of the SourceAndBackgroundInstitution model and filtering using the restfulApi plugin.
 * */
class SourceAndBackgroundInstitutionService extends ServiceBase {

    boolean transactional = true


    def list() {
        super.list()
    }


    def count() {
        super.count()
    }


    def list(Map map) {
        def filterMap = QueryBuilder.getFilterData(map)
        // If not using filters, defer to the ServiceBase or any other list implementation
        if (filterMap?.size() == 0 && !map.params && !map.criteria) return super.list(map)

        def sourceAndBackgroundInstitutionQuery = """from SourceAndBackgroundInstitution a"""
        def sourceAndBackgroundInstitutionList = new DynamicFinder(SourceAndBackgroundInstitution.class,
                                                                   sourceAndBackgroundInstitutionQuery, "a").find([params: filterMap.params,
                                                                                                                          criteria: filterMap.criteria],
                                                                                                                  filterMap.pagingAndSortParams)

        return sourceAndBackgroundInstitutionList
    }


    def count(Map map) {
        def filterMap = QueryBuilder.getFilterData(map)
        // If not using filters, defer to the ServiceBase or any other list implementation
        if (filterMap?.size() == 0 && !map.params && !map.criteria) return super.count()

        def sourceAndBackgroundInstitutionQuery = """from SourceAndBackgroundInstitution a"""
        def sourceAndBackgroundInstitutionSize = new DynamicFinder(SourceAndBackgroundInstitution.class,
                                                                   sourceAndBackgroundInstitutionQuery, "a").count([params: filterMap.params,
                criteria: filterMap.criteria])

        return sourceAndBackgroundInstitutionSize
    }

}
