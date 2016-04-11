/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4

/**
 * Geographic Area type category enum
 */
public enum GeographicAreaTypeCategory {

    GOVERNMENTAL("governmental"),
    POSTAL("postal"),
    FUNDRAISING("fundraising"),
    RECRUITMENT("recruitment"),
    INSTITUTIONAL("institutional")

    GeographicAreaTypeCategory(String value) {
        this.value = value
    }

    private final String value


    public String getValue() { return value }


    @Override
    public String toString() {
        getValue()
    }
}