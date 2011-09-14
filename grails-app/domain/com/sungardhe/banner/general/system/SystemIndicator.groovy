/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Fri Apr 01 15:11:53 IST 2011
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Transient
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator

/**
 * System Indicator Validation Table
 */
/*PROTECTED REGION ID(systemindicator_namedqueries) ENABLED START*/
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVSYSI
 *  gtvsysi_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVSYSI")
class SystemIndicator implements Serializable {

   /**
    * Surrogate ID for GTVSYSI
    */
    @Id
    @Column(name = "GTVSYSI_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSYSI_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSYSI_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSYSI_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSYSI
     */
    @Version
    @Column(name = "GTVSYSI_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * CODE: System indicator which uniquely identifies this record.
     */
    @Column(name = "GTVSYSI_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * DESCRIPTION: Description of this code.
     */
    @Column(name = "GTVSYSI_DESC", nullable = false, length = 30)
    String description

    /**
     * ACTIVITY DATE: Date that record was created or last updated.
     */
    @Column(name = "GTVSYSI_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVSYSI
     */
    @Column(name = "GTVSYSI_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSYSI
     */
    @Column(name = "GTVSYSI_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """SystemIndicator[
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
        if (!(o instanceof SystemIndicator)) return false
        SystemIndicator that = (SystemIndicator) o
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
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(systemindicator_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(systemindicator_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(systemindicator_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(systemindicator_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
