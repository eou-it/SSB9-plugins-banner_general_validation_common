/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import org.hibernate.annotations.Type

/**
 * Function Emphasis Validation Table
 */

@Entity
@Table(name = "GTVEMPH")
class FunctionEmphasis implements Serializable {

    /**
     * Surrogate ID for GTVEMPH
     */
    @Id
    @Column(name = "GTVEMPH_SURROGATE_ID")
    @SequenceGenerator(name = "GTVEMPH_SEQ_GEN", allocationSize = 1, sequenceName = "GTVEMPH_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVEMPH_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVEMPH
     */
    @Version
    @Column(name = "GTVEMPH_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Function Emphasis Code.
     */
    @Column(name = "GTVEMPH_CODE", nullable = false, unique = true, length = 10)
    String code

    /**
     * Function Emphasis Code Description.
     */
    @Column(name = "GTVEMPH_DESC", nullable = false, length = 30)
    String description

    /**
     * Date that the Function Emphasis Code was added or last changed.
     */
    @Column(name = "GTVEMPH_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVEMPH
     */
    @Column(name = "GTVEMPH_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVEMPH
     */
    @Column(name = "GTVEMPH_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FKV_GTVEMPH_INV_STVCOLL_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVEMPH_COLL_CODE", referencedColumnName = "STVCOLL_CODE")
    ])
    College college

    /**
     * Foreign Key : FKV_GTVEMPH_INV_STVDEPT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVEMPH_DEPT_CODE", referencedColumnName = "STVDEPT_CODE")
    ])
    Department department


    public String toString() {
        """FunctionEmphasis[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					college=$college,
					department=$department]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof FunctionEmphasis)) return false
        FunctionEmphasis that = (FunctionEmphasis) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (college != that.college) return false
        if (department != that.department) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (college != null ? college.hashCode() : 0)
        result = 31 * result + (department != null ? department.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 10)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        college(nullable: true)
        department(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']


}
