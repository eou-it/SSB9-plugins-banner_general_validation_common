/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.LocationTypeView
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for "location-types" API
 */
class LocationType {
    @Delegate
    private final LocationTypeView locationTypeView
    Metadata metadata
    Map<String, String> type

    LocationType(LocationTypeView locationTypeView, Metadata metadata, String entityType, String locationType) {
        this.metadata = metadata
        this.locationTypeView = locationTypeView
        this.type = [entityType: entityType, locationType: locationType]
    }


    @Override
    public String toString() {
        return "LocationType{" +
                "locationTypeView=" + locationTypeView +
                ", metadata=" + metadata +
                ", type=" + type +
                '}';
    }
}
