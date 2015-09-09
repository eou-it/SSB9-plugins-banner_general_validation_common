/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicCredentialsView
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for "academic-credentials" API
 */
class AcademicCredentials {

    @Delegate
    private final  AcademicCredentialsView academicCredential
    Metadata metadata

    AcademicCredentials(AcademicCredentialsView academicCredential,Metadata metadata) {
        this.academicCredential = academicCredential
        this.metadata = metadata
    }

    @Override
    public String toString() {
        return "AcademicCredentials{" +
                "academicCredential=" + academicCredential +
                ", metadata=" + metadata +
                '}';
    }
}
