/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 1.24
 Generated: Thu Jul 28 14:22:48 IST 2011
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
 * Relationship Validation Table
 */
/*PROTECTED REGION ID(relationship_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: STVRELT
 *  stvrelt.stvrelt_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "STVRELT")
class Relationship implements Serializable {

    /**
     * Surrogate ID for STVRELT
     */
    @Id
    @Column(name = "STVRELT_SURROGATE_ID")
    @SequenceGenerator(name = "STVRELT_SEQ_GEN", allocationSize = 1, sequenceName = "STVRELT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVRELT_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVRELT
     */
    @Version
    @Column(name = "STVRELT_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * This field identifies the relationship code referenced on the Emergency       Contact Form (SPAEMER) and the Guardian Information Form (SOAFOLK).
     */
    @Column(name = "STVRELT_CODE", nullable = false, unique = true, length = 1)
    String code

    /**
     * This field specifies the relationship of contact person to person associated    with record.
     */
    @Column(name = "STVRELT_DESC", length = 30)
    String description

    /**
     * SEVIS EQUIVALENCY: SEVIS code for relationship
     */
    @Column(name = "STVRELT_SEVIS_EQUIV", length = 3)
    String studentExchangeVisitorInformationSystemEquivalent

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVRELT_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVRELT
     */
    @Column(name = "STVRELT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVRELT
     */
    @Column(name = "STVRELT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Relationship[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					studentExchangeVisitorInformationSystemEquivalent=$studentExchangeVisitorInformationSystemEquivalent,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Relationship)) return false
        Relationship that = (Relationship) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (studentExchangeVisitorInformationSystemEquivalent != that.studentExchangeVisitorInformationSystemEquivalent) return false
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
        result = 31 * result + (studentExchangeVisitorInformationSystemEquivalent != null ? studentExchangeVisitorInformationSystemEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 1)
        description(nullable: true, maxSize: 30)
        studentExchangeVisitorInformationSystemEquivalent(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(relationship_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(relationship_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(relationship_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(relationship_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
