/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Regulatory Race Validation Table.
 */
@Entity
@Table(name = "GTVRRAC")
class RegulatoryRace implements Serializable {

    /**
     * Surrogate ID for GTVRRAC
     */
    @Id
    @Column(name = "GTVRRAC_SURROGATE_ID")
    @SequenceGenerator(name = "GTVRRAC_SEQ_GEN", allocationSize = 1, sequenceName = "GTVRRAC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVRRAC_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVRRAC
     */
    @Version
    @Column(name = "GTVRRAC_VERSION")
    Long version

    /**
     * REGULATORY RACE CODE: This field identifies the regulatory race code. The values will be used in data collection of race information required by U.S. regulations. Other countries with race data collection requirements can also use this field.
     */
    @Column(name = "GTVRRAC_CODE")
    String code

    /**
     * REGULATORY RACE DESCRIPTION: This field specifies the regulatory race description.
     */
    @Column(name = "GTVRRAC_DESC")
    String description

    /**
     * SYSTEM REQURED INICATOR: This field denotes whether the regulatory race code is system required.  Valid values are (Y)es and (N)o.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVRRAC_SYS_REQ_IND")
    Boolean systemRequiredIndicator

    /**
     * ACTIVITY DATE: This field defines the most current date a record is added or changed.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVRRAC_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: User who inserted or last updated the data.
     */
    @Column(name = "GTVRRAC_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row.
     */
    @Column(name = "GTVRRAC_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """RegulatoryRace[
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
        if (!(o instanceof RegulatoryRace)) return false
        RegulatoryRace that = (RegulatoryRace) o
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
        code(nullable: false, maxSize: 1)
        description(nullable: false, maxSize: 60)
        systemRequiredIndicator(nullable: false, maxSize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
