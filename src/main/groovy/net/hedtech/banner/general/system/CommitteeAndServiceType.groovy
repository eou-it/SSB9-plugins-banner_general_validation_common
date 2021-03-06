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
 * Committee Type Code Table
 */

@Entity
@Table(name = "STVCOMT")
class CommitteeAndServiceType implements Serializable {

    /**
     * Surrogate ID for STVCOMT
     */
    @Id
    @Column(name = "STVCOMT_SURROGATE_ID")
    @SequenceGenerator(name = "STVCOMT_SEQ_GEN", allocationSize = 1, sequenceName = "STVCOMT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCOMT_SEQ_GEN")
    Long id

    /**
     * Committee type code.
     */
    @Column(name = "STVCOMT_CODE", nullable = false, length = 6)
    String code

    /**
     * Committee type description.
     */
    @Column(name = "STVCOMT_DESC", nullable = false, length = 30)
    String description

    /**
     * This field indicates whether the committee type will appear on transcript.
     */
    @Column(name = "STVCOMT_TRANS_PRINT", length = 1)
    String transactionPrint

    /**
     * This field identifies the most current date a record was created or changed.
     */
    @Column(name = "STVCOMT_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for STVCOMT
     */
    @Version
    @Column(name = "STVCOMT_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVCOMT
     */
    @Column(name = "STVCOMT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVCOMT
     */
    @Column(name = "STVCOMT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "CommitteeAndServiceType[id=$id, code=$code, description=$description, transactionPrint=$transactionPrint, lastModified=$lastModified, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        transactionPrint(nullable: true, maxSize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof CommitteeAndServiceType)) return false;

        CommitteeAndServiceType that = (CommitteeAndServiceType) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (transactionPrint != that.transactionPrint) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (transactionPrint != null ? transactionPrint.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

}
