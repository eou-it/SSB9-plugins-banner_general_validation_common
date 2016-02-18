/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the AddressType model.
 **/
class AddressTypeService extends ServiceBase {

    boolean transactional = true

    /**
     * fetch AddressType based on code
     * @param code
     * @return AddressType
     */
    AddressType fetchByCode(String code){
        return AddressType.fetchByCode(code)
    }
}
