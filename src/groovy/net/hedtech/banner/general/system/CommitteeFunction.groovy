/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator

/**
 * Committee Function Code Table
 */

@Entity
@Table(name = "STVCOMF")
@NamedQueries(value = [
        @NamedQuery(name = "CommitteeFunction.fetchAllWithGuid", query = """ FROM CommitteeFunction n,GlobalUniqueIdentifier g where g.ldmName = 'campus-involvement-roles' AND g.domainKey = n.code """),
        @NamedQuery(name = "CommitteeFunction.fetchAllWithGuidByCodeInList", query = """ FROM CommitteeFunction n,GlobalUniqueIdentifier g where g.ldmName = 'campus-involvement-roles' AND g.domainKey = n.code and n.code in :codes """),
        @NamedQuery(name = "CommitteeFunction.fetchAllByCodeInList", query = """ FROM CommitteeFunction n where n.code in :codes """)
])
class CommitteeFunction implements Serializable {

    /**
     * Surrogate ID for STVCOMF
     */
    @Id
    @Column(name = "STVCOMF_SURROGATE_ID")
    @SequenceGenerator(name = "STVCOMF_SEQ_GEN", allocationSize = 1, sequenceName = "STVCOMF_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCOMF_SEQ_GEN")
    Long id

    /**
     * Committee Function type code.
     */
    @Column(name = "STVCOMF_CODE", nullable = false, length = 2)
    String code

    /**
     * Committee Function description.
     */
    @Column(name = "STVCOMF_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most current date a record was created or changed.
     */
    @Column(name = "STVCOMF_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for STVCOMF
     */
    @Version
    @Column(name = "STVCOMF_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVCOMF
     */
    @Column(name = "STVCOMF_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVCOMF
     */
    @Column(name = "STVCOMF_DATA_ORIGIN", length = 30)
    String dataOrigin


    public String toString() {
        "CommitteeFunction[id=$id, code=$code, description=$description, lastModified=$lastModified, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
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
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
}
