/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.Campus

/**
 * Decorator for Site LDM (/base/domain/site/v1/site.json-schema)
 *
 */
class SiteDetail {

    @Delegate
    private final Campus campus

    String guid
    Organization organization
    def buildings = []


    def SiteDetail(String guid, def campus, Organization organization, def buildings) {
        this.guid = guid
        this.campus = campus
        this.organization = organization
        this.buildings = buildings
    }

    def SiteDetail(String guid, def campus) {
        this.guid = guid
        this.campus = campus
    }

}
