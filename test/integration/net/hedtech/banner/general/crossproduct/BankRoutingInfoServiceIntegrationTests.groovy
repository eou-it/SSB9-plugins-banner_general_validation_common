/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.general.overall.BankRoutingInfoService

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 *
 */
class BankRoutingInfoServiceIntegrationTests extends BaseIntegrationTestCase {

    def bankRoutingInfoService
    
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testRoutingNumberValidation() {
        try {
            bankRoutingInfoService.validateRoutingNumber("103448999");
            fail("I should have received an error but it passed; @@r1:invalidRoutingNum@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidRoutingNum"
        }
    }

    @Test
    void testRoutingNumberFormatValidation() {
        try {
            bankRoutingInfoService.validateRoutingNumFormat("fail1234abc");
            fail("I should have received an error but it passed; @@r1:invalidRoutingNumFmt@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidRoutingNumFmt"
        }
    }
}