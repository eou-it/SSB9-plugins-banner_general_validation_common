/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.Degree
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for "academic-credentials" API
 */
class AcademicCredentials {
    @Delegate
    private final  Degree degree
    Metadata metadata
    String guid
    String type
    List<Map<String, String>>  abbreviation
    List<Map<String, String>> title

    AcademicCredentials(Degree degree,String guid,Metadata metadata,String type) {
        this.degree = degree
        this.type=type
        this.guid = guid
        this.metadata = metadata
        if(degree.description){
            title = []
            this.title <<["en": degree.description]
        }
        if(degree.code){
            abbreviation=[]
            this.abbreviation <<["en": degree.code]
        }
    }

    @Override
    public String toString() {
        return "AcademicCredentials{" +
                ", metadata=" + metadata +
                ", guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", title=" + title +
                '}';
    }
}
