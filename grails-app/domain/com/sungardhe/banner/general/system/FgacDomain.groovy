
/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
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
 * FGAC Domain Code Validation Table
 */
 
@Entity
@Table(name="GTVFDMN")
class FgacDomain implements Serializable {
	
	/**
	 * Surrogate ID for GTVFDMN
	 */
	@Id
	@Column(name="GTVFDMN_SURROGATE_ID")
    @SequenceGenerator(name = "GTVFDMN_SEQ_GEN", allocationSize = 1, sequenceName = "GTVFDMN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVFDMN_SEQ_GEN")
	Long id

	/**
	 * DOMAIN CODE: FGAC Domain code.
	 */
	@Column(name="GTVFDMN_CODE", nullable = false, length=30)
	String code

	/**
	 * DOMAIN DESCRIPTION: Description of the FGAC domain code.
	 */
	@Column(name="GTVFDMN_DESC", nullable = false, length=60)
	String description

	/**
	 * ACTIVITY DATE: The most recent date a record was created or updated
	 */
	@Column(name="GTVFDMN_ACTIVITY_DATE")
	Date lastModified

	/**
	 * USER ID: The most recent user to create or update a record.
	 */
	@Column(name="GTVFDMN_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Version column which is used as a optimistic lock token for GTVFDMN
	 */
	@Version
	@Column(name="GTVFDMN_VERSION", length=19)
	Long version

	/**
	 * Data Origin column for GTVFDMN
	 */
	@Column(name="GTVFDMN_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"""FgacDomain[
					id=$id, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					version=$version, 
					dataOrigin=$dataOrigin, ]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true;
	    if (!(o instanceof FgacDomain)) return false;
	    FgacDomain that = (FgacDomain) o;
        if(id != that.id) return false;
        if(code != that.code) return false;
        if(description != that.description) return false;
        if(lastModified != that.lastModified) return false;
        if(lastModifiedBy != that.lastModifiedBy) return false;
        if(version != that.version) return false;
        if(dataOrigin != that.dataOrigin) return false;
        return true;
    }

	
	int hashCode() {
		int result;
	    result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
	}

	static constraints = {
		code(nullable:false, maxSize:30)
		description(nullable:false, maxSize:60)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)


		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(fgacdomain_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(fgacdomain_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
