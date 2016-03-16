/*******************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.restfulapi

import grails.util.Holders
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException

/**
 * Helper class for RESTful APIs
 *
 */
class RestfulApiValidationUtility {

    private final static List<String> ALLOWED_SORTORDER = ['ASC', 'DESC']

    public static final Integer MAX_DEFAULT = 500
    public static final Integer MAX_UPPER_LIMIT = 1000

    /**
     * Check the values for "max" and "offset" in params map and corrects them if required.
     * Note this method updates the provided map so if that is not desired please send cloned map.
     *
     * @param params Map containing keys "max" and "offset".
     * @param maxDefault Default value to be assigned if "max" is absent.  By default it is turned off (zero).
     * @param maxUpperLimit Upper limit for "max".  By default it is turned off (zero).
     */
    public static void correctMaxAndOffset(Map params, Integer maxDefault = 0, Integer maxUpperLimit = 0) {
        // Override "max" upper limit with setting api.<resource name>.page.maxUpperLimit (if available).
        String resourceName = params?.pluralizedResourceName
        if (resourceName) {
            if (Holders.config.api."${resourceName}".page.maxUpperLimit) {
                maxUpperLimit = Holders.config.api."${resourceName}".page.maxUpperLimit
            }
        }

        if (maxUpperLimit > 0) {
            if (maxDefault < 1 || maxDefault > maxUpperLimit) {
                maxDefault = maxUpperLimit
            }
        }
        // MAX
        if (params.max) {
            if (params.max.isInteger()) {
                Integer max = params.max.toInteger()
                if (max < 1) {
                    if (maxDefault > 0) {
                        params.max = maxDefault.toString()
                    } else {
                        params.remove("max")
                    }
                } else if (maxUpperLimit > 0 && max > maxUpperLimit) {
                    params.max = maxUpperLimit.toString()
                }
            } else {
                if (maxDefault > 0) {
                    params.max = maxDefault.toString()
                } else {
                    params.remove("max")
                }
            }
        } else {
            if (maxDefault > 0) {
                params << [max: maxDefault.toString()]
            }
        }
        // OFFSET
        if (params.offset) {
            if (params.offset.isInteger()) {
                Integer offset = params.offset.toInteger()
                if (offset < 0) {
                    params.offset = "0"
                }
            } else {
                params.offset = "0"
            }
        }
    }


    public static void validateSortField(String sortField, List allowedSortFields) {
        if (sortField && allowedSortFields) {
            if (!allowedSortFields.contains(sortField)) {
                throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidSortField", [sortField])
            }
        }
    }

    /**
     * Validates each filter (criterion).
     *
     * Also validates field and operator used in each criterion on providing optional parameters "allowedSearchFields" and "allowedOperators".
     * Note that if you have different set of operators for each field then this method should not be used.
     *
     * @param filters List of filters returned by QueryBuilder.createFilters method. Each filter is map with four keys "field", "operator", "value", "type".
     * @param allowedSearchFields List of allowed values for "field" in each filter.
     * @param allowedOperators List of allowed values for "operator" in each filter.
     */
    public static void validateCriteria(def filters, def allowedSearchFields = [], def allowedOperators = []) {
        filters?.each {
            String field = it.field
            String operator = it.operator
            String value = it.value
            if (!field || !operator || !value) {
                throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidCriterion")
            } else {
                // Validate field
                if (allowedSearchFields && !allowedSearchFields.contains(field)) {
                    throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidSearchFieldInCriterion")
                }
                // Validate operator
                if (allowedOperators && !allowedOperators.contains(operator)) {
                    throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidOperatorInCriterion")
                }
            }
        }
    }

    /**
     * Validates each filter (criterion).  Also validates field and operator used in each criterion using second parameter "map".
     * This method should be used, if the operators supported for each search field are different.
     *
     * @param filters List of filters returned by QueryBuilder.createFilters method. Each filter is map with four keys "field", "operator", "value", "type".
     * @param map Map where all keys are allowed search fields.  For each key (field), the value is list of allowed operators.
     */
    public static void validateCriteria(def filters, Map map) {
        filters?.each {
            String field = it.field
            String operator = it.operator
            String value = it.value
            if (!field || !operator || !value) {
                throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidCriterion")
            } else {
                if (map) {
                    if (map.containsKey(field)) {
                        def allowedOperators = map[field]
                        if (allowedOperators && !allowedOperators.contains(operator)) {
                            throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidOperatorInCriterion")
                        }
                    } else {
                        throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidSearchFieldInCriterion")
                    }
                }
            }
        }
    }


    public static void throwValidationExceptionForObjectNotFound(String objectName, String objectId) {
        throw new RestfulApiValidationException("default.not.found.message", [objectName, objectId])
    }


    public static void copyProperties(source, target) {
        if (source && target) {
            target.metaClass.properties.each {
                if (source.metaClass.hasProperty(source, it.name) && it.name != 'metaClass' && it.name != 'class') {
                    try {
                        it.setProperty(target, source.metaClass.getProperty(source, it.name))
                    } catch (GroovyRuntimeException e) {
                        // May be read-only property
                    }
                }
            }
        }
    }


    public static def cloneMapExcludingKeys(map, def keys = []) {
        def clonedMap = map?.clone()
        keys?.each {
            if (clonedMap?.containsKey(it)) {
                clonedMap.remove(it)
            }
        }
        return clonedMap
    }


    public static void validateSortOrder(String sortOrder) {
        if (sortOrder) {
            if (!ALLOWED_SORTORDER.contains(sortOrder.toUpperCase())) {
                throw new RestfulApiValidationException("RestfulApiValidationUtility.invalidSortOrder", [sortOrder])
            }
        }
    }

    /**
     * This method can be used when it is necessary to distinguish between "api" and "qapi" requests with in service.list() method.
     *
     * @param params
     * @return
     */
    public static boolean isQApiRequest(Map params) {
        boolean qapiReq = false
        if (params && params.action) {
            if (params.action["POST"] == "list") {
                qapiReq = true
            }
        }
        return qapiReq
    }


    public static getRequestParams() {
        try {  // Avoid restful-api plugin dependencies.
            Class fetchedClass = Class.forName(
                    "net.hedtech.banner.restfulapi.RestfulApiRequestParams",
                    true,
                    Thread.currentThread().getContextClassLoader()
            )
            fetchedClass.get()
        }
        catch (ClassNotFoundException e) {
            return null
        }
    }

    /**
     * check if given input string is a possible XSS attack
     *
     * @param inputField
     */
    public static void checkXSS(String inputField) {
        String encodedInputField = inputField?.encodeAsHTML()
        if (encodedInputField != inputField) {
            throw new ApplicationException('RestfulApiValidationUtility', new BusinessLogicValidationException("possible.XSS.attack", null))
        }
    }

}
