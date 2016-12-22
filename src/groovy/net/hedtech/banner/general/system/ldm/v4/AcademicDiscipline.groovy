/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4
/**
 * Decorator for academic disciplines
 */
class AcademicDiscipline {
    String type
    String guid
    String description
    String code
    String cipCode


    //used by the genericBasicValidationService/genericComlexValidationService
    AcademicDiscipline(){}

    AcademicDiscipline(String code, String description, String type, String guid) {
        this.guid = guid
        this.type = type
        this.code = code
        this.description = description

    }

    def getReporting(){
        if(cipCode){
            List reporting = []
            reporting.add(country:[code:"USA",cipCode:cipCode])
            return reporting
        }
    }

    @Override
    public String toString() {
        return "AcademicDiscipline{" +
                "type='" + type + '\'' +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
