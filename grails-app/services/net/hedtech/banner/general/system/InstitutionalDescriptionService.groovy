/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the InstitutionalDescriptionService model. 
 * */
class InstitutionalDescriptionService extends ServiceBase{

    boolean transactional = true


    def InstitutionalDescription findByKey() {
        return InstitutionalDescription.fetchByKey()
    }
}
