/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.Level

/**
 * Decorator for Academic Level LDM (/base/domain/academicLevel/v1/academicLevel.json-schema)
 *
 */
class AcademicLevel {

    @Delegate
    private final Level level
    Metadata metadata
    String guid


    AcademicLevel(Level level, String guid, Metadata metadata) {
        this.level = level
        this.guid = guid
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        AcademicLevel that = (AcademicLevel) o
        if (level != that.level) return false
        if (metadata != that.metadata) return false
        if (guid != that.guid) return false
        return true
    }


    int hashCode() {
        int result
        result = (level != null ? level.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }

    public String toString() {
        """AcademicLevel[
                    level=$level,
                    guid=$guid,
                    metadata=$metadata]"""
    }
}
