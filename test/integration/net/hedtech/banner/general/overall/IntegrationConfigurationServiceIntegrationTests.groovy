/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.overall

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class IntegrationConfigurationServiceIntegrationTests extends BaseIntegrationTestCase {

    static final String PROCESS_CODE = "HEDM"
    static final String NATION_ISO = "NATION.ISOCODE"
    IntegrationConfigurationService integrationConfigurationService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testIsInstitutionUsingISO2CountryCodesTrue() {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)[0]
        if (!intConfig) {
            shouldFail(ApplicationException) {
                integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            }
        } else {
            intConfig.value = '2'
            boolean result
            result = integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            assertTrue(result)
        }
    }

    @Test
    void testIsInstitutionUsingISO2CountryCodesFalse() {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)[0]
        if (!intConfig) {
            shouldFail(ApplicationException) {
                integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            }
        } else {
            intConfig.value = '3'
            boolean result
            result = integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            assertFalse(result)
        }
    }

    @Test
    void testIsInstitutionUsingISO2CountryCodesInvalid() {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)[0]
        if (!intConfig) {
            shouldFail(ApplicationException) {
                integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            }
        } else {
            intConfig.value = 'In-valid'
            boolean result
            shouldFail(ApplicationException) {
                result = integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
            }
        }
    }
}
