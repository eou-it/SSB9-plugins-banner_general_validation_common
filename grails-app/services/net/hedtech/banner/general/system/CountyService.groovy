/** *****************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

class CountyService extends ServiceBase {

    def fetchCountyList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = County.createCriteria()
        def countyList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        countyList
    }

    def fetchCounty(def code) {
        def county = County.fetchByCode(code)

        if(!county){
            throw new ApplicationException(County, "@@r1:invalidCounty@@")
        }

        county
    }


    public def fetchAllByCodeInList(Collection<String> codes) {
        def counties = []
        if (codes) {
            counties = County.withSession { session ->
                def query = session.getNamedQuery('County.fetchAllByCodeInList')
                query.setParameterList('codes', codes)
                query.list()
            }
        }
        return counties
    }


    def getCodeToIsoCodeMap(Collection<String> countyCodeList) {

        Map<String, String> countyToIsoMap = [:]
        List<County> isoCodeList = this.fetchAllByCodeInList(countyCodeList)
        isoCodeList?.each {
            countyToIsoMap.put(it.code, it.isoCode)
        }
        return countyToIsoMap
    }


    def fetchAllByIsoCode(String isoCode) {
        Collection<County> entities = []
        if (isoCode) {
            entities = County.withSession { session ->
                session.getNamedQuery('County.fetchAllByIsoCode').setString('isoCode', isoCode).list()
            }
        }
        return entities
    }

}
