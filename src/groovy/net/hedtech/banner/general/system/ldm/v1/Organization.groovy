/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.College

/**
 * Decorator for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 *
 */
class Organization {

    @Delegate
    private final College underlyingDomain

    String guid
    String organizationType
    def sites = []


    def Organization(String guid, def underlyingDomain, String organizationType) {
        this.guid = guid
        this.underlyingDomain = underlyingDomain
        this.organizationType = organizationType
    }

}
