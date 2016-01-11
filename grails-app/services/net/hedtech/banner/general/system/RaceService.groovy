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
     *fetch Race details which are mapped to goriccr setting
     * @param content
     * @return
     */
    def  fetchRaceDetails(def content) {
       return Race.fetchRaceDetails(content)
    }

    /**
     * fetching Race details based on race code
     * @param code
     * @return
     */
     Race fetchByRace(String racialCode){
        return Race.fetchByRace(racialCode)
    }

    /**
     * will return count of races which are mapped to goriccr setting
     * @param content
     * @return
     */
   def fetchRaceDetailsCount(){
     return Race.fetchRaceDetailsCount()
   }
}
