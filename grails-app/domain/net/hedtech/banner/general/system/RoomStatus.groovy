/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

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
 * Room Status Code Validation Table
 */

@Entity
@Table(name = "STVRMST")
class RoomStatus implements Serializable {
	
	/**
	 * Surrogate ID for STVRMST
	 */
	@Id
	@Column(name="STVRMST_SURROGATE_ID")
    @SequenceGenerator(name = "STVRMST_SEQ_GEN", allocationSize = 1, sequenceName = "STVRMST_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVRMST_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for STVRMST
	 */
	@Version
	@Column(name = "STVRMST_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * Room Status Code
	 */
	@Column(name = "STVRMST_CODE", nullable = false, unique = true, length = 2)
	String code

	/**
	 * Description of Room Status Code
	 */
	@Column(name = "STVRMST_DESC", length = 30)
	String description

	/**
	 * Inactive Indicator.
	 */
	@Column(name = "STVRMST_INACT_IND", length = 1)
	String inactiveIndicator

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name = "STVRMST_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Last modified by column for STVRMST
	 */
	@Column(name = "STVRMST_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for STVRMST
	 */
	@Column(name = "STVRMST_DATA_ORIGIN", length = 30)
	String dataOrigin

	
	
	public String toString() {
		"""RoomStatus[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					inactiveIndicator=$inactiveIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof RoomStatus)) return false
	    RoomStatus that = (RoomStatus) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(inactiveIndicator != that.inactiveIndicator) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        return true
    }

	
	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (inactiveIndicator != null ? inactiveIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:2)
		description(nullable:true, maxSize:30)
		inactiveIndicator(nullable:true, maxSize:1, inList:["Y"])
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
    }
    
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]

}
