/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * CDM Enumerations for races.racialCategory property
 */
public enum RaceRacialCategory {

    private static final List<String> RACE_RACIAL_CATEGORY = ["africanAmerican",
                                                              "alaskaNative",
                                                              "americanIndian",
                                                              "asian",
                                                              "black",
                                                              "nativeHawaiian",
                                                              "otherPacificIslander",
                                                              "white"]

    RaceRacialCategory(String value) { this.value = value }

    private final String value

    public String getValue() { return value }

}
