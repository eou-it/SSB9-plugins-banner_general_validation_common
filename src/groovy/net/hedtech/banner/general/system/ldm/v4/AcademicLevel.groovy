/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for Academic Level
 *
 */
class AcademicLevel {

    @Delegate
    private final Level level
    Metadata metadata
    String guid
    List<Map<String, String>> title


    AcademicLevel(Level level, String guid, Metadata metadata) {
        this.level = level
        this.guid = guid
        this.metadata = metadata
        if(level.description){
            title=[]
            this.title <<[en: level.description]
            
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        AcademicLevel that = (AcademicLevel) o

        if (guid != that.guid) return false
        if (level != that.level) return false
        if (metadata != that.metadata) return false
        if (title != that.title) return false

        return true
    }


    @Override
    public String toString() {
        return "AcademicLevel[" +
                "level=" + level +
                ", metadata=" + metadata +
                ", guid='" + guid + '\'' +
                ", title=" + title +
                ']';
    }
}
