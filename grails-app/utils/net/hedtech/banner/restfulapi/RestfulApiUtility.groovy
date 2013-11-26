/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.restfulapi

/**
 * Helper class for RESTful APIs
 *
 */
class RestfulApiUtility {

    public static void correctMaxAndOffset(Map params, Integer maxDefault = 0, Integer maxUpperLimit = 0) {
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

}
