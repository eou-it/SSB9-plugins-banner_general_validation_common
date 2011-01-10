
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
/**
 * Import Related Entities if they are external to this package
 */
import com.sungardhe.banner.general.system.CIPCode

/**
 * Major, Minor, Concentration Validation Table
 */
@Entity
@Table(name="STVMAJR")
class MajorMinorConcentration implements Serializable {
	
	/**
	 * Surrogate ID for STVMAJR
	 */
	@Id
	@Column(name="STVMAJR_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the major code referenced in the Catalog, Class Sched., Recruit., Admissions, Gen. Student, Registr., and Acad. Hist. Modules. Reqd. value: 00 - Major Not Declared.
	 */
	@Column(name="STVMAJR_CODE", nullable = false, length=4)
	String code

	/**
	 * This field specifies the major area of study associated with the major code.
	 */
	@Column(name="STVMAJR_DESC", length=30)
	String description

	/**
	 * This field indicates whether the area of study is a valid major.
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_VALID_MAJOR_IND")
	Boolean validMajorIndicator

	/**
	 * This field indicates whether the area of study is a valid minor.
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_VALID_MINOR_IND")
	Boolean validMinorIndicator

	/**
	 * This field indicates whether the area of study is a valid concentration.
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_VALID_CONCENTRATN_IND")
	Boolean validConcentratnIndicator

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVMAJR_activity_date")
	Date lastModified

	/**
	 * Occupation specific indicator. Y indicates program has been designated as occupation specific
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_OCCUPATION_IND")
	Boolean occupationIndicator

	/**
	 * A indicator which indicates the eligibility for aid
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_AID_ELIGIBILITY_IND")
	Boolean aidEligibilityIndicator

	/**
	 * System Required Indicator
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_SYSTEM_REQ_IND")
	Boolean systemRequiredIndicator

	/**
	 * The Voice Response message number assigned to the recorded message that describes the major code.
	 */
	@Column(name="STVMAJR_VR_MSG_NO", length=22)
	String voiceResponseMsgNumber

	/**
	 * Display on Web Indicator is now obsolete.
	 */
	@Type(type = "yes_no")
	@Column(name="STVMAJR_DISP_WEB_IND")
	Boolean displayWebIndicator

	/**
	 * SEVIS EQUIVALENCY: SEVIS code for primary major
	 */
	@Column(name="STVMAJR_SEVIS_EQUIV", length=6)
	String sevisEquiv

	/**
	 * Version column which is used as a optimistic lock token for STVMAJR
	 */
	@Version
	@Column(name="STVMAJR_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for STVMAJR
	 */
	@Column(name="STVMAJR_USER_ID" )
	String lastModifiedBy

	/**
	 * Data Origin column for STVMAJR
	 */
	@Column(name="STVMAJR_DATA_ORIGIN" )
	String dataOrigin

	
	/**
	 * Foreign Key : FK1_STVMAJR_INV_STVCIPC_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="STVMAJR_CIPC_CODE", referencedColumnName="STVCIPC_CODE")	
	])
	CIPCode	cipcCode

	
	public String toString() {
		"MajorMinorConcentration[id=$id, code=$code, description=$description, validMajorIndicator=$validMajorIndicator, validMinorIndicator=$validMinorIndicator, validConcentratnIndicator=$validConcentratnIndicator, lastModified=$lastModified, occupationIndicator=$occupationIndicator, aidEligibilityIndicator=$aidEligibilityIndicator, systemRequiredIndicator=$systemRequiredIndicator, voiceResponseMsgNumber=$voiceResponseMsgNumber, displayWebIndicator=$displayWebIndicator, sevisEquiv=$sevisEquiv, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:30)
		validMajorIndicator(nullable:true, maxSize:1)
		validMinorIndicator(nullable:true, maxSize:1)
		validConcentratnIndicator(nullable:true, maxSize:1)
		occupationIndicator(nullable:true, maxSize:1)
		aidEligibilityIndicator(nullable:true, maxSize:1)
		systemRequiredIndicator(nullable:true, maxSize:1)
		voiceResponseMsgNumber(nullable:true, maxSize:22)
		displayWebIndicator(nullable:true, maxSize:1)
		sevisEquiv(nullable:true, maxSize:6)
		dataOrigin(nullable:false, maxSize:30)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof MajorMinorConcentration)) return false;

        MajorMinorConcentration that = (MajorMinorConcentration) o;

        if (aidEligibilityIndicator != that.aidEligibilityIndicator) return false;
        if (cipcCode != that.cipcCode) return false;
        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (displayWebIndicator != that.displayWebIndicator) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (occupationIndicator != that.occupationIndicator) return false;
        if (sevisEquiv != that.sevisEquiv) return false;
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false;
        if (validConcentratnIndicator != that.validConcentratnIndicator) return false;
        if (validMajorIndicator != that.validMajorIndicator) return false;
        if (validMinorIndicator != that.validMinorIndicator) return false;
        if (version != that.version) return false;
        if (voiceResponseMsgNumber != that.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (validMajorIndicator != null ? validMajorIndicator.hashCode() : 0);
        result = 31 * result + (validMinorIndicator != null ? validMinorIndicator.hashCode() : 0);
        result = 31 * result + (validConcentratnIndicator != null ? validConcentratnIndicator.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (occupationIndicator != null ? occupationIndicator.hashCode() : 0);
        result = 31 * result + (aidEligibilityIndicator != null ? aidEligibilityIndicator.hashCode() : 0);
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0);
        result = 31 * result + (sevisEquiv != null ? sevisEquiv.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (cipcCode != null ? cipcCode.hashCode() : 0);
        return result;
    }
}
