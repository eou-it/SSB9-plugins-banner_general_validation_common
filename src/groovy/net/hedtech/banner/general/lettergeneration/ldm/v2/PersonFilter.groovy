/** *****************************************************************************
 © 2015 Ellucian.  All Rights Reserved.
 ****************************************************************************** */

package net.hedtech.banner.general.lettergeneration.ldm.v2


import net.hedtech.banner.general.lettergeneration.PopulationSelectionBase
import net.hedtech.banner.general.system.ldm.v1.Metadata


/**
 * HEDM decorator for person-filters resource
 */
class PersonFilter {

    @Delegate
    private final PopulationSelectionBase populationSelectionBase
    private static final String SEPARATOR = '-^'
    String guid
    Metadata metadata
    String title

    PersonFilter(PopulationSelectionBase populationSelectionBase, String guid, Metadata metadata) {
        this.populationSelectionBase = populationSelectionBase
        this.guid = guid
        this.title = formTitle(populationSelectionBase.application, populationSelectionBase.selection, populationSelectionBase.creatorId, populationSelectionBase.lastModifiedBy ?: null)
        this.metadata = metadata
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        PersonFilter that = (PersonFilter) o
        if (populationSelectionBase != that.populationSelectionBase) return false
        if (guid != that.guid) return false
        if (title != that.title) return false
        if (metadata != that.metadata) return false
        return true
    }


    int hashCode() {
        int result
        result = (populationSelectionBase != null ? populationSelectionBase.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (title != null ? title.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


    public String toString() {
        """PersonFilter[
                    populationSelectionBase=$populationSelectionBase,
                    guid=$guid,
                    metadata=$metadata,
                    title=$title]"""
    }


    private String formTitle(String application, String selection, String creator, String user) {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(application)
        stringBuilder.append(SEPARATOR)
        stringBuilder.append(selection)
        stringBuilder.append(SEPARATOR)
        stringBuilder.append(creator)
        if (user != null) {
            stringBuilder.append(SEPARATOR)
            stringBuilder.append(user)
        }

        return stringBuilder.toString()
    }
}
