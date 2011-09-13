
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
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type

/**
 * Originator Validation Table
 */

@Entity
@Table(name="STVORIG")
class Originator implements Serializable {

    /**
	 * Surrogate ID for STVORIG
	 */
	@Id
	@Column(name="STVORIG_SURROGATE_ID")
    @SequenceGenerator(name = "STVORIG_SEQ_GEN", allocationSize = 1, sequenceName = "STVORIG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVORIG_SEQ_GEN")
	Integer id

    /**
	 * Optimistic lock token for STVORIG
	 */
	@Version
	@Column(name="STVORIG_VERSION")
	Integer version
    
	/**
	 * This field identifies the originator code referenced in the Gen. Person, Recruiting, Student Billing, and Acad. Hist. Modules.
	 */
	@Column(name="STVORIG_CODE")
	String code

	/**
	 * This field specifies the originator associated with the originator code. Table  is used to identify origin of comments.
	 */
	@Column(name="STVORIG_DESC")
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVORIG_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Last modified by column for STVORIG
	 */
	@Column(name="STVORIG_USER_ID")
	String lastModifiedBy

	/**
	 * Data origin column for STVORIG
	 */
	@Column(name="STVORIG_DATA_ORIGIN")
	String dataOrigin

	
	public String toString() {
		"""Originator[
					id=$id,
					version=$version,
					code=$code,
					description=$description, 
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}


    static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:30)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
    }


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof Originator)) return false
	    Originator that = (Originator) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
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

    
    public static readonlyProperties = [ 'code',  ]
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(originator_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
