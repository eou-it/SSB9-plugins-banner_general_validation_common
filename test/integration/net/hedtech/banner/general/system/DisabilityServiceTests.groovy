/** *****************************************************************************
 Copyright 2009-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Disability model.
 * */

class DisabilityServiceTests extends BaseIntegrationTestCase {

    def disabilityService


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
    void testCreateDisability() {
        def disability = new Disability(code: "TT", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        disability = disabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Create Service Test", disability.id
        assertNotNull "Disability Code is null in Disability Create Service Test", disability.code
    }

    @Test
    void testUpdateDisability() {
        def disability = new Disability(code: "TT", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        disability = disabilityService.create(disability)

        Disability disabilityUpdate = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Update Service Test", disabilityUpdate.id

        disabilityUpdate.description = "ZZ"
        disabilityUpdate = disabilityService.update(disabilityUpdate)
        assertEquals "ZZ", disabilityUpdate.description
    }

    @Test
    void testDeleteDisability() {
        def disability = new Disability(code: "TT", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        disability = disabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Delete Service Test", disability.id

        Disability disabilityDelete = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Delete Service Test", disabilityDelete.id

        disabilityService.delete(disabilityDelete.id)
        assertNull "Disability should have been deleted in Disability Delete Service Test", disability.get(disabilityDelete.id)
    }

    @Test
    void testInvalidDisability(){
        try{
            disabilityService.fetchDisability('DJD')
            fail("I should have received an error but it passed; @@r1:invalidDisabilty@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "@@r1:invalidDisabilty@@"
        }
    }

    @Test
    void testValidDisability(){
        def result = disabilityService.fetchDisability('DY')
        assertEquals 'I have (or had) a disability', result.description
    }

}
