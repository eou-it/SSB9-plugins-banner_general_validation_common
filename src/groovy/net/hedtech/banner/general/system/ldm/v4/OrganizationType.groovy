/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.common.GeneralValidationCommonConstants

/**
 *  Predefined constants for "organizationType" inside Organization LDM for version 4
 *
 */
enum OrganizationType {

    COLLEGE(GeneralValidationCommonConstants.COLLEGE),
    DEPARTMENT(GeneralValidationCommonConstants.DEPARTMENT),
    DIVISION(GeneralValidationCommonConstants.DIVISION),
    FACULTY(GeneralValidationCommonConstants.FACULTY),
    SCHOOL(GeneralValidationCommonConstants.SCHOOL),
    UNIVERSITY(GeneralValidationCommonConstants.UNIVERSITY),
    BUSINESS(GeneralValidationCommonConstants.BUSINESS)

    private final String value


    OrganizationType(String value) { this.value = value }


    public String getValue() { return value }

    public static OrganizationType getByValue(String value){
        Iterator itr = OrganizationType.values().iterator()
        while(itr.hasNext()){
            OrganizationType type = itr.next()
            if(type.value == value){
                return type
            }
        }
        return null
    }
}
