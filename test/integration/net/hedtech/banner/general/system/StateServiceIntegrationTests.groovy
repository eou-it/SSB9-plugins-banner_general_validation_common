package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by ddunn on 6/13/2016.
 */
class StateServiceIntegrationTests extends BaseIntegrationTestCase {

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
}
