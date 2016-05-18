package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.Organization

/**
 * Created by vijayt on 5/16/2016.
 */
@EqualsAndHashCode
@ToString(includeNames = true, includeFields = true)
class EducationalInstitutionUnit extends Organization {
    EducationalInstitutionUnitParent parents;

    public EducationalInstitutionUnit(String guid, String abbreviation, String title,
                                      String organizationType, Metadata metadata,
                                      EducationalInstitutionUnitParent parents) {
        super(guid, abbreviation, title, organizationType, metadata)
        this.parents = parents
    }
}
