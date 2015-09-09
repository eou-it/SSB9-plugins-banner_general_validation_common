/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for academic disciplines
 */
class AcademicDiscipline {
    @Delegate
    private final AcademicDisciplineView academicDisciplineView
    Metadata metadata
    String type
    String guid

    AcademicDiscipline(AcademicDisciplineView academicDisciplineView) {
        this.metadata = new Metadata(academicDisciplineView.dataOrigin)
        this.guid=academicDisciplineView.guid
        this.type = academicDisciplineView.type
        this.academicDisciplineView = academicDisciplineView
    }

    @Override
    public String toString() {
        return "AcademicDiscipline{" +
                ", metadata=" + metadata +
                ", guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
