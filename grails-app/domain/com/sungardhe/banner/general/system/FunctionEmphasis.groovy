/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Tue May 10 23:42:04 IST 2011
 */
package com.sungardhe.banner.general.system

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
/*PROTECTED REGION ID(functionemphasis_namedqueries) ENABLED START*/
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVEMPH
 *  gtvemph_code

 */
/*PROTECTED REGION END*/
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
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(functionemphasis_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(functionemphasis_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionemphasis_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionemphasis_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
