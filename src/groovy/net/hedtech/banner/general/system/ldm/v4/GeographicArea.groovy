package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created by invthannee on 4/9/2016.
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
      return  new GeographicAreaType(regionId)
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
