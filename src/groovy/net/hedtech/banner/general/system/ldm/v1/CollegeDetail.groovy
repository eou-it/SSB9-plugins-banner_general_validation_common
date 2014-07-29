/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.College


/**
 * LDM decorator for College  resource
 * which is sent back to the consumer
 */
class CollegeDetail {


    @Delegate private final College college
    String guid
    List sites = []


    CollegeDetail(College college, String guid) {
        this.college = college
        this.guid = guid
    }

    CollegeDetail(College college, String guid, def sites) {
        this.college = college
        this.guid = guid
        this.sites = sites
    }

    /**
     * Equals method to compare the two CollegeDetail
     * Objects
     * @param o
     * @return
     */
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        CollegeDetail that = (CollegeDetail) o

        if (guid != that.guid) return false
        if (college != that.college) return false

        return true
    }

    /**
     *  returns hash code
     * @return int
     */
    int hashCode() {
        int result
        result = (college != null ? college.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        return result
    }

}
