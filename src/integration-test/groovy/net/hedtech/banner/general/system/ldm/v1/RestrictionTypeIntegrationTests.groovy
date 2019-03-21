/** *******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class RestrictionTypeIntegrationTests extends BaseIntegrationTestCase {

    HoldType i_success_hold_type
    Metadata i_success_metadata
    String i_success_guid
    RestrictionType i_success_restriction_type


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }


    void initializeTestDataForReferences() {
        i_success_hold_type = HoldType.findByCode('AS')
        i_success_metadata = [dataOrigin: 'Banner']
        i_success_guid = "aaaaaa-bbbbbb-cccccc-ddddddd-eeeeee"
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testRestrictionType() {
        i_success_restriction_type = validRestrictionType()
        assertNotNull i_success_restriction_type.toString()
        assertEquals i_success_restriction_type.code, i_success_hold_type.code
        assertEquals i_success_restriction_type.metadata.dataOrigin, i_success_metadata.dataOrigin
        assertEquals i_success_restriction_type.guid, i_success_guid
        assertEquals i_success_restriction_type, new RestrictionType(i_success_hold_type, i_success_guid, i_success_metadata)
    }


    private def validRestrictionType() {
        def newRestrictionType = new RestrictionType(i_success_hold_type, i_success_guid, i_success_metadata)
        return newRestrictionType
    }

}
