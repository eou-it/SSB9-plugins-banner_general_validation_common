/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.overall

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.ldm.HedmAddressType
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.service.ServiceBase

class IntegrationConfigurationService extends ServiceBase {

    static final String PROCESS_CODE = "HEDM"
    static final String NATION_ISO = "NATION.ISOCODE"
    static final String ADDRESSES_DEFAULT_ADDRESSTYPE = "ADDRESSES.DEFAULT.ADDRESSTYPE"
    static final String COUNTRY_DEFAULT_ISO = "ADDRESS.COUNTRY.DEFAULT"

    boolean transactional = true

    IsoCodeService isoCodeService


    public boolean isInstitutionUsingISO2CountryCodes() {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)[0]
        if (!intConfig) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [NATION_ISO]))
        }
        if (intConfig.value == '2') {
            return true
        } else if (intConfig.value == '3') {
            return false
        } else {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException('goriccr.invalid.value.message', [NATION_ISO]))
        }
    }


    public String getDefaultAddressTypeV6() {
        IntegrationConfiguration intConf = IntegrationConfiguration.findByProcessCodeAndSettingName(PROCESS_CODE, ADDRESSES_DEFAULT_ADDRESSTYPE)
        if (!intConf) {
            throw new ApplicationException("Default Address", new BusinessLogicValidationException("goriccr.not.found.message", [ADDRESSES_DEFAULT_ADDRESSTYPE]))
        }
        HedmAddressType hedmAddressType = HedmAddressType.getByString(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6)
        if (!hedmAddressType) {
            throw new ApplicationException("Default Address", new BusinessLogicValidationException("goriccr.invalid.value.message", [ADDRESSES_DEFAULT_ADDRESSTYPE]))
        }

        return intConf.translationValue
    }

    /**
     * Default country code to be displayed for Banner addresses that do not have an associated country code.
     * Country code can be 2-char ISO code or 3-char ISO code depending on client preference.
     *
     * @return
     */
    public String getDefaultISOCountryCodeForAddress() {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, COUNTRY_DEFAULT_ISO)[0]
        if (!intConfig) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [COUNTRY_DEFAULT_ISO]))
        }
        if (!isoCodeService.getISO2CountryCode(intConfig.value) && !isoCodeService.getISO3CountryCode(intConfig.value)) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException('goriccr.invalid.value.message', [COUNTRY_DEFAULT_ISO]))
        }
        return intConfig.value
    }


    public String getDefaultISO3CountryCodeForAddress() {
        String isoCountryCode = getDefaultISOCountryCodeForAddress()
        if (isInstitutionUsingISO2CountryCodes()) {
            isoCountryCode = isoCodeService.getISO3CountryCode(isoCountryCode)

        }
        return isoCountryCode
    }


    public String getDefaultISO3CountryCodeForAddress(boolean institutionUsingISO2CountryCodes) {
        String isoCountryCode = getDefaultISOCountryCodeForAddress()
        if (institutionUsingISO2CountryCodes) {
            isoCountryCode = isoCodeService.getISO3CountryCode(isoCountryCode)

        }
        return isoCountryCode
    }

    /**
     * ISO 3166-1 two-letter region code that denotes the region that we are expecting the number to be from.
     *
     * @return
     */
    String getDefaultISO2CountryCodeForPhoneNumberParsing() {
        String settingName = "PERSON.PHONES.COUNTRY.DEFAULT"
        IntegrationConfiguration intConf = getIntegrationConfiguration(PROCESS_CODE, settingName)
        if (!isoCodeService.getISO3CountryCode(intConf.value)) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
        }
        return intConf.value
    }


    boolean canUpdatePersonSSN() {
        boolean val = false
        String settingName = "PERSON.UPDATESSN"
        IntegrationConfiguration intConf = getIntegrationConfiguration(PROCESS_CODE, settingName)
        if (intConf.value == 'Y') {
            val = true
        }
        return val
    }


    private IntegrationConfiguration getIntegrationConfiguration(processCode, settingName) {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchByProcessCodeAndSettingName(processCode, settingName)
        if (!intConfig) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return intConfig
    }

}
