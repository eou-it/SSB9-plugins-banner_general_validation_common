/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Unit of Measure Validation Table
 */
@Entity
@Table(name = "GTVUOMS")
class UnitOfMeasure implements Serializable {

    /**
     * Surrogate ID for GTVUOMS
     */
    @Id
    @Column(name = "GTVUOMS_SURROGATE_ID")
    @SequenceGenerator(name = "GTVUOMS_SEQ_GEN", allocationSize = 1, sequenceName = "GTVUOMS_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVUOMS_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVUOMS
     */
    @Version
    @Column(name = "GTVUOMS_VERSION")
    Long version

    /**
     * A code representing the unit of measure used in height and weight.
     */
    @Column(name = "GTVUOMS_CODE")
    String code

    /**
     * Descriptive text about the unit of measure code.
     */
    @Column(name = "GTVUOMS_DESC")
    String description

    /**
     * The date that the record was inserted or last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVUOMS_ACTIVITY_DATE")
    Date lastModified

    /**
     * The ORACLE user id of the person last updating this record.
     */
    @Column(name = "GTVUOMS_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVUOMS
     */
    @Column(name = "GTVUOMS_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """UnitOfMeasure[
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
        if (!(o instanceof UnitOfMeasure)) return false
        UnitOfMeasure that = (UnitOfMeasure) o
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
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
