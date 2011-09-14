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
package com.sungardhe.banner.general.system;


import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration tests for <code>DisabilityAssistance</code>.
 */
class DisabilityAssistanceIntegrationTests extends BaseIntegrationTestCase {

    def disabilityAssistanceService;


    protected void setUp() {
        formContext = ['STVMDEQ'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreate() {
        def disabilityAssistance = new DisabilityAssistance(code: "zz", description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "psykes", dataOrigin: "Banner")

        if (!disabilityAssistance.save()) {
            fail("Could not save disabilityAssistance");
        }
        disabilityAssistance.save()
    }


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
