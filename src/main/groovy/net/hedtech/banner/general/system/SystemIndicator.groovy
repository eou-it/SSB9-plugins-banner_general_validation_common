/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * System Indicator Validation Table
 */

@Entity
@Table(name = "GTVSYSI")
class SystemIndicator implements Serializable {

    /**
     * Surrogate ID for GTVSYSI
     */
    @Id
    @Column(name = "GTVSYSI_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSYSI_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSYSI_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSYSI_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSYSI
     */
    @Version
    @Column(name = "GTVSYSI_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * CODE: System indicator which uniquely identifies this record.
     */
    @Column(name = "GTVSYSI_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * DESCRIPTION: Description of this code.
     */
    @Column(name = "GTVSYSI_DESC", nullable = false, length = 30)
    String description

    /**
     * ACTIVITY DATE: Date that record was created or last updated.
     */
    @Column(name = "GTVSYSI_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVSYSI
     */
    @Column(name = "GTVSYSI_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSYSI
     */
    @Column(name = "GTVSYSI_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """SystemIndicator[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof SystemIndicator)) return false
        SystemIndicator that = (SystemIndicator) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
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
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
