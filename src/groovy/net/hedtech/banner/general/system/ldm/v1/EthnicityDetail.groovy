/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


import net.hedtech.banner.general.system.Ethnicity


/**
 * CDM decorator for ethnicities resource (/base/domain/ethnicities/ethnicities.json-schema)
 */
class EthnicityDetail {

    @Delegate
    private final Ethnicity ethnicity
    String guid
    String parentCategory
    Metadata metadata

    EthnicityDetail(Ethnicity ethnicity, String guid, String parentCategory, Metadata metadata) {
        this.ethnicity = ethnicity
        this.guid = guid
        this.parentCategory = parentCategory
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        EthnicityDetail that = (EthnicityDetail) o
        if (ethnicity != that.ethnicity) return false
        if (guid != that.guid) return false
        if (parentCategory != that.parentCategory) return false
        if (metadata != that.metadata) return false
        return true
    }


    int hashCode() {
        int result
        result = (ethnicity != null ? ethnicity.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """EthnicityDetail[
                    ethnicity=$ethnicity,
                    guid=$guid,
                    metadata=$metadata,
                    parentCategory=$parentCategory]"""
    }

}
