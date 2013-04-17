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
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import org.hibernate.annotations.Type

/**
 * Validation entries for SQL Rule Codes.
 */

@Entity
@Table(name = "GTVSQRU")
class EntriesForSql implements Serializable {

    /**
     * Surrogate ID for GTVSQRU
     */
    @Id
    @Column(name = "GTVSQRU_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSQRU_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSQRU_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSQRU_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSQRU
     */
    @Version
    @Column(name = "GTVSQRU_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * RULE CODE: Rule code for dynamic SQL rules.
     */
    @Column(name = "GTVSQRU_CODE", nullable = false, unique = true, length = 30)
    String code

    /**
     * RULE CODE DESCRIPTION: Description of dynamic SQL rule code.
     */
    @Column(name = "GTVSQRU_DESC", nullable = false, length = 60)
    String description

    /**
     * SYSTEM REQUIRED INDICATOR: This field identifies whether or not this code is required by the system.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVSQRU_SYS_REQ_IND", nullable = false, length = 1)
    Boolean systemRequiredIndicator

    /**
     * START DATE: Date code became effective.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "GTVSQRU_START_DATE", nullable = false)
    Date startDate

    /**
     * END DATE: Date code became obsolete.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "GTVSQRU_END_DATE")
    Date endDate

    /**
     * ACTIVITY DATE: This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVSQRU_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: This field identifies the most recent user to create or update a record.
     */
    @Column(name = "GTVSQRU_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSQRU
     */
    @Column(name = "GTVSQRU_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """EntriesForSql[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					systemRequiredIndicator=$systemRequiredIndicator,
					startDate=$startDate,
					endDate=$endDate,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof EntriesForSql)) return false
        EntriesForSql that = (EntriesForSql) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
        if (startDate != that.startDate) return false
        if (endDate != that.endDate) return false
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
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0)
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 30)
        description(nullable: false, maxSize: 60)
        systemRequiredIndicator(nullable: false)
        startDate(nullable: false)
        endDate(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
