/*********************************************************************************
 Copyright 2010-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.restfulapi

import grails.util.Holders
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.Term
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class RestfulApiValidationUtilityIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCorrectMaxAndOffset() {
        // No "max" will be added in the absence of maxDefault
        def params = [:]
        RestfulApiValidationUtility.correctMaxAndOffset(params)
        assertFalse params.containsKey("max")

        // Invalid "max" will be removed in the absence of maxDefault
        // Invalid "offset" will be replaced with 0
        params = ["max": "-1", "offset": "-1"]
        RestfulApiValidationUtility.correctMaxAndOffset(params)
        assertFalse params.containsKey("max")
        assertEquals "0", params["offset"]

        // "max" will added with value maxDefault
        Integer maxDefault = RestfulApiValidationUtility.MAX_DEFAULT
        params = [:]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault)
        assertEquals maxDefault.toString(), params["max"]

        // Invalid "max" will be corrected to maxDefault
        params = ["max": "abc"]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault)
        assertEquals maxDefault.toString(), params["max"]

        // Exceeding "max" will be corrected to maxUpperLimit
        Integer maxUpperLimit = RestfulApiValidationUtility.MAX_UPPER_LIMIT
        params = [max: "1000"]
        RestfulApiValidationUtility.correctMaxAndOffset(params, maxDefault, maxUpperLimit)
        assertEquals maxUpperLimit.toString(), params["max"]
    }


    @Test
    void testCorrectMaxAndOffset_OverrideMaxUpperLimit() {
        String pluralizedResourceName = "instructional-events"
        def params = [:]

        // No pluralizedResourceName
        RestfulApiValidationUtility.correctMaxAndOffset(params, 0, 30)
        assertEquals "30", params.max

        // No override setting in config file
        params = [pluralizedResourceName: pluralizedResourceName]
        RestfulApiValidationUtility.correctMaxAndOffset(params, 0, 30)
        assertEquals "30", params.max

        // Override setting available in config file
        params = [pluralizedResourceName: pluralizedResourceName]
        def propApi = [:]
        propApi.put(pluralizedResourceName, ["page": ["maxUpperLimit": 234]])
        Holders.config.setProperty("api", propApi)
        RestfulApiValidationUtility.correctMaxAndOffset(params, 0, 30)
        Holders.config.remove("api")
        assertEquals "234", params.max

        // Override setting with zero value will not impact
        params = [pluralizedResourceName: pluralizedResourceName]
        propApi.put(pluralizedResourceName, ["page": ["maxUpperLimit": 0]])
        Holders.config.setProperty("api", propApi)
        RestfulApiValidationUtility.correctMaxAndOffset(params, 0, 30)
        Holders.config.remove("api")
        assertEquals "30", params.max
    }

    // TODO HRU-5518
    @Ignore
    @Test
    void testValidateSortField() {
        def allowedSortFields = ["firstName", "lastName"]

        // Valid sort field should not cause any exception
        RestfulApiValidationUtility.validateSortField("firstName", allowedSortFields)

        // Invalid sort field causes RestfulApiValidationException
        try {
            RestfulApiValidationUtility.validateSortField("dummy", allowedSortFields)
            fail("Should have failed with RestfulApiValidationException")
        } catch (RestfulApiValidationException rve) {
            assertEquals 400, rve.getHttpStatusCode()
            def localizer = { mapToLocalize ->
                this.message(mapToLocalize)
            }
            def map = rve.returnMap(localizer)
            assertNotNull map.headers
            assertEquals "Validation failed", map.headers["X-Status-Reason"]
            assertNotNull map.message
            assertNotNull map.errors
        }
    }


    @Test
    void testValidateCriteria() {
        // Valid "field" "operator" "value"
        def filters = [["field": "firstName", "operator": "equals", "value": "Cliff"], ["field": "lastName", "operator": "contains", "value": "star"]]
        RestfulApiValidationUtility.validateCriteria(filters)

        // Invalid "value" in second filter, should cause exception
        filters = [["field": "firstName", "operator": "equals", "value": "Cliff"], ["field": "lastName", "operator": "contains", "value": null]]
        shouldFail(RestfulApiValidationException) { RestfulApiValidationUtility.validateCriteria(filters) }

        // All filters have valid "field"
        def allowedSearchFields = ["firstName", "lastName"]
        filters = [["field": "firstName", "operator": "equals", "value": "Cliff"], ["field": "lastName", "operator": "contains", "value": "star"]]
        RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields)

        // Invalid "field" in second filter, should cause exception
        allowedSearchFields = ["firstName", "middleName"]
        shouldFail(RestfulApiValidationException) {
            RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields)
        }

        // All filters have valid "operator"
        allowedSearchFields = ["firstName", "lastName"]
        def allowedOperators = [Operators.EQUALS, Operators.CONTAINS]
        RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators)

        // Invalid "operator" in second filter, should cause exception
        allowedSearchFields = ["firstName", "lastName"]
        allowedOperators = [Operators.EQUALS, Operators.STARTS_WITH]
        shouldFail(RestfulApiValidationException) {
            RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators)
        }
    }


    @Test
    void testValidateCriteriaMapVersion() {
        // Valid "field" "operator" "value"
        def filters = [["field": "firstName", "operator": "equals", "value": "Cliff"], ["field": "lastName", "operator": "contains", "value": "star"]]
        def map = [:]
        RestfulApiValidationUtility.validateCriteria(filters, map)

        // All filters have valid "field" and "operator"
        map = ["firstName": [Operators.EQUALS, Operators.STARTS_WITH], "lastName": [Operators.EQUALS_IGNORE_CASE, Operators.CONTAINS]]
        RestfulApiValidationUtility.validateCriteria(filters, map)

        // Invalid "field" in second filter, should cause exception
        map = ["firstName": [Operators.EQUALS, Operators.STARTS_WITH]]
        shouldFail(RestfulApiValidationException) { RestfulApiValidationUtility.validateCriteria(filters, map) }

        // Invalid "operator" in second filter, should cause exception
        map = ["firstName": [Operators.EQUALS, Operators.STARTS_WITH], "lastName": [Operators.EQUALS_IGNORE_CASE, Operators.EQUALS]]
        shouldFail(RestfulApiValidationException) { RestfulApiValidationUtility.validateCriteria(filters, map) }
    }


    @Test
    void testThrowValidationExceptionForObjectNotFound() {
        shouldFail(RestfulApiValidationException, {
            RestfulApiValidationUtility.throwValidationExceptionForObjectNotFound("Term", "201410")
        })
    }


    @Test
    void testCopyProperties() {
        def code = "201410"
        def desc = "201410 term"
        def source = new Term("code": code, "description": desc)
        def target = new Term()
        RestfulApiValidationUtility.copyProperties(source, target)
        assertEquals code, target["code"]
        assertEquals desc, target["description"]
    }


    @Test
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

    //Can't test getting params without restful-api plugin,
    // so just test the result when plugin isn't loaded.
    @Test
    void testGetRequestParams() {
        assertNull RestfulApiValidationUtility.getRequestParams()
    }


    @Test
    void testValidGuid() {
        def errMsg01 = shouldFail(ApplicationException, {
            RestfulApiValidationUtility.validateGUID(null)
        })
        assertEquals("guid.error.invalid.null", errMsg01)

        def errMsg02 = shouldFail(ApplicationException, {
            RestfulApiValidationUtility.validateGUID("00000000-0000-0000-0000-000000000000")
        })
        assertEquals("guid.error.invalid.nilGUID", errMsg02)

        def errMsg03 = shouldFail(ApplicationException, {
            RestfulApiValidationUtility.validateGUID("not a guid")
        })
        assertEquals("guid.error.invalid", errMsg03)

        //verson != 4
        def errMsg04 = shouldFail(ApplicationException, {
            RestfulApiValidationUtility.validateGUID("C56A4180-65AA-12EC-A945-5FD21DEC0538")
        })
        assertEquals("guid.error.invalid", errMsg04)

        assertTrue RestfulApiValidationUtility.validateGUID("00000000-0000-0000-0000-000000000000", true)

        assertTrue RestfulApiValidationUtility.validateGUID("C56A4180-65AA-42EC-A945-5FD21DEC0538")
        assertTrue RestfulApiValidationUtility.validateGUID("C56A4180-65AA-42EC-A945-5FD21DEC0538".toLowerCase())
        assertTrue RestfulApiValidationUtility.validateGUID("C56A418065AA42ECA9455FD21DEC0538".toLowerCase())
        //still a guid
        assertTrue RestfulApiValidationUtility.validateGUID("C56A4180-65AA42ECA945-5FD21DEC0538".toLowerCase())
        //still a guid :)
    }


    @Test
    void testIsValidVersion4UUID() {
        assertFalse RestfulApiValidationUtility.isValidVersion4UUID(null)
        assertFalse RestfulApiValidationUtility.isValidVersion4UUID("    ")
        assertFalse RestfulApiValidationUtility.isValidVersion4UUID(GeneralValidationCommonConstants.NIL_GUID)
        assertTrue RestfulApiValidationUtility.isValidVersion4UUID("2052f903-595a-43d8-b9ce-4ec407e86a59")
        assertTrue RestfulApiValidationUtility.isValidVersion4UUID("2052F903-595A-43D8-B9CE-4EC407E86A59")
    }

}
