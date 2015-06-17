/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * CDM Enumerations for ethnicities.parentCategory property
 */
public enum EthnicityParentCategory {

    HISPANIC("Hispanic"), NON_HISPANIC("Non-Hispanic")


    EthnicityParentCategory(String value) { this.value = value }

    private final String value


    public String getValue() { return value }


    static boolean contains(String val) {
        boolean found = false
        def obj = values().find { it.value == val }
        if (obj) {
            found = true
        }
        return found
    }

}
