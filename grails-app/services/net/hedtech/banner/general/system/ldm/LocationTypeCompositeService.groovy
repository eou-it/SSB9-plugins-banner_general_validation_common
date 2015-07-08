/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.LocationTypeView
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v4.LocationType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Location Type Service.</p>
 * <p> It will return location types of person and organization.</p>
 * <p> If location type code was configure for both person and organization, then priority is given to person.</p>
 */
class LocationTypeCompositeService {

    //This filed is used for only to create and update of Location type data
    def  locationTypeService

    private static final String DEFAULT_SORT_FIELD = 'code'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final String LDM_NAME ='location-types'
    private static final List<String> allowedSortFields = ['code']

    /**
     * GET /api/location-types
     * @param params
     * @return List
     */
    @Transactional(readOnly = true)
    def  list(Map params) {
        List<LocationType> locationTypeArrayList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ? params?.sort : DEFAULT_SORT_FIELD
        params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        getLocationTypes(false,params).each {locationTypeView ->
            locationTypeArrayList <<  new LocationType(locationTypeView, new Metadata(locationTypeView.dataOrigin), locationTypeView.locationType,locationTypeView.translationValue)
        }
        locationTypeArrayList
    }

    /**
     * @return Long value as total count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        getLocationTypes(true,[:])
    }

    /**
     * Fetches the data based on location type.
     * If the same code is mapped to both person and organization types, then API will return this code only once as 'Person' entity type.
     */
    private def getLocationTypes(Boolean count, Map params) {
        if (count) {
            LocationTypeView.count()
        } else {
            LocationTypeView.list(params)
        }
    }


    /**
     * GET /api/location-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    LocationType get(String guid) {
        LocationTypeView locationTypeView = LocationTypeView.get(guid?.trim())
        if (locationTypeView) {
            new LocationType(locationTypeView, new Metadata(locationTypeView.dataOrigin), locationTypeView.locationType,locationTypeView.translationValue)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier=GlobalUniqueIdentifier.findByGuid(guid?.trim())
            if(globalUniqueIdentifier && globalUniqueIdentifier?.ldmName!=LDM_NAME) {
                throw new RestfulApiValidationException("locationType.invalidGuid")
            }else {
                throw new ApplicationException("locationType", new NotFoundException())
            }
        }
    }

}
