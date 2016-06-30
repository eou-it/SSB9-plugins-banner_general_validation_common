/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

/**
 *  International Organization for Standardization (ISO) develop and publish International Standards.
 *
 *  ISO 3166-1 defines codes for countries
 *  ISO 3166-1 alpha-2  -  Two-letter country codes - known as ISO2
 *  ISO 3166-1 alpha-3 – three-letter country codes - known as ISO3
 *
 *  ISO 639 is the International Standard for language codes.
 *  ISO 639-1 alpha-2 - Two-letter language codes
 *  ISO 639-3 alpha-3 - Three-letter language codes
 *
 *  ISO 4217 is the International Standard for currency codes.
 *
 *  This class provides utility methods to deal with ISO codes of countries, languages and currencies
 */
class IsoCodeService {

    static transactional = false

    private Map<String, String> iso3ToIso2CountryCodeMap = [:]
    private Map<String, String> iso2ToIso3CountryCodeMap = [:]

    private Map<String, String> iso3ToIso2LanguageCodeMap = [:]
    private Map<String, String> iso2ToIso3LanguageCodeMap = [:]

    private Set<String> iso4217CurrencyCodes = []
    private Map<String, String> iso2CountryCodeToIso4217CurrencyCodeMap = [:]
    private Map<String, String> iso3CountryCodeToIso4217CurrencyCodeMap = [:]


    IsoCodeService() {
        String[] iso2CountryCodes = Locale.getISOCountries()
        for (String iso2CountryCode : iso2CountryCodes) {
            Locale locale = new Locale("", iso2CountryCode);
            iso3ToIso2CountryCodeMap.put(locale.getISO3Country(), iso2CountryCode);
            iso2ToIso3CountryCodeMap.put(iso2CountryCode, locale.getISO3Country())
            Currency currency = Currency.getInstance(locale)
            if (currency) {
                iso3CountryCodeToIso4217CurrencyCodeMap.put(locale.getISO3Country(), currency.getCurrencyCode())
                iso2CountryCodeToIso4217CurrencyCodeMap.put(iso2CountryCode, currency.getCurrencyCode())
                iso4217CurrencyCodes << currency.getCurrencyCode()
            }
        }

        String[] iso2LanguageCodes = Locale.getISOLanguages()
        for (String iso2LanguageCode : iso2LanguageCodes) {
            Locale locale = new Locale(iso2LanguageCode);
            iso3ToIso2LanguageCodeMap.put(locale.getISO3Language(), iso2LanguageCode);
            iso2ToIso3LanguageCodeMap.put(iso2LanguageCode, locale.getISO3Language())
        }
    }

    /**
     * For a given ISO3 country code returns corresponding ISO2 country code
     *
     * @param iso3CountryCode ISO3 country code
     * @return
     */
    String getISO2CountryCode(String iso3CountryCode) {
        return iso3ToIso2CountryCodeMap.get(iso3CountryCode?.toUpperCase())
    }

    /**
     * For a given ISO2 country code returns corresponding ISO3 country code
     *
     * @param iso2CountryCode ISO2 country code
     * @return
     */
    String getISO3CountryCode(String iso2CountryCode) {
        return iso2ToIso3CountryCodeMap.get(iso2CountryCode?.toUpperCase())
    }


    String getISO2LanguageCode(String iso3LanguageCode) {
        return iso3ToIso2LanguageCodeMap.get(iso3LanguageCode?.toLowerCase())
    }


    String getISO3LanguageCode(String iso2LanguageCode) {
        return iso2ToIso3LanguageCodeMap.get(iso2LanguageCode?.toLowerCase())
    }


    boolean isValidISO4217CurrencyCode(String iso4217CurrencyCode) {
        boolean valid = false
        if (iso4217CurrencyCode) {
            if (iso4217CurrencyCodes.contains(iso4217CurrencyCode.toUpperCase())) {
                valid = true
            }
        }
        return valid
    }


    String getISO4217CurrencyCodeByISO2CountryCode(String iso2CountryCode) {
        return iso2CountryCodeToIso4217CurrencyCodeMap.get(iso2CountryCode?.toUpperCase())
    }


    String getISO4217CurrencyCodeByISO3CountryCode(String iso3CountryCode) {
        return iso3CountryCodeToIso4217CurrencyCodeMap.get(iso3CountryCode?.toUpperCase())
    }

}
