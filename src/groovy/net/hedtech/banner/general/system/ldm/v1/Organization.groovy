/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1
/**
 * Decorator for Organization LDM (/base/domain/organization/v1/organization.json-schema)
 *
 */
class Organization {

    String guid
    String title
    String description
    String organizationType
    def sites = []


    def Organization(String guid, String title, String description, String organizationType) {
        this.guid = guid
        this.title = title
        this.description = description
        this.organizationType = organizationType
    }

}
