/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Duration Unit Code Validation Table
 */
@Entity
@Table(name = "GTVDUNT")
@NamedQueries(value = [
@NamedQuery(name = "DurationUnit.fetchByCodeOrDescriptionILike",
query = """SELECT a.code,
                  a.description
             FROM DurationUnit a
		    WHERE (a.code like :filter OR UPPER(a.description) like :filter)
		    ORDER BY UPPER(a.description)""")
])
class DurationUnit implements Serializable {

    @Id
    @Column(name = "GTVDUNT_SURROGATE_ID")
    @SequenceGenerator(name = "GTVDUNT_SEQ_GEN", allocationSize = 1, sequenceName = "GTVDUNT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVDUNT_SEQ_GEN")
    Long id

    /**
     * Duration Unit Code: The Duration Unit Code
     */
    @Column(name = "GTVDUNT_CODE", nullable = false, length = 4)
    String code

    @Column(name = "GTVDUNT_DESC", nullable = false, length = 30)
    String description

    /**
     * Represents the number of days that one duration unit would equate to (ie 1 week equals 7 days)
     */
    @Column(name = "GTVDUNT_NUMBER_OF_DAYS", nullable = false, precision = 7, scale = 2)
    Double numberOfDays

    @Column(name = "GTVDUNT_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    @Column(name = "GTVDUNT_USER_ID")
    String lastModifiedBy

    /**
     * Voice Response Message Number
     */
    @Column(name = "GTVDUNT_VR_MSG_NO", precision = 6)
    Integer vrMsgNo

    @Version
    @Column(name = "GTVDUNT_VERSION", nullable = false, precision = 19)
    Long version

    @Column(name = "GTVDUNT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "DurationUnitCode[id=$id, code=$code, description=$description, numberOfDays=$numberOfDays, lastModified=$lastModified, lastModifiedBy=$lastModifiedBy, vrMsgNo=$vrMsgNo, version=$version, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        numberOfDays(nullable: false)
        vrMsgNo(nullable: true)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof DurationUnit)) return false

        DurationUnit that = (DurationUnit) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (id != that.id) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (numberOfDays != that.numberOfDays) return false
        if (version != that.version) return false
        if (vrMsgNo != that.vrMsgNo) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (numberOfDays != null ? numberOfDays.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (vrMsgNo != null ? vrMsgNo.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']


    public static List fetchByCodeOrDescriptionILike(String filter) {
        def durationUnits = DurationUnit.withSession {session ->
            session.getNamedQuery('DurationUnit.fetchByCodeOrDescriptionILike').setString('filter', filter).list()
        }
        return durationUnits
    }

}
