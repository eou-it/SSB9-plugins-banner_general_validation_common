package net.hedtech.banner.general.system.ldm.v4

/**
 * Enumerations for  AcademicDiscipline
 * Created by invthannee on 6/2/2015.
 */
public enum AcademicDisciplineType {

    MINOR("minor"), MAJOR("major"), CONCENTRATION("concentration")
    private final String value

    AcademicDisciplineType(String value) { this.value = value }

    public String getValue() { return value }
}