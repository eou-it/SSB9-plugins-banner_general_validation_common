/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type

/**
 * State Code Validation Table
 */

@Entity
@Table(name = "STVSTAT")
class State implements Serializable {

    /**
     * Surrogate ID for STVSTAT
     */
    @Id
    @Column(name = "STVSTAT_SURROGATE_ID")
    @SequenceGenerator(name = "STVSTAT_SEQ_GEN", allocationSize = 1, sequenceName = "STVSTAT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSTAT_SEQ_GEN")
    Long id

    /**
     * This field identifies the state code referenced in the Gen.  Person, Admissions, Gen.  Student, Student Billing, and Academic History Modules.
     */
    @Column(name = "STVSTAT_CODE", nullable = false, length = 3)
    String code

    /**
     * This field specifies the state associated with the state code.
     */
    @Column(name = "STVSTAT_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or deleted.
     */
    @Column(name = "STVSTAT_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This is the state equivalent code EDI will use for electronic transcripts for the BANNER2 state code matched on column STVSTAT_CODE.
     */
    @Column(name = "STVSTAT_EDI_EQUIV", length = 2)
    String ediEquiv

    /**
     * Statistics Canadian specific code.
     */
    @Column(name = "STVSTAT_STATSCAN_CDE", length = 5)
    String statscan

    /**
     * Free format code for Student IPEDS reporting.
     */
    @Column(name = "STVSTAT_IPEDS_CDE", length = 5)
    String ipeds

    /**
     * Version column which is used as a optimistic lock token for STVSTAT
     */
    @Version
    @Column(name = "STVSTAT_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for STVSTAT
     */
    @Column(name = "STVSTAT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVSTAT
     */
    @Column(name = "STVSTAT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """State[
					id=$id, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					ediEquiv=$ediEquiv, 
					statscan=$statscan, 
					ipeds=$ipeds, 
					version=$version, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, ]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true;
        if (!(o instanceof State)) return false;
        State that = (State) o;
        if (id != that.id) return false;
        if (code != that.code) return false;
        if (description != that.description) return false;
        if (lastModified != that.lastModified) return false;
        if (ediEquiv != that.ediEquiv) return false;
        if (statscan != that.statscan) return false;
        if (ipeds != that.ipeds) return false;
        if (version != that.version) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (dataOrigin != that.dataOrigin) return false;
        return true;
    }


    int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (ediEquiv != null ? ediEquiv.hashCode() : 0);
        result = 31 * result + (statscan != null ? statscan.hashCode() : 0);
        result = 31 * result + (ipeds != null ? ipeds.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        ediEquiv(nullable: true, maxSize: 2)
        statscan(nullable: true, maxSize: 5)
        ipeds(nullable: true, maxSize: 5)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


}
