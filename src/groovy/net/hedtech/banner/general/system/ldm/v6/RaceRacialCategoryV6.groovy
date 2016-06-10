/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

/**
 * CDM Enumerations for races.racialCategory v6 property
 */

public enum RaceRacialCategoryV6 {

    public static final List<String> RACE_RACIAL_CATEGORY = ["americanIndianOrAlaskaNative",
                                                             "asian",
                                                             "blackOrAfricanAmerican",
                                                             "hawaiianOrPacificIslander",
                                                             "white"]

    RaceRacialCategoryV6(String value) { this.value = value }

    private final String value

    public String getValue() { return value }

}