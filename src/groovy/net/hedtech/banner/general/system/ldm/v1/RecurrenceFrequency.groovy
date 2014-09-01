/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * Enumerations for recurrence frequency
 */
public enum RecurrenceFrequency {

    Daily('Daily'),
    Weekly('Weekly'),
    Monthly('Monthly'),
    Yearly('Yearly')


    RecurrenceFrequency( String value ) {
        this.value = value
    }

    private final String value


    public String getValue() {return value}

}
