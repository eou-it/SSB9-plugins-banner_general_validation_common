/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

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
        def filterMap = getFilterData(map)
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
        def filterMap = getFilterData(map)
        // If not using filters, defer to the ServiceBase or any other list implementation
        if (filterMap?.size() == 0 && !map.params && !map.criteria) return super.count()

        def sourceAndBackgroundInstitutionQuery = """from SourceAndBackgroundInstitution a"""
        def sourceAndBackgroundInstitutionSize = new DynamicFinder(SourceAndBackgroundInstitution.class,
                                                                   sourceAndBackgroundInstitutionQuery, "a").count([params: filterMap.params,
                criteria: filterMap.criteria])

        return sourceAndBackgroundInstitutionSize
    }


    /**
     * prepare filter map for dynamic finder given map of criteria from zk page or restful API interface
     * @param map
     * @return map of filters ready for dynamic finder
     */
    private def getFilterData(Map map) {
        def paramsMap = [:]
        def criteriaMap = []
        def pagingAndSortParams = [:]

        def filtered = createFilters(map)

        def hqlBuilderOperators = ["eq": Operators.EQUALS, "lt": Operators.LESS_THAN, "gt": Operators.GREATER_THAN]

        // Prepare each restfulApi filter (putting it into maps for DynamicFinder)
        filtered.each {
            if (hqlBuilderOperators.containsKey(it["operator"])) {
                // For backward compatibility convert old HQLBuilder operator to DynamicFinder operator
                it["operator"] = hqlBuilderOperators[it["operator"]]
            } else {
                // filter[index][operator] value is from net.hedtech.banner.query.operators.Operators
                // No validation done here.  Will be passed to DynamicFinder directly.
            }

            if (it.containsKey("type")) {
                // URL parameter "filter[index][type]" exists.  Either "numeric" or "date".
                if (it["type"] == "num" || it["type"] == "number") {
                    it["value"] = it["value"].toLong()
                } else if (it["type"] == "date") {
                    it["value"] = parseDate(map, it)
                }
            }

            if (it["operator"] == "contains" && !(it["value"].contains("%"))) it["value"] = "%${it["value"]}%"
            else if (it["operator"] == "startswith" && !(it["value"].contains("%"))) it["value"] = "${it["value"]}%"

            paramsMap.put(it["field"], it["value"])
            criteriaMap.add([key: it["field"], binding: it["field"], operator: it["operator"]])
        }

        // If criteria are passed in with the correct format already, just copy them.
        if (map?.containsKey("params")) paramsMap.putAll(map.params)
        if (map?.containsKey("criteria")) criteriaMap.addAll(map.criteria)

        // pull out the pagination criteria
        if (map?.containsKey("max")) pagingAndSortParams.put("max", map["max"].toInteger())
        if (map?.containsKey("offset")) pagingAndSortParams.put("offset", map["offset"].toInteger())
        // sort
        if (map?.containsKey("sort")) {
            pagingAndSortParams.put("sortColumn", map["sort"])
            if (map?.containsKey("order")) pagingAndSortParams.put("sortDirection", map["order"])
        } else if (map?.containsKey("sortCriteria") && map["sortCriteria"] instanceof Collection) {
            pagingAndSortParams.put("sortCriteria", map["sortCriteria"])
        }
        if (map?.containsKey("pagingAndSortParams")) pagingAndSortParams.putAll(map.pagingAndSortParams)

        return [params: paramsMap, criteria: criteriaMap, pagingAndSortParams: pagingAndSortParams]
    }


    public static def createFilters(def map) {
        def filterRE = /filter\[([0-9]+)\]\[(field|operator|value|type)\]=(.*)/
        def filters = [:]
        def matcher
        // find if the operator is contains or startswith

        map.each {
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
