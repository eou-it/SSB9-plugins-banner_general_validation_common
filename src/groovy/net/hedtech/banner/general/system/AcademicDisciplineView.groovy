/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * <p>Read only view for Academic Disciplines.</p>
 */
@Entity
@Table(name = "SVQ_MAJORS")
class AcademicDisciplineView implements Serializable {

    /**
     * This field specifies the GUID.
     */
    @Id
    @Column(name = "STVMAJR_GUID")
    String guid

    /**
     * Surrogate ID for STVMAJR
     */
    @Column(name = "STVMAJR_SURROGATE_ID")
    Long surrogateId


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
     * This field specifies the type of STVMAJR.

     */
    @Column(name = "STVMAJR_VALID_TYPE", length = 30)
    String type

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AcademicDisciplineView)) return false

        AcademicDisciplineView that = (AcademicDisciplineView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (guid != that.guid) return false
        if (surrogateId != that.surrogateId) return false
        if (type != that.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (surrogateId != null ? surrogateId.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "AcademicDisciplineView{" +
                "guid='" + guid + '\'' +
                ", surrogateId=" + surrogateId +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    def static countAll(filterData) {
        return finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        return finderByAll().find(filterData, pagingAndSortParams)

    }

    def private static finderByAll = {
        def query = "from AcademicDisciplineView a where 1 = 1"
        return new DynamicFinder(AcademicCredentialView.class, query, "a")
    }
}
