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
            throw new ApplicationException(this, "@@r1:invalidState@@")
        }

        state
    }
}
