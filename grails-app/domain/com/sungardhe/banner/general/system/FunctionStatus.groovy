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
 Generated: Fri Apr 01 15:11:53 IST 2011
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
 * Function Status Validation Table
 */
/*PROTECTED REGION ID(functionstatus_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVFSTA
 *  gtvfsta_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVFSTA")
class FunctionStatus implements Serializable {

   /**
     * Surrogate ID for GTVFSTA
     */
    @Id
    @Column(name = "GTVFSTA_SURROGATE_ID")
    @SequenceGenerator(name = "GTVFSTA_SEQ_GEN", allocationSize = 1, sequenceName = "GTVFSTA_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVFSTA_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVFSTA
     */
    @Version
    @Column(name = "GTVFSTA_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Function Status Code.
     */
    @Column(name = "GTVFSTA_CODE", nullable = false, unique = true, length = 1)
    String code

    /**
     * Function Status Code Description.
     */
    @Column(name = "GTVFSTA_DESC", nullable = false, length = 30)
    String description

    /**
     * Status Code for the Function means that the function is active or inactive.
     */
    @Column(name = "GTVFSTA_ACTIVE_IND", length = 1)
    String activeIndicator

    /**
     * Date on which the Status Code was created or last modified.
     */
    @Column(name = "GTVFSTA_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for GTVFSTA
     */
    @Column(name = "GTVFSTA_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVFSTA
     */
    @Column(name = "GTVFSTA_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """FunctionStatus[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					activeIndicator=$activeIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof FunctionStatus)) return false
        FunctionStatus that = (FunctionStatus) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (activeIndicator != that.activeIndicator) return false
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
        result = 31 * result + (activeIndicator != null ? activeIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 1)
        description(nullable: false, maxSize: 30)
        activeIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(functionstatus_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(functionstatus_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionstatus_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionstatus_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
