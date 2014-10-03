/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Disability model.
 * */

class DisabilityServiceTests extends BaseIntegrationTestCase {

    def DisabilityService


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
        disability = DisabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Create Service Test", disability.id
        assertNotNull "Disability Code is null in Disability Create Service Test", disability.code
    }

	@Test
    void testUpdateDisability() {
        def disability = new Disability(code: "TT", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        disability = DisabilityService.create(disability)

        Disability disabilityUpdate = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Update Service Test", disabilityUpdate.id

        disabilityUpdate.description = "ZZ"
        disabilityUpdate = DisabilityService.update(disabilityUpdate)
        assertEquals "ZZ", disabilityUpdate.description
    }

	@Test
    void testDeleteDisability() {
        def disability = new Disability(code: "TT", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        disability = DisabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Delete Service Test", disability.id

        Disability disabilityDelete = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Delete Service Test", disabilityDelete.id

        DisabilityService.delete(disabilityDelete.id)
        assertNull "Disability should have been deleted in Disability Delete Service Test", disability.get(disabilityDelete.id)
    }

}
