/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration tests for <code>MedicalCondition</code>.
 *
 */
class MedicalConditionIntegrationTests extends BaseIntegrationTestCase {

    def medicalConditionService


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
    void testCreate() {
        def medCond = newMedicalCondition()
        save medCond
        assertNotNull medCond.id
    }


    @Test
    void testUpdate() {

        def medCond = newMedicalCondition()

        save medCond
        def id = medCond.id

        medCond.description = "updated"
        save medCond

        def found = MedicalCondition.get(id)
        assertNotNull "found must not be null", found
        assertEquals "updated", found.description
        assertEquals 1L, found.version
    }


    @Test
    void testDelete() {

        def medCond = newMedicalCondition()
        save medCond

        def id = medCond.id

        medCond.delete()
        def found = MedicalCondition.get(id)
        assertNull found
    }

    /**
     * Tests validation of constraints.
     */
    @Test
    void testValidation() {

        def medicalCondition = new MedicalCondition()
        assertFalse medicalCondition.validate() // missing required code and description

        medicalCondition.code = "AA"
        medicalCondition.description = "unit test"
        medicalCondition.lastModified = new Date()
        medicalCondition.lastModifiedBy = "horizon"
        assertTrue medicalCondition.validate()

        medicalCondition.code = "AAAAAAAAAAAAAAAA"
        assertFalse medicalCondition.validate()
    }


    @Test
    void testList() {

        def originalSize = MedicalCondition.count()
        def medCond = newMedicalCondition()
        save medCond
        assertTrue(MedicalCondition.count() == originalSize + 1)
    }


    private def newMedicalCondition() {
        new MedicalCondition(code: "zz", description: "unit-test",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
    }

}
