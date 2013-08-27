/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Participant Type Code Validation Table
 */

@Entity
@Table(name = "GTVPTYP")
class ParticipantType implements Serializable {

    /**
     * Surrogate ID for GTVPTYP
     */
    @Id
    @Column(name = "GTVPTYP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVPTYP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVPTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVPTYP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVPTYP
     */
    @Version
    @Column(name = "GTVPTYP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Participant Type Code.
     */
    @Column(name = "GTVPTYP_CODE", nullable = false, unique = true, length = 5)
    String code

    /**
     * Participant Type Code Description.
     */
    @Column(name = "GTVPTYP_DESC", nullable = false, length = 30)
    String description

    /**
     * Participant Type Code Activity Date.
     */
    @Column(name = "GTVPTYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVPTYP
     */
    @Column(name = "GTVPTYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVPTYP
     */
    @Column(name = "GTVPTYP_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """ParticipantType[
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
        if (!(o instanceof ParticipantType)) return false
        ParticipantType that = (ParticipantType) o
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
        code(nullable: false, maxSize: 5)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
