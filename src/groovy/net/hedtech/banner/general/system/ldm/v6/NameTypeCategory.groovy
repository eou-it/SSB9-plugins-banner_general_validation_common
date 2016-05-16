/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

/**
 * Enumerations for  person name type category.
 */
public enum NameTypeCategory {
    PERSONAL("personal"), BIRTH("birth"),LEGRAL("legal")
    private final String value
    NameTypeCategory(String value) { this.value = value }
    public String getValue() { return value }
}