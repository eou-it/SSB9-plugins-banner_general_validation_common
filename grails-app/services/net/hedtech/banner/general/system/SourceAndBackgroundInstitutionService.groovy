/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
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
 * A transactional service supporting persistence of the SourceAndBackgroundInstitution model and filtering using the restfulApi plugin.
 * */
class SourceAndBackgroundInstitutionService extends ServiceBase {

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
        def paramsMap = [:]
        def criteriaMap = []
        def filtered = createFilters(map)

        // If not using filters, defer to the ServiceBase or any other list implementation
        if (filtered.size() == 0 && !map.params && !map.criteria) return super.list(map)

        // Otherwise prepare each restfulApi filter (putting it into maps for DynamicFinder)
        filtered.each {
            paramsMap.put(it["field"], it["value"])
            criteriaMap.add([key: it["field"], binding: it["field"], operator: operatorConversions[it["operator"]]])
        }

        // If criteria are passed in with the correct format already, just copy them.
        if (map?.containsKey("params")) paramsMap.putAll(map.params)
        if (map?.containsKey("criteria")) criteriaMap.addAll(map.criteria)

        def pagingAndSortParams = [:]
        if (map?.containsKey("max")) pagingAndSortParams.put("max", map["max"].toInteger())
        if (map?.containsKey("offset")) pagingAndSortParams.put("offset", map["offset"].toInteger())
        if (map?.containsKey("sort")) pagingAndSortParams.put("sort", map["sort"])
        if (map?.containsKey("pagingAndSortParams")) pagingAndSortParams.putAll(map.pagingAndSortParams)

        map = [params: paramsMap, criteria: criteriaMap]
        def sourceAndBackgroundInstitutionQuery = """from SourceAndBackgroundInstitution a"""
        def sourceAndBackgroundInstitutionList = new DynamicFinder(SourceAndBackgroundInstitution.class, sourceAndBackgroundInstitutionQuery, "a").find(map, pagingAndSortParams)

        return sourceAndBackgroundInstitutionList
    }


    def count(Map map) {
        def paramsMap = [:]
        def criteriaMap = []
        def filtered = createFilters(map)

        // If not using filters, defer to the ServiceBase or any other count implementation
        if (filtered.size() == 0 && !map.params && !map.criteria) return super.count(map)

        // Otherwise prepare each restfulApi filter (putting it into maps for DynamicFinder)
        filtered.each {
            paramsMap.put(it["field"], it["value"])
            criteriaMap.add([key: it["field"], binding: it["field"], operator: operatorConversions[it["operator"]]])
        }

        // If criteria are passed in with the correct format already, just copy them.
        if (map?.containsKey("params")) paramsMap.putAll(map.params)
        if (map?.containsKey("criteria")) criteriaMap.addAll(map.criteria)

        map = [params: paramsMap, criteria: criteriaMap]
        def sourceAndBackgroundInstitutionQuery = """from SourceAndBackgroundInstitution a"""
        def sourceAndBackgroundInstitutionSize = new DynamicFinder(SourceAndBackgroundInstitution.class, sourceAndBackgroundInstitutionQuery, "a").count(map)

        return sourceAndBackgroundInstitutionSize
    }


    private static createFilters(Map params) {
        def filterRE = /filter\[([0-9]+)\]\[(field|operator|value|type)\]=(.*)/
        def filters = [:]
        def matcher

        params.each {
            if (it.key.startsWith('filter')) {
                matcher = (it =~ filterRE)
                if (matcher.count) {
                    // Regex matches are broken up into parts for ease of understanding
                    // toString is called to convert GStrings to strings, which is important to note.
                    def filterNumber = "${matcher[0][1]}".toString()
                    def key = "${matcher[0][2]}".toString()
                    def value = "${matcher[0][3]}".toString()
                    if (!filters.containsKey(filterNumber)) filters.put(filterNumber, [:])
                    filters[filterNumber]?.put(key, value)
                }
            }
        }
        return filters.values()
    }
}
