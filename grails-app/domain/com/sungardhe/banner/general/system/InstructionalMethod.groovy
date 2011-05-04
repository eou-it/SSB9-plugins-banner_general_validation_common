/** *****************************************************************************
 � 2010 SunGard Higher Education.  All Rights Reserved.
 CONFIDENTIAL BUSINESS INFORMATION
 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import javax.persistence.*
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type


/**
 * Instructional Method Code  Validation Table
 */
@Entity
@Table(name="GTVINSM")
class InstructionalMethod implements Serializable {
	
	/**
	 * Surrogate ID for GTVINSM
	 */
	@Id
	@Column(name="gtvinsm_surrogate_id")
    @SequenceGenerator(name = "GTVINSM_SEQ_GEN", allocationSize = 1, sequenceName = "GTVINSM_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVINSM_SEQ_GEN")
	Long id

	/**
	 * Instructional Method Code: The Instructional Method Code
	 */
	@Column(name="gtvinsm_code", nullable = false, length=5)
	String code

	/**
	 * Description: Description of the instructional method code
	 */
	@Column(name="gtvinsm_desc", nullable = false, length=30)
	String description

	/**
	 * Activity Date: Date this record entered or last updated
	 */
	@Column(name="gtvinsm_activity_date")
	Date lastModified

	/**
	 * User ID: The username of the person who entered or last updated this record
	 */
	@Column(name="gtvinsm_user_id" )
	String lastModifiedBy

	/**
	 * Voice Response Message Number
	 */
	@Column(name="gtvinsm_vr_msg_no", length=22)
	BigDecimal voiceResponseMessageNumber

	/**
	 * Optimistic Lock Token for GTVINSM
	 */
	@Version
	@Column(name="gtvinsm_version", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for GTVINSM
	 */
	@Column(name="gtvinsm_data_origin", length=30)
	String dataOrigin

		
	public String toString() {
		"InstructionalMethod[id=$id, code=$code, description=$description, lastModified=$lastModified, lastModifiedBy=$lastModifiedBy, voiceResponseMessageNumber=$voiceResponseMessageNumber, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:5)
		description(nullable:false, maxSize:30)
		voiceResponseMessageNumber(nullable:true, maxSize:22)
        // While these are required in the database, we'll set them via a callback if necessary
        dataOrigin(nullable: true)
        lastModified(nullable:true)
        lastModifiedBy(nullable: true)
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof InstructionalMethod)) return false;

        InstructionalMethod that = (InstructionalMethod) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (version != that.version) return false;
        if (voiceResponseMessageNumber != that.voiceResponseMessageNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
