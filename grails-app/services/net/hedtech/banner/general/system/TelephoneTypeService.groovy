/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the TelephoneTypeService model.
 **/
class TelephoneTypeService extends ServiceBase {
    boolean transactional = true

    /**
     * fetching TelephoneType details based on code
     * @param code
     * @return
     */
      TelephoneType fetchByCode(String code){
        return TelephoneType.fetchByCode(code)
    }
}
