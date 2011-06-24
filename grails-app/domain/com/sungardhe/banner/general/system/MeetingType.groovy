
/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

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
import org.hibernate.annotations.Type
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator

/**
 * Meeting Type Code validation table
 */

@Entity
@Table(name="GTVMTYP")
class MeetingType implements Serializable {
	
	/**
	 * Surrogate ID for GTVMTYP
	 */
	@Id
	@Column(name="GTVMTYP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVMTYP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVMTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVMTYP_SEQ_GEN")
	Long id

	/**
	 * Meeting Type Code: The Meeting Type Code
	 */
	@Column(name="GTVMTYP_CODE", nullable = false, length=4)
	String code

	/**
	 * Description: Description of the Meeting Type code
	 */
	@Column(name="GTVMTYP_DESC", nullable = false, length=30)
	String description

	/**
	 * System Required Indicator: Indicates whether or not this record is required to exist on the database
	 */
	@Type(type = "yes_no")
	@Column(name="GTVMTYP_SYS_REQ_IND", nullable = false)
	Boolean systemRequiredIndicator

	/**
	 * Activty Date: Date this record entered or last updated
	 */
	@Column(name="GTVMTYP_activity_date")
	Date lastModified

	/**
	 * User ID: The username of the person who entered or last updated this record
	 */
	@Column(name="GTVMTYP_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Voice Response Message Number
	 */
	@Column(name="GTVMTYP_VR_MSG_NO", length=22)
	BigDecimal voiceResponseMsgNumber

	/**
	 * Version column which is used as a optimistic lock token for GTVMTYP
	 */
	@Version
	@Column(name="GTVMTYP_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Data Origin column for GTVMTYP
	 */
	@Column(name="GTVMTYP_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"MeetingType[id=$id, code=$code, description=$description, systemRequiredIndicator=$systemRequiredIndicator, lastModified=$lastModified, lastModifiedBy=$lastModifiedBy, voiceResponseMsgNumber=$voiceResponseMsgNumber, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:4)
		description(nullable:false, maxSize:30)
		systemRequiredIndicator(nullable:false)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		voiceResponseMsgNumber(nullable:true, maxSize:22, scale:0)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(meetingtype_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof MeetingType)) return false;

        MeetingType that = (MeetingType) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false;
        if (version != that.version) return false;
        if (voiceResponseMsgNumber != that.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(meetingtype_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
