/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Service Integration  test for the 'holdType' model.
 * */
class HoldTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def holdTypeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testHoldTypeListAll() {
        def list = holdTypeService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    void testHoldTypeListAllWithPagination() {
        def params = ["max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType

    }


    void testHoldTypeListFilteredNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }

    }


    void testHoldTypeListFilteredStartsWith() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    void testHoldTypeListFilteredWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "A%", "filter[0][operator]": "contains",
                "max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType

    }


    void testHoldTypeListApiFilterByDate() {
        def params = ["filter[0][field]": "lastModified", "filter[0][value]": "2010-01-01T01:00:00-00:00", "filter[0][operator]": "gt", "filter[0][type]": "date"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    void testHoldTypeListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "BA", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%a%", "filter[1][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    void testHoldTypeListMultipleFiltersContainsWithoutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "BA", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%a%", "filter[1][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    void testHoldTypeListMultipleFiltersContainsStartsWthOutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    void testHoldTypeListMultipleFiltersContainsStartsWthWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B%", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    void testHoldTypeListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "ZZ", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%A%", "filter[1][operator]": "contains",
                "max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType
    }


    void testHoldTypeCount() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B%", "filter[0][operator]": "contains"]
        def listSize = holdTypeService.count(params)
        assertTrue listSize > 0
    }


    void testHoldTypeListNonApiFilter() {
        def filterCriteria = ["params": ["code": "B%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]]]
        def list = holdTypeService.list(filterCriteria)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    void testHoldTypeListNonApiFilterWithPagination() {
        def filterCriteria = ["params": ["code": "A%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]],
                "pagingAndSortParams": ["max": 3, "offset": 2, "sort": "code"]]
        def list = holdTypeService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType
    }

    void testHoldTypeShowWithValidHoldType() {
        def args = formMapForShow()
        def holdType = holdTypeService.show(args)
        assertNotNull holdType
        assertEquals holdType.code, "BA"
    }


    void testHoldTypeShowWithInvalidHoldTypeCode() {
        def args = formMapForShow()
        args << [id: "wwwwwww"]
        shouldFail(ApplicationException) {
            def holdType = holdTypeService.show(args)
        }
    }


    private def formMapForShow() {
        Map args = [id: "BA", pluralizedResourceName: "holdTypes"]
        return args
    }
}
