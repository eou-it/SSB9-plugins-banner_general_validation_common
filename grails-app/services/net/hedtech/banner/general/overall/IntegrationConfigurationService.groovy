/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.overall

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.general.system.ldm.HedmAddressType
import net.hedtech.banner.general.common.GeneralValidationCommonConstants

class IntegrationConfigurationService extends  ServiceBase {

    boolean transactional = true

    IsoCodeService isoCodeService

    static final String PROCESS_CODE = "HEDM"
    static final String NATION_ISO = "NATION.ISOCODE"
    static final String ADDRESSES_DEFAULT_ADDRESSTYPE = "ADDRESSES.DEFAULT.ADDRESSTYPE"
    static final String COUNTRY_DEFAULT_ISO = "ADDRESS.COUNTRY.DEFAULT"

    public boolean isInstitutionUsingISO2CountryCodes(){
        IntegrationConfiguration intConfig= IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, NATION_ISO)[0]
        if (!intConfig) {
            throw new ApplicationException('Nation ISOCODE', new BusinessLogicValidationException("goriccr.not.found.message", [NATION_ISO]))
        }
        if (intConfig.value=='2'){
            return true
        }
        else if(intConfig.value=='3'){
            return false
        }
        else {
          //  throw new ApplicationException('Nation ISOCODE', new BusinessLogicValidationException('goriccr.invalid.value.message', [NATION_ISO]))
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


    public String getDefaultISOCountryCodeForAddress() {
        IntegrationConfiguration intConfig= IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(PROCESS_CODE, COUNTRY_DEFAULT_ISO)[0]
        if (!intConfig) {
            throw new ApplicationException('Country ISOCODE', new BusinessLogicValidationException("goriccr.not.found.message", [COUNTRY_DEFAULT_ISO]))
        }
        if (intConfig.value){
            if (isInstitutionUsingISO2CountryCodes()){
                if(isoCodeService.getISO3CountryCode(intConfig.value) == null){
                    throw new ApplicationException('Country ISOCODE', new BusinessLogicValidationException('goriccr.invalid.value.message', [COUNTRY_DEFAULT_ISO]))
                }
            } else {
                if(isoCodeService.getISO2CountryCode(intConfig.value) == null){
                    throw new ApplicationException('Country ISOCODE', new BusinessLogicValidationException('goriccr.invalid.value.message', [COUNTRY_DEFAULT_ISO]))
                }
            }
            return intConfig.value
        }
        else {
            throw new ApplicationException('Country ISOCODE', new BusinessLogicValidationException('goriccr.invalid.value.message', [COUNTRY_DEFAULT_ISO]))
        }
    }
}
