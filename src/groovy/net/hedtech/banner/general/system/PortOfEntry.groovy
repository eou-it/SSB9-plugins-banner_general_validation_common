/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Port of Entry Validation Table
 */
@Entity
@Table(name = "STVPENT")
class PortOfEntry implements Serializable {

    /**
     * Surrogate ID for STVPENT
     */
    @Id
    @Column(name = "STVPENT_SURROGATE_ID")
    @SequenceGenerator(name = "STVPENT_SEQ_GEN", allocationSize = 1, sequenceName = "STVPENT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVPENT_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVPENT
     */
    @Version
    @Column(name = "STVPENT_VERSION")
    Long version

    /**
     * This field identifies the port of entry code referenced on the International Info. Form (GOAINTL).
     */
    @Column(name = "STVPENT_CODE")
    String code

    /**
     * This field specifies the port of entry associated with the port of entry code.
     */
    @Column(name = "STVPENT_DESC")
    String description

    /**
     * PORT OF ENTRY SEVIS EQUIVALENCY: SEVIS code for port of entry
     */
    @Column(name = "STVPENT_SEVIS_EQUIV")
    String studentExchangeVisitorInformationSystemEquivalent

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVPENT_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVPENT
     */
    @Column(name = "STVPENT_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVPENT
     */
    @Column(name = "STVPENT_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """PortOfEntry[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					studentExchangeVisitorInformationSystemEquivalent=$studentExchangeVisitorInformationSystemEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof PortOfEntry)) return false
        PortOfEntry that = (PortOfEntry) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (studentExchangeVisitorInformationSystemEquivalent != that.studentExchangeVisitorInformationSystemEquivalent) return false
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
        result = 31 * result + (studentExchangeVisitorInformationSystemEquivalent != null ? studentExchangeVisitorInformationSystemEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: true, maxSize: 30)
        studentExchangeVisitorInformationSystemEquivalent(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
