/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.
 CONFIDENTIAL BUSINESS INFORMATION
 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Represents a service that could be provided to a disabled student.
 */
@Entity
@Table(name = "STVSPSR")
class DisabilityAssistance  implements Serializable {
	
	/**
	 * The unique identifier for this domain object.
	 */
	@Id
    @SequenceGenerator(name = "STVSPSR_SEQ_GEN", allocationSize = 1, sequenceName = "STVSPSR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSPSR_SEQ_GEN")
    @Column(name="STVSPSR_SURROGATE_ID")
	Long id
	
	/**
	 * Date that this domain object was last modified.
	 */
	@Column(name="STVSPSR_activity_date")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified
	
	/**
	 * Id of person to last modify this domain object.
	 */
	@Column(name="STVSPSR_USER_ID" )
	String lastModifiedBy
	
	/**
	 * Source system that created or updated this domain object.
	 */
	@Column(name="STVSPSR_DATA_ORIGIN", length = 30)
	String dataOrigin
	
	/**
	 * Optimistic lock token.
	 */
	@Version
	@Column(name="STVSPSR_VERSION",nullable = false)
	Long version
	
	/**
	 * Disability service code.
	 */
	@Column(name="STVSPSR_CODE",nullable = false, length =2)
	String code
	
	/**
	 * Disability service description.
	 */
	@Column(name="STVSPSR_DESC",nullable = false, length = 30)
	String description
	
	
    public String toString() {
        "DisabilityAssistance[id=$id, code=$code, description=$description, dataOrigin=$dataOrigin, version=$version, lastModifiedBy=$lastModifiedBy, lastModified=$lastModified]"
    }
	
	
	static constraints = {
		
		code(maxSize:2, blank:false)
		description(maxSize: 30, blank:false)
		dataOrigin(nullable:true)
		
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof DisabilityAssistance)) return false;

        DisabilityAssistance that = (DisabilityAssistance) o;

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
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
