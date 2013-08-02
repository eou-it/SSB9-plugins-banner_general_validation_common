/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.apache.commons.lang.StringUtils

import javax.persistence.*

/**
 * Race Rules Table.
 */
@Entity
@Table(name = "GV_GORRACE")
@NamedQueries(value = [
@NamedQuery(name = "Race.fetchAllLikeRaceOrDescription",
query = """FROM  Race a
		   WHERE a.race LIKE :filter
		      OR a.description LIKE :filter
		   ORDER BY a.race
	""")
])
class Race implements Serializable {

    /**
     * Surrogate ID for GORRACE
     */
    @Id
    @Column(name = "GORRACE_SURROGATE_ID")
    @SequenceGenerator(name = "GORRACE_SEQ_GEN", allocationSize = 1, sequenceName = "GORRACE_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GORRACE_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GORRACE
     */
    @Version
    @Column(name = "GORRACE_VERSION")
    Long version

    /**
     * RACE CODE: This field identifies the race code referenced on the Person Identification Forms. The codes are institution defined.
     */
    @Column(name = "GORRACE_RACE_CDE")
    String race

    /**
     * RACE DESCRIPTION: This field specifies the race description associated with the race code.
     */
    @Column(name = "GORRACE_DESC")
    String description

    /**
     * EDI EQUIVALENT: The EDI/SPEEDE race code that equates to the race code.
     */
    @Column(name = "GORRACE_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * LMS EQUIVALENT: The LMS (IA Plus) race code that equates to this Banner race code. Translation to this LMS code occurs during the LMS transaction feed from Banner FA.
     */
    @Column(name = "GORRACE_LMS_EQUIV")
    String lmsEquivalent

    /**
     * ACTIVITY DATE: This field defines the most current date a record is added or changed.
     */
    @Column(name = "GORRACE_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: User who inserted or last updated the data.
     */
    @Column(name = "GORRACE_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row.
     */
    @Column(name = "GORRACE_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FKV_GORRACE_INV_GTVRRAC_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GORRACE_RRAC_CODE", referencedColumnName = "GTVRRAC_CODE")
    ])
    RegulatoryRace regulatoryRace


    public String toString() {
        """Race[
					id=$id, 
					version=$version, 
					race=$race, 
					description=$description, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lmsEquivalent=$lmsEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					regulatoryRace=$regulatoryRace]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Race)) return false
        Race that = (Race) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (race != that.race) return false
        if (description != that.description) return false
        if (electronicDataInterchangeEquivalent != that.electronicDataInterchangeEquivalent) return false
        if (lmsEquivalent != that.lmsEquivalent) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (regulatoryRace != that.regulatoryRace) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (race != null ? race.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lmsEquivalent != null ? lmsEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (regulatoryRace != null ? regulatoryRace.hashCode() : 0)
        return result
    }


    static constraints = {
        race(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 60)
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 1)
        lmsEquivalent(nullable: true, maxSize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        regulatoryRace(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['race']

    // Used for lookups
    public static Object fetchAllLikeRaceOrDescription() {
        def returnObj = [list: Race.list().sort { it.race }]
        return returnObj
    }

    // Used for lookups
    public static Object fetchAllLikeRaceOrDescription(String filter) {
        def sqlFilter
        def result = []

        // If a wildcard character exists, use the given string. Otherwise wrap the string with wildcards characters.
        if (filter) {
            if (StringUtils.contains(filter, "%")) {
                sqlFilter = filter.toUpperCase()
            } else {
                sqlFilter = ("%" + filter + "%").toUpperCase()
            }

            result = Race.withSession { session ->
                session.getNamedQuery('Race.fetchAllLikeRaceOrDescription').setString('filter', sqlFilter).list()
            }
        }

        return result
    }

}