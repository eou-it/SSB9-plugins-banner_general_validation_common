/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


import net.hedtech.banner.general.system.HoldType


/**
 * CDM decorator for restriction-types resource
 */
class RestrictionType {

    @Delegate
    private final HoldType holdType
    Metadata metadata
    String guid


    RestrictionType(HoldType holdType, String guid, Metadata metadata) {
        this.holdType = holdType
        this.guid = guid
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        RestrictionType that = (RestrictionType) o
        if (holdType != that.holdType) return false
        if (metadata != that.metadata) return false
        if (guid != that.guid) return false
        return true
    }


    int hashCode() {
        int result
        result = (holdType != null ? holdType.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """RestrictionType[
                    holdType=$holdType,
                    guid=$guid,
                    metadata=$metadata]"""
    }
}
