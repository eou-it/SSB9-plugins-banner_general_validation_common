/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import javax.persistence.*

import org.hibernate.annotations.GenericGenerator;

/**
 * Duration Unit Code Validation Table
 */
@Entity
@Table(name="GTVDUNT")  
class DurationUnit implements Serializable {
	
	@Id
	@Column(name="gtvdunt_surrogate_id")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * Duration Unit Code: The Duration Unit Code
	 */
	@Column(name="GTVDUNT_CODE", nullable = false, length=4)
	String code

	@Column(name="GTVDUNT_DESC", nullable = false, length=30)
	String description

	/**
	 * Represents the number of days that one duration unit would equate to (ie 1 week equals 7 days)
	 */
	@Column(name="GTVDUNT_NUMBER_OF_DAYS", nullable = false, length=22)
	BigDecimal numberOfDays

	@Column(name="GTVDUNT_activity_date")
	Date lastModified

	@Column(name="GTVDUNT_USER_ID" )
	String lastModifiedBy

	/**
	 * Voice Response Message Number
	 */
	@Column(name="GTVDUNT_VR_MSG_NO", length=22)
	BigDecimal vrMsgNo

	@Version
	@Column(name="GTVDUNT_VERSION", nullable = false, length=19)
	Long version

	@Column(name="gtvdunt_data_origin", length=30)
	String dataOrigin

	
	
	public String toString() {
		"DurationUnitCode[id=$id, code=$code, description=$description, numberOfDays=$numberOfDays, lastModified=$lastModified, lastModifiedBy=$lastModifiedBy, vrMsgNo=$vrMsgNo, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:false, maxSize:30)
		numberOfDays(nullable:false, maxSize:22)
		vrMsgNo(nullable:true, maxSize:22)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof DurationUnit)) return false;

        DurationUnit that = (DurationUnit) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (numberOfDays != that.numberOfDays) return false;
        if (version != that.version) return false;
        if (vrMsgNo != that.vrMsgNo) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (numberOfDays != null ? numberOfDays.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (vrMsgNo != null ? vrMsgNo.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
