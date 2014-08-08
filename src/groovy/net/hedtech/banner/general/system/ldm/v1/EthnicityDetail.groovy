/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


import net.hedtech.banner.general.system.Ethnicity


/**
 * LDM decorator for ethnicities resource (/base/domain/ethnicities/ethnicities.json-schema)
 */
class EthnicityDetail {

    @Delegate
    private final Ethnicity ethnicity
    String guid
    String parentCategory


    EthnicityDetail(Ethnicity ethnicity, String guid, String parentCategory) {
        this.ethnicity = ethnicity
        this.guid = guid
        this.parentCategory = parentCategory
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        Ethnicity that = (Ethnicity) o
        if (ethnicity != that.ethnicity) return false
        if (guid != that.guid) return false
        if (parentCategory != that.parentCategory) return false
        return true
    }


    int hashCode() {
        int result
        result = (ethnicity != null ? ethnicity.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0)
        return result
    }


    public String toString() {
        """EthnicityDetail[
                    ethnicity=$ethnicity,
                    guid=$guid,
                    parentCategory=$parentCategory]"""
    }

}
