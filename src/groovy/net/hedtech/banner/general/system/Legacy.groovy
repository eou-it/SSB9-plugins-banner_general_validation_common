
/*******************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Transient
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type


/**
 * Legacy Code Validation Table
 */
@Entity
@Table(name = "STVLGCY")
class Legacy implements Serializable {
	
	/**
	 * Surrogate ID for STVLGCY
	 */
	@Id
	@Column(name="STVLGCY_SURROGATE_ID")
	@SequenceGenerator(name ="STVLGCY_SEQ_GEN", allocationSize =1, sequenceName  ="STVLGCY_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="STVLGCY_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for STVLGCY
	 */
	@Version
	@Column(name = "STVLGCY_VERSION")
	Long version

	/**
	 * This field identifies the legacy code referenced in the Gen. Person, Recruit., and Admissions Modules.
	 */
	@Column(name = "STVLGCY_CODE")
	String code

	/**
	 * This field specifies the relationship of the legacy (alumnus relative) to the   applicant.
	 */
	@Column(name = "STVLGCY_DESC")
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STVLGCY_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Last modified by column for STVLGCY
	 */
	@Column(name = "STVLGCY_USER_ID")
	String lastModifiedBy

	/**
	 * Data origin column for STVLGCY
	 */
	@Column(name = "STVLGCY_DATA_ORIGIN")
	String dataOrigin

	
	
	public String toString() {
		"""Legacy[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof Legacy)) return false
	    Legacy that = (Legacy) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
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
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:1)
		description(nullable:true, maxSize:30)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
    }
    
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]

}
