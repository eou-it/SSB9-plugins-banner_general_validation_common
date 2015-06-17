/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v1.Metadata
/**
 * <p> Decorator for Academic Honor REST Service</p>
 */
class AcademicHonorDetail {
    Metadata metadata
    String guid
    String type
    List<String> organization

    def titles=[]
    def descriptions=[]

    AcademicHonorDetail(AcademicHonorView academicHonorView,String type) {
        this.guid = academicHonorView.guid
        this.metadata = new Metadata(academicHonorView.dataOrigin)
        this.organization = null
        this.type = type
        titles << ["en":academicHonorView.code]
        descriptions << ["en":academicHonorView.description]
    }

    @Override
    public String toString() {
        return "AcademicHonor{" +
                "metadata=" + metadata +
                ", id='" + guid + '\'' +
                ", title='" + titles + '\'' +
                ", decription='" + descriptions + '\'' +
                ", type='" + type + '\'' +
                ", organization=" + organization +
                '}';
    }


}
