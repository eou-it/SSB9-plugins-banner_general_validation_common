
/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.Type
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.GenerationType

/**
 * Person Hold Type Validation Table
 */

@Entity
@Table(name="STVHLDD")
class HoldType implements Serializable {

    /**
	 * Surrogate ID for STVHLDD
	 */
	@Id
	@Column(name="STVHLDD_SURROGATE_ID")
    @SequenceGenerator(name = "STVHLDD_SEQ_GEN", allocationSize = 1, sequenceName = "STVHLDD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVHLDD_SEQ_GEN")
	Integer id

    /**
	 * Optimistic lock token for STVHLDD
	 */
	@Version
	@Column(name="STVHLDD_VERSION")
	Integer version

	/**
	 * This field identifies the hold type code referenced in the Academic History Module and cross-referenced by the Hold Form (SOAHOLD) and Hold Query-Only Form (SOQHOLD).
	 */
	@Column(name="STVHLDD_CODE")
	String code

	/**
	 * This field indicates whether the hold type prevents registration.
	 */
	@Column(name="STVHLDD_REG_HOLD_IND")
	String registrationHoldIndicator

	/**
	 * This field indicates whether the hold type prevents transcript generation.
	 */
	@Column(name="STVHLDD_TRANS_HOLD_IND")
	String transactionHoldIndicator

	/**
	 * This field indicates whether the hold type prevents graduation.
	 */
	@Column(name="STVHLDD_GRAD_HOLD_IND")
	String gradHoldIndicator

	/**
	 * This field indicates whether the hold type prevents grade report generation.
	 */
	@Column(name="STVHLDD_GRADE_HOLD_IND")
	String gradeHoldIndicator

	/**
	 * This field specifies the hold type (e.g. registrars hold, library fine, financial hold, etc.) associated with the hold type code.
	 */
	@Column(name="STVHLDD_DESC")
	String description

	/**
	 * Accounts Receivable Hold Indicator.
	 */
	@Column(name="STVHLDD_AR_HOLD_IND")
	String accountsReceivableHoldIndicator

	/**
	 * Enrollment verification hold indicator
	 */
	@Column(name="STVHLDD_ENV_HOLD_IND")
	String enrollmentVerificationHoldIndicator

	/**
	 * The Voice Response message number assigned to the recorded message that describes the hold type code.
	 */
	@Column(name="STVHLDD_VR_MSG_NO")
	BigDecimal voiceResponseMessageNumber

	/**
	 * Web display indicator
	 */
    @Type(type = "yes_no")
	@Column(name="STVHLDD_DISP_WEB_IND")
	Boolean displayWebIndicator = false

	/**
	 * Indicates application hold which will prevent creation of application.
	 */
    @Type(type = "yes_no")
	@Column(name="STVHLDD_APPLICATION_HOLD_IND")
	Boolean applicationHoldIndicator = false

	/**
	 * This field indicates whether the hold type prevents compliance.
	 */
    @Type(type = "yes_no")
	@Column(name="STVHLDD_COMPLIANCE_HOLD_IND")
	Boolean complianceHoldIndicator = false

    /**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVHLDD_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Last modified by column for STVHLDD
	 */
	@Column(name="STVHLDD_USER_ID")
	String lastModifiedBy

	/**
	 * Data origin column for STVHLDD
	 */
	@Column(name="STVHLDD_DATA_ORIGIN")
	String dataOrigin


	public String toString() {
		"""HoldType[
		            id=$id,
		            version=$version,
					code=$code, 
					registrationHoldIndicator=$registrationHoldIndicator, 
					transactionHoldIndicator=$transactionHoldIndicator, 
					gradHoldIndicator=$gradHoldIndicator, 
					gradeHoldIndicator=$gradeHoldIndicator, 
					description=$description, 
					accountsReceivableHoldIndicator=$accountsReceivableHoldIndicator,
					enrollmentVerificationHoldIndicator=$enrollmentVerificationHoldIndicator,
					voiceResponseMessageNumber=$voiceResponseMessageNumber,
					displayWebIndicator=$displayWebIndicator, 
					applicationHoldIndicator=$applicationHoldIndicator, 
					complianceHoldIndicator=$complianceHoldIndicator,
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin
					]"""
	}


    static constraints = {
        code(nullable:false, maxSize:2)
        registrationHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        transactionHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        gradHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        gradeHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        description(nullable:true, maxSize:30)
        accountsReceivableHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        enrollmentVerificationHoldIndicator(nullable:true, maxSize:1, inList:["Y"])
        voiceResponseMessageNumber(nullable:true, scale:0)
        displayWebIndicator(nullable:false)
        applicationHoldIndicator(nullable:false)
        complianceHoldIndicator(nullable:false)
        lastModified(nullable:true)
        lastModifiedBy(nullable:true, maxSize:30)
        dataOrigin(nullable:true, maxSize:30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(holdtype_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof HoldType)) return false
	    HoldType that = (HoldType) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(registrationHoldIndicator != that.registrationHoldIndicator) return false
        if(transactionHoldIndicator != that.transactionHoldIndicator) return false
        if(gradHoldIndicator != that.gradHoldIndicator) return false
        if(gradeHoldIndicator != that.gradeHoldIndicator) return false
        if(description != that.description) return false
        if(accountsReceivableHoldIndicator != that.accountsReceivableHoldIndicator) return false
        if(enrollmentVerificationHoldIndicator != that.enrollmentVerificationHoldIndicator) return false
        if(voiceResponseMessageNumber != that.voiceResponseMessageNumber) return false
        if(displayWebIndicator != that.displayWebIndicator) return false
        if(applicationHoldIndicator != that.applicationHoldIndicator) return false
        if(complianceHoldIndicator != that.complianceHoldIndicator) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        return true
    }

	
	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (registrationHoldIndicator != null ? registrationHoldIndicator.hashCode() : 0)
        result = 31 * result + (transactionHoldIndicator != null ? transactionHoldIndicator.hashCode() : 0)
        result = 31 * result + (gradHoldIndicator != null ? gradHoldIndicator.hashCode() : 0)
        result = 31 * result + (gradeHoldIndicator != null ? gradeHoldIndicator.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (accountsReceivableHoldIndicator != null ? accountsReceivableHoldIndicator.hashCode() : 0)
        result = 31 * result + (enrollmentVerificationHoldIndicator != null ? enrollmentVerificationHoldIndicator.hashCode() : 0)
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0)
        result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0)
        result = 31 * result + (applicationHoldIndicator != null ? applicationHoldIndicator.hashCode() : 0)
        result = 31 * result + (complianceHoldIndicator != null ? complianceHoldIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}


    public static readonlyProperties = [ 'code',  ]
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(holdtype_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
