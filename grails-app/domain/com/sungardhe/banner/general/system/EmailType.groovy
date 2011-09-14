
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
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:15 EDT 2011 
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

import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.Type
//import com.sungardhe.banner.general.person.PersonEMail
//import com.sungardhe.banner.general.person.PersonRelatedHold

/**
 * The GTVEMAL table contains the valid e-mail type codes.
 */
/*PROTECTED REGION ID(emailtype_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: GTVEMAL
  *  gtvemal_code

*/
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVEMAL")
class EmailType implements Serializable {
	
	/**
	 * Surrogate ID for GTVEMAL
	 */
	@Id
	@Column(name="GTVEMAL_SURROGATE_ID")
	@SequenceGenerator(name ="GTVEMAL_SEQ_GEN", allocationSize =1, sequenceName  ="GTVEMAL_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GTVEMAL_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GTVEMAL
	 */
	@Version
	@Column(name = "GTVEMAL_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * A code identifying the type of e-mail address.
	 */
	@Column(name = "GTVEMAL_CODE", nullable = false, unique = true, length = 4)
	String code

	/**
	 * The description of the e-mail address.
	 */
	@Column(name = "GTVEMAL_DESC", length = 60)
	String description

	/**
	 * Display on Web Indicator, Y indicates this type of email should display
	 */
	@Type(type = "yes_no")
	@Column(name = "GTVEMAL_DISP_WEB_IND", nullable = false, length = 1)
	Boolean displayWebIndicator

	/**
	 * URL Indicator, Y indicates that this email record is actually a URL.
	 */
	@Type(type = "yes_no")
	@Column(name = "GTVEMAL_URL_IND", nullable = false, length = 1)
	Boolean urlIndicator

	/**
	 * The date on which the row was added or modified.
	 */
	@Column(name = "GTVEMAL_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Last modified by column for GTVEMAL
	 */
	@Column(name = "GTVEMAL_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for GTVEMAL
	 */
	@Column(name = "GTVEMAL_DATA_ORIGIN", length = 30)
	String dataOrigin

	
	
	public String toString() {
		"""EmailType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					displayWebIndicator=$displayWebIndicator, 
					urlIndicator=$urlIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof EmailType)) return false
	    EmailType that = (EmailType) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(displayWebIndicator != that.displayWebIndicator) return false
        if(urlIndicator != that.urlIndicator) return false
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
        result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0)
        result = 31 * result + (urlIndicator != null ? urlIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:60)
		displayWebIndicator(nullable:false)
		urlIndicator(nullable:false)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(emailtype_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /*PROTECTED REGION ID(emailtype_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]
    /*PROTECTED REGION END*/        
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(emailtype_custom_attributes) ENABLED START*/
    
    /*PROTECTED REGION END*/
        
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(emailtype_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
