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
    String guid


    def AcademicLevel(String guid, Level level) {
        this.guid = guid
        this.level = level
    }

}
