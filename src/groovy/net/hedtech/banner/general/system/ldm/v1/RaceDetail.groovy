/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1


import net.hedtech.banner.general.system.Race


/**
 * LDM decorator for races resource (/base/domain/races/races.json-schema)
 */
class RaceDetail {

    @Delegate
    private final Race raceDecorator
    String guid
    String parentCategory
    Metadata metadata


    RaceDetail(Race race, String guid, String parentCategory, Metadata metadata) {
        this.raceDecorator = race
        this.guid = guid
        this.parentCategory = parentCategory
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        RaceDetail that = (RaceDetail) o
        if (raceDecorator != that.raceDecorator) return false
        if (guid != that.guid) return false
        if (parentCategory != that.parentCategory) return false
        if (metadata != that.metadata) return false
        return true
    }


    public String toString() {
        """RaceDetail[
                    raceDecorator=$raceDecorator,
                    guid=$guid,
                    metadata=$metadata,
                    parentCategory=$parentCategory]"""
    }
}
