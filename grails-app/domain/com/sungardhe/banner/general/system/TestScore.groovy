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
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Test Score Validation Table
 */
@Entity
@Table(name = "STVTESC")
class TestScore implements Serializable {

    /**
     * Surrogate ID for STVTESC
     */
    @Id
    @Column(name = "stvtesc_surrogate_id")
    @SequenceGenerator(name = "STVTESC_SEQ_GEN", allocationSize = 1, sequenceName = "STVTESC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVTESC_SEQ_GEN")
    Long id

    /**
     * This field identifies the test code referenced on the Admissions Decision (SAADCRV) and Admissions Decision Rules (SAADECN) Forms and by SOATEST and SOABGIY.
     */
    @Column(name = "STVTESC_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * This field specifies the test (e.g.  SAT Verbal, ACT Composite, Graduate Record Exam) associated with the test code.
     */
    @Column(name = "STVTESC_DESC", length = 30)
    String description

    /**
     * This field specifies the number of positions of the score for the associated test.  Valid are values 1 through 5.
     */
    @Column(name = "STVTESC_NO_POSITIONS", nullable = false, precision = 1)
    Integer numberPositions

    /**
     * This field identifies the data type (e.g.  alphanumeric, numeric) for the associated test scores.
     */
    @Column(name = "STVTESC_DATA_TYPE", nullable = false, length = 1)
    String dataType

    /**
     * This field identifies the value of the minimum score for the associated test.
     */
    @Column(name = "STVTESC_MIN_VALUE", length = 5)
    String minimumValue

    /**
     * This field identifies the value of the maximum score for the associated test.
     */
    @Column(name = "STVTESC_MAX_VALUE", length = 5)
    String maximumValue

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVTESC_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * System Required Indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVTESC_SYSTEM_REQ_IND", length = 1)
    Boolean systemRequiredIndicator

    /**
     * The Management Information System code
     */
    @Column(name = "STVTESC_MIS_CODE", length = 6)
    String managementInformationSystemCode

    /**
     * The Assessment Instrument Data code
     */
    @Column(name = "STVTESC_ASSESSMENT_FORM", length = 1)
    String assessmentForm

    /**
     * The Voice Response message number assigned to the recorded message that describes the test score code.
     */
    @Column(name = "STVTESC_VR_MSG_NO", precision = 6)
    Integer voiceResponseMessageNumber

    /**
     * Version column which is used as a optimistic lock token for STVTESC
     */
    @Version
    @Column(name = "STVTESC_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Last Modified By column for STVTESC
     */
    @Column(name = "STVTESC_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVTESC
     */
    @Column(name = "STVTESC_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVTESC_INV_STVADMR_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVTESC_ADMR_CODE", referencedColumnName = "STVADMR_CODE")
    ])
    AdmissionRequest admissionRequest


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
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 30)
        numberPositions(nullable: false, max: 9)
        dataType(nullable: false, maxSize: 1)
        minimumValue(nullable: true, maxSize: 5)
        maximumValue(nullable: true, maxSize: 5)
        systemRequiredIndicator(nullable: true)
        managementInformationSystemCode(nullable: true, maxSize: 6)
        assessmentForm(nullable: true, maxSize: 1)
        voiceResponseMessageNumber(nullable: true, max: 999999)
        admissionRequest(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof TestScore)) return false

        TestScore testScore = (TestScore) o

        if (admissionRequest != testScore.admissionRequest) return false
        if (assessmentForm != testScore.assessmentForm) return false
        if (code != testScore.code) return false
        if (dataOrigin != testScore.dataOrigin) return false
        if (dataType != testScore.dataType) return false
        if (description != testScore.description) return false
        if (id != testScore.id) return false
        if (lastModified != testScore.lastModified) return false
        if (lastModifiedBy != testScore.lastModifiedBy) return false
        if (managementInformationSystemCode != testScore.managementInformationSystemCode) return false
        if (maximumValue != testScore.maximumValue) return false
        if (minimumValue != testScore.minimumValue) return false
        if (numberPositions != testScore.numberPositions) return false
        if (systemRequiredIndicator != testScore.systemRequiredIndicator) return false
        if (version != testScore.version) return false
        if (voiceResponseMessageNumber != testScore.voiceResponseMessageNumber) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (numberPositions != null ? numberPositions.hashCode() : 0)
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0)
        result = 31 * result + (minimumValue != null ? minimumValue.hashCode() : 0)
        result = 31 * result + (maximumValue != null ? maximumValue.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (managementInformationSystemCode != null ? managementInformationSystemCode.hashCode() : 0)
        result = 31 * result + (assessmentForm != null ? assessmentForm.hashCode() : 0)
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (admissionRequest != null ? admissionRequest.hashCode() : 0)
        return result
    }
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(testscore_custom_methods) ENABLED START*/



    /*PROTECTED REGION END*/
}
