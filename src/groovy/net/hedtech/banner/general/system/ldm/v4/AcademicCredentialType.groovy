/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  academic credential type.
 */
public enum AcademicCredentialType {
    DEGREE("degree"), HONORARY("honorary"), DIPLOMA("diploma"),CERTIFICATE("certificate")
    private final String value
    AcademicCredentialType(String value) { this.value = value }
    public String getValue() { return value }
}