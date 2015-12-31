/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system


import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Race model.
 * */
class RaceService extends ServiceBase{
    boolean transactional = true

    /**
     * if count is false then fetch Race details which are mapped to goriccr setting otherwise it will return count of races which are mapped to goriccr setting
     * @param content
     * @param count
     * @return
     */
    def  fetchRaceDetails(def content, def count = false) {
       return Race.fetchRaceDetails(content,count)
    }

    /**
     * fetching Race details based on race code
     * @param code
     * @return
     */
     Race fetchByRace(String racialCode){
        return Race.fetchByRace(racialCode)
    }
}
