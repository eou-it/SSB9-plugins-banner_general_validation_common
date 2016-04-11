package net.hedtech.banner.general.system.ldm.v4

/**
 * Created by invthannee on 4/9/2016.
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
