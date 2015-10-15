/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 *<p>Read only view for Academic Credentials.</p>
 */

@Entity
@Table(name = "gvq_academic_credentials")
class AcademicCredentialView implements Serializable{

    /**
     * Surrogate ID for STVDEGC
     */
    @Id
    @Column(name = "STVDEGC_SURROGATE_ID")
    String id

    /**
     * This field identifies the degree code referenced in the Recruiting, Admissions, Gen. Student, Registration, Academic History and Degree Audit Modules. 000000 - Degree Not Declared.
     */
    @Column(name = "STVDEGC_CODE")
    String code

    /**
     * This field specifies the degree (e.g. Bachelor of Business Administration, Master of Arts, Juris Doctor, etc.) associated with the degree code.
     */
    @Column(name = "STVDEGC_DESC")
    String description

    /**
     * Last Modified By column for STVDEGC
     */
    @Column(name = "STVDEGC_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVDEGC
     */
    @Column(name = "STVDEGC_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVDEGC_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This field specifies the type of Degree.
     */
    @Column(name = "TYPE", length = 30)
    String type

    /**
     * This field specifies the GUID.
     */
    @Column(name = "GORGUID_GUID")
    String guid


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AcademicCredentialView)) return false

        AcademicCredentialView that = (AcademicCredentialView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (guid != that.guid) return false
        if (id != that.id) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (type != that.type) return false

        return true
    }

    int hashCode() {
        int result
        result = id.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + lastModifiedBy.hashCode()
        result = 31 * result + dataOrigin.hashCode()
        result = 31 * result + lastModified.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + guid.hashCode()
        return result
    }

    @Override
    public String toString() {
        return "AcademicCredentialView{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", lastModified=" + lastModified +
                ", type='" + type + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }

    def static countAll(filterData) {
       return finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        return finderByAll().find(filterData, pagingAndSortParams)

    }

    def private static finderByAll = {
        def query = "from AcademicCredentialView a where 1 = 1"
        return new DynamicFinder(AcademicCredentialView.class, query, "a")
    }
}
