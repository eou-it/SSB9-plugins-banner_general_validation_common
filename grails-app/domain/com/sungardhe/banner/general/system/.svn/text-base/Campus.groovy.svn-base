
package com.sungardhe.banner.general.system

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

/**
 * Campus Validation Table
 */
@Entity
@Table(name="STVCAMP")
class Campus implements Serializable {
	
	/**
	 * Surrogate ID for STVCAMP.
	 */
	@Id
	@Column(name="stvcamp_surrogate_id")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * STVCAMP_DICD_CODE: District Identifier Code validated by form GTVDICD.
	 */
	@Column(name="stvcamp_code", nullable = false, length=3)
	String code

	/**
	 * This field defines the institution"s campus associated with the campus code.
	 */
	@Column(name="stvcamp_desc", length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="stvcamp_activity_date")
	Date lastModified

	/**
	 * District Identifier Code validated by HR form PTVDICD.
	 */
	@Column(name="stvcamp_dicd_code", length=3)
	String districtIdentifierCode

	/**
	 * Column for storing last modified by for STVCAMP.
	 */
	@Column(name="stvcamp_user_id", nullable = false, length=30)
	String lastModifiedBy

	/**
	 * Optimistic Lock Token for STVCAMP.
	 */
	@Version
	@Column(name="stvcamp_version", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for STVCAMP.
	 */
	@Column(name="stvcamp_data_origin", length=30)
	String dataOrigin
	
	
	public String toString() {
		"Campus[id=$id, code=$code, description=$description, lastModified=$lastModified, districtIdentifierCode=$districtIdentifierCode, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:3)
		description(nullable:true, maxSize:30)
        districtIdentifierCode(nullable:true, maxSize:3)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Campus)) return false;

        Campus campus = (Campus) o;

        if (code != campus.code) return false;
        if (dataOrigin != campus.dataOrigin) return false;
        if (description != campus.description) return false;
        if (districtIdentifierCode != campus.districtIdentifierCode) return false;
        if (id != campus.id) return false;
        if (lastModified != campus.lastModified) return false;
        if (lastModifiedBy != campus.lastModifiedBy) return false;
        if (version != campus.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (districtIdentifierCode != null ? districtIdentifierCode.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
