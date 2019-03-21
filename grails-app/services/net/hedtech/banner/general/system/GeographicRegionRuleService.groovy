/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * GeographicRegionRuleService.
 * A transactional service supporting persistence of the GeographicRegionRule model.
 */
@Transactional
class GeographicRegionRuleService extends ServiceBase {

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

    def fetchAllByGuidInList(Collection<String> guids){
        List rows = []
        if (guids) {
          List entities = GeographicRegionRule.withSession { session ->
                session.getNamedQuery('GeographicRegionRule.fetchAllByGuidInList')
                        .setParameterList('guids', guids)
                        .setString('gAreaLdmName', GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME)
                        .list()
            }

            entities.each{
                GeographicRegionRule geographicRegionRule = it[0]
                GlobalUniqueIdentifier globalUniqueIdentifier = it[1]
                rows << [geographicRegionRule: geographicRegionRule, globalUniqueIdentifier: globalUniqueIdentifier]
            }
        }
        return rows
    }
}
