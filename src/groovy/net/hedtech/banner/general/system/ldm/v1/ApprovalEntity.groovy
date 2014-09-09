/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * Enumerations for instructional events validation rule status
 */
public enum ApprovalEntity {

    User( 'User' ),
    System( 'System' )


    ApprovalEntity( String value ) {
        this.value = value
    }

    private final String value


    public String getValue() {return value}


    @Override
    public String toString() {
        getValue()
    }

}
