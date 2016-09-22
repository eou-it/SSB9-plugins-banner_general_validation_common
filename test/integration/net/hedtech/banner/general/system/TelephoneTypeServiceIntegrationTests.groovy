/*******************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

/**
 * Integration test for the "Telephone Type" service.
 * */
class TelephoneTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def telephoneTypeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testTelephoneTypeListAll() {
        def list = telephoneTypeService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof TelephoneType
        assertNotNull list.find { it.code == "CELL" }
    }

    @Test
    void testFetchByCode() {
        def telephoneType = telephoneTypeService.fetchByCode('EMER')

        assertNotNull telephoneType
        assertEquals 'Emergency Contact', telephoneType.description
    }

    @Test
    void testFetchUpdateableTelephoneTypeListTwenty() {
        def telephoneTypeList = telephoneTypeService.fetchUpdateableTelephoneTypeList(20)

        assertEquals 20, telephoneTypeList.size()
        assertEquals 'AMCAS Phone type', telephoneTypeList[1].description
        assertEquals 'Home', telephoneTypeList[19].description
    }

    @Test
    void testFetchUpdateableTelephoneTypeListMidList() {
        def telephoneTypeList = telephoneTypeService.fetchUpdateableTelephoneTypeList(10, 10)

        assertEquals 10, telephoneTypeList.size()
        assertEquals 'Dorm', telephoneTypeList[0].description
    }

    @Test
    void testFetchUpdateableTelephoneTypeListEndOfList() {
        def telephoneTypeList = telephoneTypeService.fetchUpdateableTelephoneTypeList(10, 40)

        assertEquals 8, telephoneTypeList.size()
        assertEquals 'Temporary', telephoneTypeList[0].description
    }

    @Test
    void testFetchUpdateableTelephoneTypesListUsingPartialSearchTerm() {
        def telephoneTypeList = telephoneTypeService.fetchUpdateableTelephoneTypeList(10, 0, 'adm')

        assertEquals 1, telephoneTypeList.size()
        assertEquals 'Administrative', telephoneTypeList[0].description
    }
}
