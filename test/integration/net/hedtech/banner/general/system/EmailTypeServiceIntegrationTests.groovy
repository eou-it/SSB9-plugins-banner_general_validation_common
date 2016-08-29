/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
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
}