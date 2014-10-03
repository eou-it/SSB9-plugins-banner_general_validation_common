/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore

/**
 * Service Integration  test for the 'term' model.
 * */
class TermServiceIntegrationTests extends BaseIntegrationTestCase {

    def termService


	@Before
	public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	@Test
    void testTermListAll() {
        def list = termService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201410" }

    }


	@Test
    void testTermListAllWithPagination() {
        def params = ["max": 50, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 50, list.size()
        assertTrue list[0] instanceof Term

    }


	@Test
    void testTermListFilteredNoPagination() {
        def termControlList = Term.findAllByCodeIlike("%201%")
        assertTrue termControlList.size() > 0
        assertNotNull termControlList.find { it.code == "201410" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "201", "filter[0][operator]": "contains"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals termControlList.size(), list.size()
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201410" }

    }


	@Test
    void testTermListFilteredStartsWith() {
        def termControlList = Term.findAllByCodeIlike("201%")
        assertTrue termControlList.size() > 0
        assertNotNull termControlList.find { it.code == "201410" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "201", "filter[0][operator]": "startswith"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals termControlList.size(), list.size()
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201410" }

    }


	@Test
    void testTermListFilteredWithPagination() {

        def params = ["filter[0][field]": "code", "filter[0][value]": "201%", "filter[0][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof Term

    }

	@Test
    void testTermListApiFilterByDate() {
        def term = Term.findByCode("201610")
        assertTrue term.lastModified >= new Date("01/01/2010")
        def params = ["filter[0][field]": "lastModified", "filter[0][value]": "2010-01-01T01:00:00-00:00", "filter[0][operator]": "gt", "filter[0][type]": "date"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201610" }
    }


	@Test
    void testTermListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall%", "filter[1][operator]": "contains"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


	@Test
    void testTermListMultipleFiltersContainsWithoutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall", "filter[1][operator]": "contains"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


	@Test
    void testTermListMultipleFiltersContainsStartsWthOutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201", "filter[0][operator]": "startswith"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


	@Test
    void testTermListMultipleFiltersContainsStartsWthWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201%", "filter[0][operator]": "startswith"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


	@Test
    void testTermListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall%", "filter[1][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof Term
    }


	@Test
    void testListCodeFilterSupportedOperators() {
        def params = ["filter[0][field]": "code", "filter[0][operator]": "equals", "filter[0][value]": "201410"]
        def list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "contains", "filter[0][value]": "2014"]
        list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "startswith", "filter[0][value]": "2014"]
        list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "lessthan", "filter[0][value]": "201410"]
        list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "code", "filter[0][operator]": "greaterthan", "filter[0][value]": "201410"]
        list = termService.list(params)
        assertTrue list.size() > 0
    }


	@Test
    void testListDescriptionFilterSupportedOperators() {
        def params = ["filter[0][field]": "description", "filter[0][operator]": "equals", "filter[0][value]": "Fall 2013(201410)"]
        def list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "description", "filter[0][operator]": "equalsignorecase", "filter[0][value]": "faLl 2013(201410)"]
        list = termService.list(params)
        assertTrue list.size() > 0

        params = ["filter[0][field]": "description", "filter[0][operator]": "contains", "filter[0][value]": "fall"]
        list = termService.list(params)
        assertTrue list.size() > 0
    }


	@Test
    void testListDescriptionFilterUnsupportedOperator() {
        // Unsupported operator
        def params = ["filter[0][field]": "description", "filter[0][operator]": "startswith", "filter[0][value]": "fall"]
        shouldFail(RestfulApiValidationException) { termService.list(params) }

        // Invalid operator (equalsignorecas - missing 'e' at end)
        params = ["filter[0][field]": "description", "filter[0][operator]": "equalsignorecas", "filter[0][value]": "faLl 2013(201410)"]
        shouldFail(RestfulApiValidationException) { termService.list(params) }
    }


	@Test
    void testTermCount() {
        def termControlList = Term.findAllByCodeIlike("201%")
        assertTrue termControlList.size() > 0
        assertNotNull termControlList.find { it.code == "201410" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "201%", "filter[0][operator]": "contains"]
        def listSize = termService.count(params)
        assertTrue listSize > 0
        assertEquals termControlList.size(), listSize
    }


	@Test
    void testTermListNonApiFilter() {
        def termControlList = Term.findAllByCodeIlike("201%")
        assertTrue termControlList.size() > 0
        assertNotNull termControlList.find { it.code == "201410" }
        // now try the new service (with parameters already in the correct form, not from the restfulApi plugin)
        def filterCriteria = ["params": ["code": "201%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]]]
        def list = termService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals termControlList.size(), list.size()
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201410" }
    }


	@Test
    void testTermListNonApiFilterWithPagination() {
        def filterCriteria = ["params": ["code": "201%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]],
                "pagingAndSortParams": ["max": 10, "offset": 2, "sort": "code"]]
        def list = termService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof Term
    }


    @Ignore
	@Test
    void testGetWithValidTerm() {
        def args = formMapForGet()
        def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
        RestfulApiRequestParams.set(args)
        def term = termService.get(args.id)
        assertNotNull term
        assertEquals term.code, "201410"
    }


    @Ignore
	@Test
    void testGetWithInvalidTermCode() {
        def args = formMapForGet()
        args << [id: "wwwwwww"]
        def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
        RestfulApiRequestParams.set(args)
        shouldFail(ApplicationException) {
            def term = termService.get(args.id)
        }
    }


    private def formMapForGet() {
        Map args = [id: "201410", pluralizedResourceName: "terms"]
        return args
    }

}
