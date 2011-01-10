/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import java.io.Serializable
import java.util.Date

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type


/**
 * Term Type Validation Table.
 */
@Entity
@Table(name="STVTRMT")
class TermType implements Serializable {
	
	/**
	 * Surrogate ID for STVTRMT
	 */
	@Id
	@Column(name="STVTRMT_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * Type of term, eg.  2 - semester, 4 - quarter.
	 */
	@Column(name="STVTRMT_CODE", nullable = false, length=1)
	String code

	/**
	 * Specifies the type of term associated with term type code.
	 */
	@Column(name="STVTRMT_DESC", nullable = false, length=30)
	String description

	/**
	 * Most recent date record was created or updated.
	 */
	@Column(name="STVTRMT_activity_date")
	Date lastModified

	/**
	 * Column for storing last modified by for STVTRMT
	 */
	@Column(name="STVTRMT_USER_ID" )
	String lastModifiedBy

	/**
	 * Optimistic Lock Token for STVTRMT
	 */
	@Version
	@Column(name="STVTRMT_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Column for storing data origin for STVTRMT
	 */
	@Column(name="STVTRMT_DATA_ORIGIN" )
	String dataOrigin

	
	
	public String toString() {
		"TermType[id=$id, code=$code, description=$description, lastModified=$lastModified, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:1)
		description(nullable:false, maxSize:30)
	}


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof TermType)) return false;

        TermType termType = (TermType) o;

        if (code != termType.code) return false;
        if (dataOrigin != termType.dataOrigin) return false;
        if (description != termType.description) return false;
        if (id != termType.id) return false;
        if (lastModified != termType.lastModified) return false;
        if (lastModifiedBy != termType.lastModifiedBy) return false;
        if (version != termType.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
