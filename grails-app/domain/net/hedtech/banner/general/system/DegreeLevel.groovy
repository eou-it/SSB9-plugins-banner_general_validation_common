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
 * Faculty Member Degree Level Validation Table
 */

@Entity
@Table(name = "STVDLEV")
class DegreeLevel implements Serializable {

    /**
     * Surrogate ID for STVDLEV
     */
    @Id
    @Column(name = "STVDLEV_SURROGATE_ID")
    @SequenceGenerator(name = "STVDLEV_SEQ_GEN", allocationSize = 1, sequenceName = "STVDLEV_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVDLEV_SEQ_GEN")
    Long id

    /**
     * Faculty member degree level code
     */
    @Column(name = "STVDLEV_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * Description of faculty member degree level code
     */
    @Column(name = "STVDLEV_DESC", nullable = false, length = 30)
    String description

    /**
     * The activity date
     */
    @Column(name = "STVDLEV_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Numeric Ranking Value
     */
    @Column(name = "STVDLEV_NUMERIC_VALUE", precision = 2)
    Integer numericValue

    /**
     * Version column which is used as a optimistic lock token for STVDLEV
     */
    @Version
    @Column(name = "STVDLEV_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Last Modified By column for STVDLEV
     */
    @Column(name = "STVDLEV_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVDLEV
     */
    @Column(name = "STVDLEV_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "DegreeLevel[id=$id, code=$code, description=$description, lastModified=$lastModified, numericValue=$numericValue, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        numericValue(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof DegreeLevel)) return false;

        DegreeLevel that = (DegreeLevel) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (numericValue != that.numericValue) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (numericValue != null ? numericValue.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}	                                     
