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

       // def filterMap = QueryBuilder.getFilterData(map)
        def filterMap = getFilterData(map)
        // If not using filters, defer to the ServiceBase or any other list implementation
        if (filterMap?.size() == 0 && !map.params && !map.criteria) return super.list(map)

        def termQuery = """from Term a"""
        def termList = new DynamicFinder(Term.class, termQuery, "a").find([params: filterMap.params, criteria: filterMap.criteria],
                                                                          filterMap.pagingAndSortParams)

        return termList
    }


    def count(Map map) {

       // def filterMap = QueryBuilder.getFilterData(map)
        def filterMap = getFilterData(map)


        // If not using filters, defer to the ServiceBase or any other count implementation
        if (filterMap.size() == 0 && !map.params && !map.criteria) return super.count()

        def termQuery = """from Term a"""
        def termSize = new DynamicFinder(Term.class, termQuery, "a").count([params: filterMap.params, criteria: filterMap.criteria])

        return termSize
    }

    /**
     * TODO: Remove this method once banner_core QueryBuilder.getFilterData is available
     *
     * @param map
     * @return
     */
    public static def getFilterData(Map map) {
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

    /**
     * TODO: Remove this method once banner_core QueryBuilder.createFilters is available
     *
     * @param params
     * @return
     */
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


    /**
     * TODO: Remove this method once banner_core QueryBuilder.parseDate is available
     *
     * @param params
     * @return
     */
    private static Date parseDate(def params, filter) {
        if (filter.value == null) return null
        //see if its numeric, if so, treat as millis since Epoch
        try {
            Long l = Long.valueOf(filter.value)
            return new Date(l)
        } catch (Exception e) {
            //can't parse as a long
        }
        //try to parse as ISO 8601
        try {
            def cal = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(filter.value)
            return cal.toGregorianCalendar().getTime()
        } catch (Exception e) {
            //can't parse as ISO 8601
        }
        //wasn't able to parse as a date
        throw new Exception(params.pluralizedResourceName + filter + "exception")//BadDateFilterException(params.pluralizedResourceName,filter)
    }

}
