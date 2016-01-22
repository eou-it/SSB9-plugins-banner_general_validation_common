/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.CitizenType

/**
 * Decorator for citizenship-statuses HEDM API JSON v4 schema
 *
 */
class CitizenshipStatus {

    @Delegate
    private final CitizenType citizenType
    String guid
    String category


    CitizenshipStatus(CitizenType citizenType, String guid, String category) {
        this.citizenType = citizenType
        this.guid = guid
        this.category = category
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        CitizenshipStatus that = (CitizenshipStatus) o
        if (citizenType != that.citizenType) return false
        if (guid != that.guid) return false
        if (category != that.category) return false
        return true
    }


    public String toString() {
        """CitizenshipStatus[
                    citizenType=$citizenType,
                    guid=$guid,
                    category=$category]"""
    }

}
