/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * GeographicRegionRuleService.
 * A transactional service supporting persistence of the GeographicRegionRule model.
 */
class GeographicRegionRuleService extends ServiceBase {

    boolean transactional = true

    /**
     * return the distinct list of geographic areas
     * @param params
     * @return
     */
    List fetchAll(Map params) {
        return GeographicRegionRule.fetchAll(params)
    }

    /**
     * return the distinct count of geographic areas
     * @return
     */
    def countAll() {
        return GeographicRegionRule.countAll()
    }

    /**
     * return the geographic areas based on guid
     * @param guid
     * @return
     */
    def fetchByGuid(String guid) {
        return GeographicRegionRule.fetchByGuid(guid)
    }

}
