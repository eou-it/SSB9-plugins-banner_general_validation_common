/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.overall

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.service.ServiceBase

class IntegrationConfigurationService extends  ServiceBase {

    boolean transactional = true

    static final String PROCESS_CODE = "HEDM"
    static final String NATION_ISO = "NATION.ISOCODE"

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
            throw new ApplicationException('Nation ISOCODE', new BusinessLogicValidationException('goriccr.invalid.value.message', [NATION_ISO]))
        }
    }
}
