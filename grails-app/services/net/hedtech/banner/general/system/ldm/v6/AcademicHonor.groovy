/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.AcademicHonorView

/**
 * <p> Decorator for Academic Honor REST Service</p>
 */
class AcademicHonor {


    @Delegate
    final private AcademicHonorView academicHonorView


    AcademicHonor(AcademicHonorView academicHonorView) {
        this.academicHonorView=academicHonorView

    }


    public String getHonorType(){
        if (academicHonorView.type == GeneralValidationCommonConstants.LDM_NAME_DEPARTMENTAL) {
            return 'award'
        }
        if (academicHonorView.type == GeneralValidationCommonConstants.LDM_NAME_INSTITUTIONAL) {
            return 'distinction'
        }
    }



}
