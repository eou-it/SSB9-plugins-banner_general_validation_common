/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Activity Category Validation Table
 */
@Entity
@Table(name = "STVACCG")
class ActivityCategory implements Serializable {

    /**
     * Surrogate ID for STVACCG
     */
    @Id
    @Column(name = "STVACCG_SURROGATE_ID")
    @SequenceGenerator(name = "STVACCG_SEQ_GEN", allocationSize = 1, sequenceName = "STVACCG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVACCG_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVACCG
     */
    @Version
    @Column(name = "STVACCG_VERSION")
    Long version

    /**
     * This field specifies the code for the category of activity.
     */
    @Column(name = "STVACCG_CODE")
    String code

    /**
     * This field specifies the description of the activity category code.
     */
    @Column(name = "STVACCG_DESC")
    String description

    /**
     * This system maintained field specifies the date this record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVACCG_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVACCG
     */
    @Column(name = "STVACCG_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVACCG
     */
    @Column(name = "STVACCG_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """ActivityCategory[
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
        if (!(o instanceof ActivityCategory)) return false
        ActivityCategory that = (ActivityCategory) o
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
        code(nullable: false, maxSize: 5)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    public static readonlyProperties = ['code']
}
