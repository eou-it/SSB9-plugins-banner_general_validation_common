/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.ldm.MetadataV3

/**
 * Decorator for the 'metadata' property in HeDM json schemas
 */
class MetadataV4 extends MetadataV3{
    String createdBy


    MetadataV4(String dataOrigin, Date createdOn, Date modifiedOn,String createdBy) {
        super(dataOrigin,createdOn,modifiedOn)
        this.createdBy = createdBy
    }
}
