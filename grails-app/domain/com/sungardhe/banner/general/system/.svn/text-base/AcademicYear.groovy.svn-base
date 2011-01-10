/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import java.io.Serializable
import java.util.Date

import javax.persistence.*

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type


/**
 * Academic Year Validation Table.
 */
@Entity
@Table(name="STVACYR")
class AcademicYear implements Serializable {
	
	/**
	 * Surrogate ID for STVACYR
	 */
	@Id
	@Column(name="STVACYR_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * Identifies the abbreviation for the beginning/ ending periods for academic year referenced in the General Student, Academic History, Degree Audit Modules. Format CCYY (e.g. 1995-1996 coded 1996).
	 */
	@Column(name="stvacyr_code", nullable = false, length=4)
	String code

	/**
	 * This field specifies the academic year associated with the academic year code.
	 */
	@Column(name="stvacyr_desc", length=30)
	String description

	/**
	 * This field identifies the most current date a record was created or updated.
	 */
	@Column(name="stvacyr_activity_date")
	Date lastModified

	/**
	 * The system required indicator
	 */
	@Type(type = "yes_no")
	@Column(name="stvacyr_sysreq_ind")
	Boolean sysreqInd

	/**
	 * Column for storing last modified by for STVACYR
	 */
	@Column(name="stvacyr_user_id", nullable = false, length=30)
	String lastModifiedBy

	/**
	 * Optimistic Lock Token for STVACYR
	 */
	@Version
	@Column(name="stvacyr_version", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for STVACYR
	 */
	@Column(name="stvacyr_data_origin", length=30)
	String dataOrigin

	
	
	public String toString() {
		"AcademicYear[id=$id, code=$code, description=$description, lastModified=$lastModified, sysreqInd=$sysreqInd, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:30)
		sysreqInd(nullable:true, maxSize:1)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof AcademicYear)) return false;

        AcademicYear that = (AcademicYear) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (sysreqInd != that.sysreqInd) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (sysreqInd != null ? sysreqInd.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
