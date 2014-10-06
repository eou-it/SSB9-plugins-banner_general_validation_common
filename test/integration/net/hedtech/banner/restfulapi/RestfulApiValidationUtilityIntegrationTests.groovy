/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.restfulapi
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.system.Term
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase

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
        assertFalse params.containsKey("offset")

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


	@Ignore
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
        shouldFail(RestfulApiValidationException) { RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields) }

        // All filters have valid "operator"
        allowedSearchFields = ["firstName", "lastName"]
        def allowedOperators = [Operators.EQUALS, Operators.CONTAINS]
        RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators)

        // Invalid "operator" in second filter, should cause exception
        allowedSearchFields = ["firstName", "lastName"]
        allowedOperators = [Operators.EQUALS, Operators.STARTS_WITH]
        shouldFail(RestfulApiValidationException) { RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators) }
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
        shouldFail(RestfulApiValidationException, { RestfulApiValidationUtility.throwValidationExceptionForObjectNotFound("Term", "201410") })
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

}
