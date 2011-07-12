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

class StateIntegrationTests extends BaseIntegrationTestCase {

    def stateService


    protected void setUp() {
        formContext = ['STVSTAT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateState() {
        def state = newState()
        save state
        //Test if the generated entity now has an id assigned
        assertNotNull state.id
    }


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
            state.save(flush:true, failOnError:true)
        }
    }


    void testDeleteState() {
        def state = newState()
        save state
        def id = state.id
        assertNotNull id
        state.delete()
        assertNull State.get(id)
    }


    void testValidation() {
        def state = newState()
        assertTrue "State could not be validated as expected due to ${state.errors}", state.validate()
    }


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


    void testMaxSizeValidationFailures() {
        def state = new State(
                ediEquiv: 'XXXX',
                statscan: 'XXXXXXX',
                ipeds: 'XXXXXXX')
        assertFalse "State should have failed validation", state.validate()
        assertErrorsFor state, 'maxSize', ['ediEquiv', 'statscan', 'ipeds']
    }


    void testValidationMessages() {
        def state = newState()

        state.code = null
        assertFalse state.validate()
        assertLocalizedError state, 'nullable', /.*Field.*code.*of class.*State.*cannot be null.*/, 'code'

        state.description = null
        assertFalse state.validate()
        assertLocalizedError state, 'nullable', /.*Field.*description.*of class.*State.*cannot be null.*/, 'description'


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(state_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
