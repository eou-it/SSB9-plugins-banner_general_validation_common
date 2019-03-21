/** *****************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
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
 * A transactional service supporting persistence of the StudentActivity model and filtering using the restfulApi plugin.
 * */
@Transactional
class StudentActivityService extends ServiceBase {

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
                             "activityType": [Operators.EQUALS, Operators.CONTAINS, Operators.STARTS_WITH, "lt", Operators.LESS_THAN, "gt", Operators.GREATER_THAN]]
        RestfulApiValidationUtility.validateCriteria(QueryBuilder.createFilters(map), allowedFields)

        // activityType references a validation table, so adjust the binding
        filterMap.criteria.each {
            if (it.key == "activityType") it.binding= "activityType.code"
        } 
        
        def studentActivityQuery = """from StudentActivity a"""
        def studentActivityList = new DynamicFinder(StudentActivity.class, studentActivityQuery, "a").find([params: filterMap.params, criteria: filterMap.criteria],
                                                                          filterMap.pagingAndSortParams)

        return studentActivityList
    }


    def count(Map map) {

        def filterMap = QueryBuilder.getFilterData(map)

        // If not using filters, defer to the ServiceBase or any other count implementation
        if (filterMap.size() == 0 && !map.params && !map.criteria) return super.count()
        
        // activityType references a validation table, so adjust the binding
        filterMap.criteria.each {
            if (it.key == "activityType") it.binding= "activityType.code"
        }

        def studentActivityQuery = """from StudentActivity a"""
        def studentActivitySize = new DynamicFinder(StudentActivity.class, studentActivityQuery, "a").count([params: filterMap.params, criteria: filterMap.criteria])

        return studentActivitySize
    }


    def get(id) {
        StudentActivity studentActivity
        Map params = [:]

        try {
            def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
            params = RestfulApiRequestParams.get();
        } catch (e) {
        }

        if (params?.pluralizedResourceName) {
            String studentActivityCode = id
            studentActivity = StudentActivity.findByCode(studentActivityCode)
            if (!studentActivity) {
                throw new ApplicationException("StudentActivity", studentActivityCode, new NotFoundException(id: studentActivityCode, entityClassName: StudentActivity.class.simpleName))
            }
        } else {
            studentActivity = super.get(id)
        }
        return studentActivity
    }

}
