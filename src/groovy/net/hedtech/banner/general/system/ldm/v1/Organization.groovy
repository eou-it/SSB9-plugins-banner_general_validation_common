/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.College

/**
 * Decorator for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 *
 */
class Organization {

    @Delegate
    private final College underlyingDomain

    String guid
    String organizationType
    Metadata metadata


    def Organization(String guid, def underlyingDomain, String organizationType, Metadata metadata) {
        this.guid = guid
        this.underlyingDomain = underlyingDomain
        this.organizationType = organizationType
        this.metadata = metadata
    }

    /**
     * Equals method to compare the two Organization
     * Objects
     * @param o
     * @return
     */
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Organization that = (Organization) o

        if (guid != that.guid) return false
        if (underlyingDomain != that.underlyingDomain) return false
        if (metadata != that.metadata) return false
        return true
    }

    /**
     *  returns hash code
     * @return int
     */
    int hashCode() {
        int result
        result = (underlyingDomain != null ? underlyingDomain.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """Organization[
                    underlyingDomain=$underlyingDomain,
                    guid=$guid,
                    organizationType=$organizationType,
                    metadata=$metadata]"""
    }
}
