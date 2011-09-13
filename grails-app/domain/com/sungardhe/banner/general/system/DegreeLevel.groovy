
/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

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
 * Faculty Member Degree Level Validation Table
 */

@Entity
@Table(name="STVDLEV")
class DegreeLevel implements Serializable {

	/**
	 * Surrogate ID for STVDLEV
	 */
	@Id
	@Column(name="STVDLEV_SURROGATE_ID")
    @SequenceGenerator(name = "STVDLEV_SEQ_GEN", allocationSize = 1, sequenceName = "STVDLEV_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDLEV_SEQ_GEN")
    Long id

	/**
	 * Faculty member degree level code
	 */
	@Column(name="STVDLEV_CODE", nullable = false, unique = true, length=2)
	String code

	/**
	 * Description of faculty member degree level code
	 */
	@Column(name="STVDLEV_DESC", nullable = false, length=30)
	String description

	/**
	 * The activity date
	 */
	@Column(name="STVDLEV_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Numeric Ranking Value
	 */
	@Column(name="STVDLEV_NUMERIC_VALUE", precision=2)
	Integer numericValue

	/**
	 * Version column which is used as a optimistic lock token for STVDLEV
	 */
	@Version
	@Column(name="STVDLEV_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * Last Modified By column for STVDLEV
	 */
	@Column(name="STVDLEV_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVDLEV
	 */
	@Column(name="STVDLEV_DATA_ORIGIN", length=30)
	String dataOrigin



	public String toString() {
		"DegreeLevel[id=$id, code=$code, description=$description, lastModified=$lastModified, numericValue=$numericValue, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:2)
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		numericValue(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)

		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(degreelevel_custom_constraints) ENABLED START*/

	    /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof DegreeLevel)) return false;

        DegreeLevel that = (DegreeLevel) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (numericValue != that.numericValue) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (numericValue != null ? numericValue.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

	//Read Only fields that should be protected against update
	public static readonlyProperties = [ 'code' ]
	/**
	 * Please put all the custom methods/code in this protected section to protect the code
	 * from being overwritten on re-generation
	 */
	/*PROTECTED REGION ID(degreelevel_custom_methods) ENABLED START*/

	/*PROTECTED REGION END*/
}	                                     
