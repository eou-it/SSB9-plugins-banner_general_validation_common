
/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/

package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

/**
 * Department Validation Table
 */
@Entity
@Table (name = "STVDEPT")
class Department implements Serializable {
  
    /**
	 * Surrogate ID for STVDEPT
	 */
    @Id
    @GeneratedValue (generator = "triggerAssigned")
    @GenericGenerator (name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
    @Column (name = "STVDEPT_SURROGATE_ID")
    Long id

    /**
	 * This field identifies the most recent date a record was created or updated.
	 */
    @Column (name="STVDEPT_activity_date")
    Date lastModified

    /**
	 * Column for storing last modified by for STVDEPT
	 */
    @Column (name="STVDEPT_USER_ID")
    String lastModifiedBy

    /**
	 * Column for storing data origin for STVDEPT
	 */
    @Column (name="STVDEPT_DATA_ORIGIN", nullable = true, length = 30)
    String dataOrigin

    /**
	 * Optimistic Lock Token for STVDEPT
	 */
    @Version
    @Column(name="STVDEPT_VERSION", nullable = false, length=19)
    Long version

    /**
	 * This field identifies the department code.
	 */
    @Column(name="STVDEPT_CODE", nullable = false, length=2)
    String code

    /**
	 * This field specifies the description of the college.
	 */
	@Column(name="STVDEPT_DESC", nullable = false, length=30)
    String description

    /**
	 * System Required Indicator
	 */
    @Column (name="STVDEPT_SYSTEM_REQ_IND", nullable = true, length = 1)
    String systemRequiredIndicator

    /**
	 * The Voice Response message number assigned to the recorded message that describes the department code.
	 */
    @Column (name="STVDEPT_VR_MSG_NO", nullable = true, length = 6)
    Integer voiceResponseMessageNumber

    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList:['Y','N'])
        voiceResponseMessageNumber(nullable: true, maxSize: 6)
        lastModified(nullable:true)
		lastModifiedBy(nullable: true, maxSize: 30)
		dataOrigin(nullable: true, maxSize: 30)
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Department)) return false;

        Department that = (Department) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false;
        if (version != that.version) return false;
        if (voiceResponseMessageNumber != that.voiceResponseMessageNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0);
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0);
        return result;
    }
}
