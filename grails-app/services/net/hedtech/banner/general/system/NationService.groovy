package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

class NationService extends ServiceBase {

    def fetchNationList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = Nation.createCriteria()
        def nationList = criteria.list(max: max, offset: offset, sort: 'nation', order: 'asc') {
            and {
                ilike("nation", "%${searchString}%")
            }
        }

        nationList
    }

    def fetchNation(def code) {
        def nation = Nation.fetchByCode(code)

        if(!nation){
            throw new ApplicationException(Nation, "@@r1:invalidNation@@")
        }

        nation
    }
}
