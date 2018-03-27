/** *****************************************************************************
 Copyright 2017-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class InstitutionalDescriptionServiceIntegrationTests extends BaseIntegrationTestCase {

    InstitutionalDescriptionService institutionalDescriptionService
    IsoCodeService isoCodeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testGetISO4217CurrencyCodeForInstitutionBaseCurrency() {
        def isoCurrencyCode = institutionalDescriptionService.getISO4217CurrencyCodeForInstitutionBaseCurrency()

        assertNotNull isoCurrencyCode
        assertTrue(isoCodeService.isValidISO4217CurrencyCode(isoCurrencyCode))
    }


    @Test
    void testGetDatabaseUtcOffset() {
        String utcOffset = institutionalDescriptionService.getDatabaseUtcOffset()
        assertNotNull utcOffset
        assertNotNull institutionalDescriptionService.getDatabaseSessionTimeZone()
        assertEquals TimeZone.getDefault().getID(), institutionalDescriptionService.getDatabaseSessionTimeZone()
    }

}
