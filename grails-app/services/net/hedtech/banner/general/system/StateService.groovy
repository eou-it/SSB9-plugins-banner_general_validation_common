/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

class StateService extends ServiceBase {

    def fetchStateList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = State.createCriteria()
        def stateList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        stateList
    }

    def fetchState(def code) {
        def state = State.fetchByCode(code)

        if(!state){
            throw new ApplicationException(State, "@@r1:invalidState@@")
        }

        state
    }

    public def fetchAllByCodeInList(Collection<String> codes) {
        def states = []
        if (codes) {
            states = State.withSession { session ->
                def query = session.getNamedQuery('State.fetchAllByCodeInList')
                query.setParameterList('codes', codes)
                query.list()
            }
        }
        return states
    }


    public Map fetchIsoCodeToStateCodeMap(Collection<String> stateCodeList) {

        Map<String, String> stateToIsoMap = [:]
        List<State> isoCodeList = this.fetchAllByCodeInList(stateCodeList)
        isoCodeList?.each {
            stateToIsoMap.put(it.code, it)
        }
        return stateToIsoMap
    }
}
