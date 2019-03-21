/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


/**
 * CDM decorator for the 'metadata' property which is present in all the CDM json schemas
 */
class Metadata {

    String dataOrigin

    Metadata(String dataOrigin) {
        this.dataOrigin = dataOrigin
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        Metadata that = (Metadata) o
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    public String toString() {
        """Metadata[
                    dataOrigin=$dataOrigin]"""
    }
}
