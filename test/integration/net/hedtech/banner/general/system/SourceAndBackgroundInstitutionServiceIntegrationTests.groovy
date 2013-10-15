/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Service Integration  test for the 'sourceAndBackgroundInstitution' model.
 * */
class SourceAndBackgroundInstitutionServiceIntegrationTests extends BaseIntegrationTestCase {

    def sourceAndBackgroundInstitutionService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testSourceAndBackgroundInstitutionListAll() {
        def list = sourceAndBackgroundInstitutionService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof SourceAndBackgroundInstitution
        assertNotNull list.find { it.code == "1005" }

    }


    void testSourceAndBackgroundInstitutionListAllWithPagination() {
        def params = ["max": 50, "offset": 2, "sort": "code"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertEquals 50, list.size()
        assertTrue list[0] instanceof SourceAndBackgroundInstitution

    }


    void testSourceAndBackgroundInstitutionListFilteredNoPagination() {
        def sourceAndBackgroundInstitutionControlList = SourceAndBackgroundInstitution.findAllByCodeIlike("1%")
        assertTrue sourceAndBackgroundInstitutionControlList.size() > 0
        assertNotNull sourceAndBackgroundInstitutionControlList.find { it.code == "1005" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "1%", "filter[0][operator]": "contains"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertEquals sourceAndBackgroundInstitutionControlList.size(), list.size()
        assertTrue list[0] instanceof SourceAndBackgroundInstitution
        assertNotNull list.find { it.code == "1005" }

    }


    void testSourceAndBackgroundInstitutionListFilteredWithPagination() {

        def params = ["filter[0][field]": "code", "filter[0][value]": "1%", "filter[0][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof SourceAndBackgroundInstitution

    }


    void testSourceAndBackgroundInstitutionListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "344193", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%S%", "filter[1][operator]": "contains"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof SourceAndBackgroundInstitution
        assertNotNull list.find { it.code == "1005" }
    }


    void testSourceAndBackgroundInstitutionListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "344193", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%S%", "filter[1][operator]": "contains",
                "max": 10, "offset": 2, "sort": "code"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertEquals 10, list.size()
        assertTrue list[0] instanceof SourceAndBackgroundInstitution
    }


    void testSourceAndBackgroundInstitutionSourceIndicatorFilter() {
        def params = ["filter[0][field]": "srceIndicator", "filter[0][operator]": "eq", "filter[0][value]": "Y"]
        def list = sourceAndBackgroundInstitutionService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof SourceAndBackgroundInstitution
        assertNull list.find { it.code == "999999" }
    }


    void testSourceAndBackgroundInstitutionCount() {
        def sourceAndBackgroundInstitutionControlList = SourceAndBackgroundInstitution.findAllByCodeIlike("1%")
        assertTrue sourceAndBackgroundInstitutionControlList.size() > 0
        assertNotNull sourceAndBackgroundInstitutionControlList.find { it.code == "1005" }
        // now try the new service
        def params = ["filter[0][field]": "code", "filter[0][value]": "1%", "filter[0][operator]": "contains"]
        def listSize = sourceAndBackgroundInstitutionService.count(params)
        assertTrue listSize > 0
        assertEquals sourceAndBackgroundInstitutionControlList.size(), listSize
    }
}
