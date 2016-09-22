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

    @Test
    void testFetchRelationshipListFive() {
        def relationshipList = relationshipService.fetchRelationshipList(5)

        assertEquals 5, relationshipList.size()
        assertEquals 'Brother', relationshipList[1].description
        assertEquals 'Father', relationshipList[4].description
    }

    @Test
    void testFetchRelationshipListMidList() {
        def relationshipList = relationshipService.fetchRelationshipList(5, 5)

        assertEquals 5, relationshipList.size()
        assertEquals 'Ferpa Clearance', relationshipList[0].description
    }

    @Test
    void testFetchRelationshipListEndOfList() {
        def relationshipList = relationshipService.fetchRelationshipList(10, 10)

        assertEquals 6, relationshipList.size()
        assertEquals 'Mother', relationshipList[0].description
    }

    @Test
    void testFetchRelationshipListUsingPartialSearchTerm() {
        def relationshipList = relationshipService.fetchRelationshipList(10, 0, 'fath')

        assertEquals 1, relationshipList.size()
        assertEquals 'Father', relationshipList[0].description
    }
}
