
/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

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
 * Function Code Validation Table
 */

@Entity
@Table(name="GTVFUNC")
class Function implements Serializable {
	
	/**
	 * Surrogate ID for GTVFUNC
	 */
	@Id
	@Column(name="GTVFUNC_SURROGATE_ID")
    @SequenceGenerator(name = "GTVFUNC_SEQ_GEN", allocationSize = 1, sequenceName = "GTVFUNC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVFUNC_SEQ_GEN")
	Long id

	/**
	 * Function Code.
	 */
	@Column(name="GTVFUNC_CODE", nullable = false, length=12)
	String code

	/**
	 * Function Code Description.
	 */
	@Column(name="GTVFUNC_DESC", nullable = false, length=60)
	String description

	/**
	 * Default Function Event Type Code.
	 */
 	@Column(name="GTVFUNC_ETYP_CODE", nullable = false, length=4)
	String etypCode

	/**
	 * Function Code Activity Date.
	 */
	@Column(name="GTVFUNC_activity_date")
	Date lastModified

	/**
	 * Version column which is used as a optimistic lock token for GTVFUNC
	 */
	@Version
	@Column(name="GTVFUNC_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for GTVFUNC
	 */
	@Column(name="GTVFUNC_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for GTVFUNC
	 */
	@Column(name="GTVFUNC_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"Function[id=$id, code=$code, description=$description, etypCode=$etypCode, lastModified=$lastModified, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:12)
		description(nullable:false, maxSize:60)
		etypCode(nullable:false, maxSize:4)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(function_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Function)) return false;

        Function function = (Function) o;

        if (code != function.code) return false;
        if (dataOrigin != function.dataOrigin) return false;
        if (description != function.description) return false;
        if (etypCode != function.etypCode) return false;
        if (id != function.id) return false;
        if (lastModified != function.lastModified) return false;
        if (lastModifiedBy != function.lastModifiedBy) return false;
        if (version != function.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (etypCode != null ? etypCode.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(function_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
