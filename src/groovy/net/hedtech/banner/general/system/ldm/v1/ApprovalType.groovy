/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v1

/**
 * Enumerations for instructional events approval type.
 */
public enum ApprovalType {

    INSTRUCTOR_AVAILABILITY( 'instructorAvailability' ),
    INSTRUCTOR_CAPACITY( 'instructorCapacity' ),
    ROOM_AVAILABILITY( 'roomAvailability' ),
    ROOM_CAPACITY( 'roomCapacity' )


    ApprovalType( String value ) {
        this.value = value
    }

    private final String value


    public String getValue() {return value}


    @Override
    public String toString() {
        getValue()
    }

}
