/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

/**
 * Decorator for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 *
 */
class Organization {

    String guid
    String abbreviation
    String title
    String organizationType
    Metadata metadata


    def Organization(String guid, String abbreviation, String title, String organizationType, Metadata metadata) {
        this.guid = guid
        this.abbreviation = abbreviation
        this.title = title
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
        if (abbreviation != that.abbreviation) return false
        if (title != that.title) return false
        if (organizationType != that.organizationType) return false
        if (metadata != that.metadata) return false
        return true
    }

    /**
     *  returns hash code
     * @return int
     */
    int hashCode() {
        int result
        result = (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0)
        result = 31 * result + (title != null ? title.hashCode() : 0)
        result = 31 * result + (organizationType != null ? organizationType.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """Organization[
                    guid=$guid,
                    abbreviation=$abbreviation,
                    title=$title,
                    organizationType=$organizationType,
                    metadata=$metadata]"""
    }
}
