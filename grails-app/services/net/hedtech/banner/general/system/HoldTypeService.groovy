/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
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
 * A transactional service supporting persistence of the HoldType model and filtering using the restfulApi plugin.
 * */
@Transactional
class HoldTypeService extends ServiceBase {


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

        def holdQuery = """from HoldType a"""
        def holdList = new DynamicFinder(HoldType.class, holdQuery, "a").find([params: filterMap.params, criteria: filterMap.criteria],
                                                                          filterMap.pagingAndSortParams)

        return holdList
    }


    def count(Map map) {

        def filterMap = QueryBuilder.getFilterData(map)

        // If not using filters, defer to the ServiceBase or any other count implementation
        if (filterMap.size() == 0 && !map.params && !map.criteria) return super.count()

        def holdQuery = """from HoldType a"""
        def holdSize = new DynamicFinder(HoldType.class, holdQuery, "a").count([params: filterMap.params, criteria: filterMap.criteria])

        return holdSize
    }


    def show(Map map) {
        HoldType hold
        if (map?.pluralizedResourceName) { // RESTful API request  for hold
            // map.id represents holdCode
            String holdCode = map.id
            hold = HoldType.findByCode(holdCode)
            if (!hold) {
                throw new ApplicationException("HoldType", holdCode, new NotFoundException(id: holdCode, entityClassName: HoldType.class.simpleName))
            }
        }
        return hold
    }

}
