/*******************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * A transactional service supporting persistence of the MajorMinorConcentration model.
 **/
@Transactional
class MajorMinorConcentrationService extends ServiceBase {

    /**
     * fetch MajorMinorConcentration based on code
     * @param code
     * @return MajorMinorConcentration
     */
    MajorMinorConcentration fetchByCode(String code){
        return MajorMinorConcentration.fetchByCode(code)
    }
}
