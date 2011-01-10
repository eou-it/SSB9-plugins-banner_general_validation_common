
/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

/**
 * Award Catagory Validation Table
 */
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: STVACAT
  *  stvacat_code

*/
@Entity
@Table(name="STVACAT")
class AwardCategory implements Serializable {
	
	/**
	 * Surrogate ID for STVACAT
	 */
	@Id
	@Column(name="STVACAT_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * Award Catagory Code
	 */
	@Column(name="STVACAT_CODE", nullable = false, length=2)
	String code

	/**
	 * Description of Award Catagory Code
	 */
	@Column(name="STVACAT_DESC", nullable = false, length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVACAT_activity_date")
	Date lastModified

	/**
	 * System required indicator (informational only)
	 */
	@Column(name="STVACAT_SYSTEM_REQ_IND", length=1)
	String systemRequiredIndicator

	/**
	 * Version column which is used as a optimistic lock token for STVACAT
	 */
	@Version
	@Column(name="STVACAT_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for STVACAT
	 */
	@Column(name="STVACAT_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVACAT
	 */
	@Column(name="STVACAT_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"AwardCategory[id=$id, code=$code, description=$description, lastModified=$lastModified, systemRequiredIndicator=$systemRequiredIndicator, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:2)
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		systemRequiredIndicator(nullable:true, maxSize:1)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(awardcategory_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof AwardCategory)) return false;

        AwardCategory that = (AwardCategory) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(awardcategory_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
