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
}
