
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
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type

/**
 * Integration Partner System Code Validation Table.
 */

@Entity
@Table(name="GTVINTP")
class IntegrationPartner implements Serializable {
	
	/**
	 * Surrogate ID for GTVINTP
	 */
	@Id
	@Column(name="GTVINTP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVINTP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVINTP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVINTP_SEQ_GEN")
    Long id

	/**
	 * PARTNER SYSTEM CODE: Used to define an external Integration Partner System. Valid Codes are 'WEBCT' for WebCT Integration or 'BB' for Blackboard Integration.
	 */
	@Column(name="GTVINTP_CODE", nullable = false, length=5)
	String code

	/**
	 * DESCRIPTION: Description of external Integration Partner System Code. For example 'WebCT Campus Edition / Vista' or 'Blackboard'.
	 */
	@Column(name="GTVINTP_DESC", nullable = false, length=30)
	String description

	/**
	 * USER ID: The unique identification of the user.
	 */
	@Column(name="GTVINTP_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * ACTIVITY DATE: The date that the information for the row was inserted or updated in the GTVINTP table.
	 */
	@Column(name="GTVINTP_activity_date")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * DATA ORIGIN: Source system that created or updated the row
	 */
	@Column(name="GTVINTP_DATA_ORIGIN", length=30)
	String dataOrigin

	/**
	 * Version column which is used as a optimistic lock token for GTVINTP
	 */
	@Version
	@Column(name="GTVINTP_VERSION", nullable = false, length=19)
	Long version

	
	
	public String toString() {
		"IntegrationPartner[id=$id, code=$code, description=$description, lastModifiedBy=$lastModifiedBy, lastModified=$lastModified, dataOrigin=$dataOrigin, version=$version]"
	}

	static constraints = {
		code(nullable:false, maxSize:5)
		description(nullable:false, maxSize:30)
		lastModifiedBy(nullable:true, maxSize:30)
		lastModified(nullable:true)
		dataOrigin(nullable:true, maxSize:30)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(integrationpartner_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof IntegrationPartner)) return false;

        IntegrationPartner that = (IntegrationPartner) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(integrationpartner_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
