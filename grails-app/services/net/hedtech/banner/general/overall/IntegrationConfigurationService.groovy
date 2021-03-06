/*******************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.overall

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.ldm.HedmAddressType
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional

@Transactional
class IntegrationConfigurationService extends ServiceBase {

    static final String PROCESS_CODE = "HEDM"
    static final String NATION_ISO = "NATION.ISOCODE"
    static final String ADDRESSES_DEFAULT_ADDRESSTYPE = "ADDRESSES.DEFAULT.ADDRESSTYPE"
    static final String COUNTRY_DEFAULT_ISO = "ADDRESS.COUNTRY.DEFAULT"
    static final String BASE_ISO_CURRENCY_CODE = "ISO.BASE.CURRENCY.CODE"
    static final String DEFAULT_ISO_CURRENCY_CODE = 'USD.ISO.CURRENCY.CODE'

    IsoCodeService isoCodeService


    public boolean isInstitutionUsingISO2CountryCodes() {
        IntegrationConfiguration intConfig = fetchByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)
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
        IntegrationConfiguration intConf = fetchByProcessCodeAndSettingName(PROCESS_CODE, ADDRESSES_DEFAULT_ADDRESSTYPE)
        if (!intConf) {
            throw new ApplicationException("Default Address", new BusinessLogicValidationException("goriccr.not.found.message", [ADDRESSES_DEFAULT_ADDRESSTYPE]))
        }
        HedmAddressType hedmAddressType = HedmAddressType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6)
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
        IntegrationConfiguration intConfig = fetchByProcessCodeAndSettingName(PROCESS_CODE, COUNTRY_DEFAULT_ISO)
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
        IntegrationConfiguration intConf = fetchByProcessCodeAndSettingName(PROCESS_CODE, settingName)
        if (!isoCodeService.getISO3CountryCode(intConf.value)) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
        }
        return intConf.value
    }


    /**
     * ISO 3166-1 two-letter region code that denotes the region that we are expecting the number to be from.
     *
     * @return
     */
    String getDefaultISO2CountryCodeForOrganizationPhoneNumberParsing() {
        String settingName = "PHONES.COUNTRY.DEFAULT"
        IntegrationConfiguration intConf = fetchByProcessCodeAndSettingName(PROCESS_CODE, settingName)
        if (!isoCodeService.getISO3CountryCode(intConf.value)) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.invalid.value.message", [settingName]))
        }
        return intConf.value
    }

    boolean canUpdatePersonSSN() {
        boolean val = false
        String settingName = "PERSON.UPDATESSN"
        IntegrationConfiguration intConf = fetchByProcessCodeAndSettingName(PROCESS_CODE, settingName)
        if (intConf.value == 'Y') {
            val = true
        }
        return val
    }

    /**
     * Default zip code to be associated with the address of person
     *
     * In address,
     * if STATE is present, then ZIP is required
     * if COUNTRY is present, then neither STATE nor ZIP are required
     * If STATE is present (regardless of whether COUNTRY is present or not), then ZIP is required
     *
     * @return
     */
    String getDefaultZipCode() {
        IntegrationConfiguration intConfig = fetchByProcessCodeAndSettingName(PROCESS_CODE, "PERSON.ADDRESSES.POSTAL.CODE")
        return intConfig.value
    }

    /**
     * Default zip code to be associated with the address of Organization
     *
     * In address,
     * if STATE is present, then ZIP is required
     * if COUNTRY is present, then neither STATE nor ZIP are required
     * If STATE is present (regardless of whether COUNTRY is present or not), then ZIP is required
     *
     * @return
     */
    String getDefaultOrganizationZipCode() {
        IntegrationConfiguration intConfig = fetchByProcessCodeAndSettingName(PROCESS_CODE, "ADDRESSES.POSTAL.CODE")
        return intConfig.value
    }


    IntegrationConfiguration fetchByProcessCodeAndSettingName(String processCode, String settingName) {
        IntegrationConfiguration intConfig = IntegrationConfiguration.fetchByProcessCodeAndSettingName(processCode, settingName)
        if (!intConfig) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return intConfig
    }


    def fetchAllByProcessCodeAndSettingName(String processCode, String settingName) {
        def intConfs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(processCode, settingName)
        if (!intConfs) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return intConfs
    }

    private String getDefaultBaseIsoCurrencyCode() {
        String defaultCurrencyCode
        List<IntegrationConfiguration> currencyCodeConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, BASE_ISO_CURRENCY_CODE)
        if (currencyCodeConfigs.size() > 0) {
            defaultCurrencyCode = currencyCodeConfigs[0].value
        } else {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [BASE_ISO_CURRENCY_CODE]))
        }
        return defaultCurrencyCode
    }

    private String getDefaultIsoCurrencyCode() {
        String defaultCurrencyCode
        List<IntegrationConfiguration> currencyCodeConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, DEFAULT_ISO_CURRENCY_CODE)
        if (currencyCodeConfigs.size() > 0) {
            defaultCurrencyCode = currencyCodeConfigs[0].value
        } else {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("goriccr.not.found.message", [DEFAULT_ISO_CURRENCY_CODE]))
        }
        return defaultCurrencyCode
    }


}
