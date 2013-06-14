/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Activity Type Validation Table
 */
@Entity
@Table(name = "STVACTP")
class ActivityType implements Serializable {

    /**
     * Surrogate ID for STVACTP
     */
    @Id
    @Column(name = "STVACTP_SURROGATE_ID")
    @SequenceGenerator(name = "STVACTP_SEQ_GEN", allocationSize = 1, sequenceName = "STVACTP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVACTP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVACTP
     */
    @Version
    @Column(name = "STVACTP_VERSION")
    Long version

    /**
     * This field specifies the code for the type of activity.
     */
    @Column(name = "STVACTP_CODE")
    String code

    /**
     * This field specifies the description of the activity code.
     */
    @Column(name = "STVACTP_DESC")
    String description

    /**
     * System Required Indicator
     */
    @Column(name = "STVACTP_SYSTEM_REQ_IND")
    String systemRequiredIndicator

    /**
     * This system maintained field specifies the date this record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVACTP_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVACTP
     */
    @Column(name = "STVACTP_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVACTP
     */
    @Column(name = "STVACTP_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """ActivityType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					systemRequiredIndicator=$systemRequiredIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ActivityType)) return false
        ActivityType that = (ActivityType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
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
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 5)
        description(nullable: false, maxSize: 30)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    public static readonlyProperties = ['code']
}
