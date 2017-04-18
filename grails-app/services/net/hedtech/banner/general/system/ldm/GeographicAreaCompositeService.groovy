/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.GeographicRegionRule
import net.hedtech.banner.general.system.ldm.v4.GeographicArea
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for GeographicArea Service.</p>
 * <p> It will return geographic areas.</p>
 */
class GeographicAreaCompositeService extends LdmService {
    def geographicRegionRuleService

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V6]
    /**
     *GET /api/geographic-areas/
     * @param params
     * @return list of geographic-areas
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)
        List<GeographicArea> geographicAreaList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        geographicRegionRuleService.fetchAll(params).each { geographicArea ->
            geographicAreaList << new GeographicArea(geographicArea.getAt(0), geographicArea.getAt(1), geographicArea.getAt(2), geographicArea.getAt(3), geographicArea.getAt(4))
        }

        return geographicAreaList
    }

    /**
     *GET /api/geographic-areas/
     * @return count value of geographic-areas
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def count() {
        return geographicRegionRuleService.countAll()
    }

    /**
     *GET /api/geographic-areas/{guid}
     * @param guid
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {

        String acceptVersion = getAcceptVersion(VERSIONS)
     def  geographicArea = geographicRegionRuleService.fetchByGuid(guid.toLowerCase().trim())
     if(geographicArea){
         return new GeographicArea(geographicArea.getAt(0), geographicArea.getAt(1), geographicArea.getAt(2), geographicArea.getAt(3), geographicArea.getAt(4))
     }else{
         throw new ApplicationException(GeographicAreaCompositeService.class, new NotFoundException())
     }

    }

    def getGeographicAreaGuidToGeographicAreaRuleMap(Collection geographicAreaGuids){
        Map guidToGeographicAreaRuleMap = [:]
        if(geographicAreaGuids) {
            List rows = geographicRegionRuleService.fetchAllByGuidInList(geographicAreaGuids)
            rows.each {
                GeographicRegionRule geographicRegionRule = it.geographicRegionRule
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                guidToGeographicAreaRuleMap.put(globalUniqueIdentifier.guid, geographicRegionRule)
            }
        }
        return guidToGeographicAreaRuleMap
    }
}

