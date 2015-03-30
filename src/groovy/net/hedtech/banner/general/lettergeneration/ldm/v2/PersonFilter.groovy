/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.lettergeneration.ldm.v2

import net.hedtech.banner.general.system.ldm.v1.Metadata


/**
 * HEDM decorator for person-filters resource
 */
class PersonFilter {

    String abbreviation
    String guid
    Metadata metadata
    String title

    PersonFilter(String abbreviation, String guid, String title, Metadata metadata) {
        this.abbreviation = abbreviation
        this.guid = guid
        this.title = title
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        PersonFilter that = (PersonFilter) o
        if (abbreviation != that.abbreviation) return false
        if (guid != that.guid) return false
        if (title != that.title) return false
        if (metadata != that.metadata) return false
        return true
    }


    int hashCode() {
        int result
        result = (abbreviation != null ? abbreviation.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (title != null ? title.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """PersonFilter[
                    abbreviation=$abbreviation,
                    guid=$guid,
                    metadata=$metadata,
                    title=$title]"""
    }

}
