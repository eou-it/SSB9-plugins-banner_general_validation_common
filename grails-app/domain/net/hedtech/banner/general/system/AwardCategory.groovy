/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Award Catagory Validation Table
 */

@Entity
@Table(name = "STVACAT")
class AwardCategory implements Serializable {

    /**
     * Surrogate ID for STVACAT
     */
    @Id
    @Column(name = "STVACAT_SURROGATE_ID")
    @SequenceGenerator(name = "STVACAT_SEQ_GEN", allocationSize = 1, sequenceName = "STVACAT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVACAT_SEQ_GEN")
    Long id

    /**
     * Award Catagory Code
     */
    @Column(name = "STVACAT_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * Description of Award Catagory Code
     */
    @Column(name = "STVACAT_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVACAT_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * System required indicator (informational only)
     */
    @Column(name = "STVACAT_SYSTEM_REQ_IND", length = 1)
    String systemRequiredIndicator

    /**
     * Version column which is used as a optimistic lock token for STVACAT
     */
    @Version
    @Column(name = "STVACAT_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Last Modified By column for STVACAT
     */
    @Column(name = "STVACAT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVACAT
     */
    @Column(name = "STVACAT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "AwardCategory[id=$id, code=$code, description=$description, lastModified=$lastModified, systemRequiredIndicator=$systemRequiredIndicator, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        systemRequiredIndicator(nullable: true, maxSize: 1)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)

        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(awardcategory_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof AwardCategory)) return false

        AwardCategory that = (AwardCategory) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (id != that.id) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
        if (version != that.version) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(awardcategory_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/

}
