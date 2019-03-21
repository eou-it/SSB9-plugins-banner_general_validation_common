/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class IsoCodeServiceIntegrationTests extends BaseIntegrationTestCase {

    IsoCodeService isoCodeService


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
    void testGetISO2CountryCode() {
        // Give null, Get null
        String iso3CountryCode = null
        String iso2CountryCode = isoCodeService.getISO2CountryCode(iso3CountryCode)
        assertNull iso2CountryCode

        // Give empty, Get null
        iso3CountryCode = ""
        iso2CountryCode = isoCodeService.getISO2CountryCode(iso3CountryCode)
        assertNull iso2CountryCode

        // Give invalid ISO3 code, Get null
        iso3CountryCode = "XXX"
        iso2CountryCode = isoCodeService.getISO2CountryCode(iso3CountryCode)
        assertNull iso2CountryCode

        // Give valid ISO3 code, Get valid ISO2 code
        iso3CountryCode = "USA"
        iso2CountryCode = isoCodeService.getISO2CountryCode(iso3CountryCode)
        assertNotNull iso2CountryCode
        assertEquals "US", iso2CountryCode

        // Give valid ISO3 code in lower case, Get valid ISO2 code
        iso3CountryCode = "usa"
        iso2CountryCode = isoCodeService.getISO2CountryCode(iso3CountryCode)
        assertNotNull iso2CountryCode
        assertEquals "US", iso2CountryCode
    }


    @Test
    void testGetISO3CountryCode() {
        // Give null, Get null
        String iso2CountryCode = null
        String iso3CountryCode = isoCodeService.getISO3CountryCode(iso2CountryCode)
        assertNull iso3CountryCode

        // Give empty, Get null
        iso2CountryCode = ""
        iso3CountryCode = isoCodeService.getISO3CountryCode(iso2CountryCode)
        assertNull iso3CountryCode

        // Give invalid ISO2 code, Get null
        iso2CountryCode = "XX"
        iso3CountryCode = isoCodeService.getISO3CountryCode(iso2CountryCode)
        assertNull iso3CountryCode

        // Give valid ISO2 code, Get valid ISO3 code
        iso2CountryCode = "US"
        iso3CountryCode = isoCodeService.getISO3CountryCode(iso2CountryCode)
        assertNotNull iso3CountryCode
        assertEquals "USA", iso3CountryCode

        // Give valid ISO2 code in lower case, Get valid ISO3 code
        iso2CountryCode = "us"
        iso3CountryCode = isoCodeService.getISO3CountryCode(iso2CountryCode)
        assertNotNull iso3CountryCode
        assertEquals "USA", iso3CountryCode
    }


    @Test
    void testGetISO2LanguageCode() {
        // Give null, Get null
        String iso3LanguageCode = null
        String iso2LanguageCode = isoCodeService.getISO2LanguageCode(iso3LanguageCode)
        assertNull iso2LanguageCode

        // Give empty, Get null
        iso3LanguageCode = ""
        iso2LanguageCode = isoCodeService.getISO2LanguageCode(iso3LanguageCode)
        assertNull iso2LanguageCode

        // Give invalid ISO3 code, Get null
        iso3LanguageCode = "xxx"
        iso2LanguageCode = isoCodeService.getISO2LanguageCode(iso3LanguageCode)
        assertNull iso2LanguageCode

        // Give valid ISO3 code, Get valid ISO2 code
        iso3LanguageCode = "eng"
        iso2LanguageCode = isoCodeService.getISO2LanguageCode(iso3LanguageCode)
        assertNotNull iso2LanguageCode
        assertEquals "en", iso2LanguageCode

        // Give valid ISO3 code in upper case, Get valid ISO2 code
        iso3LanguageCode = "ENG"
        iso2LanguageCode = isoCodeService.getISO2LanguageCode(iso3LanguageCode)
        assertNotNull iso2LanguageCode
        assertEquals "en", iso2LanguageCode
    }


    @Test
    void testGetISO3LanguageCode() {
        // Give null, Get null
        String iso2LanguageCode = null
        String iso3LanguageCode = isoCodeService.getISO3LanguageCode(iso2LanguageCode)
        assertNull iso3LanguageCode

        // Give empty, Get null
        iso2LanguageCode = ""
        iso3LanguageCode = isoCodeService.getISO3LanguageCode(iso2LanguageCode)
        assertNull iso3LanguageCode

        // Give invalid ISO2 code, Get null
        iso2LanguageCode = "xx"
        iso3LanguageCode = isoCodeService.getISO3LanguageCode(iso2LanguageCode)
        assertNull iso3LanguageCode

        // Give valid ISO2 code, Get valid ISO3 code
        iso2LanguageCode = "en"
        iso3LanguageCode = isoCodeService.getISO3LanguageCode(iso2LanguageCode)
        assertNotNull iso3LanguageCode
        assertEquals "eng", iso3LanguageCode

        // Give valid ISO2 code in upper case, Get valid ISO3 code
        iso2LanguageCode = "EN"
        iso3LanguageCode = isoCodeService.getISO3LanguageCode(iso2LanguageCode)
        assertNotNull iso3LanguageCode
        assertEquals "eng", iso3LanguageCode
    }


    @Test
    void testIsValidISO4217CurrencyCode() {
        // Give null, Get false
        String iso4217CurrencyCode = null
        assertFalse isoCodeService.isValidISO4217CurrencyCode(iso4217CurrencyCode)

        // Give invalid, Get false
        iso4217CurrencyCode = "xxx"
        assertFalse isoCodeService.isValidISO4217CurrencyCode(iso4217CurrencyCode)

        // Give valid, Get true
        iso4217CurrencyCode = "USD"
        assertTrue isoCodeService.isValidISO4217CurrencyCode(iso4217CurrencyCode)

        // Give valid in lower case, Get true
        iso4217CurrencyCode = "usd"
        assertTrue isoCodeService.isValidISO4217CurrencyCode(iso4217CurrencyCode)
    }


    @Test
    void testGetISO4217CurrencyCodeByISO3CountryCode() {
        // Give null, Get null
        String iso3CountryCode = null
        String iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO3CountryCode(iso3CountryCode)
        assertNull iso4217CurrencyCode

        // Give empty, Get null
        iso3CountryCode = ""
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO3CountryCode(iso3CountryCode)
        assertNull iso4217CurrencyCode

        // Give invalid ISO3 code, Get null
        iso3CountryCode = "XXX"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO3CountryCode(iso3CountryCode)
        assertNull iso4217CurrencyCode

        // Give valid ISO3 code, Get valid currency code
        iso3CountryCode = "USA"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO3CountryCode(iso3CountryCode)
        assertNotNull iso4217CurrencyCode
        assertEquals "USD", iso4217CurrencyCode

        // Give valid ISO3 code in lower case, Get valid currency code
        iso3CountryCode = "usa"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO3CountryCode(iso3CountryCode)
        assertNotNull iso4217CurrencyCode
        assertEquals "USD", iso4217CurrencyCode
    }


    @Test
    void testGetISO4217CurrencyCodeByISO2CountryCode() {
        // Give null, Get null
        String iso2CountryCode = null
        String iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO2CountryCode(iso2CountryCode)
        assertNull iso4217CurrencyCode

        // Give empty, Get null
        iso2CountryCode = ""
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO2CountryCode(iso2CountryCode)
        assertNull iso4217CurrencyCode

        // Give invalid ISO2 code, Get null
        iso2CountryCode = "XX"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO2CountryCode(iso2CountryCode)
        assertNull iso4217CurrencyCode

        // Give valid ISO2 code, Get valid ISO3 code
        iso2CountryCode = "US"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO2CountryCode(iso2CountryCode)
        assertNotNull iso4217CurrencyCode
        assertEquals "USD", iso4217CurrencyCode

        // Give valid ISO2 code in lower case, Get valid ISO3 code
        iso2CountryCode = "us"
        iso4217CurrencyCode = isoCodeService.getISO4217CurrencyCodeByISO2CountryCode(iso2CountryCode)
        assertNotNull iso4217CurrencyCode
        assertEquals "USD", iso4217CurrencyCode
    }

}
