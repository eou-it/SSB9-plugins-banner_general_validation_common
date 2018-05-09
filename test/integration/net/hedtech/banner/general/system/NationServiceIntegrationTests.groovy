/** *****************************************************************************
 Copyright 2016-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class NationServiceIntegrationTests extends BaseIntegrationTestCase {
    def nationService

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
    void testListNations() {
        def nationList = Nation.list()

        assertTrue 176 <= nationList.size()
    }

    @Test
    void testFetchNationList() {
        def nationList = nationService.fetchNationList()

        assertEquals 10, nationList.size()
    }

    @Test
    void testFetchNationListFifty() {
        def nationList = nationService.fetchNationList(50)

        assertEquals 50, nationList.size()
        assertEquals '5 Character Test', nationList[0].nation
        assertEquals 'Bosnia-Herzegovina', nationList[49].nation
    }

    @Test
    void testFetchNationListMidList() {
        def nationList = nationService.fetchNationList(12, 8)

        assertEquals 12, nationList.size()
        assertEquals 'American Samoa', nationList[0].nation
    }

    @Test
    void testFetchPNationsList() {
        def nationList = nationService.fetchNationList(10, 0, 'p')

        assertEquals 10, nationList.size()
        assertEquals 'Cape Verde', nationList[0].nation
    }

    @Test
    void testFetchPNationsMidList() {
        def nationList = nationService.fetchNationList(10, 20, 'p')

        assertEquals 10, nationList.size()
        assertEquals 'Japan', nationList[0].nation
    }


    @Test
    void testInvalidNation(){
        try{
            nationService.fetchNation('DJD')
            fail("I should have received an error but it passed; @@r1:invalidNation@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidNation"
        }
    }

    @Test
    void testValidNation(){
        def nation = nationService.fetchNation('7826')
        assertEquals 'Scotland', nation.nation
    }
}
