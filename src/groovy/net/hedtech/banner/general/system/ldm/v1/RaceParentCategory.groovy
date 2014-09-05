/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * CDM Enumerations for races.parentCategory property
 */
public enum RaceParentCategory {

    private static final List<String> RACE_PARENT_CATEGORY = ["African American",
                                                                "Alaska Native",
                                                                "American Indian",
                                                                "Asian",
                                                                "Black",
                                                                "Native Hawaiian",
                                                                "Other Pacific Islander",
                                                                "White"]

    RaceParentCategory(String value) { this.value = value }

    private final String value

    public String getValue() { return value }

}
