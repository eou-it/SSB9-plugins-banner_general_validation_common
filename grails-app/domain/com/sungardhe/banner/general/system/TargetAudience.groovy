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
 Generated: Fri Apr 01 14:03:34 IST 2011
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import javax.persistence.SequenceGenerator
import javax.persistence.GenerationType

/**
 * Target Audience Validation Table
 */
/*PROTECTED REGION ID(targetaudience_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVTARG
 *  gtvtarg_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVTARG")
class TargetAudience implements Serializable {

    /**
     * Surrogate ID for GTVTARG
     */
    @Id
    @Column(name = "GTVTARG_SURROGATE_ID")
    @SequenceGenerator(name ="GTVTARG_SEQ_GEN", allocationSize =1, sequenceName  ="GTVTARG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "GTVTARG_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVTARG
     */
    @Version
    @Column(name = "GTVTARG_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Target Audience Code.
     */
    @Column(name = "GTVTARG_CODE", nullable = false, unique = true, length = 5)
    String code

    /**
     * Target Audience Code Description.
     */
    @Column(name = "GTVTARG_DESC", nullable = false, length = 30)
    String description

    /**
     * Date on which the Code was created or last modified.
     */
    @Column(name = "GTVTARG_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for GTVTARG
     */
    @Column(name = "GTVTARG_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVTARG
     */
    @Column(name = "GTVTARG_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """TargetAudience[
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
        if (!(o instanceof TargetAudience)) return false
        TargetAudience that = (TargetAudience) o
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
        code(nullable: false, maxSize: 5)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(targetaudience_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(targetaudience_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(targetaudience_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(targetaudience_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
