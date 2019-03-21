/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Visa Source Code Validation Table
 */
@Entity
@Table(name = "GTVSRCE")
class VisaSource implements Serializable {

    /**
     * Surrogate ID for GTVSRCE
     */
    @Id
    @Column(name = "GTVSRCE_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSRCE_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSRCE_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSRCE_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSRCE
     */
    @Version
    @Column(name = "GTVSRCE_VERSION")
    Long version

    /**
     * SOURCE CODE: This field indicates Source Code Validation code.
     */
    @Column(name = "GTVSRCE_CODE")
    String code

    /**
     * DESCRIPTION: This field indicates Source Description.
     */
    @Column(name = "GTVSRCE_DESC")
    String description

    /**
     * ACTIVITY DATE: The date that the information for the row was inserted or updated in the GTVSRCD table.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVSRCE_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER IDENTIFICATION: The unique identification of the user who changed the record.
     */
    @Column(name = "GTVSRCE_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVSRCE
     */
    @Column(name = "GTVSRCE_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """VisaSource[
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
        if (!(o instanceof VisaSource)) return false
        VisaSource that = (VisaSource) o
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
