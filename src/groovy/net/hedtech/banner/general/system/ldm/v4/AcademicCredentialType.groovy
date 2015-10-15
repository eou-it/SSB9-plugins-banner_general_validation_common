/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  academic credential type.
 */
public enum AcademicCredentialType {
    degree("1"), honorary("2"), diploma("3"),certificate("4")
    private final String value
    AcademicCredentialType(String value) { this.value = value }
    public String getValue() { return value }
}