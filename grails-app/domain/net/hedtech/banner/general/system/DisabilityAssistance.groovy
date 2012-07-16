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
package net.hedtech.banner.general.system

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
