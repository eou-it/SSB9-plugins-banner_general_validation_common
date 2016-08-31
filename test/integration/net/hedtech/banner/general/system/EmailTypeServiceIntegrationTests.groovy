/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class EmailTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def emailTypeService

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
    void testFetchByCodeAndWebDisplayable() {
        def emailType  = emailTypeService.fetchByCodeAndWebDisplayable('BUSI');

        assertEquals 'Business E-Mail', emailType.description
    }

    @Test
    void testFetchByCodeAndNotWebDisplayable() {
        try {
            def emailType = emailTypeService.fetchByCodeAndWebDisplayable('SCHL');
            fail("I should have received an error but it passed; @@r1:invalidEmailType@@ ")
        }
        catch(ApplicationException ae) {
            assertApplicationException ae, "invalidEmailType"
        }
    }

    @Test
    void testListEmailTypes() {
        def emailTypeList = EmailType.list()

        assertEquals 29, emailTypeList.size()
    }

    @Test
    void testFetchEmailTypeList() {
        def emailTypeList = emailTypeService.fetchEmailTypeList()

        assertEquals 10, emailTypeList.size()
    }

    @Test
    void testFetchEmailTypeListFifty() {
        def emailTypeList = emailTypeService.fetchEmailTypeList(50)

        assertEquals 28, emailTypeList.size()
        assertEquals 'AOL Email', emailTypeList[0].description
        assertEquals 'Support E-Mail', emailTypeList[27].description
    }

    @Test
    void testFetchEmailTypeListMidList() {
        def emailTypeList = emailTypeService.fetchEmailTypeList(12, 15)

        assertEquals 12, emailTypeList.size()
        assertEquals 'Legal E-Mail', emailTypeList[0].description
    }

    @Test
    void testFetchOEmailTypesList() {
        def emailTypeList = emailTypeService.fetchEmailTypeList(10, 0, 'o')

        assertEquals 10, emailTypeList.size()
        assertEquals 'AOL Email', emailTypeList[0].description
    }

    @Test
    void testFetchOEmailTypesMidList() {
        def emailTypeList = emailTypeService.fetchEmailTypeList(5, 2, 'o')

        assertEquals 5, emailTypeList.size()
        assertEquals 'Colorado Springs Email', emailTypeList[0].description
    }
}
