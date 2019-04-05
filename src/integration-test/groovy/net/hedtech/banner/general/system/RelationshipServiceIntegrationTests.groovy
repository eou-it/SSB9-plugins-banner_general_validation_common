/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback

/**
 * Integration test for the "Relationship" service.
 * */
class RelationshipServiceIntegrationTests extends BaseIntegrationTestCase {
    RelationshipService relationshipService
    GlobalUniqueIdentifier i_success_globalUniqueIdentifier

    Relationship i_success_relationship
    Collection<String> i_success_relationship_codes = ['A', 'B', 'S']


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    void initializeTestDataForReferences() {
        i_success_relationship = Relationship.findByCode("A")
        i_success_globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName('A', 'personal-relationship-types')
    }


    @Test
    void testFetchAllByCodeInList() {
        List<Relationship> o_success_relationships = relationshipService.fetchAllByCodeInList(i_success_relationship_codes)
        assertNotNull o_success_relationships
        assertTrue o_success_relationships.size() > 0
        Relationship o_success_relationship = o_success_relationships.find { it.code == i_success_relationship.code }
        assertNotNull o_success_relationship
        assertEquals i_success_relationship.code, o_success_relationship.code
        assertEquals i_success_relationship.description, o_success_relationship.description
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

