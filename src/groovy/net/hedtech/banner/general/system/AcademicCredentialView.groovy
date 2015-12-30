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
import javax.persistence.Version

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

    /**
     * Optimistic lock token for STVDEGC
     */
    @Version
    @Column(name = "STVDEGC_VERSION")
    Long version

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
        if (version != that.version) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        return result
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
                ", version=" + version +
                '}';
    }
}
