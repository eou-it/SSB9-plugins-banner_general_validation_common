/** *****************************************************************************

 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

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


    void testOptimisticLock() {
        def term = createValidTerm(code: "TT", description: "TT")
        save term

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVTERM set STVTERM_VERSION = 999 where STVTERM_SURROGATE_ID = ?", [term.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        term.description = "Updated Description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            term.save(flush: true)
        }
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

        def nextTerm = Term.fetchPreviousTerm("000001")
        assertNotNull nextTerm
        assertEquals nextTerm.code, "000000"

    }



    void testNullValidationFailure() {
        def term = new Term()
        assertFalse "Term should have failed validation", term.validate()
        assertErrorsFor term, 'nullable',
                [
                        'code',
                        'description',
                        'startDate',
                        'endDate',
                        'housingStartDate',
                        'housingEndDate',
                        'acyr_code',
                        'trmt_code'
                ]
        assertNoErrorsFor term,
                [
                        'financialAidProcessingYear',
                        'financialAidTerm',
                        'financeAidPeriod',
                        'financeAidEndPeriod',
                        'systemReqInd',
                        'financeSummerIndicator'
                ]
    }


    void testMaxSizeValidationFailures() {
        def term = new Term(
                financialAidProcessingYear: 'XXXXXX',
                financialAidTerm: 'XXX')
        assertFalse "Term should have failed validation", term.validate()
        assertErrorsFor term, 'maxSize', ['financialAidProcessingYear', 'financialAidTerm']
    }


    void testValidationMessages() {
        def term = createValidTerm(code: "TT", description: "TT")
        term.code = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*code.*of class.*Term.*cannot be null.*/, 'code'
        term.description = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*description.*of class.*Term.*cannot be null.*/, 'description'
        term.startDate = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*startDate.*of class.*Term.*cannot be null.*/, 'startDate'
        term.endDate = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*endDate.*of class.*Term.*cannot be null.*/, 'endDate'
        term.housingStartDate = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*housingStartDate.*of class.*Term.*cannot be null.*/, 'housingStartDate'
        term.housingEndDate = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*housingEndDate.*of class.*Term.*cannot be null.*/, 'housingEndDate'
        term.acyr_code = null
        assertFalse term.validate()
        assertLocalizedError term, 'nullable', /.*Field.*acyr_code.*of class.*Term.*cannot be null.*/, 'acyr_code'
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
                financeSummerIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner",
                acyr_code: academicYear,
                trmt_code: termType)

        // Overwrite any term based properites
        if (p) term.properties = p

        return term
    }
}
