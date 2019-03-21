/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * International Sponsor Validation Table
 */
@Entity
@Table(name = "STVSPON")
class InternationalSponsor implements Serializable {

    /**
     * Surrogate ID for STVSPON
     */
    @Id
    @Column(name = "STVSPON_SURROGATE_ID")
    @SequenceGenerator(name = "STVSPON_SEQ_GEN", allocationSize = 1, sequenceName = "STVSPON_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSPON_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVSPON
     */
    @Version
    @Column(name = "STVSPON_VERSION")
    Long version

    /**
     * This field identifies the international student sponsor code referenced on the I nternational Information Form (GOAINTL).
     */
    @Column(name = "STVSPON_CODE")
    String code

    /**
     * This field identifies the international student sponsoring organization associated with the sponsor code.
     */
    @Column(name = "STVSPON_DESC")
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVSPON_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVSPON
     */
    @Column(name = "STVSPON_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVSPON
     */
    @Column(name = "STVSPON_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """InternationalSponsor[
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
        if (!(o instanceof InternationalSponsor)) return false
        InternationalSponsor that = (InternationalSponsor) o
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
        code(nullable: false, maxSize: 3)
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
