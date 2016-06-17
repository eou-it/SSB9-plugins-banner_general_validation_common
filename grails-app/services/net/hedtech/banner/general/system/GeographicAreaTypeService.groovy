/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system

/**
 * Geographic area type view domain service
 */
class GeographicAreaTypeService {

    /**
     * returns the geographic are type based on guid
     * @param guid
     * @return
     */
    public  GeographicAreaType fetchByGuid(String guid){
      return  GeographicAreaType.fetchByGuid(guid)
    }

    /**
     * returns the list of geographic are types
     * @param params
     * @return
     */
    public  List<GeographicAreaType> fetchAll(Map params){
       return GeographicAreaType.fetchAll(params)
    }

    /**
     * returns the count of geographic are types
     * @return
     */
    public  Long countAll(){
      return  GeographicAreaType.countAll()
     }
}
