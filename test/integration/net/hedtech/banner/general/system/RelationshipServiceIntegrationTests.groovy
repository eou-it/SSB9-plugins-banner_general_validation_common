/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


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

}
