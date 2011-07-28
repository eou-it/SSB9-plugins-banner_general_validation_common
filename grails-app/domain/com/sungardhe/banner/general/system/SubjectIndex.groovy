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
 Generated: Fri Apr 01 14:17:45 IST 2011
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Version

/**
 * Subject Index Validation Table
 */
/*PROTECTED REGION ID(subjectindex_namedqueries) ENABLED START*/

/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVSUBJ")
class SubjectIndex implements Serializable {

    /**
     * Surrogate ID for GTVSUBJ
     */
    @Id
    @Column(name = "GTVSUBJ_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSUBJ_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSUBJ_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSUBJ_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSUBJ
     */
    @Version
    @Column(name = "GTVSUBJ_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Subject Index Code.
     */
    @Column(name = "GTVSUBJ_CODE", nullable = false, unique = true, length = 6)
    String code

    /**
     * Subject Index Code Description.
     */
    @Column(name = "GTVSUBJ_DESC", nullable = false, length = 30)
    String description

    /**
     * Date on which the Code was created or last modified.
     */
    @Column(name = "GTVSUBJ_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for GTVSUBJ
     */
    @Column(name = "GTVSUBJ_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSUBJ
     */
    @Column(name = "GTVSUBJ_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """SubjectIndex[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof SubjectIndex)) return false
        SubjectIndex that = (SubjectIndex) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
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
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        dataOrigin(nullable: true, maxSize: 30)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)

        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(subjectindex_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(subjectindex_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(subjectindex_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(subjectindex_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
