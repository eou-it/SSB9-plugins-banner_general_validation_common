/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * This Table contains the Additional Identification types
 */
@Entity
@Table(name = "GTVADID")
class AdditionalIdentificationType implements Serializable {

    /**
     * Surrogate ID for GTVADID
     */
    @Id
    @Column(name = "GTVADID_SURROGATE_ID")
    @SequenceGenerator(name = "GTVADID_SEQ_GEN", allocationSize = 1, sequenceName = "GTVADID_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVADID_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVADID
     */
    @Version
    @Column(name = "GTVADID_VERSION")
    Long version

    /**
     * CODE:Additional Identification Type Code.
     */
    @Column(name = "GTVADID_CODE")
    String code

    /**
     * DESCRIPTION:Additional Identification Type Description.
     */
    @Column(name = "GTVADID_DESC")
    String description

    /**
     * SEARCH BYPASS: If set to 'Y', additional ids will not be included in GUISRCH lookup.
     */
    @Column(name = "GTVADID_GUISRCH_BYPASS")
    String searchBypass

    /**
     * ACTIVITY DATE:Date on which the record was created or last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVADID_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER IDENTIFICATION: User ID of the user who created or last updated the record.
     */
    @Column(name = "GTVADID_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN:Source system that created or updated the row.
     */
    @Column(name = "GTVADID_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """AdditionalIdentificationType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description,
                    searchBypass=$searchBypass,
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AdditionalIdentificationType)) return false
        AdditionalIdentificationType that = (AdditionalIdentificationType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (searchBypass != that.searchBypass) return false
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
        result = 31 * result + (searchBypass != null ? searchBypass.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        searchBypass(nullable: true, maxSize: 1, inList: ["Y", "N"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
