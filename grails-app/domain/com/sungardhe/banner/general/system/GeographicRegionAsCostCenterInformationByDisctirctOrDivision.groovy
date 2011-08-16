/** *****************************************************************************
 Â© 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
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
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.GenerationType
import org.hibernate.annotations.Type

/**
 * This table is used to subdivide geographic regions to capture cost center information by disctirct or division.
 */

@Entity
@Table(name = "GTVDICD")
class GeographicRegionAsCostCenterInformationByDisctirctOrDivision implements Serializable {

    /**
     * Surrogate ID for GTVDICD
     */
    @Id
    @Column(name = "GTVDICD_SURROGATE_ID")
    @SequenceGenerator(name = "GTVDICD_SEQ_GEN", allocationSize = 1, sequenceName = "GTVDICD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVDICD_SEQ_GEN")
    Long id

    /**
     * The district or division associated with an employee.
     */
    @Column(name = "GTVDICD_CODE", nullable = false, unique = true, length = 3)
    String code

    /**
     * The description for the corresponding district/division code
     */
    @Column(name = "GTVDICD_DESC", nullable = false, length = 30)
    String description

    /**
     * The date this row was updated.
     */
    @Column(name = "GTVDICD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for GTVDICD
     */
    @Version
    @Column(name = "GTVDICD_VERSION", precision = 19)
    Long version

    /**
     * Last Modified By column for GTVDICD
     */
    @Column(name = "GTVDICD_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for GTVDICD
     */
    @Column(name = "GTVDICD_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """GeographicRegionAsCostCenterInformationByDisctirctOrDivision[
					id=$id,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					version=$version,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin, ]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof GeographicRegionAsCostCenterInformationByDisctirctOrDivision)) return false
        GeographicRegionAsCostCenterInformationByDisctirctOrDivision that = (GeographicRegionAsCostCenterInformationByDisctirctOrDivision) o
        if (id != that.id) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (version != that.version) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)

        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(geographicregionascostcenterinformationbydisctirctordivision_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(geographicregionascostcenterinformationbydisctirctordivision_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
