/*******************************************************************************
 Copyright 2016-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class StateServiceIntegrationTests extends BaseIntegrationTestCase {

    def stateService

    final String NEW_YORK = 'NY'
    final String NEW_YORK_ISO = 'US-NY'

    final String TEXAS = 'TX'
    final String TEXAS_ISO = 'US-TX'

    final String UTAH = 'UT'
    final String UTAH_ISO = 'US-UT'



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
    void testListStates() {
        def stateList = State.list()

        assertEquals 115, stateList.size()
    }

    @Test
    void testFetchStateList() {
        def stateList = stateService.fetchStateList()

        assertEquals 10, stateList.size()
    }

    @Test
    void testFetchStateListFifty() {
        def stateList = stateService.fetchStateList(50)

        assertEquals 50, stateList.size()
        assertEquals 'Alabama', stateList[0].description
        assertEquals 'Michigan', stateList[49].description
    }

    @Test
    void testFetchStateListMidList() {
        def stateList = stateService.fetchStateList(12, 20)

        assertEquals 12, stateList.size()
        assertEquals 'Delaware', stateList[0].description
    }

    @Test
    void testFetchPStatesList() {
        def stateList = stateService.fetchStateList(10, 0, 'p')

        assertEquals 10, stateList.size()
        assertEquals 'Australian Capital Territory', stateList[0].description
    }

    @Test
    void testFetchPStatesMidList() {
        def stateList = stateService.fetchStateList(10, 10, 'p')

        assertEquals 3, stateList.size()
        assertEquals 'Provence of Quebec', stateList[0].description
    }


    @Test
    void testInvalidState(){
        try{
            stateService.fetchState('DJD')
            fail("I should have received an error but it passed; @@r1:invalidState@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidState"
        }
    }

    @Test
    void testValidState(){
        def state = stateService.fetchState('NJ')
        assertEquals 'New Jersey', state.description
    }

    @Ignore
    void test_FetchAllByCodeInList(){
        def stateCodes = [NEW_YORK, TEXAS, UTAH]

        updateStateWithIsoCodeInList(stateCodes)

        def states = stateService.fetchAllByCodeInList(stateCodes)
        assertNotNull states
        assertTrue states.size() > 0

        def new_york_state = states.find {state -> state.code == NEW_YORK}
        assertNotNull new_york_state

        def texas_state = states.find {state -> state.code == TEXAS}
        assertNotNull texas_state

        def utah_state = states.find {state -> state.code == UTAH}
        assertNotNull utah_state
    }

    private updateStateWithIsoCodeInList(Collection<String> stateCodes) {
        Session session = sessionFactory.getCurrentSession()

        if(stateCodes && stateCodes.contains(NEW_YORK)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + NEW_YORK_ISO + "' WHERE STVSTAT_CODE='" + NEW_YORK + "' ").executeUpdate()
        }

        if(stateCodes && stateCodes.contains(TEXAS)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + TEXAS_ISO + "' WHERE STVSTAT_CODE='" + TEXAS + "' ").executeUpdate()
        }

        if(stateCodes && stateCodes.contains(UTAH)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + UTAH_ISO + "' WHERE STVSTAT_CODE='" + UTAH + "' ").executeUpdate()
        }
    }
}
