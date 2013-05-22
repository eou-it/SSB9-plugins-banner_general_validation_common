/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Academic History Institutional Honors Validation Table
 */
@Entity
@Table(name = "STVHONR")
class InstitutionalHonor implements Serializable {

    /**
     * Surrogate ID for STVHONR
     */
    @Id
    @Column(name = "STVHONR_SURROGATE_ID")
    @SequenceGenerator(name = "STVHONR_SEQ_GEN", allocationSize = 1, sequenceName = "STVHONR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVHONR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVHONR
     */
    @Version
    @Column(name = "STVHONR_VERSION")
    Long version

    /**
     * The institutional honor code referenced on the Degrees/Other Formal Awards Form (SHADEGR).
     */
    @Column(name = "STVHONR_CODE")
    String code

    /**
     * The institutional honor (e.g.  Magna Cum Laude) associated with the institutional honor code.
     */
    @Column(name = "STVHONR_DESC")
    String description

    /**
     * The transcript print indicator. Y indicates honor should print on transcript.
     */
    @Column(name = "STVHONR_TRANSC_PRT_IND")
    String transcPrintIndicator

    /**
     * The commencement print indicator. Y indicates honor should print on commencement report.
     */
    @Column(name = "STVHONR_COMMENCE_PRT_IND")
    String commencePrintIndicator

    /**
     * EDI/SPEEDE standard honors code that equates to the institutional honor. Refer to the SPEEDE Implementation Guide, with reference designator DEG05 for valid values.
     */
    @Column(name = "STVHONR_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * The most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVHONR_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVHONR
     */
    @Column(name = "STVHONR_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVHONR
     */
    @Column(name = "STVHONR_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """InstitutionalHonor[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					transcPrintIndicator=$transcPrintIndicator, 
					commencePrintIndicator=$commencePrintIndicator, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof InstitutionalHonor)) return false
        InstitutionalHonor that = (InstitutionalHonor) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (transcPrintIndicator != that.transcPrintIndicator) return false
        if (commencePrintIndicator != that.commencePrintIndicator) return false
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
        result = 31 * result + (transcPrintIndicator != null ? transcPrintIndicator.hashCode() : 0)
        result = 31 * result + (commencePrintIndicator != null ? commencePrintIndicator.hashCode() : 0)
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: true, maxSize: 30)
        transcPrintIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        commencePrintIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
