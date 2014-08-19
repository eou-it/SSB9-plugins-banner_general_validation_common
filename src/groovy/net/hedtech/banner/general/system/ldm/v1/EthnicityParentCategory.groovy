/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
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

}
