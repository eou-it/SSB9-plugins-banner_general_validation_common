/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class CommitteeFunctionIntegrationTests extends BaseIntegrationTestCase{

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
    void testCreateCommitteeFunction() {
        def committeeFunction = createValidCommitteeFunction()
        save committeeFunction
        //Test if the generated entity now has an id assigned
        assertNotNull committeeFunction.id
    }


    @Test
    void testUpdateCommitteeFunction() {
        def committeeFunction = createValidCommitteeFunction()
        save committeeFunction

        assertNotNull committeeFunction.id
        assertEquals 0L, committeeFunction.version
        assertEquals "TT", committeeFunction.code
        assertEquals "TTTTT", committeeFunction.description

        //Update the entity
        def testDate = new Date()
        committeeFunction.code = "UU"
        committeeFunction.description = "UUUUU"
        committeeFunction.lastModified = testDate
        committeeFunction.lastModifiedBy = "test"
        committeeFunction.dataOrigin = "Banner"
        save committeeFunction

        committeeFunction = CommitteeFunction.get(committeeFunction.id)
        assertEquals 1L, committeeFunction?.version
        assertEquals "UU", committeeFunction.code
        assertEquals "UUUUU", committeeFunction.description
    }


    @Test
    void testOptimisticLock() {
        def committeeFunction = createValidCommitteeFunction()
        save committeeFunction

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCOMF set STVCOMF_VERSION = 999 where STVCOMF_SURROGATE_ID = ?", [committeeFunction.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        committeeFunction.code = "UU"
        committeeFunction.description = "UUUUU"
        committeeFunction.lastModified = new Date()
        committeeFunction.lastModifiedBy = "test"
        committeeFunction.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            committeeFunction.save(flush: true)
        }
    }


    @Test
    void testDeleteCommitteeFunction() {
        def committeeFunction = createValidCommitteeFunction()
        save committeeFunction
        def id = committeeFunction.id
        assertNotNull id
        committeeFunction.delete()
        assertNull CommitteeFunction.get(id)
    }


    @Test
    void testValidation() {
        def committeeFunction = createValidCommitteeFunction()
        assertTrue "CommitteeFunction could not be validated as expected due to ${committeeFunction.errors}", committeeFunction.validate()
    }


    @Test
    void testNullValidationFailure() {
        def committeeFunction = new CommitteeFunction()
        assertFalse "CommitteeFunction should have failed validation", committeeFunction.validate()
        assertErrorsFor committeeFunction, 'nullable', ['code', 'description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def committeeFunction = new CommitteeFunction(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "Interest should have failed validation", committeeFunction.validate()
        assertErrorsFor committeeFunction, 'maxSize', ['description']
    }


    private CommitteeFunction createValidCommitteeFunction(Map p) {
        def committeeFunction = new CommitteeFunction(
                code: "TT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner")
        // overwrite any provided properties
        if (p) committeeFunction.properties = p

        return committeeFunction
    }

}
