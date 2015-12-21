/*******************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * Created by invthannee on 6/2/2015.
 */
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
