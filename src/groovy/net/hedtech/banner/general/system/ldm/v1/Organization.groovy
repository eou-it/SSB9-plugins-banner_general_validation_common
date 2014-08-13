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
    def sites = []


    def Organization(String guid, def underlyingDomain, String organizationType) {
        this.guid = guid
        this.underlyingDomain = underlyingDomain
        this.organizationType = organizationType
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
        return result
    }
}
