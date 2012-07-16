
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

package net.hedtech.banner.general.system;

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
 * Describes a disability.
 *
 */
@Entity
@Table(name = "STVDISA")
class Disability  implements Serializable {
	
	/**
	 * The unique identifier for this domain object.
	 */
	@Id
    @SequenceGenerator(name = "STVDISA_SEQ_GEN", allocationSize = 1, sequenceName = "STVDISA_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDISA_SEQ_GEN")
    @Column(name="STVDISA_SURROGATE_ID")
	Long id
	
	/**
	 * Date that this domain object was last modified.
	 */
	@Column(name="STVDISA_activity_date")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified
	
	/**
	 * Id of person to last modify this domain object.
	 */
	@Column(name="STVDISA_USER_ID")
	String lastModifiedBy
	
	/**
	 * Source system that created or updated this domain object.
	 */
	@Column(name="STVDISA_DATA_ORIGIN", length = 30)
	String dataOrigin
	
    /**
     * Version column which is used as a optimistic lock token for STVBLCK
     */
    @Version
    @Column(name="STVDISA_VERSION", nullable = false, length=19)
    Long version
  
	/**
	 * Disability code.
	 */
	@Column(name="STVDISA_CODE", nullable = false, length = 2)
	String code
	
	/**
	 * Disability description.
	 */
	@Column(name="STVDISA_DESC", nullable = false, length = 30)
	String description
	
	
	public String toString() {
	    "$description ($code)"
	}
	
		
	static constraints = {
		code(nullable: false, maxSize:2)
		description(nullable: false, maxSize: 30)
		lastModified(nullable:true)
		lastModifiedBy(nullable: true, maxSize:30)
		dataOrigin(nullable: true, maxSize:30)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Disability)) return false;

        Disability that = (Disability) o;

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
