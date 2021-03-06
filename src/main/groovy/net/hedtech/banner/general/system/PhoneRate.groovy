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
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type

/**
 * Phone Rate Code Validation Table
 */

@Entity
@Table(name = "STVPRCD")
class PhoneRate implements Serializable {

    /**
     * Surrogate ID for STVPRCD
     */
    @Id
    @Column(name = "STVPRCD_SURROGATE_ID")
    @SequenceGenerator(name = "STVPRCD_SEQ_GEN", allocationSize = 1, sequenceName = "STVPRCD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVPRCD_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVPRCD
     */
    @Version
    @Column(name = "STVPRCD_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Phone Rate Code
     */
    @Column(name = "STVPRCD_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * Monthly Basis Indicator.
     */
    @Column(name = "STVPRCD_MONTHLY_IND", nullable = false, length = 1)
    String monthlyIndicator

    /**
     * Daily Basis Indicator.
     */
    @Column(name = "STVPRCD_DAILY_IND", nullable = false, length = 1)
    String dailyIndicator

    /**
     * Termly Basis Indicator.
     */
    @Column(name = "STVPRCD_TERMLY_IND", nullable = false, length = 1)
    String termlyIndicator

    /**
     * Description of Phone Rate Code
     */
    @Column(name = "STVPRCD_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVPRCD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVPRCD
     */
    @Column(name = "STVPRCD_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVPRCD
     */
    @Column(name = "STVPRCD_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """PhoneRate[
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
        if (!(o instanceof PhoneRate)) return false
        PhoneRate that = (PhoneRate) o
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
        monthlyIndicator(nullable: false, maxSize: 1, inList: ["Y", "N"])
        dailyIndicator(nullable: false, maxSize: 1, inList: ["Y", "N"])
        termlyIndicator(nullable: false, maxSize: 1, inList: ["Y", "N"])
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
