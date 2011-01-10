/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.system.AcademicYear
import com.sungardhe.banner.general.system.Term
import com.sungardhe.banner.general.system.TermType

/**
 * Integration test for the 'term' model.
 * */
class TermIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['STVTERM'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(term_custom_test_methods) ENABLED START*/

    void testCreateTerm() {

        def term = createValidTerm(code: "TT", description: "TT")

        if (!term.save()) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        assertNotNull(term.id)
    }


    void testUpdateTerm() {
        def term = createValidTerm(code: "TT", description: "TT")
        if (!term.save(flush: true)) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        def id = term.id
        def version = term.version
        assertNotNull(id)
        assertEquals(0L, version)

        term.description = "updated"

        if (!term.save(flush: true)) {
            fail("Could not update Term; TERM ERRORS = " + term.errors);
        }
        term = Term.get(id)

        assertNotNull("found must not be null", term)
        assertEquals("updated", term.description)
        assertEquals(1, term.version)
    }


    void testDeleteTerm() {
        def term = createValidTerm(code: "TT", description: "TT")
        if (!term.save(flush: true)) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        def id = term.id
        assertNotNull(id)
        term.delete();
        def found = Term.get(id)
        assertNull(found)
    }

    void testValidation() {
        def term = createValidTerm(code: null)

        //should not pass validation since none of the required values are provided
        assertFalse(term.validate())

        term.code = "TT"

        //should pass this time
        assertTrue(term.validate())
    }


    void testFetchPreviousTerm() {
        def term = Term.findByCode("000001")
        if (term == null) {
            term = createValidTerm(code: "000001")
            term.save()
        }

        def term2 = Term.findByCode("000000")
        def nextTerm = Term.fetchPreviousTerm("000001")
        assertNotNull nextTerm
        assertEquals nextTerm.code, "000000"

    }


    private Term createValidTerm(Map p) {
        def academicYear = new AcademicYear(code: "TT", description: "TT", sysreqInd: true, lastModified: new Date(),
                                            lastModifiedBy: "test", dataOrigin: "Banner")
        academicYear.save()

        def termType = new TermType(code: "T", description: "TT", lastModified: new Date(),
                                    lastModifiedBy: "test", dataOrigin: "Banner")
        termType.save()

        def term = new Term(code: "TT",
                            description: "TT",
                            startDate: new Date(),
                            endDate: new Date(),
                            financialAidProcessingYear: "TT",
                            financialAidTerm: "T",
                            financialAidPeriod: 1,
                            financialEndPeriod: 1,
                            housingStartDate: new Date(),
                            housingEndDate: new Date(),
                            systemReqInd: true,
                            lastModified: new Date(),
                            lastModifiedBy: "test",
                            dataOrigin: "Banner",
                            acyr_code: academicYear,
                            trmt_code: termType)

        // Overwrite any term based properites
        if (p) term.properties = p

        return term
    }

    /*PROTECTED REGION END*/

}
