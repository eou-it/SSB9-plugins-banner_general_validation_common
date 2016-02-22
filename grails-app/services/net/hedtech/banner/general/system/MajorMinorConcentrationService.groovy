/*******************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the MajorMinorConcentration model.
 **/
class MajorMinorConcentrationService extends ServiceBase {
    boolean transactional = true

    /**
     * fetch MajorMinorConcentration based on code
     * @param code
     * @return MajorMinorConcentration
     */
    MajorMinorConcentration fetchByCode(String code){
        return MajorMinorConcentration.fetchByCode(code)
    }
}
