/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class StateServiceIntegrationTests extends BaseIntegrationTestCase {

    def stateService

    final String STATE_CODE_NY = 'NY'
    final String NEW_YORK_ISO = 'US-NY'

    final String STATE_CODE_TEXAS = 'TX'
    final String TEXAS_ISO = 'US-TX'

    final String STATE_CODE_UTAH = 'UT'
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

        assertTrue 115 <= stateList.size()
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
        assertEquals 'Massachusetts', stateList[49].description
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

        assertTrue 9 >= stateList.size()
        assertEquals 'Prince Edward Island', stateList[0].description
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

    @Test
    void test_FetchAllByCodeInList(){
        def stateCodes = [STATE_CODE_NY, STATE_CODE_TEXAS, STATE_CODE_UTAH]

        updateStateWithIsoCodeInList(stateCodes)

        def states = stateService.fetchAllByCodeInList(stateCodes)
        assertNotNull states
        assertTrue states.size() > 0

        def new_york_state = states.find {state -> state.code == STATE_CODE_NY}
        assertNotNull new_york_state

        def texas_state = states.find {state -> state.code == STATE_CODE_TEXAS}
        assertNotNull texas_state

        def utah_state = states.find {state -> state.code == STATE_CODE_UTAH}
        assertNotNull utah_state
    }

    @Test
    void test_FetchAllByIsoCode(){
        def stateCodes = [STATE_CODE_NY, STATE_CODE_TEXAS, STATE_CODE_UTAH]

        updateStateWithIsoCodeInList(stateCodes)

        def states = stateService.fetchAllByIsoCode(NEW_YORK_ISO)
        assertNotNull states
        assertTrue states.size() > 0

        def new_york_state = states.find {state -> state.code == STATE_CODE_NY}
        assertNotNull new_york_state
        assertNotNull new_york_state.isoCode
        assertTrue new_york_state.isoCode.equalsIgnoreCase(NEW_YORK_ISO)
    }

    @Test
    void test_getCodeToIsoCodeMap() {
        def stateCodes = [STATE_CODE_NY, STATE_CODE_TEXAS, STATE_CODE_UTAH]

        updateStateWithIsoCodeInList(stateCodes)

        def states = stateService.fetchAllByCodeInList(stateCodes)
        assertNotNull states
        assertTrue states.size() > 0

        Map statesMap = stateService.getCodeToIsoCodeMap([STATE_CODE_NY, STATE_CODE_TEXAS])
        assertNotNull statesMap
        assertTrue statesMap.size() == 2

        String stateNewNY = statesMap.get(STATE_CODE_NY)
        assertNotNull stateNewNY
        assertTrue NEW_YORK_ISO.equalsIgnoreCase(stateNewNY)

        String stateNewTX = statesMap.get(STATE_CODE_TEXAS)
        assertNotNull stateNewTX
        assertTrue TEXAS_ISO.equalsIgnoreCase(stateNewTX)

    }

    private updateStateWithIsoCodeInList(Collection<String> stateCodes) {
        Session session = sessionFactory.getCurrentSession()

        if(stateCodes && stateCodes.contains(STATE_CODE_NY)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + NEW_YORK_ISO + "' WHERE STVSTAT_CODE='" + STATE_CODE_NY + "' ").executeUpdate()
        }

        if(stateCodes && stateCodes.contains(STATE_CODE_TEXAS)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + TEXAS_ISO + "' WHERE STVSTAT_CODE='" + STATE_CODE_TEXAS + "' ").executeUpdate()
        }

        if(stateCodes && stateCodes.contains(STATE_CODE_UTAH)) {
            session.createSQLQuery( "UPDATE STVSTAT set STVSTAT_SCOD_CODE_ISO = '" + UTAH_ISO + "' WHERE STVSTAT_CODE='" + STATE_CODE_UTAH + "' ").executeUpdate()
        }
    }
}
