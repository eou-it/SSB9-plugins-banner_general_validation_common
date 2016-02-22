/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  contact entity type.
 */
public enum ContactEntityType {
    PERSON("person"), ORGANIZATION("organization")
    private final String value
    ContactEntityType(String value) { this.value = value }
    public String getValue() { return value }
}