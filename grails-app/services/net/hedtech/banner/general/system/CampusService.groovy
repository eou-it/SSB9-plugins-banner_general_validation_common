/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional

@Transactional
class CampusService extends ServiceBase {

    /**
     * Finds Campus for a give ccampusCode
     * @param campusCode
     * @return Campus - Entity
     */
    Campus fetchByCode(campusCode) {
        Campus.findByCode(campusCode)

    }


    def getCodeToTimeZoneIDMap() {
        return fetchAllCodeTimeZoneID().collectEntries { [it[0], it[1]] }
    }


    private def fetchAllCodeTimeZoneID() {
        def entities = Campus.withSession { session ->
            session.getNamedQuery('Campus.fetchAllCodeTimeZoneID').list()
        }
        return entities
    }

}
