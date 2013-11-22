/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
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
 * A transactional service supporting persistence of the Term model and filtering using the restfulApi plugin.
 * */
class TermService extends ServiceBase {

    boolean transactional = true
    def operatorConversions = ["gt": Operators.GREATER_THAN, "eq": Operators.EQUALS, "lt": Operators.LESS_THAN, "contains": Operators.CONTAINS]
    // Operators must be converted from the ones that the restfulApi plugin uses to what the CriteriaBuilder expects.

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

        def termQuery = """from Term a"""
        def termList = new DynamicFinder(Term.class, termQuery, "a").find([params: filterMap.params, criteria: filterMap.criteria],
                                                                          filterMap.pagingAndSortParams)

        return termList
    }


    def count(Map map) {

        def filterMap = QueryBuilder.getFilterData(map)

        // If not using filters, defer to the ServiceBase or any other count implementation
        if (filterMap.size() == 0 && !map.params && !map.criteria) return super.count()

        def termQuery = """from Term a"""
        def termSize = new DynamicFinder(Term.class, termQuery, "a").count([params: filterMap.params, criteria: filterMap.criteria])

        return termSize
    }


    def show(Map map) {
        Term term
        if (map?.pluralizedResourceName) { // RESTful API request  for term
            // map.id represents termCode
            String termCode = map.id
            term = Term.findByCode(termCode)
            if (!term) {
                throw new ApplicationException("Term", termCode, new NotFoundException(id: termCode, entityClassName: Term.class.simpleName))
            }
        }
        return term
    }

}
