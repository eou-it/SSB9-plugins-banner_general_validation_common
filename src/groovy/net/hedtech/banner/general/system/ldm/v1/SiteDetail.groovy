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

    def SiteDetail(String guid, def campus) {
        this.guid = guid
        this.campus = campus
    }

}
