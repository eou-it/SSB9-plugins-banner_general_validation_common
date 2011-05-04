/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import java.util.Date

import javax.persistence.Column
import javax.persistence.Entity

import javax.persistence.*

import org.hibernate.annotations.GenericGenerator


/**
 * Describes a piece of medical equipment.
 */
@Entity
@Table(name = "STVMDEQ")
class MedicalEquipment  implements Serializable {

	@Id
    @SequenceGenerator(name = "STVMDEQ_SEQ_GEN", allocationSize = 1, sequenceName = "STVMDEQ_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVMDEQ_SEQ_GEN")
    @Column(name="STVMDEQ_SURROGATE_ID")
	Long id

    /**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVMDEQ_activity_date")
	Date lastModified

    /**
	 * Column for storing last modified by for STVMDEQ
	 */
	@Column(name="STVMDEQ_USER_ID"  )
	String lastModifiedBy

    /**
     * Column for storing data origin for STVMDEQ
     */
	@Column(name="STVMDEQ_DATA_ORIGIN" )
	String dataOrigin

    /**
	 * Optimistic Lock Token for STVMDEQ
	 */
	@Version
	@Column(name="STVMDEQ_VERSION", nullable = false)
	Long version

	/**
	 * This field identifies the Medical Equipment code in STVMDEQ
	 */
	@Column(name="STVMDEQ_CODE", nullable = false, length = 3)
	String code

    /**
	 * This field specifies the Medical Equipment description in STVMDEQ
	 */
	@Column(name="STVMDEQ_DESC", nullable = false, length = 30)
	String description
	
	
	static constraints = {
		code(nullable: false, maxSize: 3)
		description(nullable: false, maxSize: 30)
        lastModified(nullable:true)
		lastModifiedBy(nullable: true, maxSize: 30)
		dataOrigin(nullable: true, maxSize: 30)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof MedicalEquipment)) return false;

        MedicalEquipment that = (MedicalEquipment) o;

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
