/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore

/**
 * Service Integration  test for the 'holdType' model.
 * */
class HoldTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def holdTypeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testHoldTypeListAll() {
        def list = holdTypeService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    @Test
    void testHoldTypeListAllWithPagination() {
        def params = ["max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType

    }


    @Test
    void testHoldTypeListFilteredNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }

    }


    @Test
    void testHoldTypeListFilteredStartsWith() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    @Test
    void testHoldTypeListFilteredWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "A%", "filter[0][operator]": "contains",
                "max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType

    }


    @Test
    void testHoldTypeListApiFilterByDate() {
        def params = ["filter[0][field]": "lastModified", "filter[0][value]": "2009-01-01T01:00:00-00:00", "filter[0][operator]": "gt", "filter[0][type]": "date"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    @Test
    void testHoldTypeListMultipleFiltersNoPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "BA", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%a%", "filter[1][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    @Test
    void testHoldTypeListMultipleFiltersContainsWithoutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "BA", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%a%", "filter[1][operator]": "contains"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    @Test
    void testHoldTypeListMultipleFiltersContainsStartsWthOutWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    @Test
    void testHoldTypeListMultipleFiltersContainsStartsWthWildcard() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B%", "filter[0][operator]": "startswith"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
    }


    @Test
    void testHoldTypeListMultipleFiltersWithPagination() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "ZZ", "filter[0][operator]": "lt",
                "filter[1][field]": "description", "filter[1][value]": "%A%", "filter[1][operator]": "contains",
                "max": 3, "offset": 2, "sort": "code"]
        def list = holdTypeService.list(params)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType
    }


    @Test
    void testHoldTypeCount() {
        def params = ["filter[0][field]": "code", "filter[0][value]": "B%", "filter[0][operator]": "contains"]
        def listSize = holdTypeService.count(params)
        assertTrue listSize > 0
    }


    @Test
    void testHoldTypeListNonApiFilter() {
        def filterCriteria = ["params": ["code": "B%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]]]
        def list = holdTypeService.list(filterCriteria)
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType
        assertNotNull list.find { it.code == "BA" }
    }


    @Test
    void testHoldTypeListNonApiFilterWithPagination() {
        def filterCriteria = ["params": ["code": "A%"],
                "criteria": [["key": "code", "binding": "code", "operator": Operators.CONTAINS]],
                "pagingAndSortParams": ["max": 3, "offset": 2, "sort": "code"]]
        def list = holdTypeService.list(filterCriteria)
        assertTrue list.size() > 0
        assertEquals 3, list.size()
        assertTrue list[0] instanceof HoldType
    }

    @Test
    void testHoldTypeShowWithValidHoldType() {
        def args = formMapForShow()
        def holdType = holdTypeService.show(args)
        assertNotNull holdType
        assertEquals holdType.code, "BA"
    }


    @Test
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
    //start-for Version 6 Schema support -PERSON-HOLD-TYPES
    @Test
    void testHoldTypeListV6() {
        Metadata i_success_metadata =[dataOrigin: 'Banner']
        List<HoldType> list = holdTypeService.list()
        RestrictionType restrictionType_oldversion = new RestrictionType(list[0],"adasdd",i_success_metadata)
        list.each { holdType ->
            RestrictionType restrictionType = new RestrictionType(holdType,"adasdd",i_success_metadata)
           if( holdType.accountsReceivableHoldIndicator){
               assertEquals(restrictionType.category,"finance")
           }
        }
        assertTrue list.size() > 0
        assertTrue list[0] instanceof HoldType

        assertNotNull restrictionType_oldversion.toString()
        assertEquals(restrictionType_oldversion.guid,"adasdd")
        assertEquals(restrictionType_oldversion.category,"academic")
         }
    //end-for Version 6 Schema support -PERSON-HOLD-TYPES
}
