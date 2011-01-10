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
 * Student Level Validation Table
 */
@Entity
@Table(name="STVLEVL")
class Level implements Serializable {

	@Id
	@Column(name="STVLEVL_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the student level code referenced in the Catalog, Recruiting, Admissions, Gen Student, Registration, and Acad Hist Modules. Required value: 00 - Level Not Declared.
	 */
	@Column(name="STVLEVL_CODE", nullable = false, length=2)
	String code

	/**
	 * This field specifies the student level (e.g. undergraduate, graduate, professional) associated with the student level code.
	 */
	@Column(name="STVLEVL_DESC", nullable = false, length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVLEVL_activity_date")
	Date lastModified

	/**
	 * This field is not currently in use.
	 */
	@Type(type = "yes_no")
	@Column(name="STVLEVL_ACAD_IND")
	Boolean acadInd

	/**
	 * Continuing Education Indicator.
	 */
	@Type(type = "yes_no")
	@Column(name="STVLEVL_CEU_IND", nullable = false)
	Boolean ceuInd

	/**
	 * System Required Indicator
	 */
	@Type(type = "yes_no")
	@Column(name="STVLEVL_SYSTEM_REQ_IND")
	Boolean systemReqInd

	/**
	 * The Voice Response message number assigned to the recorded message that describes the student level.
	 */
	@Column(name="STVLEVL_VR_MSG_NO", length=22)
	BigDecimal vrMsgNo

	/**
	 * EDI Level Code
	 */
	@Column(name="STVLEVL_EDI_EQUIV", length=2)
	String ediEquiv

	/**
	 * Column for storing last modified by for STVLEVL
	 */
	@Column(name="STVLEVL_USER_ID" )
	String lastModifiedBy

	/**
	 * Optimistic Lock Token for STVLEVL
	 */
	@Version
	@Column(name="STVLEVL_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for STVLEVL
	 */
	@Column(name="STVLEVL_DATA_ORIGIN", length=30)
	String dataOrigin


	public String toString() {
		"Level[id=$id, code=$code, description=$description, lastModified=$lastModified, acadInd=$acadInd, ceuInd=$ceuInd, systemReqInd=$systemReqInd, vrMsgNo=$vrMsgNo, ediEquiv=$ediEquiv, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
	}


	static constraints = {
		code(nullable:false, maxSize:2)
		description(nullable:false, maxSize:30)
		acadInd(nullable:true, maxSize:1)
		ceuInd(nullable:false, maxSize:1)
		systemReqInd(nullable:true, maxSize:1)
		vrMsgNo(nullable:true, maxSize:22)
		ediEquiv(nullable:true, maxSize:2)
	}

    public static Object fetchLevelForCEU(String level) {
         def returnObj = Level.findByCode(level)
         return returnObj
     }


     public static Object fetchLevelForCEU(String level, params) {
           def ceu = new Boolean(params.ceu)
           def returnObj = Level.findByCodeAndCeuInd(level, ceu)
     }


    //List of below methods is used for Lookup in Default Calendar rules component.
    /**
     * fetchAllByTerm fetches all the SectionCensusInformationBase object for the given term code.
     */
    public static Object fetchAllByCeuIndAndCodeLike( Map params ) {
        def ceu = new Boolean(params.ceu)
       return [list: Level.findAllByCeuInd(ceu)]
    }


    /**
     * fetchAllByTerm fetches all the SectionCensusInformationBase object for the given term code and academicCalendarTypecode filter.
     */
    public static Object fetchAllByCeuIndAndCodeLike( String filter, Map params ) {
        def ceu = new Boolean(params.ceu)
        return [list: Level.findAllByCeuIndAndCodeLike(ceu, filter+"%" )]
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Level)) return false;

        Level level = (Level) o;

        if (acadInd != level.acadInd) return false;
        if (ceuInd != level.ceuInd) return false;
        if (code != level.code) return false;
        if (dataOrigin != level.dataOrigin) return false;
        if (description != level.description) return false;
        if (ediEquiv != level.ediEquiv) return false;
        if (id != level.id) return false;
        if (lastModified != level.lastModified) return false;
        if (lastModifiedBy != level.lastModifiedBy) return false;
        if (systemReqInd != level.systemReqInd) return false;
        if (version != level.version) return false;
        if (vrMsgNo != level.vrMsgNo) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (acadInd != null ? acadInd.hashCode() : 0);
        result = 31 * result + (ceuInd != null ? ceuInd.hashCode() : 0);
        result = 31 * result + (systemReqInd != null ? systemReqInd.hashCode() : 0);
        result = 31 * result + (vrMsgNo != null ? vrMsgNo.hashCode() : 0);
        result = 31 * result + (ediEquiv != null ? ediEquiv.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
