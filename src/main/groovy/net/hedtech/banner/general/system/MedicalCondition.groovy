/** *****************************************************************************
 Copyright 2009-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Represents a medical condition.
 *
 */
@Entity
@Table(name = "STVMEDI")
@NamedQueries(value = [
        @NamedQuery(name = "MedicalCondition.fetchByCode",
                query = """ FROM MedicalCondition a WHERE a.code = :code """)
])
class MedicalCondition implements Serializable {

    @Id
    @Column(name = "STVMEDI_SURROGATE_ID")
    @SequenceGenerator(name = "STVMEDI_SEQ_GEN", allocationSize = 1, sequenceName = "STVMEDI_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVMEDI_SEQ_GEN")
    Long id

    @Column(name = "STVMEDI_ACTIVITY_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    @Column(name = "STVMEDI_USER_ID", nullable = true, length = 30)
    String lastModifiedBy

    @Column(name = "STVMEDI_DATA_ORIGIN", nullable = true, length = 30)
    String dataOrigin

    @Version
    @Column(name = "STVMEDI_VERSION", nullable = false)
    Long version

    @Column(name = "STVMEDI_CODE", nullable = false, length = 10)
    String code

    @Column(name = "STVMEDI_DESC", nullable = false, length = 30)
    String description

    public String toString() {
        "MedicalCondition[id=$id, code=$code, description=$description, dataOrigin=$dataOrigin, version=$version, lastModifiedBy=$lastModifiedBy, lastModified=$lastModified]"
    }

    static constraints = {
        code(maxSize: 10, blank: false)
        description(maxSize: 30, blank: false)
        dataOrigin(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true)
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof MedicalCondition)) return false;

        MedicalCondition that = (MedicalCondition) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}


