
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
 * Day of Week Validation Table
 */


@Entity
@Table(name="STVDAYS")
class DayOfWeek implements Serializable {
	
	/**
	 * Surrogate ID for STVDAYS
	 */
	@Id
	@Column(name="STVDAYS_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the day code referenced in the Class Schedule and Registration Modules.
	 */
	@Column(name="STVDAYS_CODE", nullable = false, length=1)
	String code

	/**
	 * This field specifies the day associated with the day code.
	 */
	@Column(name="STVDAYS_DESC", length=30)
	String description

	/**
	 * This field identifies the number order associated with the day.
	 */
	@Column(name="STVDAYS_NUMBER", nullable = false, length=1)
	String number

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVDAYS_activity_date")
	Date lastModified

	/**
	 * System Required Indicator.
	 */
	@Column(name="STVDAYS_SYSREQ_IND", length=1)
	String sysreqIndicator

	/**
	 * Version column which is used as a optimistic lock token for STVDAYS
	 */
	@Version
	@Column(name="STVDAYS_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for STVDAYS
	 */
	@Column(name="STVDAYS_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVDAYS
	 */
	@Column(name="STVDAYS_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"DayOfWeek[id=$id, code=$code, description=$description, number=$number, lastModified=$lastModified, sysreqIndicator=$sysreqIndicator, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin, ]"
	}

	static constraints = {
		code(nullable:false, maxSize:1)
		description(nullable:true, maxSize:30)
		number(nullable:false, maxSize:1)
		lastModified(nullable:true)
		sysreqIndicator(nullable:true, maxSize:1)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)


		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(dayofweek_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof DayOfWeek)) return false;

        DayOfWeek dayOfWeek = (DayOfWeek) o;

        if (code != dayOfWeek.code) return false;
        if (dataOrigin != dayOfWeek.dataOrigin) return false;
        if (description != dayOfWeek.description) return false;
        if (id != dayOfWeek.id) return false;
        if (lastModified != dayOfWeek.lastModified) return false;
        if (lastModifiedBy != dayOfWeek.lastModifiedBy) return false;
        if (number != dayOfWeek.number) return false;
        if (sysreqIndicator != dayOfWeek.sysreqIndicator) return false;
        if (version != dayOfWeek.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (sysreqIndicator != null ? sysreqIndicator.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(dayofweek_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
