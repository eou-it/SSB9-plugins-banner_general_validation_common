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
import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

/**
 * Fee Status Code Table
 */

@Entity
@Table(name = "GTVFEES")
class FeeStatus implements Serializable {

    /**
     * Surrogate ID for GTVFEES
     */
    @Id
    @Column(name = "GTVFEES_SURROGATE_ID")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "net.hedtech.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
    Long id

    /**
     * Optimistic lock token for GTVFEES
     */
    @Version
    @Column(name = "GTVFEES_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Fee Status Code.
     */
    @Column(name = "GTVFEES_CODE", nullable = false, unique = true, length = 5)
    String code

    /**
     * Fee Status Code Description.
     */
    @Column(name = "GTVFEES_DESC", nullable = false, length = 30)
    String description

    /**
     * Indicator for whether or not person paid for function.
     */
    @Column(name = "GTVFEES_PAID_IND", length = 1)
    String paidIndicator

    /**
     * Date Fee Status code was last created or modified.
     */
    @Column(name = "GTVFEES_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVFEES
     */
    @Column(name = "GTVFEES_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVFEES
     */
    @Column(name = "GTVFEES_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """FeeStatus[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					paidIndicator=$paidIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof FeeStatus)) return false
        FeeStatus that = (FeeStatus) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (paidIndicator != that.paidIndicator) return false
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
        result = 31 * result + (paidIndicator != null ? paidIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 5)
        description(nullable: false, maxSize: 30)
        paidIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
