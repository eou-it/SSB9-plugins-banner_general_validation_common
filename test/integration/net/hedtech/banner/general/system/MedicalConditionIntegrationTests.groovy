/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration tests for <code>MedicalCondition</code>.
 *
 */
class MedicalConditionIntegrationTests extends BaseIntegrationTestCase {

    def medicalConditionService


    protected void setUp() {
        formContext = ['STVMEDI'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() throws Exception {
        super.tearDown()
    }


    void testCreate() {
        def medCond = newMedicalCondition()
        save medCond
        assertNotNull medCond.id
    }


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
