/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

/**
 * Proxy Access Code Validation Table
 */
@Entity
@Table(name = "GTVSYST")
class ProxyAccessSystem implements Serializable{
/**
 * Surrogate ID for GTVSYST
 */
    @Id
    @Column(name = "GTVSYST_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSYST_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSYST_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSYST_SEQ_GEN")
    Long id

    /**
     * Proxy Access Code.
     */
    @Column(name = "GTVSYST_CODE", nullable = false, length = 30)
    String code

    /**
     * Proxy Access Code Description.
     */
    @Column(name = "GTVSYST_DESC",  length = 60)
    String description

    /**
     * Proxy Access System Required indicator.
     */
    @Column(name = "GTVSYST_SYSTEM_REQ_IND",  length = 1)
    String systemReqInd

    /**
     * Proxy Access Code Activity Date.
     */
    @Column(name = "GTVSYST_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for GTVSYST
     */
    @Version
    @Column(name = "GTVSYST_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for GTVSYST
     */
    @Column(name = "GTVSYST_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for GTVSYST
     */
    @Column(name = "GTVSYST_DATA_ORIGIN", length = 30)
    String dataOrigin

    public String toString() {
        "ProxyAccessSystem[id=$id, code=$code, description=$description, systemReqInd=$systemReqInd, lastModified=$lastModified, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 30)
        description(nullable: true, maxSize: 60)
        systemReqInd(nullable: true, maxSize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof ProxyAccessSystem)) return false;

        ProxyAccessSystem proxyAccessSystem = (ProxyAccessSystem) o;

        if (code != proxyAccessSystem.code) return false;
        if (dataOrigin != proxyAccessSystem.dataOrigin) return false;
        if (description != proxyAccessSystem.description) return false;
        if (systemReqInd != proxyAccessSystem.systemReqInd) return false;
        if (id != proxyAccessSystem.id) return false;
        if (lastModified != proxyAccessSystem.lastModified) return false;
        if (lastModifiedBy != proxyAccessSystem.lastModifiedBy) return false;
        if (version != proxyAccessSystem.version) return false;

        return true;
    }

    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (systemReqInd != null ? systemReqInd.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
