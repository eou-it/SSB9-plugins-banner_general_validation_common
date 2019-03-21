/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm.v1

/**
 *  Predefined constants for "organizationType" inside Organization LDM (/base/domain/organization/v1/organization.json-schema)
 *
 */
enum OrganizationType {

    COLLEGE("College"), DEPARTMENT("Department"), DIVISION("Division"), FACULTY("Faculty"), SCHOOL("School"), UNIVERSITY("University")

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
