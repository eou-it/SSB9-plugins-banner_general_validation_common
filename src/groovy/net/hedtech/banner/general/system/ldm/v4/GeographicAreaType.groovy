/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4

/**
 * Geographic Area type Decorator
 */
class GeographicAreaType {

    def regionId

    GeographicAreaType(String regionId) {
        this.regionId = regionId
    }

    def getCategory() {
      return  GeographicAreaTypeCategory.INSTITUTIONAL.value
    }

    def getDetail() {
        return new Detail(regionId)
    }

    class Detail {
        def id

        Detail(String id) {
            this.id = id
        }
    }
}
