/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Employment Type Validation Table
 */
@Entity
@Table(name = "STVEMPT")
class EmploymentType implements Serializable {

    /**
     * Surrogate ID for STVEMPT
     */
    @Id
    @Column(name = "STVEMPT_SURROGATE_ID")
    @SequenceGenerator(name = "STVEMPT_SEQ_GEN", allocationSize = 1, sequenceName = "STVEMPT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVEMPT_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVEMPT
     */
    @Version
    @Column(name = "STVEMPT_VERSION")
    Long version

    /**
     * This field identifies Employment Type code referenced by the GOAINTL form.
     */
    @Column(name = "STVEMPT_CODE")
    String code

    /**
     * This field defines the Employment Type associated with the Employment type code.
     */
    @Column(name = "STVEMPT_DESC")
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVEMPT_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVEMPT
     */
    @Column(name = "STVEMPT_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVEMPT
     */
    @Column(name = "STVEMPT_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """EmploymentType[
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
        if (!(o instanceof EmploymentType)) return false
        EmploymentType that = (EmploymentType) o
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
        code(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
