/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Person Type Validation Table
 */
@Entity
@Table(name = "STVPTYP")
class PersonType implements Serializable {

    /**
     * Surrogate ID for STVPTYP
     */
    @Id
    @Column(name = "STVPTYP_SURROGATE_ID")
    @SequenceGenerator(name = "STVPTYP_SEQ_GEN", allocationSize = 1, sequenceName = "STVPTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVPTYP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVPTYP
     */
    @Version
    @Column(name = "STVPTYP_VERSION")
    Long version

    /**
     * This field identifies the source contact person type code referenced by the Source/Background Institution Base Form (SOASBGI).
     */
    @Column(name = "STVPTYP_CODE")
    String code

    /**
     * This field specifies the source contact person type (e.g.  high school principal, guidance counselor, dean of college, etc.) associated with the contact person type code.
     */
    @Column(name = "STVPTYP_DESC")
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVPTYP_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVPTYP
     */
    @Column(name = "STVPTYP_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVPTYP
     */
    @Column(name = "STVPTYP_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """PersonType[
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
        if (!(o instanceof PersonType)) return false
        PersonType that = (PersonType) o
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
