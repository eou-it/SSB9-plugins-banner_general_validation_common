/** *****************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.After
import org.junit.Before
import org.junit.Test

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Service Integration  test for the 'studentActivity' model.
 * */
class StudentActivityServiceIntegrationTests extends BaseIntegrationTestCase {

    def studentActivityService


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
    void testStudentActivityListAll() {
        def list = studentActivityService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }

    }


    @Test
    void testStudentActivityListAllWithPagination() {
        def params = ["max": 50, "offset": 2, "sort": "code"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertEquals 50, list.size()
        assertTrue list[0] instanceof StudentActivity

    }


    @Test
    void testStudentActivityListFilteredNoPagination() {
        def studentActivityControlList = StudentActivity.findAllByCodeIlike("%3%")
        assertTrue studentActivityControlList.size() > 0
        assertNotNull studentActivityControlList.find { it.code == "130" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "3", "filter[0][operator]": "contains"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertEquals studentActivityControlList.size(), list.size()
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }

    }


    @Test
    void testStudentActivityListFilteredStartsWith() {
        def studentActivityControlList = StudentActivity.findAllByCodeIlike("1%")
        assertTrue studentActivityControlList.size() > 0
        assertNotNull studentActivityControlList.find { it.code == "130" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "1", "filter[0][operator]": "startswith"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertEquals studentActivityControlList.size(), list.size()
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }

    }


    @Test
    void testStudentActivityListFilteredWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "1", "filter[0][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof StudentActivity

    }


    @Test
    void testStudentActivityListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "130", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "Debate", "filter[1][operator]": "contains"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "101" }
    }


    @Test
    void testStudentActivityListMultipleFiltersContainsWithoutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "130", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "Debate", "filter[1][operator]": "contains"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "101" }
    }


    @Test
    void testStudentActivityListMultipleFiltersContainsStartsWthOutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "1", "filter[0][operator]": "startswith"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }
    }


    @Test
    void testStudentActivityListMultipleFiltersContainsStartsWthWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "1%", "filter[0][operator]": "startswith"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }
    }


    @Test
    void testStudentActivityListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "300", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "Team", "filter[1][operator]": "contains",
                "max": 4, "offset": 2, "sort": "code"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0
        assertEquals 4, list.size()
        assertTrue list[0] instanceof StudentActivity
    }


    @Test
    void testListCodeFilterSupportedOperators() {
        def params = ["filter[0][field]": "code", "filter[0][operator]": "equals", "filter[0][value]": "130"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "contains", "filter[0][value]": "3"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "startswith", "filter[0][value]": "13"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "lessthan", "filter[0][value]": "130"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "greaterthan", "filter[0][value]": "130"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0
    }


    @Test
    void testListDescriptionFilterSupportedOperators() {
        def params = ["filter[0][field]": "description", "filter[0][operator]": "equals", "filter[0][value]": "Debate Club"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "description", "filter[0][operator]": "equalsignorecase", "filter[0][value]": "debate club"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "description", "filter[0][operator]": "contains", "filter[0][value]": "Debate"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0
    }


    @Test
    void testListDescriptionFilterUnsupportedOperator() {
        // Unsupported operator
        def params = ["filter[0][field]": "description", "filter[0][operator]": "startswith", "filter[0][value]": "Debate"]
        shouldFail(RestfulApiValidationException) { studentActivityService.list(params) }

        // Invalid operator (equalsignorecas - missing 'e' at end)
        params = ["filter[0][field]": "description", "filter[0][operator]": "equalsignorecas", "filter[0][value]": "debate club"]
        shouldFail(RestfulApiValidationException) { studentActivityService.list(params) }
    }


    @Test
    void testListActivityTypeFilterSupportedOperators() {
        def params = ["filter[0][field]": "activityType", "filter[0][operator]": "equals", "filter[0][value]": "SPRTS"]
        def list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "activityType", "filter[0][operator]": "contains", "filter[0][value]": "R"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "activityType", "filter[0][operator]": "startswith", "filter[0][value]": "SP"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "activityType", "filter[0][operator]": "lessthan", "filter[0][value]": "T"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "activityType", "filter[0][operator]": "greaterthan", "filter[0][value]": "R"]
        list = studentActivityService.list(params)
        assertTrue list.size() > 0
    }


    @Test
    void testStudentActivityCount() {
        def studentActivityControlList = StudentActivity.findAllByCodeIlikeAndActivityType("%1%", ActivityType.findByCode("SPRTS"))
        assertTrue studentActivityControlList.size() > 0
        assertNotNull studentActivityControlList.find { it.code == "130" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "1", "filter[0][operator]": "contains",
                      "filter[1][field]": "activityType", "filter[1][operator]": "equals", "filter[1][value]": "SPRTS"]
        def listSize = studentActivityService.count(params)
        assertTrue listSize > 0
        assertEquals studentActivityControlList.size(), listSize
    }


    @Test
    void testStudentActivityListNonApiFilter() {
        def studentActivityControlList = StudentActivity.findAllByCodeIlike("1%")
        assertTrue studentActivityControlList.size() > 0
        assertNotNull studentActivityControlList.find { it.code == "130" }
        // now try the new service (with parameters already in the correct form, not from the restfulApi plugin)
        def filterCriteria = ["params": ["code": "1%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]]]
        def list = studentActivityService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals studentActivityControlList.size(), list.size()
        assertTrue list[0] instanceof StudentActivity
        assertNotNull list.find { it.code == "130" }
    }


    @Test
    void testStudentActivityListNonApiFilterWithPagination() {
        def filterCriteria = ["params": ["code": "1%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]],
                "pagingAndSortParams": ["max": 10, "offset": 2, "sort": "code"]]
        def list = studentActivityService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof StudentActivity
    }


    @Ignore
    void testGetWithValidStudentActivity() {
        def args = formMapForGet()
        def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
        RestfulApiRequestParams.set(args)
        def studentActivity = studentActivityService.get(args.id)
        assertNotNull studentActivity
        assertEquals studentActivity.code, "130"
    }


    @Ignore
    void testGetWithInvalidStudentActivityCode() {
        def args = formMapForGet()
        args << [id: "wwwwwww"]
        def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
        RestfulApiRequestParams.set(args)
        shouldFail(ApplicationException) {
            def studentActivity = studentActivityService.get(args.id)
        }
    }


    private def formMapForGet() {
        Map args = [id: "130", pluralizedResourceName: "student-activities"]
        return args
    }

}
