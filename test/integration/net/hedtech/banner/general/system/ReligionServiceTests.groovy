/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

class ReligionServiceTests extends BaseIntegrationTestCase {

    def religionService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])
        assertNotNull religion
    }


    @Test
    void testUpdateReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])

        Religion religionUpdate = Religion.findWhere(code: "T")
        assertNotNull religionUpdate

        religionUpdate.description = "ZZ"
        religionUpdate = religionService.update([domainModel: religionUpdate])
        assertEquals "ZZ", religionUpdate.description
    }


    @Test
    void testDeleteReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])
        assertNotNull religion
        def id = religion.id
        def deleteMe = Religion.get(id)
        religionService.delete([domainModel: deleteMe])
        assertNull Religion.get(id)

    }


    @Test
    void testList() {
        def religion = religionService.list()
        assertTrue religion.size() > 0
    }
}
