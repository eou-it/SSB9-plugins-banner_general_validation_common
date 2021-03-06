/** *****************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the 'term' model.
 * */
class TermIntegrationTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreateTerm() {
        def term = createValidTerm(code: "TT", description: "TT")

        if (!term.save()) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        assertNotNull(term.id)
    }


    @Test
    void testUpdateTerm() {
        def term = createValidTerm(code: "TT", description: "TT")
        if (!term.save(flush: true, failOnError: true)) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        def id = term.id
        def version = term.version
        assertNotNull(id)
        assertEquals(0L, version)

        term.description = "updated"

        if (!term.save(flush: true, failOnError: true)) {
            fail("Could not update Term; TERM ERRORS = " + term.errors);
        }
        term = Term.get(id)

        assertNotNull("found must not be null", term)
        assertEquals("updated", term.description)
        assertEquals(1, term.version)
    }


    @Test
    void testDeleteTerm() {
        def term = createValidTerm(code: "TT", description: "TT")
        if (!term.save(flush: true, failOnError: true)) {
            fail("Could not save Term; TERM ERRORS = " + term.errors);
        }
        def id = term.id
        assertNotNull(id)
        term.delete();
        def found = Term.get(id)
        assertNull(found)
    }


    @Test
    void testOptimisticLock() {
        def term = createValidTerm(code: "TT", description: "TT")
        save term

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVTERM set STVTERM_VERSION = 999 where STVTERM_SURROGATE_ID = ?", [term.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        term.description = "Updated Description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            term.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testValidation() {
        def term = createValidTerm(code: null)

        //should not pass validation since none of the required values are provided
        assertFalse(term.validate())

        term.code = "TT"

        //should pass this time
        assertTrue(term.validate())
    }


    @Test
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


    @Test
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
                        'acyr_code'
                ]
        assertNoErrorsFor term,
                [
                        'financialAidProcessingYear',
                        'financialAidTerm',
                        'financeAidPeriod',
                        'financeAidEndPeriod',
                        'systemReqInd',
                        'financeSummerIndicator',
                        'trmt_code'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def term = new Term(
                financialAidProcessingYear: 'XXXXXX',
                financialAidTerm: 'XXX')
        assertFalse "Term should have failed validation", term.validate()
        assertErrorsFor term, 'maxSize', ['financialAidProcessingYear', 'financialAidTerm']
    }


    @Test
    void testFetchMaxTermWithHousingStartDateLessThanEqualDate() {
        def cal = Calendar.instance
        cal.set(2001, 11, 31)
        def housingStartDate = cal.time
        def housingEndDate = housingStartDate.plus(4)
        def term = createValidTerm(code: "ZZZZ99", description: "TT", housingStartDate: housingStartDate, housingEndDate: housingEndDate)

        term.save(flush: true, failOnError: true)
        assertNotNull(term.id)

        //Case: search date before the housingStartDate, should not return the newly created term
        def date = term.housingStartDate.previous()
        def returnedValue = Term.fetchMaxTermWithHousingStartDateLessThanEqualDate(date)
        if (returnedValue) {
            assertFalse term == returnedValue
        }

        //Case: search date on the housingStartDate, should return the newly created term
        date = term.housingStartDate
        returnedValue = Term.fetchMaxTermWithHousingStartDateLessThanEqualDate(date)
        assertEquals term, returnedValue

        cal = Calendar.instance
        cal.setTime(term.housingStartDate)
        cal.roll(Calendar.HOUR_OF_DAY, true)
        date = cal.getTime()
        returnedValue = Term.fetchMaxTermWithHousingStartDateLessThanEqualDate(date)
        assertEquals term, returnedValue

        //Case: search date after the housingStartDate, should return the newly created term
        date = term.housingStartDate.next()
        returnedValue = Term.fetchMaxTermWithHousingStartDateLessThanEqualDate(date)
        assertEquals term, returnedValue
    }


    @Test
    void testFetchMaxTermWithStartDateLessThanEqualDate() {
        def term = createValidTerm(code: "WWWW02", description: "ZZZZ01")
        term.save(flush: true)
        term.refresh()
        assertNotNull term.id
        Term fetchedTerm = Term.fetchMaxTermWithStartDateLessThanGivenDate(new Date() + 5)
        assertEquals term, fetchedTerm
    }


    @Test
    void testListWithFilter() {
        def filter = "2014"
        def terms = Term.fetchAllByTerm(filter, 10, 0)
        assertTrue terms.size() > 0
        assertNotNull terms.find { it.code == "201410" }

        filter = "%2%"
        terms = Term.fetchAllByTerm(filter, 10, 0)
        assertTrue terms.size() == 10
    }


    @Test
    void testFetchAllByTermCodes(){
        String TERM_CODE_ONE = "MMMM01"
        String TERM_CODE_TWO = "201410"

        Term termOne = createValidTerm(code: TERM_CODE_ONE, description: "MMMM01 desc")
        termOne.save(flush: true)
        termOne.refresh()
        List<Term> terms = Term.fetchAllByTermCodes( [TERM_CODE_ONE] )
        assertTrue terms.size() == 1

        terms = Term.fetchAllByTermCodes( [TERM_CODE_ONE, TERM_CODE_TWO] )
        assertTrue terms.size() == 2
        assertNotNull terms.find{ it.code == TERM_CODE_ONE}
        assertNotNull terms.find{ it.code == TERM_CODE_TWO}
    }

    @Test
    void testFetchValidTermCodes(){
        String TERM_CODE_ONE = "202002"
        List<Term> terms = Term.fetchValidTermCodes( [TERM_CODE_ONE] )
        assertTrue terms.size() == 1

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
        if (p) {
            use(InvokerHelper) {
                term.setProperties(p)
            }
        }
        //term.properties = p

        return term
    }

}
