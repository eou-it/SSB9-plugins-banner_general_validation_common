/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Leadership Validation Table
 */
@Entity
@Table(name = "STVLEAD")
class Leadership implements Serializable {

    /**
     * Surrogate ID for STVLEAD
     */
    @Id
    @Column(name = "STVLEAD_SURROGATE_ID")
    @SequenceGenerator(name = "STVLEAD_SEQ_GEN", allocationSize = 1, sequenceName = "STVLEAD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVLEAD_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVLEAD
     */
    @Version
    @Column(name = "STVLEAD_VERSION")
    Long version

    /**
     * This field specifies the code for the leadership.
     */
    @Column(name = "STVLEAD_CODE")
    String code

    /**
     * This field specifies the description of the leadership code.
     */
    @Column(name = "STVLEAD_DESC")
    String description

    /**
     * This system maintained field specifies the date this record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVLEAD_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVLEAD
     */
    @Column(name = "STVLEAD_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVLEAD
     */
    @Column(name = "STVLEAD_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """Leadership[
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
        if (!(o instanceof Leadership)) return false
        Leadership that = (Leadership) o
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
