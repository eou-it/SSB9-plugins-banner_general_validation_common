/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.LocationTypeReadOnly

/**
 * Decorator for "location-types" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class LocationType {
    @Delegate
    private final LocationTypeReadOnly locationTypeView
    Map<String, String> type

    LocationType(LocationTypeReadOnly locationTypeView) {
        this.locationTypeView = locationTypeView
    }
}
