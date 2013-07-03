/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Ethnic Code Validation Table
 */
@Entity
@Table(name = "STVETHN")
class Ethnicity implements Serializable {

    /**
     * Surrogate ID for STVETHN
     */
    @Id
    @Column(name = "STVETHN_SURROGATE_ID")
    @SequenceGenerator(name = "STVETHN_SEQ_GEN", allocationSize = 1, sequenceName = "STVETHN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVETHN_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVETHN
     */
    @Version
    @Column(name = "STVETHN_VERSION")
    Long version

    /**
     * This field identifies the ethnic code referenced on the General Person Form (SPAPERS) and by the Source/Base Institution Year Form (SOABGIY).
     */
    @Column(name = "STVETHN_CODE")
    String code

    /**
     * This field specifies the ethnic group associated with the ethnic code.
     */
    @Column(name = "STVETHN_DESC")
    String description

    /**
     * EEOC Ethnic Code which is used in human resource system.
     */
    @Column(name = "STVETHN_EEOC_CODE")
    String ethnicCode

    /**
     * EDI/SPEEDE ethnicity code that equates to the ethnic code.  Refer to the SPEEDE Implementation Guide, with reference designator DMG05 for valid values.
     */
    @Column(name = "STVETHN_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * LMS (IA Plus) ethnic code that equates to this Banner ethnic code. Translation to this LMS code occurs during the LMS transaction feed from Banner FA.
     */
    @Column(name = "STVETHN_LMS_EQUIV")
    String lmsEquivalent

    /**
     * ETHNIC CODE: This field identifies the ethnic code defined by the U.S. government. The valid values are 1 - Not Hispanic or Latino, 2 - Hispanic or Latino, or null.
     */
    @Column(name = "STVETHN_ETHN_CDE")
    String ethnic

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVETHN_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVETHN
     */
    @Column(name = "STVETHN_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVETHN
     */
    @Column(name = "STVETHN_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FK1_STVETHN_INV_GORRACE_KEY
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVETHN_RACE_CDE", referencedColumnName = "GORRACE_RACE_CDE")
    ])
    Race race

    /**
     * Foreign Key : FK1_STVETHN_INV_STVETCT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVETHN_ETCT_CODE", referencedColumnName = "STVETCT_CODE")
    ])
    IpedsEthnicity ipedsEthnicity


    public String toString() {
        """Ethnicity[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					ethnicCode=$ethnicCode, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lmsEquivalent=$lmsEquivalent, 
					ethnic=$ethnic, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					race=$race, 
					ipedsEthnicity=$ipedsEthnicity]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Ethnicity)) return false
        Ethnicity that = (Ethnicity) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (ethnicCode != that.ethnicCode) return false
        if (electronicDataInterchangeEquivalent != that.electronicDataInterchangeEquivalent) return false
        if (lmsEquivalent != that.lmsEquivalent) return false
        if (ethnic != that.ethnic) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (race != that.race) return false
        if (ipedsEthnicity != that.ipedsEthnicity) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (ethnicCode != null ? ethnicCode.hashCode() : 0)
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lmsEquivalent != null ? lmsEquivalent.hashCode() : 0)
        result = 31 * result + (ethnic != null ? ethnic.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (race != null ? race.hashCode() : 0)
        result = 31 * result + (ipedsEthnicity != null ? ipedsEthnicity.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: true, maxSize: 30)
        ethnicCode(nullable: true, maxSize: 1)
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 1)
        lmsEquivalent(nullable: true, maxSize: 1)
        ethnic(nullable: true, maxSize: 1, inList: ["1", "2"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        race(nullable: true)
        ipedsEthnicity(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
