package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by invthannee on 6/3/2015.
 *
 * AcademicDiscipline view Table
 */
@Entity
@Table(name = "SVQ_MAJRS")
class AcademicDiscipline implements Serializable {

    @EmbeddedId
    AcademicDisciplinePK id

    /**
     * This field identifies the major code referenced in the Catalog, Class Sched., Recruit., Admissions, Gen. Student, Registr., and Acad. Hist. Modules. Reqd. value: 00 - Major Not Declared.
     */
    @Column(name = "STVMAJR_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * This field specifies the major area of study associated with the major code.
     */
    @Column(name = "STVMAJR_DESC", length = 30)
    String description

    /**
     * Data Origin column for STVMAJR
     */
    @Column(name = "STVMAJR_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * This field specifies the GUID.
     */
    @Column(name = "STVMAJR_GUID")
    String guid

    /**
     * This field specifies the GUID.

     */
    @Column(name = "STVMAJR_VALID_TYPE", length = 30)
    String type

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        AcademicDiscipline that = (AcademicDiscipline) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (guid != that.guid) return false
        if (id != that.id) return false
        if (type != that.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        return result
    }
}
