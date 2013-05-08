/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.*

/**
 * Zip Code Validation Table
 */
@Entity
@Table(name = "GTVZIPC")
class Zip implements Serializable {

    /**
     * Surrogate ID for GTVZIPC
     */
    @Id
    @Column(name = "GTVZIPC_SURROGATE_ID")
    @SequenceGenerator(name = "GTVZIPC_SEQ_GEN", allocationSize = 1, sequenceName = "GTVZIPC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVZIPC_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVZIPC
     */
    @Version
    @Column(name = "GTVZIPC_VERSION")
    Long version

    /**
     * This field contains the zip code defined by the postal service
     */
    @Column(name = "GTVZIPC_CODE")
    String code

    /**
     * This field defines the city most defined by the zip code
     */
    @Column(name = "GTVZIPC_CITY")
    String city

    /**
     * ACTIVITY DATE: Date on which the row was created or last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVZIPC_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: User ID of the person who created or last updated the row.
     */
    @Column(name = "GTVZIPC_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVZIPC
     */
    @Column(name = "GTVZIPC_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FKV_GTVZIPC_INV_STVSTAT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVZIPC_STAT_CODE", referencedColumnName = "STVSTAT_CODE")
    ])
    State state

    /**
     * Foreign Key : FKV_GTVZIPC_INV_STVNATN_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVZIPC_NATN_CODE", referencedColumnName = "STVNATN_CODE")
    ])
    Nation nation

    /**
     * Foreign Key : FKV_GTVZIPC_INV_STVCNTY_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVZIPC_CNTY_CODE", referencedColumnName = "STVCNTY_CODE")
    ])
    County county


    public String toString() {
        """Zip[
					id=$id, 
					version=$version, 
					code=$code, 
					city=$city, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					state=$state, 
					nation=$nation, 
					county=$county]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Zip)) return false
        Zip that = (Zip) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (city != that.city) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (state != that.state) return false
        if (nation != that.nation) return false
        if (county != that.county) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (city != null ? city.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (state != null ? state.hashCode() : 0)
        result = 31 * result + (nation != null ? nation.hashCode() : 0)
        result = 31 * result + (county != null ? county.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 30)
        city(nullable: false, maxSize: 50)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        state(nullable: true)
        nation(nullable: true)
        county(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code', 'city']

    /**
     * Query finder for advanced search GTQZIPC
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def sourceBackgroundInstitutionQuery = finderByAll().find(filterData, pagingAndSortParams)
        return sourceBackgroundInstitutionQuery
    }


    def private static finderByAll = {
        def query = """FROM  Zip a """

        return new DynamicFinder(Zip.class, query, "a")
    }
}
