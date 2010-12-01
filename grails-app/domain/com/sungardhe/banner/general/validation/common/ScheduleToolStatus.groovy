
/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.validation.common

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

/**
 * Validation entries for Schedule Status Codes.
 */

@Entity
@Table(name="GTVSCHS")
class ScheduleToolStatus implements Serializable {
	
	/**
	 * Surrogate ID for GTVSCHS
	 */
	@Id
	@Column(name="GTVSCHS_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * Code for Schedule Status.
	 */
	@Column(name="GTVSCHS_CODE", nullable = false, length=3)
	String code

	/**
	 * Description of Schedule Status code.
	 */
	@Column(name="GTVSCHS_DESC", nullable = false, length=30)
	String description

	/**
	 * A Y in this column indicates that the row is delivered by SCT and required for the system.
	 */
	@Type(type = "yes_no")
	@Column(name="GTVSCHS_SYSTEM_REQ_IND", nullable = false)
	Boolean systemRequiredIndicator

	/**
	 * This field identifies the date a record was created or updated.
	 */
	@Column(name="GTVSCHS_activity_date")
	Date lastModified

	/**
	 * Version column which is used as a optimistic lock token for GTVSCHS
	 */
	@Version
	@Column(name="GTVSCHS_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for GTVSCHS
	 */
	@Column(name="GTVSCHS_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for GTVSCHS
	 */
	@Column(name="GTVSCHS_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"ScheduleToolStatus[id=$id, code=$code, description=$description, systemRequiredIndicator=$systemRequiredIndicator, lastModified=$lastModified, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:3)
		description(nullable:false, maxSize:30)
		systemRequiredIndicator(nullable:false)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(scheduletoolstatus_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof ScheduleToolStatus)) return false;

        ScheduleToolStatus that = (ScheduleToolStatus) o;

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
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
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
    /*PROTECTED REGION ID(scheduletoolstatus_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
