/** *******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import javax.persistence.*

@Entity
@Table(name = "STVACTC")
class StudentActivity implements Serializable {

    /**
     * Surrogate ID for STVACTC
     */
    @Id
    @Column(name = "STVACTC_SURROGATE_ID")
    @SequenceGenerator(name = "STVACTC_SEQ_GEN", allocationSize = 1, sequenceName = "STVACTC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVACTC_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVACTC
     */
    @Version
    @Column(name = "STVACTC_VERSION")
    Long version

    /**
     * This field identifies an activity code
     */
    @Column(name = "STVACTC_CODE")
    String code

    /**
     * This field specifies a student activity associated with the activity code.
     */
    @Column(name = "STVACTC_DESC")
    String description

    /**
     * This field identifies the most current date record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVACTC_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVACTC
     */
    @Column(name = "STVACTC_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVACTC
     */
    @Column(name = "STVACTC_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FK1_STVACTC_INV_STVACTP_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVACTC_ACTP_CODE", referencedColumnName = "STVACTP_CODE")
    ])
    ActivityType activityType

    /**
     * Foreign Key : FK1_STVACTC_INV_STVACCG_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVACTC_ACCG_CODE", referencedColumnName = "STVACCG_CODE")
    ])
    ActivityCategory activityCategory

    /**
     * Foreign Key : FK1_STVACTC_INV_STVLEAD_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVACTC_LEAD_CODE", referencedColumnName = "STVLEAD_CODE")
    ])
    Leadership leadership


    public String toString() {
        """StudentActivity[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					activityType=$activityType, 
					activityCategory=$activityCategory, 
					leadership=$leadership]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof StudentActivity)) return false
        StudentActivity that = (StudentActivity) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (activityType != that.activityType) return false
        if (activityCategory != that.activityCategory) return false
        if (leadership != that.leadership) return false
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
        result = 31 * result + (activityType != null ? activityType.hashCode() : 0)
        result = 31 * result + (activityCategory != null ? activityCategory.hashCode() : 0)
        result = 31 * result + (leadership != null ? leadership.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 8)
        description(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        activityType(nullable: true)
        activityCategory(nullable: true)
        leadership(nullable: true)
    }

    public static readonlyProperties = ['code']
}
