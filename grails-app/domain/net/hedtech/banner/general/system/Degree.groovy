/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Import Related Entities if they are external to this package
 */

/**
 * Degree Code Validation Table
 */

@Entity
@Table(name = "STVDEGC")
class Degree implements Serializable {

    /**
     * Surrogate ID for STVDEGC
     */
    @Id
    @Column(name = "STVDEGC_SURROGATE_ID")
    @SequenceGenerator(name = "STVDEGC_SEQ_GEN", allocationSize = 1, sequenceName = "STVDEGC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDEGC_SEQ_GEN")
    Long id

    /**
     * This field identifies the degree code referenced in the Recruiting, Admissions, Gen. Student, Registration, Academic History and Degree Audit Modules. 000000 - Degree Not Declared.
     */
    @Column(name = "STVDEGC_CODE", nullable = false, unique = true, length = 6)
    String code

    /**
     * This field specifies the degree (e.g. Bachelor of Business Administration, Master of Arts, Juris Doctor, etc.) associated with the degree code.
     */
    @Column(name = "STVDEGC_DESC", length = 30)
    String description

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVDEGC_LEVEL_ONE", length = 2)
    String levelOne

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVDEGC_LEVEL_TWO", length = 2)
    String levelTwo

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVDEGC_LEVEL_THREE", length = 2)
    String levelThree

    /**
     * This field indicates whether the degree will count toward financial aid. Y - Count in financial aid.
     */
    @Column(name = "STVDEGC_FA_COUNT_IND", length = 1)
    String financeCountIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVDEGC_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * System Required Indicator
     */
    @Column(name = "STVDEGC_SYSTEM_REQ_IND", length = 1)
    String systemRequiredIndicator

    /**
     * The Voice Response message number assigned to the recorded message that describes the degree code.
     */
    @Column(name = "STVDEGC_VR_MSG_NO", precision = 6)
    Integer voiceResponseMsgNumber

    /**
     * Web display indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVDEGC_DISP_WEB_IND", nullable = false, length = 1)
    Boolean displayWebIndicator

    /**
     * Version column which is used as a optimistic lock token for STVDEGC
     */
    @Version
    @Column(name = "STVDEGC_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVDEGC
     */
    @Column(name = "STVDEGC_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVDEGC
     */
    @Column(name = "STVDEGC_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVDEGC_INV_STVACAT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVDEGC_ACAT_CODE", referencedColumnName = "STVACAT_CODE")
    ])
    AwardCategory awardCatCode

    /**
     * Foreign Key : FK1_STVDEGC_INV_STVDLEV_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVDEGC_DLEV_CODE", referencedColumnName = "STVDLEV_CODE")
    ])
    DegreeLevel degreeLevelCode


    public String toString() {
        "Degree[id=$id, code=$code, description=$description, levelOne=$levelOne, levelTwo=$levelTwo, levelThree=$levelThree, financeCountIndicator=$financeCountIndicator, lastModified=$lastModified, systemRequiredIndicator=$systemRequiredIndicator, voiceResponseMsgNumber=$voiceResponseMsgNumber, displayWebIndicator=$displayWebIndicator, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        levelOne(nullable: true, maxSize: 2)
        levelTwo(nullable: true, maxSize: 2)
        levelThree(nullable: true, maxSize: 2)
        financeCountIndicator(nullable: true, maxSize: 1)
        lastModified(nullable: true)
        systemRequiredIndicator(nullable: true, maxSize: 1)
        voiceResponseMsgNumber(nullable: true)
        displayWebIndicator(nullable: false)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        awardCatCode(nullable: true)
        degreeLevelCode(nullable: true)

        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(degree_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof Degree)) return false

        Degree degree = (Degree) o

        if (awardCatCode != degree.awardCatCode) return false
        if (code != degree.code) return false
        if (dataOrigin != degree.dataOrigin) return false
        if (degreeLevelCode != degree.degreeLevelCode) return false
        if (description != degree.description) return false
        if (displayWebIndicator != degree.displayWebIndicator) return false
        if (financeCountIndicator != degree.financeCountIndicator) return false
        if (id != degree.id) return false
        if (lastModified != degree.lastModified) return false
        if (lastModifiedBy != degree.lastModifiedBy) return false
        if (levelOne != degree.levelOne) return false
        if (levelThree != degree.levelThree) return false
        if (levelTwo != degree.levelTwo) return false
        if (systemRequiredIndicator != degree.systemRequiredIndicator) return false
        if (version != degree.version) return false
        if (voiceResponseMsgNumber != degree.voiceResponseMsgNumber) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (levelOne != null ? levelOne.hashCode() : 0)
        result = 31 * result + (levelTwo != null ? levelTwo.hashCode() : 0)
        result = 31 * result + (levelThree != null ? levelThree.hashCode() : 0)
        result = 31 * result + (financeCountIndicator != null ? financeCountIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0)
        result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (awardCatCode != null ? awardCatCode.hashCode() : 0)
        result = 31 * result + (degreeLevelCode != null ? degreeLevelCode.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(degree_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
