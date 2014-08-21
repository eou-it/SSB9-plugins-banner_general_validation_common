/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


import net.hedtech.banner.general.system.MaritalStatus


/**
 * LDM decorator for marital-status resource (/base/domain/marital-status/marital-status.json-schema)
 */
class MaritalStatusDetail {

    @Delegate
    private final MaritalStatus maritalStatus
    String guid
    String parentCategory
    Metadata metadata


    MaritalStatusDetail(MaritalStatus maritalStatus, String guid, String parentCategory, Metadata metadata) {
        this.maritalStatus = maritalStatus
        this.guid = guid
        this.parentCategory = parentCategory
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        MaritalStatusDetail that = (MaritalStatusDetail) o
        if (maritalStatus != that.maritalStatus) return false
        if (guid != that.guid) return false
        if (parentCategory != that.parentCategory) return false
        if (metadata != that.metadata) return false
        return true
    }


    int hashCode() {
        int result
        result = (maritalStatus != null ? maritalStatus.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """MaritalStatusDetail[
                    maritalStatus=$maritalStatus,
                    guid=$guid,
                    metadata=$metadata,
                    parentCategory=$parentCategory]"""
    }
}
