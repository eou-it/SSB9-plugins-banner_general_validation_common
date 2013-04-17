/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the 'term type' model.
 * */
class TermTypeIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreateTermType() {
        def termType = newValidForCreateTermType()

        if (!termType.save()) {
            fail("Could not save TermType; TERMTYPE ERRORS = " + termType.errors);
        }
        assertNotNull(termType.id)
    }

    void testUpdateTermType() {
        def termType = newValidForCreateTermType()
        if (!termType.save(flush: true, failOnError: true)) {
            fail("Could not save TermType; TERMTYPE ERRORS = " + termType.errors);
        }
        def id = termType.id
        def version = termType.version
        assertNotNull(id)
        assertEquals(0L, version)

        termType.description = "updated"

        if (!termType.save(flush: true, failOnError: true)) {
            fail("Could not update TermType; TERMTYPE ERRORS = " + termType.errors);
        }
        termType = TermType.get(id)

        assertNotNull("found must not be null", termType)
        assertEquals("updated", termType.description)
        assertEquals(1, termType.version)
    }

    void testDeleteTermType() {
        def termType = newValidForCreateTermType()
        if (!termType.save(flush: true, failOnError: true)) {
            fail("Could not save TermType; TERMTYPE ERRORS = " + termType.errors);
        }
        def id = termType.id
        assertNotNull(id)
        termType.delete();
        def found = TermType.get(id)
        assertNull(found)
    }

    void testOptimisticLock() {
        def termType = newValidForCreateTermType()
        save termType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVTRMT set STVTRMT_VERSION = 999 where STVTRMT_SURROGATE_ID = ?", [termType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        termType.description = "Updated Description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            termType.save(flush: true, failOnError: true)
        }
    }

    void testValidation() {
        def termType = new TermType()
        //should not pass validation since none of the required values are provided
        assertFalse(termType.validate())

        // Setup a valid termType
        termType = new TermType(code: "T", description: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        //should pass this time
        assertTrue(termType.validate())
    }


    void testNullValidationFailure() {
        def termType = new TermType()
        assertFalse "TermType should have failed validation", termType.validate()
        assertErrorsFor termType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    private def newValidForCreateTermType() {
        def termType = new TermType(
                code: "T",
                description: "TT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return termType
    }

}
