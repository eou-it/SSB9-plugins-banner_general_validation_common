/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicCredentialsView

/**
 * Decorator for "academic-credentials" API
 */
class AcademicCredentials {

    @Delegate
    private final  AcademicCredentialsView academicCredential

    AcademicCredentials(AcademicCredentialsView academicCredential) {
        this.academicCredential = academicCredential
    }

    @Override
    public String toString() {
        return "AcademicCredentials{" +
                "academicCredential=" + academicCredential +
                '}';
    }
}
