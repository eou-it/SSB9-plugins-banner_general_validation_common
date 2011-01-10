
/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

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
import org.hibernate.annotations.GenericGenerator

/**
 * Building Code Validation Table
 */
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: STVBLDG
  *  stvbldg_code

*/
@Entity
@Table(name="STVBLDG")
class Building implements Serializable {
	
	/**
	 * Surrogate ID for STVBLDG
	 */
	@Id
	@Column(name="STVBLDG_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the building code referenced in the Class Schedule and Registration Modules.
	 */
	@Column(name="STVBLDG_CODE", nullable = false, length=6)
	String code

	/**
	 * This field specifies the building associated with the building code.
	 */
	@Column(name="STVBLDG_DESC", nullable = false, length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated .
	 */
	@Column(name="STVBLDG_activity_date")
	Date lastModified

	/**
	 * The Voice Response message number assigned to the recorded message that describes the building code.
	 */
	@Column(name="STVBLDG_VR_MSG_NO", length=22)
	BigDecimal voiceResponseMsgNumber

	/**
	 * Version column which is used as a optimistic lock token for STVBLDG
	 */
	@Version
	@Column(name="STVBLDG_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for STVBLDG
	 */
	@Column(name="STVBLDG_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVBLDG
	 */
	@Column(name="STVBLDG_DATA_ORIGIN", length=30)
	String dataOrigin

	
	
	public String toString() {
		"Building[id=$id, code=$code, description=$description, lastModified=$lastModified, voiceResponseMsgNumber=$voiceResponseMsgNumber, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:6)
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		voiceResponseMsgNumber(nullable:true, maxSize:22, scale:0)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(building_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (code != building.code) return false;
        if (dataOrigin != building.dataOrigin) return false;
        if (description != building.description) return false;
        if (id != building.id) return false;
        if (lastModified != building.lastModified) return false;
        if (lastModifiedBy != building.lastModifiedBy) return false;
        if (version != building.version) return false;
        if (voiceResponseMsgNumber != building.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(building_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
