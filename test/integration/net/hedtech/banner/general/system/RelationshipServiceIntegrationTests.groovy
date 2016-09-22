/*******************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

/**
 * Integration test for the "Relationship" service.
 * */
class RelationshipServiceIntegrationTests extends BaseIntegrationTestCase {

    def relationshipService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testRelationshipListAll() {
        def list = relationshipService.list()
        assertTrue list.size() > 0
        assertTrue list[0] instanceof Relationship
        assertNotNull list.find { it.code == "P" }
    }

    @Test
    void testFetchRelationship() {
        def relationship = relationshipService.fetchRelationship('P')

        assertNotNull relationship
        assertEquals 'Spouse', relationship.description
    }

    // TODO: implement as part of BXEGS-86 "Fetch relationship types for select list"
//    @Test
//    void testFetchUpdateableTelephoneTypeListTwenty() {
//        def telephoneTypeList = relationshipService.fetchUpdateableTelephoneTypeList(20)
//
//        assertEquals 20, telephoneTypeList.size()
//        assertEquals 'Administrative', telephoneTypeList[1].description
//        assertEquals 'Loc Mgt Phn Type', telephoneTypeList[19].description
//    }
//
//    @Test
//    void testFetchUpdateableTelephoneTypeListMidList() {
//        def telephoneTypeList = relationshipService.fetchUpdateableTelephoneTypeList(10, 10)
//
//        assertEquals 10, telephoneTypeList.size()
//        assertEquals 'Dorm', telephoneTypeList[0].description
//    }
//
//    @Test
//    void testFetchUpdateableTelephoneTypeListEndOfList() {
//        def telephoneTypeList = relationshipService.fetchUpdateableTelephoneTypeList(10, 40)
//
//        assertEquals 8, telephoneTypeList.size()
//        assertEquals 'Term', telephoneTypeList[0].description
//    }
//
//    @Test
//    void testFetchUpdateableTelephoneTypesListUsingPartialSearchTerm() {
//        def telephoneTypeList = relationshipService.fetchUpdateableTelephoneTypeList(10, 0, 'adm')
//
//        assertEquals 1, telephoneTypeList.size()
//        assertEquals 'Administrative', telephoneTypeList[0].description
//    }
}
