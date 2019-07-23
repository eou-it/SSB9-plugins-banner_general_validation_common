/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Proxy Access System Option Type validation table
 */
@Entity
@Table(name = "GTVOTYP")
@NamedQueries(value = [
        @NamedQuery(name = "ProxyAccessSystemOptionType.fetchByCode",
                query = """FROM  ProxyAccessSystemOptionType a
                WHERE a.code = :code
            """),
        @NamedQuery(name = "ProxyAccessSystemOptionType.fetchByCodeAndSystemCode",
                query = """FROM  ProxyAccessSystemOptionType a
                WHERE a.code = :code
                AND a.systemCode = :systemCode
            """),
        @NamedQuery(name = "ProxyAccessSystemOptionType.fetchBySystemCode",
                query = """FROM  ProxyAccessSystemOptionType a
                WHERE a.systemCode = :systemCode
            """)
])
class ProxyAccessSystemOptionType implements Serializable {
    /**
     * Surrogate Id for GTVOTYP
     */
    @Id
    @Column(name = "GTVOTYP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVOTYP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVOTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVOTYP_SEQ_GEN")
    Long id

    /**
     * Proxy Access System Code
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVOTYP_SYST_CODE", referencedColumnName = "GTVSYST_CODE")
    ])
    ProxyAccessSystem  systemCode

    /**
     * Proxy Access code
     */
    @Column(name = "GTVOTYP_CODE", nullable = false, length =30)
    String code

    /**
     * Proxy Access description
     */
    @Column(name = "GTVOTYP_DESC", length = 60)
    String description

    /**
     * Proxy Access data type
     */
    @Column(name = "GTVOTYP_DATA_TYPE", nullable = false, length = 20)
    String proxyDataType

    /**
     * Proxy Access System Levl code
     */
    @Column(name = "GTVOTYP_SYSTEM_LEVEL_CDE", nullable = false, length = 1)
    String sytemLevelCode

    /**
     * Proxy Access Default Option
     */
    @Column(name = "GTVOTYP_OPTION_DEFAULT", nullable = false, length = 60)
    String proxyOptdefault

    /**
     * Proxy Access System Required Indicator
     */
    @Column(name = "GTVOTYP_SYSTEM_REQ_IND", length = 1)
    String systemReqInd

    /**
     * Proxy Access Acitivity Date
     */
    @Column(name = "GTVOTYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for GTVOTYP
     */
    @Version
    @Column(name = "GTVOTYP_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for GTVOTYP
     */
    @Column(name = "GTVOTYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Created Date by GTVOTYP
     */
    @Column(name = "GTVOTYP_CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date createdDate

    /**
     * Created User of GTVOTYP
     */
    @Column(name = "GTVOTYP_CREATE_USER", length = 30)
    String createdBy

    /**
     * Data Origin column for GTVOTYP
     */
    @Column(name = "GTVOTYP_DATA_ORIGIN", length = 30)
    String dataOrigin

    public String toString() {
        """ProxyAccessSystemOptionType[
                id=$id,
                code=$code,
                systemCode=$systemCode,
                description=$description,
                proxyDataType=$proxyDataType,
                sytemLevelCode=$sytemLevelCode,
                proxyOptdefault=$proxyOptdefault,
                systemReqInd=$systemReqInd,
                createdDate=$createdDate,
                createdBy=$createdBy,
                lastModified=$lastModified,
                version=$version,
                lastModifiedBy=$lastModifiedBy,
                dataOrigin=$dataOrigin]"""
    }

    static constraints = {
        code(nullable: false, maxSize: 30)
        systemCode(nullable: false, maxSize: 30)
        description(nullable: false, maxSize: 60)
        proxyDataType(nullable: false, maxSize: 20)
        sytemLevelCode(nullable: false, maxSize: 1)
        proxyOptdefault(nullable: false, maxSize: 60)
        systemReqInd(nullable: true, maxSize: 1)
        createdDate(nullable: true)
        createdBy(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof ProxyAccessSystemOptionType)) return false;

        ProxyAccessSystemOptionType proxyAccessSystemOptionType = (ProxyAccessSystemOptionType) o;

        if (code != proxyAccessSystemOptionType.code) return false;
        if (systemCode != proxyAccessSystemOptionType.systemCode) return false;
        if (dataOrigin != proxyAccessSystemOptionType.dataOrigin) return false;
        if (description != proxyAccessSystemOptionType.description) return false;
        if (proxyDataType != proxyAccessSystemOptionType.proxyDataType) return false;
        if (sytemLevelCode != proxyAccessSystemOptionType.sytemLevelCode) return false;
        if (proxyOptdefault != proxyAccessSystemOptionType.proxyOptdefault) return false;
        if (systemReqInd != proxyAccessSystemOptionType.systemReqInd) return false;
        if (createdDate != proxyAccessSystemOptionType.createdDate) return false;
        if (createdBy != proxyAccessSystemOptionType.createdBy) return false;
        if (id != proxyAccessSystemOptionType.id) return false;
        if (lastModified != proxyAccessSystemOptionType.lastModified) return false;
        if (lastModifiedBy != proxyAccessSystemOptionType.lastModifiedBy) return false;
        if (version != proxyAccessSystemOptionType.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (systemCode != null ? systemCode.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (proxyDataType != null ? proxyDataType.hashCode() : 0);
        result = 31 * result + (sytemLevelCode != null ? sytemLevelCode.hashCode() : 0);
        result = 31 * result + (proxyOptdefault != null ? proxyOptdefault.hashCode() : 0);
        result = 31 * result + (systemReqInd != null ? systemReqInd.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

	public static ProxyAccessSystemOptionType fetchByCode(String code) {
        def proxyAccessSystemOptionType = ProxyAccessSystemOptionType.withSession { session ->
            session.getNamedQuery(
                    'ProxyAccessSystemOptionType.fetchByCode').setString('code', code).list()[0]
        }
        return proxyAccessSystemOptionType
    }


    public static ProxyAccessSystemOptionType fetchByCodeAndSystemCode( String code, String systemCode ) {
        def proxyAccessSystemOptionType = ProxyAccessSystemOptionType.withSession { session ->
            session.getNamedQuery(
                    'ProxyAccessSystemOptionType.fetchByCodeAndSystemCode' ).setString( 'code', code ).setString( 'systemCode', systemCode ).list()[0]
        }
        return proxyAccessSystemOptionType
    }

    public static List fetchBySystemCode( String systemCode ) {
        def proxyAccessSystemOptionType = ProxyAccessSystemOptionType.withSession { session ->
            session.getNamedQuery(
                    'ProxyAccessSystemOptionType.fetchBySystemCode' ).setString( 'systemCode', systemCode ).list()
        }
        return proxyAccessSystemOptionType
    }
}
