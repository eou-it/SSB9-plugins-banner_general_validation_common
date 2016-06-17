/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.GeographicAreaType
import net.hedtech.banner.general.system.ldm.v4.GeographicAreaTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for GeographicAreaType Service.</p>
 * <p> It will return geographic areas types (Union of Regions and Divisions of geographic)</p>
 */
class GeographicAreaTypeCompositeService extends LdmService {
    def geographicAreaTypeService

    /**
     * GET /api/geographic-area-types/
     * @param params
     * @return list of geographic-area-types
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<GeographicAreaTypeDecorator> list(Map params) {
        List<GeographicAreaTypeDecorator> geographicAreaList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        geographicAreaTypeService.fetchAll(params).each { geographicAreaType ->
            geographicAreaList << new GeographicAreaTypeDecorator(geographicAreaType.id, geographicAreaType.code, geographicAreaType.title, geographicAreaType.description)
        }
        return geographicAreaList
    }

    /**
     * GET /api/geographic-area-types/
     * @return count value of geographic-area-types
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        return geographicAreaTypeService.countAll()
    }

    /**
     * GET /api/geographic-area-types/{guid}* @param guid
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    GeographicAreaTypeDecorator get(String guid) {
        GeographicAreaType geographicAreaType = geographicAreaTypeService.fetchByGuid(guid?.trim()?.toLowerCase())
        if (!geographicAreaType) {
            throw new ApplicationException(GeographicAreaTypeCompositeService.class, new NotFoundException())
        } else {
            return new GeographicAreaTypeDecorator(geographicAreaType.id, geographicAreaType.code, geographicAreaType.title, geographicAreaType.description)
        }
    }

}