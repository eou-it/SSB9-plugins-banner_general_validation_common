/** *****************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ****************************************************************************** */
/**
 Banner Automator Version: 1.29
 Generated: Sun May 20 17:49:39 IST 2012
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import org.hibernate.annotations.Type

/**
 * Validation entries for SQL Process Codes.
 */
/*PROTECTED REGION ID(entriesforsqlprocesss_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVSQPR
 *  ORDER BY GTVSQPR_CODE

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVSQPR")
class EntriesForSqlProcesss implements Serializable {

    /**
     * Surrogate ID for GTVSQPR
     */
    @Id
    @Column(name = "GTVSQPR_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSQPR_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSQPR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSQPR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSQPR
     */
    @Version
    @Column(name = "GTVSQPR_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * PROCESS CODE: Process code for dynamic SQL processing.
     */
    @Column(name = "GTVSQPR_CODE", nullable = false, unique = true, length = 30)
    String code

    /**
     * PROCESS CODE DESCRIPTION: Description of SQL process code.
     */
    @Column(name = "GTVSQPR_DESC", nullable = false, length = 60)
    String description

    /**
     * SYSTEM REQUIRED INDICATOR: This field identifies whether or not this code is required by the system.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVSQPR_SYS_REQ_IND", nullable = false, length = 1)
    Boolean systemRequiredIndicator

    /**
     * START DATE: Date code became effective.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "GTVSQPR_START_DATE", nullable = false)
    Date startDate

    /**
     * END DATE: Date code became obsolete.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "GTVSQPR_END_DATE")
    Date endDate

    /**
     * ACTIVITY DATE: This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVSQPR_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: This field identifies the most recent user to create or update a record.
     */
    @Column(name = "GTVSQPR_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSQPR
     */
    @Column(name = "GTVSQPR_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """EntriesForSqlProcesss[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					systemRequiredIndicator=$systemRequiredIndicator,
					startDate=$startDate,
					endDate=$endDate,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof EntriesForSqlProcesss)) return false
        EntriesForSqlProcesss that = (EntriesForSqlProcesss) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
        if (startDate != that.startDate) return false
        if (endDate != that.endDate) return false
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
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0)
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 30)
        description(nullable: false, maxSize: 60)
        systemRequiredIndicator(nullable: false)
        startDate(nullable: false)
        endDate(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(entriesforsqlprocesss_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(entriesforsqlprocesss_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(entriesforsqlprocesss_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(entriesforsqlprocesss_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
