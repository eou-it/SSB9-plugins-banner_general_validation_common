/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * Enumerations for instructional events validation rule status
 */
public enum InstructionalEventValidationRuleStatus {

    Active('Active'),
    Inactive('Inactive')


    InstructionalEventValidationRuleStatus( String value ) {
        this.value = value
    }

    private final String value


    public String getValue() {return value}

}
