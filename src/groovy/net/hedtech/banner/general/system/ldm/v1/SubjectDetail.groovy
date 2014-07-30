/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.Subject


/**
 * LDM decorator for Subject  resource
 * which is sent back to the consumer
 */
class SubjectDetail {


    @Delegate

    private final Subject subject
    String guid


    SubjectDetail(Subject subject, String guid) {
        this.subject = subject
        this.guid = guid
    }

    /**
     * Equals method to compare the two SubjectDetail
     * Objects
     * @param o
     * @return
     */
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        SubjectDetail that = (SubjectDetail) o

        if (guid != that.guid) return false
        if (subject != that.subject) return false

        return true
    }

    /**
     *  returns hash code
     * @return int
     */
    int hashCode() {
        int result
        result = (subject != null ? subject.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        return result
    }

}