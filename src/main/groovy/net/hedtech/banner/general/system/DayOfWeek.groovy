/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Day of Week Validation Table
 */

@Entity
@Table(name = "STVDAYS")
class DayOfWeek implements Serializable {

    /**
     * Surrogate ID for STVDAYS
     */
    @Id
    @Column(name = "STVDAYS_SURROGATE_ID")
    @SequenceGenerator(name = "STVDAYS_SEQ_GEN", allocationSize = 1, sequenceName = "STVDAYS_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDAYS_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVDAYS
     */
    @Version
    @Column(name = "STVDAYS_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * This field identifies the day code referenced in the Class Schedule and Registration Modules.
     */
    @Column(name = "STVDAYS_CODE", nullable = false, unique = true, length = 1)
    String code

    /**
     * This field specifies the day associated with the day code.
     */
    @Column(name = "STVDAYS_DESC", length = 30)
    String description

    /**
     * This field identifies the number order associated with the day.
     */
    @Column(name = "STVDAYS_NUMBER", nullable = false, length = 1)
    String number

    /**
     * System Required Indicator.
     */
    @Column(name = "STVDAYS_SYSREQ_IND", length = 1)
    String systemRequiredIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVDAYS_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVDAYS
     */
    @Column(name = "STVDAYS_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVDAYS
     */
    @Column(name = "STVDAYS_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """DayOfWeek[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					number=$number,
					systemRequiredIndicator=$systemRequiredIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DayOfWeek)) return false
        DayOfWeek that = (DayOfWeek) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (number != that.number) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
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
        result = 31 * result + (number != null ? number.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    static constraints = {
        code(nullable: false, maxSize: 1)
        description(nullable: true, maxSize: 30)
        number(nullable: false, maxSize: 1)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}

