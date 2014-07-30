/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase


/**
 * A transactional service supporting persistence of the College model.
 **/
class CollegeService extends ServiceBase {

    boolean transactional = true

    /**
     * Finds College for a give collegeCode
     * @param collegeCode
     * @return College - Entity
     */
    College fetchByCode(collegeCode){
        College.findByCode(collegeCode)

    }

}
