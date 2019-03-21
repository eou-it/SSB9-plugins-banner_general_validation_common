/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * IPEDS Ethnic Validation Table
 */
@Entity
@Table(name = "STVETCT")
class IpedsEthnicity implements Serializable {

    /**
     * Surrogate ID for STVETCT
     */
    @Id
    @Column(name = "STVETCT_SURROGATE_ID")
    @SequenceGenerator(name = "STVETCT_SEQ_GEN", allocationSize = 1, sequenceName = "STVETCT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVETCT_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVETCT
     */
    @Version
    @Column(name = "STVETCT_VERSION")
    Long version

    /**
     * This field identifies the ethnic code referenced by the IPEDS Completions Reporting
     */
    @Column(name = "STVETCT_CODE")
    String code

    /**
     * This field specifies the ethnic group associated with the ethnic code
     */
    @Column(name = "STVETCT_DESC")
    String description

    /**
     * This field denotes whether the ethnic code is system required.  It is informational only
     */
    @Column(name = "STVETCT_SYSTEM_REQ_IND")
    String systemRequiredIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVETCT_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVETCT
     */
    @Column(name = "STVETCT_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVETCT
     */
    @Column(name = "STVETCT_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """IpedsEthnicity[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					systemRequiredIndicator=$systemRequiredIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IpedsEthnicity)) return false
        IpedsEthnicity that = (IpedsEthnicity) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
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
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 1)
        description(nullable: false, maxSize: 30)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
