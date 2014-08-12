/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v1

/**
 * LDM Enumerations for races.parentCategory property
 * Refer /test/data/metadata/base/domain/races/races.json-schema
 */
public enum RaceParentCategory {

    AFRICAN_AMERICAN("African American"),
    ALASKA_NATIVE("Alaska Native"),
    AMERICAN_INDIAN("American Indian"),
    ASIAN("Asian"),
    BLACK("Black"),
    NATIVE_HAWAIIAN("Native Hawaiian"),
    OTHER_PACIFIC_ISLANDER("Other Pacific Islander"),
    WHITE("White")

    RaceParentCategory(String value) { this.value = value }

    private final String value

    public String getValue() { return value }

}
