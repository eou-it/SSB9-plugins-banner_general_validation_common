/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
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
    Metadata metadata
    String guid


    SubjectDetail(Subject subject, String guid, Metadata metadata) {
        this.subject = subject
        this.metadata = metadata
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
        if (metadata != that.metadata) return false
        if (guid != that.guid) return false
        if (subject != that.subject) return false

        return true
    }


    public String toString() {
        """SubjectDetail[
                    subject=$subject,
                    guid=$guid,
                    metadata=$metadata]"""
    }

}
