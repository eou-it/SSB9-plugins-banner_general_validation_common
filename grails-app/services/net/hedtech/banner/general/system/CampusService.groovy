/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

class CampusService extends ServiceBase {

    boolean transactional = true

    /**
     * Finds Campus for a give ccampusCode
     * @param campusCode
     * @return Campus - Entity
     */
    Campus fetchByCode(campusCode){
        Campus.findByCode(campusCode)

    }

}
