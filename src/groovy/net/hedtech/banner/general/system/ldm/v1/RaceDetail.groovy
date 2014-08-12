/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
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


    RaceDetail(Race race, String guid, String parentCategory) {
        this.raceDecorator = race
        this.guid = guid
        this.parentCategory = parentCategory
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        RaceDetail that = (RaceDetail) o
        if (raceDecorator != that.raceDecorator) return false
        if (guid != that.guid) return false
        if (parentCategory != that.parentCategory) return false
        return true
    }


    int hashCode() {
        int result
        result = (raceDecorator != null ? raceDecorator.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0)
        return result
    }


/*    private Race getRace(){
        return race
    }*/


    public String toString() {
        """RaceDetail[
                    raceDecorator=$raceDecorator,
                    guid=$guid,
                    parentCategory=$parentCategory]"""
    }
}
