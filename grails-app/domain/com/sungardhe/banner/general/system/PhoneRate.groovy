
/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Fri Feb 11 16:36:47 EST 2011 
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator

/**
 * Phone Rate Code Validation Table
 */
/*PROTECTED REGION ID(phonerate_namedqueries) ENABLED START*/
/*PROTECTED REGION END*/
@Entity
@Table(name = "STVPRCD")
class PhoneRate implements Serializable {
	
	/**
	 * Surrogate ID for STVPRCD
	 */
	@Id
	@Column(name="STVPRCD_SURROGATE_ID")
    @SequenceGenerator(name = "STVPRCD_SEQ_GEN", allocationSize = 1, sequenceName = "STVPRCD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVPRCD_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for STVPRCD
	 */
	@Version
	@Column(name = "STVPRCD_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * Phone Rate Code
	 */
	@Column(name = "STVPRCD_CODE", nullable = false, unique = true, length = 4)
	String code

	/**
	 * Monthly Basis Indicator.
	 */
	@Column(name = "STVPRCD_MONTHLY_IND", nullable = false, length = 1)
	String monthlyIndicator

	/**
	 * Daily Basis Indicator.
	 */
	@Column(name = "STVPRCD_DAILY_IND", nullable = false, length = 1)
	String dailyIndicator

	/**
	 * Termly Basis Indicator.
	 */
	@Column(name = "STVPRCD_TERMLY_IND", nullable = false, length = 1)
	String termlyIndicator

	/**
	 * Description of Phone Rate Code
	 */
	@Column(name = "STVPRCD_DESC", nullable = false, length = 30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name = "STVPRCD_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Last modified by column for STVPRCD
	 */
	@Column(name = "STVPRCD_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for STVPRCD
	 */
	@Column(name = "STVPRCD_DATA_ORIGIN", length = 30)
	String dataOrigin

	
	
	public String toString() {
		"""PhoneRate[
					id=$id, 
					version=$version, 
					code=$code, 
					monthlyIndicator=$monthlyIndicator, 
					dailyIndicator=$dailyIndicator, 
					termlyIndicator=$termlyIndicator, 
					description=$description, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PhoneRate)) return false
	    PhoneRate that = (PhoneRate) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(monthlyIndicator != that.monthlyIndicator) return false
        if(dailyIndicator != that.dailyIndicator) return false
        if(termlyIndicator != that.termlyIndicator) return false
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
        result = 31 * result + (monthlyIndicator != null ? monthlyIndicator.hashCode() : 0)
        result = 31 * result + (dailyIndicator != null ? dailyIndicator.hashCode() : 0)
        result = 31 * result + (termlyIndicator != null ? termlyIndicator.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		monthlyIndicator(nullable:false, maxSize:1, inList:["Y","N"])
		dailyIndicator(nullable:false, maxSize:1, inList:["Y","N"])
		termlyIndicator(nullable:false, maxSize:1, inList:["Y","N"])
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(phonerate_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /*PROTECTED REGION ID(phonerate_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]
    /*PROTECTED REGION END*/        
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(phonerate_custom_attributes) ENABLED START*/
    
    /*PROTECTED REGION END*/
        
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(phonerate_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
