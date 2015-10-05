/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicDisciplineView

/**
 * Decorator for academic disciplines
 */
class AcademicDiscipline {
    @Delegate
    private final AcademicDisciplineView academicDisciplineView
    String type
    String guid

    AcademicDiscipline(AcademicDisciplineView academicDisciplineView) {
        this.guid=academicDisciplineView.guid
        this.type = academicDisciplineView.type
        this.academicDisciplineView = academicDisciplineView
    }

    @Override
    public String toString() {
        return "AcademicDiscipline{" +
                ", guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
