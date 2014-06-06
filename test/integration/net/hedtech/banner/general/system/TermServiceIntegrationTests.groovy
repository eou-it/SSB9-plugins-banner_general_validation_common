/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore

/**
 * Service Integration  test for the 'term' model.
 * */
class TermServiceIntegrationTests extends BaseIntegrationTestCase {

    def termService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testTermListAll() {
        def list = termService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201410" }

    }


    void testTermListAllWithPagination() {
        def params = ["max": 50, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 50, list.size()
        assertTrue list[0] instanceof Term

    }


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


    void testTermListFilteredWithPagination() {

        def params = ["filter[0][field]": "code", "filter[0][value]": "201%", "filter[0][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof Term

    }

    void testTermListApiFilterByDate() {
        def term = Term.findByCode("201610")
        assertTrue term.lastModified >= new Date("01/01/2010")
        def params = ["filter[0][field]": "lastModified", "filter[0][value]": "2010-01-01T01:00:00-00:00", "filter[0][operator]": "gt", "filter[0][type]": "date"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201610" }
    }


    void testTermListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall%", "filter[1][operator]": "contains"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


    void testTermListMultipleFiltersContainsWithoutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall", "filter[1][operator]": "contains"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


    void testTermListMultipleFiltersContainsStartsWthOutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201", "filter[0][operator]": "startswith"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


    void testTermListMultipleFiltersContainsStartsWthWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201%", "filter[0][operator]": "startswith"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Term
        assertNotNull list.find { it.code == "201010" }
    }


    void testTermListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "201410", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "fall%", "filter[1][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = termService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof Term
    }


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
    void testGetWithValidTerm() {
        def args = formMapForGet()
        def RestfulApiRequestParams = (net.hedtech.banner.restfulapi.RestfulApiRequestParams as Class)
        RestfulApiRequestParams.set(args)
        def term = termService.get(args.id)
        assertNotNull term
        assertEquals term.code, "201410"
    }


    @Ignore
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
