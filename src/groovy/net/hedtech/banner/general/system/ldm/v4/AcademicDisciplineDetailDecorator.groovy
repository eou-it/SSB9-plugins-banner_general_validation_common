package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.AcademicDiscipline
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for academic disciplines
 * Created by invthannee on 6/2/2015.
 */
class AcademicDisciplineDetailDecorator {
    @Delegate
    private final AcademicDiscipline academicDiscipline
    Metadata metadata
    String guid
    String type
    List<Map<String, String>> title
    List<Map<String, String>> descriptionList

    AcademicDisciplineDetailDecorator(AcademicDiscipline academicDiscipline) {
        title = []
        descriptionList = []
        this.academicDiscipline = academicDiscipline
        this.guid = academicDiscipline.guid
        this.metadata = new Metadata(academicDiscipline.dataOrigin)
        this.type = academicDiscipline.type
        this.title << ["en": academicDiscipline.code]
        this.descriptionList << ["en": academicDiscipline.description]
    }

}
