/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
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


    public String toString() {
        """AcademicLevel[
                    level=$level,
                    guid=$guid,
                    metadata=$metadata]"""
    }
}
