
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
import org.hibernate.annotations.GenericGenerator


/**
 * This table defines the Banner name type codes.
 */
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: GTVNTYP
  *  gtvntyp_code

*/
@Entity
@Table(name="GTVNTYP")
class NameType implements Serializable {
	
	/**
	 * Surrogate ID for GTVNTYP
	 */
	@Id
	@Column(name="GTVNTYP_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * NAME TYPE CODE: The code associated with a type of name.
	 */
	@Column(name="GTVNTYP_CODE", nullable = false, length=4)
	String code

	/**
	 * DESCRIPTION: The description of the name type code.
	 */
	@Column(name="GTVNTYP_DESC", nullable = false, length=30)
	String description

	/**
	 * ACTIVITY DATE: The date that this record was created or last updated.
	 */
	@Column(name="GTVNTYP_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Version column which is used as a optimistic lock token for GTVNTYP
	 */
	@Version
	@Column(name="GTVNTYP_VERSION", length=19)
	Long version

	/**
	 * Last Modified By column for GTVNTYP
	 */
	@Column(name="GTVNTYP_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for GTVNTYP
	 */
	@Column(name="GTVNTYP_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"""NameType[
					id=$id, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					version=$version, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, ]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true;
	    if (!(o instanceof NameType)) return false;
	    NameType that = (NameType) o;
        if(id != that.id) return false;
        if(code != that.code) return false;
        if(description != that.description) return false;
        if(lastModified != that.lastModified) return false;
        if(version != that.version) return false;
        if(lastModifiedBy != that.lastModifiedBy) return false;
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
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)


		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(nametype_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(nametype_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
