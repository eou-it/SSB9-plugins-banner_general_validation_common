/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class RelationshipCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    RelationshipCompositeService relationshipCompositeService
    Relationship i_success_relationship

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    void initializeTestDataForReferences() {
        i_success_relationship = Relationship.findByCode("A")
    }

    @Test
    void testGetBannerRelationshipTypeToHedmV7RelationshipTypeMap() {
        def bannerRelationshipTypeToHedmRelationshipTypeMap = relationshipCompositeService.getBannerRelationshipTypeToHedmV7RelationshipTypeMap()
        assertNotNull bannerRelationshipTypeToHedmRelationshipTypeMap
        String hedmRelationshipType = bannerRelationshipTypeToHedmRelationshipTypeMap.get(i_success_relationship.code)
        assertNotNull hedmRelationshipType
    }


}
