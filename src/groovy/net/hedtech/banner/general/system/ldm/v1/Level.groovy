/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * Decorator for Academic Level LDM (/base/domain/academicLevel/v1/academicLevel.json-schema)
 *
 */
class Level {

    @Delegate
    private final net.hedtech.banner.general.system.Level level
    String guid


    def Level(String guid, net.hedtech.banner.general.system.Level level) {
        this.guid = guid
        this.level = level
    }

}
