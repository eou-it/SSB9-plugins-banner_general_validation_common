/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
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
 * Address Type Validation Table
 */
/*PROTECTED REGION ID(addresstype_namedqueries) ENABLED START*/
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: STVATYP
 *  stvatyp_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "STVATYP")
class AddressType implements Serializable {

    /**
     * Surrogate ID for STVATYP
     */
    @Id
    @Column(name = "STVATYP_SURROGATE_ID")
    @SequenceGenerator(name = "STVATYP_SEQ_GEN", allocationSize = 1, sequenceName = "STVATYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVATYP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVATYP
     */
    @Version
    @Column(name = "STVATYP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * A code used throughout all BANNER systems to identify the type of address for which information is maintained (for example; Mailing, Permanent, or Billing).
     */
    @Column(name = "STVATYP_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * This field identifies the address type associated with the type code.
     */
    @Column(name = "STVATYP_DESC", length = 30)
    String description

    /**
     * System Required Indicator
     */
    @Column(name = "STVATYP_SYSTEM_REQ_IND", length = 1)
    String systemRequiredIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVATYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVATYP
     */
    @Column(name = "STVATYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVATYP
     */
    @Column(name = "STVATYP_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVATYP_INV_STVTELE_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVATYP_TELE_CODE", referencedColumnName = "STVTELE_CODE")
    ])
    TelephoneType telephoneType


    public String toString() {
        """AddressType[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					systemRequiredIndicator=$systemRequiredIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					telephoneType=$telephoneType]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AddressType)) return false
        AddressType that = (AddressType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (telephoneType != that.telephoneType) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (telephoneType != null ? telephoneType.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: true, maxSize: 30)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ["Y", "N"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        telephoneType(nullable: true)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(addresstype_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(addresstype_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(addresstype_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(addresstype_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
