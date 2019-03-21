/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

@NamedQueries(value = [
        @NamedQuery(name = "Campus.fetchCampusCodeNotInList",
                query = """Select code FROM Campus a
        WHERE a.code not in (:campCodeList)"""),
        @NamedQuery(name = "Campus.fetchAllCodeTimeZoneID",
                query = """select code, timeZoneID from Campus a""")
])
/**
 * Campus Validation Table
 */
@Entity
@Table(name = "STVCAMP")
class Campus implements Serializable {

    /**
     * Surrogate ID for STVCAMP.
     */
    @Id
    @Column(name = "STVCAMP_SURROGATE_ID")
    @SequenceGenerator(name = "STVCAMP_SEQ_GEN", allocationSize = 1, sequenceName = "STVCAMP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCAMP_SEQ_GEN")
    Long id

    /**
     * STVCAMP_DICD_CODE: District Identifier Code validated by form GTVDICD.
     */
    @Column(name = "STVCAMP_CODE", nullable = false, unique = true, length = 3)
    String code

    /**
     * This field defines the institution"s campus associated with the campus code.
     */
    @Column(name = "STVCAMP_DESC", length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVCAMP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Foreign Key : FKV_STVCAMP_INV_GTVDICD_CODE
     */
    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "STVCAMP_DICD_CODE", referencedColumnName = "GTVDICD_CODE")
    ])
    GeographicRegionAsCostCenterInformationByDisctirctOrDivision districtIdentifierCode

    /**
     * Column for storing last modified by for STVCAMP.
     */
    @Column(name = "STVCAMP_USER_ID", nullable = false, length = 30)
    String lastModifiedBy

    /**
     * Optimistic Lock Token for STVCAMP.
     */
    @Version
    @Column(name = "STVCAMP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Column for storing data origin for STVCAMP.
     */
    @Column(name = "STVCAMP_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * UTC Offset for campus
     */
    @Column(name = "STVCAMP_UTC_OFFSET", length = 6)
    String utcOffset

    /**
     * TIMEZONE NAME: This field identifies the time zone in which campus resides.
     * @see java.util.TimeZone#getTimeZone(String)
     */
    @Column(name = "STVCAMP_TIMEZONE_NAME")
    String timeZoneID


    public String toString() {
        "Campus[id=$id, code=$code, description=$description, " +
                "lastModified=$lastModified, " +
                "districtIdentifierCode=$districtIdentifierCode, " +
                "lastModifiedBy=$lastModifiedBy, " +
                "version=$version, " +
                "dataOrigin=$dataOrigin, " +
                "utcOffset=$utcOffset, " +
                "timeZoneID=$timeZoneID ]"
    }

    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: true, maxSize: 30)
        districtIdentifierCode(nullable: true)
        utcOffset(nullable: true)
        timeZoneID(nullable: true)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof Campus)) return false

        Campus campus = (Campus) o

        if (code != campus.code) return false
        if (dataOrigin != campus.dataOrigin) return false
        if (description != campus.description) return false
        if (districtIdentifierCode != campus.districtIdentifierCode) return false
        if (id != campus.id) return false
        if (lastModified != campus.lastModified) return false
        if (lastModifiedBy != campus.lastModifiedBy) return false
        if (version != campus.version) return false
        if (utcOffset != campus.utcOffset) return false
        if (timeZoneID != campus.timeZoneID) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (districtIdentifierCode != null ? districtIdentifierCode.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (utcOffset != null ? utcOffset.hashCode() : 0)
        result = 31 * result + (timeZoneID != null ? timeZoneID.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']


    public static List fetchCampusCodesNotInList(List campCodes) {

        def result = Campus.withSession { session ->
            session.getNamedQuery('Campus.fetchCampusCodeNotInList').setParameterList("campCodeList", campCodes).list()
        }
        return result
    }

}
