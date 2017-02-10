/** *****************************************************************************
 Copyright 2009-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Disability model. 
 * */
class DisabilityService extends ServiceBase{

    boolean transactional = true

    def fetchDisability(def code) {
        def disability

        Disability.withSession { session ->
            disability = session.getNamedQuery(
                    'Disability.fetchByCode')
                    .setString('code', code).list()[0]
        }

        if(!disability){
            throw new ApplicationException(Disability, "@@r1:invalidDisabilty@@")
        }

        disability
    }

}
