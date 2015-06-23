/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  academic credential type.
 */
public enum AcademicCredentialType {
    DEGREE("1"), HONORARY("2"), DIPLOMA("3"),CERTIFICATE("4")
    private final String value
    AcademicCredentialType(String value) { this.value = value }
    public String getValue() { return value }
}