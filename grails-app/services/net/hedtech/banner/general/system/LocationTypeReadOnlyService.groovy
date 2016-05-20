/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
/**
 * service layer for location types
 * */
class LocationTypeReadOnlyService {

    /**
     * fetching Address Type data based on guid
     * @param guid
     * @return
     */
    LocationTypeReadOnly fetchByGuid(String guid){
        return LocationTypeReadOnly.get(guid)
    }

    /**
     * fetching Address Type data
     * @param Map params
     * @return
     */
    List<LocationTypeReadOnly> fetchAll(Map params){
        return LocationTypeReadOnly.createCriteria().list(max: params.max, offset: params.offset){}
    }

    /**
     * fetching total count of address-types
     * @return
     */
    Long fetchCountAll() {
        return LocationTypeReadOnly.createCriteria().get {
            projections  {
                count()
            }
        }
    }
}
