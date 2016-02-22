/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  academic discipline type.
 */
public enum AcademicDisciplineType {

    MINOR("minor"), MAJOR("major"), CONCENTRATION("concentration")
    private final String value

    AcademicDisciplineType(String value) { this.value = value }

    public String getValue() { return value }
}