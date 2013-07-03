/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.Type

/**
 * RSVP Code Table
 */

@Entity
@Table(name = "GTVRSVP")
class Rsvp implements Serializable {

    /**
     * Surrogate ID for GTVRSVP
     */
    @Id
    @Column(name = "GTVRSVP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVRSVP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVRSVP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVRSVP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVRSVP
     */
    @Version
    @Column(name = "GTVRSVP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * RSVP Code.
     */
    @Column(name = "GTVRSVP_CODE", nullable = false, unique = true, length = 6)
    String code

    /**
     * RSVP Code Description.
     */
    @Column(name = "GTVRSVP_DESC", nullable = false, length = 30)
    String description

    /**
     * Indicator for whether or not person plans to attend function.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVRSVP_PLAN_TO_ATTEND_IND", nullable = false, length = 1)
    Boolean planToAttendenceIndicator

    /**
     * Date RSVP code was last created or modified.
     */
    @Column(name = "GTVRSVP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVRSVP
     */
    @Column(name = "GTVRSVP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVRSVP
     */
    @Column(name = "GTVRSVP_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Rsvp[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					planToAttendenceIndicator=$planToAttendenceIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Rsvp)) return false
        Rsvp that = (Rsvp) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (planToAttendenceIndicator != that.planToAttendenceIndicator) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (planToAttendenceIndicator != null ? planToAttendenceIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        planToAttendenceIndicator(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
