/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
/**
 * service layer for phone types
 * */
class PhoneTypeService {

    /**
     * fetching PhoneType data based on guid
     * @param guid
     * @return
     */
    PhoneType fetchByGuid(String guid){
        return PhoneType.fetchByGuid(guid)
    }

    /**
     * fetching PhoneType data
     * @param Map params
     * @return
     */
    List<PhoneType> fetchAll(Map params){
        return PhoneType.createCriteria().list(max: params.max, offset: params.offset){}
    }

    /**
     * fetching total count of phone-types
     * @return
     */
    Long fetchCountAll() {
        return PhoneType.createCriteria().get {
            projections  {
                count()
            }
        }
    }
}
