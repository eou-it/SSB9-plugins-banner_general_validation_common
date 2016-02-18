/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.LocationTypeView

/**
 * Decorator for "location-types" API
 */
class LocationType {
    @Delegate
    private final LocationTypeView locationTypeView
    Map<String, String> type

    LocationType(LocationTypeView locationTypeView, String entityType, String locationType) {
        this.locationTypeView = locationTypeView
        if (entityType == GeneralValidationCommonConstants.ORGANIZATION){
            this.type = ["organization" : ["locationType": locationType]]
        }
        if (entityType ==GeneralValidationCommonConstants.PERSON){
            this.type = ["person" : ["locationType": locationType]]
        }
    }


    @Override
    public String toString() {
        return "LocationType{" +
                "locationTypeView=" + locationTypeView +
                ", type=" + type +
                '}';
    }
}
