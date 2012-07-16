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
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Building/Room Attributes Validation Table
 */

@Entity
@Table(name = "STVRDEF")
class BuildingAndRoomAttribute implements Serializable {

    /**
     * Surrogate ID for STVRDEF
     */
    @Id
    @Column(name = "STVRDEF_SURROGATE_ID")
    @SequenceGenerator(name = "STVRDEF_SEQ_GEN", allocationSize = 1, sequenceName = "STVRDEF_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVRDEF_SEQ_GEN")
    Long id

    /**
     * Building/Room Attributes Code
     */
    @Column(name = "STVRDEF_CODE", nullable = false, length = 4)
    String code

    /**
     * Description of Building/Room Attributes Code
     */
    @Column(name = "STVRDEF_DESC", length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVRDEF_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Numeric Identification number for scheduling tool.
     */
    @Column(name = "STVRDEF_SCHEDULER_NUMBER", length = 22)
    BigDecimal schedulerNumber

    /**
     * Indicator whether Room Definition will be used by the Scheduling Tool.  If this is N, Room Definition Information will not be passed to the tool.
     */
    @Type(type = "yes_no")
    @Column(name = "STVRDEF_AUTO_SCHEDULER_IND", nullable = false, length = 1)
    Boolean autoSchedulerIndicator

    /**
     * Version column which is used as a optimistic lock token for STVRDEF
     */
    @Version
    @Column(name = "STVRDEF_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVRDEF
     */
    @Column(name = "STVRDEF_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVRDEF
     */
    @Column(name = "STVRDEF_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """BuildingAndRoomAttribute[id=$id,
		  version=$version,
          code=$code,
          description=$description,
          schedulerNumber=$schedulerNumber,
          autoSchedulerIndicator=$autoSchedulerIndicator,
          lastModified=$lastModified,lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"""
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        schedulerNumber(nullable: true, scale: 0)
        autoSchedulerIndicator(nullable: false)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)

        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(buildingandroomattributes_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof BuildingAndRoomAttribute)) return false;

        BuildingAndRoomAttribute that = (BuildingAndRoomAttribute) o;

        if (autoSchedulerIndicator != that.autoSchedulerIndicator) return false;
        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (schedulerNumber != that.schedulerNumber) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (schedulerNumber != null ? schedulerNumber.hashCode() : 0);
        result = 31 * result + (autoSchedulerIndicator != null ? autoSchedulerIndicator.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
/**
 * Please put all the custom methods/code in this protected section to protect the code
 * from being overwritten on re-generation
 */
    /*PROTECTED REGION ID(buildingandroomattributes_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
