/** *****************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

class StateService extends ServiceBase {

    boolean transactional = true

    State fetchByCode(String code){
        State state = State.withSession { session ->
        session.getNamedQuery('State.fetchByCode').setString('code',code).uniqueResult()
    }
        return state
    }

}
