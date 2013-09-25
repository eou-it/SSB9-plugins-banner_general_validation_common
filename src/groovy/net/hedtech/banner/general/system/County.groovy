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
 * County Code Validation Table
 */

@Entity
@Table(name = "STVCNTY")
class County implements Serializable {

    /**
     * Surrogate ID for STVCNTY
     */
    @Id
    @Column(name = "STVCNTY_SURROGATE_ID")
    @SequenceGenerator(name = "STVCNTY_SEQ_GEN", allocationSize = 1, sequenceName = "STVCNTY_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCNTY_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVCNTY
     */
    @Version
    @Column(name = "STVCNTY_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * This field identifies the county code referenced on the Quick Entry (SAAQUIK),  Identification (SPAIDEN) and Source/Base Institution Year (SOABGIY) Forms.
     */
    @Column(name = "STVCNTY_CODE", nullable = false, unique = true, length = 5)
    String code

    /**
     * This field specifies the county associated with the county code.
     */
    @Column(name = "STVCNTY_DESC", length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVCNTY_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVCNTY
     */
    @Column(name = "STVCNTY_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVCNTY
     */
    @Column(name = "STVCNTY_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """County[
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
        if (!(o instanceof County)) return false
        County that = (County) o
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
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}