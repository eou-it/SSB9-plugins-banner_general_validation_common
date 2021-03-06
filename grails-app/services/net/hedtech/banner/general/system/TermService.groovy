/** *****************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
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
@Transactional
class TermService extends ServiceBase {

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

        def allowedFields = ["code"        : [Operators.EQUALS, Operators.CONTAINS, Operators.STARTS_WITH, "lt", Operators.LESS_THAN, "gt", Operators.GREATER_THAN],
                             "description" : [Operators.EQUALS, Operators.EQUALS_IGNORE_CASE, Operators.CONTAINS],
                             "startDate"   : [Operators.EQUALS, "lt", Operators.LESS_THAN, "le", Operators.LESS_THAN_EQUALS, "gt", Operators.GREATER_THAN, "ge", Operators.GREATER_THAN_EQUALS],
                             "endDate"     : [Operators.EQUALS, "lt", Operators.LESS_THAN, "le", Operators.LESS_THAN_EQUALS, "gt", Operators.GREATER_THAN, "ge", Operators.GREATER_THAN_EQUALS],
                             "lastModified": [Operators.EQUALS, "lt", Operators.LESS_THAN, "gt", Operators.GREATER_THAN]]
        RestfulApiValidationUtility.validateCriteria(QueryBuilder.createFilters(map), allowedFields)

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


    def get(id) {
        Term term
        Map params = [:]

        try {
            def RestfulApiRequestParams = this.class.classLoader.loadClass( 'net.hedtech.banner.restfulapi.RestfulApiRequestParams' ).newInstance()
            params = RestfulApiRequestParams.get();
        } catch (ClassNotFoundException e) {
        }

        if (params?.pluralizedResourceName) {
            String termCode = id
            term = Term.findByCode(termCode)
            if (!term) {
                throw new ApplicationException("term", new NotFoundException())
            }
        } else {
            term = super.get(id)
        }
        return term
    }

    public List<Term> fetchAllByTermCodes( List<String> termCodes ) {
        Term.fetchAllByTermCodes( termCodes )
    }

}
