/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Decorator for "academic-credentials" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class AcademicCredentialDecorator {

    def type
    def guid
    def code
    def description
    def supplementalDesc

    AcademicCredentialDecorator(code, description, guid, type,supplementalDesc) {
        this.type = type
        this.guid = guid
        this.code = code
        this.description = description
        this.supplementalDesc = supplementalDesc
    }
}
