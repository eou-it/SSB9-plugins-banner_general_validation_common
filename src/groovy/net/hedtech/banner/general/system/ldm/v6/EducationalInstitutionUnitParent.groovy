package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created by vijayt on 5/16/2016.
 */
@EqualsAndHashCode
@ToString(includeFields = true, includeNames = true)
class EducationalInstitutionUnitParent {
    //TODO: the institution actually needs to be a educational-institution object, but as the api is not yet implemented we don't have a decorator, so this is just the dummy decorator
    EducationalInstitutionUnit institution
    EducationalInstitutionUnit unit
}
