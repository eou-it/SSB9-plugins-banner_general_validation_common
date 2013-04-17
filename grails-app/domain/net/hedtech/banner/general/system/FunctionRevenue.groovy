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
 * Function Revenue Validation Table
 */

@Entity
@Table(name = "GTVREVN")
class FunctionRevenue implements Serializable {

    /**
     * Surrogate ID for GTVREVN
     */
    @Id
    @Column(name = "GTVREVN_SURROGATE_ID")
    @SequenceGenerator(name = "GTVREVN_SEQ_GEN", allocationSize = 1, sequenceName = "GTVREVN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVREVN_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVREVN
     */
    @Version
    @Column(name = "GTVREVN_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Function Revenue Code.
     */
    @Column(name = "GTVREVN_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * Function Revenue Code Description.
     */
    @Column(name = "GTVREVN_DESC", nullable = false, length = 30)
    String description

    /**
     * Date the Revenue Code was created or last modified.
     */
    @Column(name = "GTVREVN_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVREVN
     */
    @Column(name = "GTVREVN_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVREVN
     */
    @Column(name = "GTVREVN_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """FunctionRevenue[
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
        if (!(o instanceof FunctionRevenue)) return false
        FunctionRevenue that = (FunctionRevenue) o
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
