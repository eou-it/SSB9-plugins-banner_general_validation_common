/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4
/**
 * Decorator for "academic-credentials" API
 */
class AcademicCredentialDecorator {

    def type
    def guid
    def code
    def description

    AcademicCredentialDecorator(code, description, guid, type) {
        this.type = type
        this.guid = guid
        this.code = code
        this.description = description
    }


    @Override
    public String toString() {
        return "AcademicCredentialDecorator{" +
                "type=" + type +
                ", guid=" + guid +
                ", code=" + code +
                ", description=" + description +
                '}';
    }
}
