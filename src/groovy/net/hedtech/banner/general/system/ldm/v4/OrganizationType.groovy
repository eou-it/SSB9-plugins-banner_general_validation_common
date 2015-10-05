/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v4

/**
 *  Predefined constants for "organizationType" inside Organization LDM for version 4
 *
 */
enum OrganizationType {

    COLLEGE("college"), DEPARTMENT("department"), DIVISION("division"), FACULTY("faculty"), SCHOOL("school"), UNIVERSITY("university"),BUSINESS("business")

    private final String value


    OrganizationType(String value) { this.value = value }


    public String getValue() { return value }

}
