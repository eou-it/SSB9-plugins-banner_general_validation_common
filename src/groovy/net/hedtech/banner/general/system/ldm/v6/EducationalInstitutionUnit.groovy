/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includeNames = true, includeFields = true)
class EducationalInstitutionUnit {
    String guid
    String title
    String type
    String instituteGuid

    def EducationalInstitutionUnit(String guid, String title, String type, String instituteGuid) {
        this.guid = guid
        this.title = title
        this.type = type
        this.instituteGuid = instituteGuid
    }

    def getParents() {
        return ["institution": ["id": instituteGuid]]
    }

}
