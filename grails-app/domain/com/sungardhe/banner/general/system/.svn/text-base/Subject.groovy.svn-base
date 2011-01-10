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
 * Subject Validation Table
 */
@Entity
@Table(name="STVSUBJ")
class Subject implements Serializable {
	
	/**
	 * Surrogate ID for STVSUBJ
	 */
	@Id
	@Column(name="STVSUBJ_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the subject code referenced in the Catalog, Registration and Acad.  Hist.  Modules.
	 */
	@Column(name="STVSUBJ_CODE", nullable = false, length=4)
	String code

	/**
	 * This field specifies the subject associated with the subject code.
	 */
	@Column(name="STVSUBJ_DESC", length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.  
	 */
	@Column(name="STVSUBJ_activity_date")
	Date lastModified

	/**
	 * The Voice Response message number assigned to the recorded message that describes the subject code.
	 */
	@Column(name="STVSUBJ_VR_MSG_NO", length=22)
	BigDecimal vrMsgNo

	/**
	 * Web registration indicator
	 */
	@Type(type = "yes_no")
	@Column(name="STVSUBJ_DISP_WEB_IND", nullable = false)
	Boolean dispWebInd

	/**
	 * Column for storing last modified by for STVSUBJ
	 */
	@Column(name="STVSUBJ_USER_ID" )
	String lastModifiedBy

	/**
	 * Optimistic Lock Token for STVSUBJ
	 */
	@Version
	@Column(name="STVSUBJ_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for STVSUBJ
	 */
	@Column(name="STVSUBJ_DATA_ORIGIN" )
	String dataOrigin

	
	
	public String toString() {
		"Subject[id=$id, code=$code, description=$description, lastModified=$lastModified, vrMsgNo=$vrMsgNo, dispWebInd=$dispWebInd, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:true, maxSize:30)
		vrMsgNo(nullable:true, maxSize:22)
		dispWebInd(nullable:false, maxSize:1)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (code != subject.code) return false;
        if (dataOrigin != subject.dataOrigin) return false;
        if (description != subject.description) return false;
        if (dispWebInd != subject.dispWebInd) return false;
        if (id != subject.id) return false;
        if (lastModified != subject.lastModified) return false;
        if (lastModifiedBy != subject.lastModifiedBy) return false;
        if (version != subject.version) return false;
        if (vrMsgNo != subject.vrMsgNo) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (vrMsgNo != null ? vrMsgNo.hashCode() : 0);
        result = 31 * result + (dispWebInd != null ? dispWebInd.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
