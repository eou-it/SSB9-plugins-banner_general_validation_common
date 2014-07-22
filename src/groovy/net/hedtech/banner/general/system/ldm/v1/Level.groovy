/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * LDM decorator for level resource address.
 */
class Level {

    @Delegate private final net.hedtech.banner.general.system.Level level

    def Level(net.hedtech.banner.general.system.Level level){
        this.level = level
    }
 }
