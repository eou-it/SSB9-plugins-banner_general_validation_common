/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.restfulapi

import net.hedtech.banner.testing.BaseIntegrationTestCase

class RestfulApiValidationUtilityIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCorrectMaxAndOffset() {
        // No "max" will be added in the absence of maxDefault
        def params = [:]
        RestfulApiValidationUtility.correctMaxAndOffset(params)
        assertFalse params.containsKey("max")
        assertFalse params.containsKey("offset")

        // Invalid "max" will be removed in the absence of maxDefault
        // Invalid "offset" will be replaced with 0
        params = ["max": "-1", "offset": "-1"]
        RestfulApiValidationUtility.correctMaxAndOffset(params)
        assertFalse params.containsKey("max")
        assertEquals "0", params["offset"]

        // "max" will added with value maxDefault
        Integer maxDefault = 10
        params = [:]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault)
        assertEquals maxDefault.toString(), params["max"]

        // Invalid "max" will be corrected to maxDefault
        params = ["max": "abc"]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault)
        assertEquals maxDefault.toString(), params["max"]

        // Exceeding "max" will be corrected to maxUpperLimit
        Integer maxUpperLimit = 30
        params = [max: "40"]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault, maxUpperLimit)
        assertEquals maxUpperLimit.toString(), params["max"]
    }


    void testCloneMapExcludingKeys() {
        def map = ["key1": "val1", "key2": "val2"]

        // Just clone
        def clonedMap = RestfulApiValidationUtility.cloneMapExcludingKeys(map)
        assertTrue clonedMap.size() == 2

        // Clone and remove "key2"
        clonedMap = RestfulApiValidationUtility.cloneMapExcludingKeys(map, ["key2"])
        assertTrue clonedMap.size() == 1
        assertFalse clonedMap.containsKey("key2")
    }

}
