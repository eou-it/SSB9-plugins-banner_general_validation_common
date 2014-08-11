/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v1

/**
 * LDM Enumerations for marital-status.parentCategory property
 * Refer /test/data/metadata/base/domain/marital-status/marital-status.json-schema
 */
public enum MaritalStatusParentCategory {

    SINGLE("Single"), MARRIED("Married"), DIVORCED("Divorced"), WIDOWED("Widowed"), SEPARATED("Separated")


    MaritalStatusParentCategory(String value) { this.value = value }

    private final String value


    public String getValue() { return value }

}
