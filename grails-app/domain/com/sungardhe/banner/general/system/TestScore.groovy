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
import org.hibernate.annotations.Type

/**
 * Test Score Validation Table
 */
@Entity
@Table(name="STVTESC")
class TestScore implements Serializable {
	
	/**
	 * Surrogate ID for STVTESC
	 */
	@Id
	@Column(name="stvtesc_surrogate_id")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id
	
	/**
	 * This field identifies the test code referenced on the Admissions Decision (SAADCRV) and Admissions Decision Rules (SAADECN) Forms and by SOATEST and SOABGIY.
	 */
	@Column(name="stvtesc_code", nullable = false, length=4)
	String code
	
	/**
	 * This field specifies the test (e.g.  SAT Verbal, ACT Composite, Graduate Record Exam) associated with the test code.
	 */
	@Column(name="stvtesc_desc", length=30)
	String description
	
	/**
	 * This field specifies the number of positions of the score for the associated test.  Valid are values 1 through 5.
	 */
	@Column(name="stvtesc_no_positions", nullable = false, length=1)
	Integer numberPositions
	
	/**
	 * This field identifies the data type (e.g.  alphanumeric, numeric) for the associated test scores.  
	 */
	@Column(name="stvtesc_data_type", nullable = false, length=1)
	String dataType
	
	/**
	 * This field identifies the value of the minimum score for the associated test.  
	 */
	@Column(name="stvtesc_min_value", length=5)
	String minimumValue
	
	/**
	 * This field identifies the value of the maximum score for the associated test.  
	 */
	@Column(name="stvtesc_max_value", length=5)
	String maximumValue
	
	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="stvtesc_activity_date", nullable = true)
	Date lastModified
	
	/**
	 * System Required Indicator
	 */
	@Type(type = "yes_no")
	@Column(name="stvtesc_system_req_ind", length=1)
	Boolean systemRequiredIndicator
	
	/**
	 * The Management Information System code
	 */
	@Column(name="stvtesc_mis_code", length=6)
	String managementInformationSystemCode
	
	/**
	 * The Assessment Instrument Data code
	 */
	@Column(name="stvtesc_assessment_form", length=1)
	String assessmentForm
	
	/**
	 * The Voice Response message number assigned to the recorded message that describes the test score code.
	 */
	@Column(name="stvtesc_vr_msg_no", length=6)
	Integer voiceResponseMessageNumber
	
	/**
	 * Version column which is used as a optimistic lock token for STVTESC
	 */
	@Version
	@Column(name="stvtesc_version", nullable = false, length=19)
	Long version
	
	/**
	 * Last Modified By column for STVTESC
	 */
	@Column(name="stvtesc_user_id", nullable = true, length=30)
	String lastModifiedBy
	
	/**
	 * Data Origin column for STVTESC
	 */
	@Column(name="stvtesc_data_origin", nullable = true, length=30)
	String dataOrigin
	
	/**
	 * This field identifies admissions materials required with the associated test.  (Refer to Admissions Checklist Validation Form (STVADMR).
	 */
	@Column(name="stvtesc_admr_code", length=4)
	String admissionRequest
	
	
	public String toString() {
		"TestScore[id=$id, " +
		"code=$code, " +
		"description=$description," +
		"numberpositions=$numberPositions," +
		"datatype=$dataType," +
		"minimumvalue=$minimumValue, " +
		"maximumvalue=$maximumValue, " +
		"lastmodified=$lastModified, " +
		"systemrequiredindicator=$systemRequiredIndicator, " +
		"miscode=$managementInformationSystemCode, " +
		"assessmentform=$assessmentForm, " +
		"voiceresponsemsgnumber=$voiceResponseMessageNumber, " +
		"version=$version, " +
		"lastmodifiedby=$lastModifiedBy, " +
		"dataorigin=$dataOrigin" +
		"admissionRequest=$admissionRequest]"
	}
	
	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:30)
		numberPositions(nullable:false, max:9)
		dataType(nullable:false, maxSize:1)
		minimumValue(nullable:true, maxSize:5)
		maximumValue(nullable:true, maxSize:5)
		systemRequiredIndicator(nullable:true, maxSize:1)
		managementInformationSystemCode(nullable:true, maxSize:6)
		assessmentForm(nullable:true, maxSize:1)
		voiceResponseMessageNumber(nullable:true, max:999999)
		admissionRequest(nullable:true, maxSize:4)
		
		dataOrigin(      nullable: true            )
        lastModified(    nullable: true            )
        lastModifiedBy(  nullable: true            )
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof TestScore)) return false;

        TestScore testScore = (TestScore) o;

        if (admissionRequest != testScore.admissionRequest) return false;
        if (assessmentForm != testScore.assessmentForm) return false;
        if (code != testScore.code) return false;
        if (dataOrigin != testScore.dataOrigin) return false;
        if (dataType != testScore.dataType) return false;
        if (description != testScore.description) return false;
        if (id != testScore.id) return false;
        if (lastModified != testScore.lastModified) return false;
        if (lastModifiedBy != testScore.lastModifiedBy) return false;
        if (managementInformationSystemCode != testScore.managementInformationSystemCode) return false;
        if (maximumValue != testScore.maximumValue) return false;
        if (minimumValue != testScore.minimumValue) return false;
        if (numberPositions != testScore.numberPositions) return false;
        if (systemRequiredIndicator != testScore.systemRequiredIndicator) return false;
        if (version != testScore.version) return false;
        if (voiceResponseMessageNumber != testScore.voiceResponseMessageNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (numberPositions != null ? numberPositions.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (minimumValue != null ? minimumValue.hashCode() : 0);
        result = 31 * result + (maximumValue != null ? maximumValue.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
        result = 31 * result + (managementInformationSystemCode != null ? managementInformationSystemCode.hashCode() : 0);
        result = 31 * result + (assessmentForm != null ? assessmentForm.hashCode() : 0);
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (admissionRequest != null ? admissionRequest.hashCode() : 0);
        return result;
    }
}