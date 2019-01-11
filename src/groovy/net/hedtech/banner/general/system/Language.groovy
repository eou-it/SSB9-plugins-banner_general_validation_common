/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Native Language Validation Table
 */
@Entity
@Table(name = "STVLANG")
@NamedQueries(value = [
        @NamedQuery(name = "Language.fetchByCode",
                query = """ FROM Language a WHERE a.code = :code """
        ),
        @NamedQuery(name = "Language.fetchAllByCodeInList",
                query = """ FROM Language a WHERE a.code IN :codes """
        ),
        @NamedQuery(name = "Language.fetchAllByIsoCodeInList",
                query = """ FROM Language a WHERE a.isoCode IN :isoCodes """
        )
])

class Language implements Serializable {

    /**
     * Surrogate ID for STVLANG
     */
    @Id
    @Column(name = "STVLANG_SURROGATE_ID")
    @SequenceGenerator(name = "STVLANG_SEQ_GEN", allocationSize = 1, sequenceName = "STVLANG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVLANG_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVLANG
     */
    @Version
    @Column(name = "STVLANG_VERSION")
    Long version

    /**
     * This field identifies the Native Language code referenced by the GOAINTL form.
     */
    @Column(name = "STVLANG_CODE")
    String code

    /**
     * This field defines the Native language associated with the native language code.
     */
    @Column(name = "STVLANG_DESC")
    String description

    /**
     * This field identifies the three character International Standards Organization (ISO) Code associated with the user defined language code
     */
    @Column(name = "STVLANG_SCOD_CODE_ISO")
    String isoCode

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVLANG_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVLANG
     */
    @Column(name = "STVLANG_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVLANG
     */
    @Column(name = "STVLANG_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """Language[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description,
                    isoCode=$isoCode,
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Language)) return false
        Language that = (Language) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (isoCode != that.isoCode) return false
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
        result = 31 * result + (isoCode != null ? isoCode.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 30)
        isoCode(nullable: true, maxSize: 8)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
