/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Event Type Validation Table
 */

@Entity
@Table(name = "STVETYP")
class EventType implements Serializable {

    /**
     * Surrogate ID for STVETYP
     */
    @Id
    @Column(name = "STVETYP_SURROGATE_ID")
    @SequenceGenerator(name = "STVETYP_SEQ_GEN", allocationSize = 1, sequenceName = "STVETYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVETYP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVETYP
     */
    @Version
    @Column(name = "STVETYP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Event Type Code
     */
    @Column(name = "STVETYP_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * Description of Event Type Code
     */
    @Column(name = "STVETYP_DESC", length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVETYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVETYP
     */
    @Column(name = "STVETYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVETYP
     */
    @Column(name = "STVETYP_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """EventType[
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
        if (!(o instanceof EventType)) return false
        EventType that = (EventType) o
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
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
