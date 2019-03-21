/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Background Institution Characteristics Validation Table
 */
@Entity
@Table(name = "STVBCHR")
class BackgroundInstitutionCharacteristic implements Serializable {

    /**
     * Surrogate ID for STVBCHR
     */
    @Id
    @Column(name = "STVBCHR_SURROGATE_ID")
    @SequenceGenerator(name = "STVBCHR_SEQ_GEN", allocationSize = 1, sequenceName = "STVBCHR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVBCHR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVBCHR
     */
    @Version
    @Column(name = "STVBCHR_VERSION")
    Long version

    /**
     * This field identifies the background code for an institution as referenced in the Source/Background Institution Year Form (SOABGIY).
     */
    @Column(name = "STVBCHR_CODE")
    String code

    /**
     * This field describes the institution (e.g.  public, military, private) associated with the background code.
     */
    @Column(name = "STVBCHR_DESC")
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVBCHR_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVBCHR
     */
    @Column(name = "STVBCHR_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVBCHR
     */
    @Column(name = "STVBCHR_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """BackgroundInstitutionCharacteristic[
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
        if (!(o instanceof BackgroundInstitutionCharacteristic)) return false
        BackgroundInstitutionCharacteristic that = (BackgroundInstitutionCharacteristic) o
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
        code(nullable: false, maxSize: 1)
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
