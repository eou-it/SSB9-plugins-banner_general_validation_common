/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Nation model.
 * */
class NationService extends ServiceBase {

    public List<Nation> fetchAllByCodeInList(Collection<String> nationCodes) {
        def entities = []
        if (nationCodes) {
            String hql = '''from Nation a WHERE a.code in :nationCodes'''
            Nation.withSession { session ->
                def query = session.createQuery(hql)
                query.setParameterList('nationCodes', nationCodes)
                entities = query.list()
            }
        }
        return entities
    }

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
