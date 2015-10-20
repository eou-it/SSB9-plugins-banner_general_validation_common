/*******************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v4

/**
 * LDM Enumerations for marital-status.martialCategory property
 *
 */
public enum MaritalStatusMaritalCategory {

    static final List<String> MARITAL_STATUS_MARTIAL_CATEGORY = ["single", "divorced", "widowed", "separated", "married"]

    MaritalStatusMaritalCategory(String value) { this.value = value }

    private final String value

    public String getValue() { return value }

}
