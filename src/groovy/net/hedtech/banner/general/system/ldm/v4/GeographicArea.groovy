/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Geographic Area Decorator
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class GeographicArea {

    def id
    def code
    def title
    def divisionId
    def regionId

    GeographicArea(code, title, id, divisionId, regionId) {
        this.code = code
        this.id = id
        this.divisionId = divisionId
        this.regionId = regionId
        this.title = title
    }

    def getType() {
      return  new GeographicAreaTypeDecorator(regionId)
    }

    def getIncludedAreas() {
      return  [new IncludedArea(divisionId)]
    }

    class IncludedArea{
        def id
        IncludedArea(def id){
            this.id = id
        }
    }
}
