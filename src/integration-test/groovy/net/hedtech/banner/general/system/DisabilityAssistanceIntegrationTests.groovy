/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system;
import org.junit.Before
import org.junit.Test
import org.junit.After


import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration tests for <code>DisabilityAssistance</code>.
 */
class DisabilityAssistanceIntegrationTests extends BaseIntegrationTestCase {

    def disabilityAssistanceService;


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreate() {
        def disabilityAssistance = new DisabilityAssistance(code: "zz", description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "psykes", dataOrigin: "Banner")

        if (!disabilityAssistance.save()) {
            fail("Could not save disabilityAssistance");
        }
        disabilityAssistance.save()
    }


    @Test
    void testUpdate() {

        def disabilityAssistance = new DisabilityAssistance(code: "yy", description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "psykes", dataOrigin: "Banner")

        if (!disabilityAssistance.save()) {
            fail("Could not save disabilityAssistance");
        }

        def id = disabilityAssistance.id
        disabilityAssistance.description = "updated"

        if (!disabilityAssistance.save()) {
            fail("Could not update disabilityAssistance");
        }

        def found = DisabilityAssistance.get(id)
        assertNotNull("found must not be null", found)
        assertEquals("updated", found.description)
    }


    @Test
    void testDelete() {

        def disabilityAssistance = new DisabilityAssistance(code: "xx", description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "psykes", dataOrigin: "Banner")

        if (!disabilityAssistance.save()) {
            fail("Could not save disabilityAssistance");
        }

        def id = disabilityAssistance.id
        disabilityAssistance.delete()

        def found = DisabilityAssistance.get(id)
        assertNull(found)
    }


    @Test
    void testValidation() {

        def disabilityAssistance = new DisabilityAssistance()

        //should not pass validation since code and description are not provided
        assertFalse(disabilityAssistance.validate())

        disabilityAssistance.code = "AA"
        disabilityAssistance.description = "unit test"
        disabilityAssistance.lastModified = new Date()
        disabilityAssistance.lastModifiedBy = "psykes"
        disabilityAssistance.dataOrigin = "banner"

        //should pass this time
        assertTrue(disabilityAssistance.validate())

        disabilityAssistance.code = "AAA"

        //should fail validation since code can not be more than 2 characters
        assertFalse(disabilityAssistance.validate())
    }


}
