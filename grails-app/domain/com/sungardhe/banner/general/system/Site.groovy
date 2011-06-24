
/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import javax.persistence.*

/**
 * Site Validation Table
 */

@Entity
@Table(name="STVSITE")
class Site implements Serializable {
	
	/**
	 * Surrogate ID for STVSITE
	 */
	@Id
	@Column(name="STVSITE_SURROGATE_ID")
    @SequenceGenerator(name = "STVSITE_SEQ_GEN", allocationSize = 1, sequenceName = "STVSITE_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSITE_SEQ_GEN")
	Long id

	/**
	 * This field identifies the site code referenced on the Admissions Application Form (SAAADMS).
	 */
	@Column(name="STVSITE_CODE", nullable = false, length=3)
	String code

	/**
	 * This field specifies the site to which applicant requests admission.  It is associated with the site code.
	 */
	@Column(name="STVSITE_DESC", length=30)
	String description

	/**
	 * This field maintains line one of the site street address.  
	 */
	@Column(name="STVSITE_STREET_ADDR1", length=75)
	String streetAddress1

	/**
	 * This field maintains line two of the site street address.
	 */
	@Column(name="STVSITE_STREET_ADDR2", length=75)
	String streetAddress2

	/**
	 * This field maintains line three of the site street address.
	 */
	@Column(name="STVSITE_STREET_ADDR3", length=75)
	String streetAddress3

	/**
	 * This field maintains the name of the city/town in which the site is located.
	 */
	@Column(name="STVSITE_CITY", length=50)
	String city

	/**
	 * This field maintains the two character abbreviation for the name of the state in which the site is located.
	 */
	@Column(name="STVSITE_STATE", length=3)
	String state

	/**
	 * This field maintains the name of the foreign country in which the site is located.
	 */
	@Column(name="STVSITE_FOREIGN_COUNTRY", length=28)
	String foreignCountry

	/**
	 * This field maintains the zip code/postal code of the site.  
	 */
	@Column(name="STVSITE_ZIPC", length=30)
	String zip

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVSITE_ACTIVITY_DATE")
	Date lastModified

	/**
	 * STREET ADDR4: This field maintains line four of the site street address.
	 */
	@Column(name="STVSITE_STREET_ADDR4", length=75)
	String streetAddress4

	/**
	 * HOUSE NUMBER: Building number in a street or area.
	 */
	@Column(name="STVSITE_HOUSE_NUMBER", length=10)
	String houseNumber

	/**
	 * Version column which is used as a optimistic lock token for STVSITE
	 */
	@Version
	@Column(name="STVSITE_VERSION", length=19)
	Long version

	/**
	 * Last Modified By column for STVSITE
	 */
	@Column(name="STVSITE_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVSITE
	 */
	@Column(name="STVSITE_DATA_ORIGIN", length=30)
	String dataOrigin

	
	/**
	 * Foreign Key : FKV_STVSITE_INV_STVNATN_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="STVSITE_NATN_CODE", referencedColumnName="STVNATN_CODE")
		])
	Nation nation

	
	public String toString() {
		"""Site[
					id=$id, 
					code=$code, 
					description=$description, 
					streetAddress1=$streetAddress1, 
					streetAddress2=$streetAddress2, 
					streetAddr3=$streetAddress3, 
					city=$city, 
					state=$state, 
					foreignCountry=$foreignCountry, 
					zip=$zip, 
					lastModified=$lastModified, 
					streetAddress4=$streetAddress4, 
					houseNumber=$houseNumber, 
					version=$version, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					nation=$nation]"""
	}



    boolean equals(o) {
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        Site site = (Site) o;

        if (city != site.city) return false;
        if (code != site.code) return false;
        if (dataOrigin != site.dataOrigin) return false;
        if (description != site.description) return false;
        if (foreignCountry != site.foreignCountry) return false;
        if (houseNumber != site.houseNumber) return false;
        if (id != site.id) return false;
        if (lastModified != site.lastModified) return false;
        if (lastModifiedBy != site.lastModifiedBy) return false;
        if (nation != site.nation) return false;
        if (state != site.state) return false;
        if (streetAddress1 != site.streetAddress1) return false;
        if (streetAddress2 != site.streetAddress2) return false;
        if (streetAddress3 != site.streetAddress3) return false;
        if (streetAddress4 != site.streetAddress4) return false;
        if (version != site.version) return false;
        if (zip != site.zip) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (streetAddress1 != null ? streetAddress1.hashCode() : 0);
        result = 31 * result + (streetAddress2 != null ? streetAddress2.hashCode() : 0);
        result = 31 * result + (streetAddress3 != null ? streetAddress3.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (foreignCountry != null ? foreignCountry.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (streetAddress4 != null ? streetAddress4.hashCode() : 0);
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (nation != null ? nation.hashCode() : 0);
        return result;
    }


    static constraints = {
		code(nullable:false, maxSize:3)
		description(nullable:true, maxSize:30)
		streetAddress1(nullable:true, maxSize:75)
		streetAddress2(nullable:true, maxSize:75)
		streetAddress3(nullable:true, maxSize:75)
		city(nullable:true, maxSize:50)
		state(nullable:true, maxSize:3)
		foreignCountry(nullable:true, maxSize:28)
		zip(nullable:true, maxSize:30)
		lastModified(nullable:true)
		streetAddress4(nullable:true, maxSize:75)
		houseNumber(nullable:true, maxSize:10)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)

		nation(nullable:true)

		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(site_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(site_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
