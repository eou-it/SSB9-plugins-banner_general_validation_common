/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.restfulapi

/**
 * Helper class for RESTful APIs
 *
 */
class RestfulApiValidationUtility {

    /**
     * Check the values for "max" and "offset" in params map and corrects them if required.
     * Note this method updates the provided map so if that is not desired please send cloned map.
     *
     * @param params Map containing keys "max" and "offset".
     * @param maxDefault Default value to be assigned if "max" is absent.  By default it is turned off (zero).
     * @param maxUpperLimit Upper limit for "max".  By default it is turned off (zero).
     */
    public static void correctMaxAndOffset(Map params, Integer maxDefault = 0, Integer maxUpperLimit = 0) {
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


    public static void copyProperties(def source, def target) {
        target?.metaClass.properties.each {
            if (source?.metaClass.hasProperty(source, it.name) && it.name != 'metaClass' && it.name != 'class')
                it.setProperty(target, source.metaClass.getProperty(source, it.name))
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

}
