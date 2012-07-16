
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
 Banner Automator Version: 1.26
 Generated: Thu Aug 11 12:05:30 EDT 2011
 */
package net.hedtech.banner.general.system

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

import org.hibernate.annotations.Type
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Recruiting Initials Code Validation Table
 */
/*PROTECTED REGION ID(initials_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: STVINIT
  *  stvinit_code

*/
/*PROTECTED REGION END*/
@Entity
@Table(name = "STVINIT")
class Initials implements Serializable {

	/**
	 * Surrogate ID for STVINIT
	 */
	@Id
	@Column(name="STVINIT_SURROGATE_ID")
	@SequenceGenerator(name ="STVINIT_SEQ_GEN", allocationSize =1, sequenceName  ="STVINIT_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="STVINIT_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for STVINIT
	 */
	@Version
	@Column(name = "STVINIT_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * This field identifies the recruiting initials code referenced on the            Recruiting Material Control Form (SRAMATL) and the Prospect Information Form    (SRARECR).
	 */
	@Column(name = "STVINIT_CODE", nullable = false, unique = true, length = 4)
	String code

	/**
	 * This field specifies the recruiter associated with the recruiting initials      code.  This form is used to generate signature line for recruiting materials.
	 */
	@Column(name = "STVINIT_DESC", nullable = false, length = 60)
	String description

	/**
	 * This field identifies the first line of a title associated with the recruiter.
	 */
	@Column(name = "STVINIT_TITLE1", nullable = false, length = 35)
	String title1

	/**
	 * This field identifies the second line of a title associated with the            recruiter.
	 */
	@Column(name = "STVINIT_TITLE2", length = 35)
	String title2

	/**
	 * Email address for person whose initials are defined for letter.
	 */
	@Column(name = "STVINIT_EMAIL_ADDRESS", length = 128)
	String emailAddress

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STVINIT_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Last modified by column for STVINIT
	 */
	@Column(name = "STVINIT_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for STVINIT
	 */
	@Column(name = "STVINIT_DATA_ORIGIN", length = 30)
	String dataOrigin



	public String toString() {
		"""Initials[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					title1=$title1,
					title2=$title2,
					emailAddress=$emailAddress,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
	}


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof Initials)) return false
	    Initials that = (Initials) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(title1 != that.title1) return false
        if(title2 != that.title2) return false
        if(emailAddress != that.emailAddress) return false
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
        result = 31 * result + (title1 != null ? title1.hashCode() : 0)
        result = 31 * result + (title2 != null ? title2.hashCode() : 0)
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:false, maxSize:60)
		title1(nullable:false, maxSize:35)
		title2(nullable:true, maxSize:35)
		emailAddress(nullable:true, maxSize:128)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(initials_custom_constraints) ENABLED START*/

	    /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(initials_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(initials_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(initials_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
