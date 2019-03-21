/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Diploma Type Validation Table
 */
@Entity
@Table(name = "STVDPLM")
class DiplomaType implements Serializable {

    /**
     * Surrogate ID for STVDPLM
     */
    @Id
    @Column(name = "STVDPLM_SURROGATE_ID")
    @SequenceGenerator(name = "STVDPLM_SEQ_GEN", allocationSize = 1, sequenceName = "STVDPLM_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDPLM_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVDPLM
     */
    @Version
    @Column(name = "STVDPLM_VERSION")
    Long version

    /**
     * This field identifies the diploma type code referenced in the Admissions Module and cross-referenced by SOAHSCH and SOABGIY.
     */
    @Column(name = "STVDPLM_CODE")
    String code

    /**
     * This field specifies the diploma type (e.g.  college preparatory, general education) associated with the diploma type code.
     */
    @Column(name = "STVDPLM_DESC")
    String description

    /**
     * EDI/SPEEDE diploma type that equates to the diploma type code.  Refer to the SPEEDE Implementation Guide, Appendix B, data element 641 for valid values.
     */
    @Column(name = "STVDPLM_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVDPLM_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVDPLM
     */
    @Column(name = "STVDPLM_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVDPLM
     */
    @Column(name = "STVDPLM_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """DiplomaType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DiplomaType)) return false
        DiplomaType that = (DiplomaType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (electronicDataInterchangeEquivalent != that.electronicDataInterchangeEquivalent) return false
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
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
