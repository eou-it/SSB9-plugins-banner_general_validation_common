/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Expense Code Validation Table
 */

@Entity
@Table(name = "GTVEXPN")
class Expense implements Serializable {

    /**
     * Surrogate ID for GTVEXPN
     */
    @Id
    @Column(name = "GTVEXPN_SURROGATE_ID")
    @SequenceGenerator(name = "GTVEXPN_SEQ_GEN", allocationSize = 1, sequenceName = "GTVEXPN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(generator = "GTVEXPN_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVEXPN
     */
    @Version
    @Column(name = "GTVEXPN_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Expense Code.
     */
    @Column(name = "GTVEXPN_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * Expense Code Description.
     */
    @Column(name = "GTVEXPN_DESC", nullable = false, length = 30)
    String description

    /**
     * Expense Code Activity Date.
     */
    @Column(name = "GTVEXPN_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVEXPN
     */
    @Column(name = "GTVEXPN_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVEXPN
     */
    @Column(name = "GTVEXPN_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Expense[
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
        if (!(o instanceof Expense)) return false
        Expense that = (Expense) o
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
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
