/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Visa Issuing Authority Validation Table
 */
@Entity
@Table(name = "GTVVISS")
class VisaIssuingAuthority implements Serializable {

    /**
     * Surrogate ID for GTVVISS
     */
    @Id
    @Column(name = "GTVVISS_SURROGATE_ID")
    @SequenceGenerator(name = "GTVVISS_SEQ_GEN", allocationSize = 1, sequenceName = "GTVVISS_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVVISS_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVVISS
     */
    @Version
    @Column(name = "GTVVISS_VERSION")
    Long version

    /**
     * AUTHORITY CODE: This field indicates Visa Issuing Authority Validation code.
     */
    @Column(name = "GTVVISS_CODE")
    String code

    /**
     * DESCRIPTION: This field indicates Visa Issuing Authority Description.
     */
    @Column(name = "GTVVISS_DESC")
    String description

    /**
     * ACTIVITY DATE: The date that the information for the row was inserted or updated in the GTVVISS table.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVVISS_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER IDENTIFICATION: The unique identification of the user who changed the record.
     */
    @Column(name = "GTVVISS_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVVISS
     */
    @Column(name = "GTVVISS_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """VisaIssuingAuthority[
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
        if (!(o instanceof VisaIssuingAuthority)) return false
        VisaIssuingAuthority that = (VisaIssuingAuthority) o
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
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
