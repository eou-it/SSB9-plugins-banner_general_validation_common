/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v1.Metadata
/**
 * <p> Decorator for Academic Honor REST Service</p>
 */
class AcademicHonor {


    public static final String LDM_NAME_INSTITUTIONAL = 'institutional-honors'
    public static final String LDM_NAME_DEPARTMENTAL = 'departmental-honors'

    @Delegate
    final private AcademicHonorView academicHonorView
    Metadata metadata
    String guid
    String honorType
    List<String> organization

    def titles=[]
    def descriptions=[]

    AcademicHonor(AcademicHonorView academicHonorView) {
        this.academicHonorView=academicHonorView
        this.guid = academicHonorView.guid
        this.metadata = new Metadata(academicHonorView.dataOrigin)
        this.organization = null
        this.honorType = academicHonorView.type
        titles << ["en":academicHonorView.code]
        descriptions << ["en":academicHonorView.description]
    }


    public String getHonorType(){
        return checkType(academicHonorView)
    }

    private String checkType(AcademicHonorView academicHonorView) {
        def type = ''
        if (academicHonorView.type == LDM_NAME_DEPARTMENTAL) {
            type = 'award'
        }
        if (academicHonorView.type == LDM_NAME_INSTITUTIONAL) {
            type = 'distinction'
        }
        type
    }
    @Override
    public String toString() {
        return "AcademicHonor{" +
                "metadata=" + metadata +
                ", id='" + guid + '\'' +
                ", title='" + titles + '\'' +
                ", description='" + descriptions + '\'' +
                ", type='" + type + '\'' +
                ", organization=" + organization +
                '}'
    }
}
