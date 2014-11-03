/*******************************************************************************
Copyright 2014 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

/**
 * SQL Process Parameter Validation Table.
 */

@Entity
@Table(name = "GTVSQPA")
class SqlProcessParameter implements Serializable {

	/**
	 * Surrogate ID for GTVSQPA
	 */
	@Id
	@Column(name="GTVSQPA_SURROGATE_ID")
	@SequenceGenerator(name = "GTVSQPA_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSQPA_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSQPA_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GTVSQPA
	 */
	@Version
	@Column(name = "GTVSQPA_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * PARAMETER CODE: Parameter code.
	 */
	@Column(name = "GTVSQPA_CODE", nullable = false, unique = true, length = 30)
	String code

	/**
	 * PARAMETER DESCRIPTION: Description of the parameter code.
	 */
	@Column(name = "GTVSQPA_DESC", nullable = false, length = 60)
	String description

	/**
	 * DATA TYPE INDICATOR: Indicates whether this parameter is (C)haracter, (N)umber or (D)ate.
	 */
	@Column(name = "GTVSQPA_DATA_TYPE_CDE", nullable = false, length = 1)
	String dataType

	/**
	 * START DATE: Date code became effective.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "GTVSQPA_START_DATE", nullable = false)
	Date startDate

	/**
	 * END DATE: Date code became obsolete.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "GTVSQPA_END_DATE")
	Date endDate

	/**
	 * ACTIVITY DATE: The most recent date a record was created or updated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GTVSQPA_ACTIVITY_DATE")
	Date lastModified

	/**
	 * USER ID: The most recent user to create or update a record.
	 */
	@Column(name = "GTVSQPA_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for GTVSQPA
	 */
	@Column(name = "GTVSQPA_DATA_ORIGIN", length = 30)
	String dataOrigin



	public String toString() {
		"""SqlProcessParameter[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					dataType=$dataType,
					startDate=$startDate,
					endDate=$endDate,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
	}


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof SqlProcessParameter)) return false
	    SqlProcessParameter that = (SqlProcessParameter) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(dataType != that.dataType) return false
        if(startDate != that.startDate) return false
        if(endDate != that.endDate) return false
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
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0)
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0)
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		code(nullable:false, maxSize:30)
		description(nullable:false, maxSize:60)
		dataType(nullable:false, maxSize:1, inList:["C","N","D"])
		startDate(nullable:false)
		endDate(nullable:true)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]
}
