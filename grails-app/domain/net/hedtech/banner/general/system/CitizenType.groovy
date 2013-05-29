/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Citizen Type Validation Table
 */
@Entity
@Table(name = "STVCITZ")
class CitizenType implements Serializable {

    /**
     * Surrogate ID for STVCITZ
     */
    @Id
    @Column(name = "STVCITZ_SURROGATE_ID")
    @SequenceGenerator(name = "STVCITZ_SEQ_GEN", allocationSize = 1, sequenceName = "STVCITZ_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCITZ_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVCITZ
     */
    @Version
    @Column(name = "STVCITZ_VERSION")
    Long version

    /**
     * Citizen Type Code
     */
    @Column(name = "STVCITZ_CODE")
    String code

    /**
     * Description of Citizen Type Code
     */
    @Column(name = "STVCITZ_DESC")
    String description

    /**
     * Citizen Indicator,Y=citizen
     */
    @Type(type = "yes_no")
    @Column(name = "STVCITZ_CITIZEN_IND")
    Boolean citizenIndicator

    /**
     * EDI Citizenship Code
     */
    @Column(name = "STVCITZ_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * Activity Date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVCITZ_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVCITZ
     */
    @Column(name = "STVCITZ_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVCITZ
     */
    @Column(name = "STVCITZ_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """CitizenType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					citizenIndicator=$citizenIndicator, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CitizenType)) return false
        CitizenType that = (CitizenType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (citizenIndicator != that.citizenIndicator) return false
        if (electronicDataInterchangeEquivalent != that.electronicDataInterchangeEquivalent) return false
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
        result = 31 * result + (citizenIndicator != null ? citizenIndicator.hashCode() : 0)
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        citizenIndicator(nullable: false)
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 2)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
