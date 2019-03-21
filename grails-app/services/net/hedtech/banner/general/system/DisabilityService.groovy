/** *****************************************************************************
 Copyright 2009-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * A transactional service supporting persistence of the Disability model. 
 * */
@Transactional
class DisabilityService extends ServiceBase{

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
