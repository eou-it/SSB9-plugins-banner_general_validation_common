/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class StateIntegrationTests extends BaseIntegrationTestCase {

    def stateService


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
    void testCreateState() {
        def state = newState()
        save state
        //Test if the generated entity now has an id assigned
        assertNotNull state.id
    }


	@Test
    void testUpdateState() {
        def state = newState()
        save state

        assertNotNull state.id
        assertEquals 0L, state.version
        assertEquals "TTT", state.code
        assertEquals "TTTTT", state.description
        assertEquals "TT", state.ediEquiv
        assertEquals "TTTTT", state.statscan
        assertEquals "TTTTT", state.ipeds

        //Update the entity
        def testDate = new Date()
        state.code = "UUU"
        state.description = "UUUUU"
        state.ediEquiv = "UU"
        state.statscan = "UUUUU"
        state.ipeds = "UUUUU"
        state.lastModified = testDate
        state.lastModifiedBy = "test"
        state.dataOrigin = "Banner"
        save state

        state = State.get(state.id)
        assertEquals 1L, state?.version
        assertEquals "UUU", state.code
        assertEquals "UUUUU", state.description
        assertEquals "UU", state.ediEquiv
        assertEquals "UUUUU", state.statscan
        assertEquals "UUUUU", state.ipeds
    }


	@Test
    void testOptimisticLock() {
        def state = newState()
        save state

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVSTAT set STVSTAT_VERSION = 999 where STVSTAT_SURROGATE_ID = ?", [state.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        state.code = "UUU"
        state.description = "UUUUU"
        state.ediEquiv = "UU"
        state.statscan = "UUUUU"
        state.ipeds = "UUUUU"
        state.lastModified = new Date()
        state.lastModifiedBy = "test"
        state.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            state.save(flush: true, failOnError: true)
        }
    }


	@Test
    void testDeleteState() {
        def state = newState()
        save state
        def id = state.id
        assertNotNull id
        state.delete()
        assertNull State.get(id)
    }


	@Test
    void testValidation() {
        def state = newState()
        assertTrue "State could not be validated as expected due to ${state.errors}", state.validate()
    }


	@Test
    void testNullValidationFailure() {
        def state = new State()
        assertFalse "State should have failed validation", state.validate()
        assertErrorsFor state, 'nullable',
                [
                        'code',
                        'description',

                ]
        assertNoErrorsFor state,
                [
                        'ediEquiv',
                        'statscan',
                        'ipeds',

                ]
    }


	@Test
    void testMaxSizeValidationFailures() {
        def state = new State(
                ediEquiv: 'XXXX',
                statscan: 'XXXXXXX',
                ipeds: 'XXXXXXX')
        assertFalse "State should have failed validation", state.validate()
        assertErrorsFor state, 'maxSize', ['ediEquiv', 'statscan', 'ipeds']
    }


    private def newState() {
        def state = new State(
                code: "TTT",
                description: "TTTTT",
                ediEquiv: "TT",
                statscan: "TTTTT",
                ipeds: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return state
    }

}
