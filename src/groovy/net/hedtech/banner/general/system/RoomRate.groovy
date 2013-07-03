/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.Type
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator

/**
 * Room Rate Code Validation Table
 */

@Entity
@Table(name = "STVRRCD")
class RoomRate implements Serializable {

    /**
     * Surrogate ID for STVRRCD
     */
    @Id
    @Column(name = "STVRRCD_SURROGATE_ID")
    @SequenceGenerator(name = "STVRRCD_SEQ_GEN", allocationSize = 1, sequenceName = "STVRRCD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVRRCD_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVRRCD
     */
    @Version
    @Column(name = "STVRRCD_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Room Rate Code
     */
    @Column(name = "STVRRCD_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * Monthly Basis Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVRRCD_MONTHLY_IND", nullable = false, length = 1)
    Boolean monthlyIndicator

    /**
     * Daily Basis Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVRRCD_DAILY_IND", nullable = false, length = 1)
    Boolean dailyIndicator

    /**
     * Termly Basis Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVRRCD_TERMLY_IND", nullable = false, length = 1)
    Boolean termlyIndicator

    /**
     * Description of Room Rate Code
     */
    @Column(name = "STVRRCD_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVRRCD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVRRCD
     */
    @Column(name = "STVRRCD_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVRRCD
     */
    @Column(name = "STVRRCD_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """RoomRate[
					id=$id, 
					version=$version, 
					code=$code, 
					monthlyIndicator=$monthlyIndicator, 
					dailyIndicator=$dailyIndicator, 
					termlyIndicator=$termlyIndicator, 
					description=$description, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RoomRate)) return false
        RoomRate that = (RoomRate) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (monthlyIndicator != that.monthlyIndicator) return false
        if (dailyIndicator != that.dailyIndicator) return false
        if (termlyIndicator != that.termlyIndicator) return false
        if (description != that.description) return false
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
        result = 31 * result + (monthlyIndicator != null ? monthlyIndicator.hashCode() : 0)
        result = 31 * result + (dailyIndicator != null ? dailyIndicator.hashCode() : 0)
        result = 31 * result + (termlyIndicator != null ? termlyIndicator.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    static constraints = {
        code(nullable: false, maxSize: 4)
        monthlyIndicator(nullable: false)
        dailyIndicator(nullable: false)
        termlyIndicator(nullable: false)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
