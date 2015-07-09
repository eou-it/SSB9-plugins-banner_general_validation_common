/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for the 'metadata' property in HeDM json schemas
 */
class MetadataV3 extends Metadata {

    Date createdOn
    Date modifiedOn


    MetadataV3(String dataOrigin, Date createdOn, Date modifiedOn) {
        super(dataOrigin)
        this.createdOn = createdOn
        this.modifiedOn = modifiedOn
    }

}
