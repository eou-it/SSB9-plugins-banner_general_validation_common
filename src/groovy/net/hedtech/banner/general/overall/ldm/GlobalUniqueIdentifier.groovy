/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import javax.persistence.*

/**
 * GlobalUniqueIdentifier Table.
 */
@Entity
@Table(name = "GORGUID")
@NamedQueries(value = [
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNamesAndGuid",
                query = """FROM  GlobalUniqueIdentifier a
	  	                WHERE a.ldmName IN ('colleges', 'departments', 'divisions')
	  	                AND a.guid = :guid """),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNameAndGuid",
                query = """FROM  GlobalUniqueIdentifier a
	  	                WHERE a.ldmName = :ldmName
	  	                AND a.guid = :guid """),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNameAndDomainSurrogateId",
                query = """FROM GlobalUniqueIdentifier a
                        WHERE a.ldmName = :ldmName
                        AND a.domainId in (:domainSurrogateIds)"""),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNameAndDomainKeys",
                query = """FROM GlobalUniqueIdentifier a
                                WHERE a.ldmName = :ldmName
                                AND a.domainKey in (:domainKeys)"""),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNameAndDomainKey",
                query = """FROM GlobalUniqueIdentifier a
                                WHERE a.ldmName = :ldmName
                                AND a.domainKey =   :domainKey  """),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyLike",
                query = """FROM GlobalUniqueIdentifier a
                                WHERE a.ldmName = :ldmName
                                AND a.domainKey like (:domainKey)"""),
        @NamedQuery(name = "GlobalUniqueIdentifier.fetchByLdmNameAndDomainId",
                query = """FROM GlobalUniqueIdentifier a
                                WHERE a.ldmName = :ldmName
                                AND a.domainId =   :id  """)
])
class GlobalUniqueIdentifier implements Serializable {
    /**
     * Surrogate ID for GORGUID
     */
    @Id
    @Column(name = "GORGUID_SURROGATE_ID")
    @SequenceGenerator(name = "GORGUID_SEQ_GEN", allocationSize = 1, sequenceName = "GORGUID_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GORGUID_SEQ_GEN")
    Integer id

    /**
     * Optimistic lock token for GORGUID
     */
    @Version
    @Column(name = "GORGUID_VERSION")
    Integer version

    /**
     * GUID: Unique identifier across all database LDM objects.
     */
    @Column(name = "GORGUID_GUID")
    String guid

    /**
     * LDM NAME: The LDM name of this object.
     */
    @Column(name = "GORGUID_LDM_NAME")
    String ldmName

    /**
     * DOMAIN ID: The hibernate id index of this object within it's domain.
     */
    @Column(name = "GORGUID_DOMAIN_SURROGATE_ID")
    Long domainId

    /**
     * DOMAIN KEY: The Banner domain business key.
     */
    @Column(name = "GORGUID_DOMAIN_KEY")
    String domainKey

    /**
     * This field defines the most current date a record is added or changed.
     */
    @Column(name = "GORGUID_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: User who inserted or last update the data
     */
    @Column(name = "GORGUID_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "GORGUID_DATA_ORIGIN")
    String dataOrigin

    static constraints = {
        guid(nullable: false, maxSize: 36)
        ldmName(nullable: false, maxSize: 100)
        domainId(nullable: false)
        domainKey(nullable: true, maxSize: 100)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    public String toString() {
        """GlobalUniqueIdentifier[
		            id=$id,
		            version=$version,
					guid=$guid,
                    domainName=$ldmName,
                    domainId=$domainId,
                    domainKey=$domainKey,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof GlobalUniqueIdentifier)) return false
        GlobalUniqueIdentifier that = (GlobalUniqueIdentifier) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (guid != that.guid) return false
        if (ldmName != that.ldmName) return false
        if (domainId != that.domainId) return false
        if (domainKey != that.domainKey) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (ldmName != null ? ldmName.hashCode() : 0)
        result = 31 * result + (domainId != null ? domainId.hashCode() : 0)
        result = 31 * result + (domainKey != null ? domainKey.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static GlobalUniqueIdentifier fetchByGuid(ldmName, guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByGuid(guid)
        if (globalUniqueIdentifier?.ldmName != ldmName) {
            globalUniqueIdentifier = null
        }
        return globalUniqueIdentifier
    }


    static GlobalUniqueIdentifier fetchByLdmNamesAndGuid(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNamesAndGuid').setString('guid', guid).uniqueResult()
        }

        return globalUniqueIdentifier
    }


    static GlobalUniqueIdentifier fetchByLdmNameAndGuid(String ldmName, String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNameAndGuid').setString('guid', guid).setString("ldmName", ldmName).uniqueResult()
        }

        return globalUniqueIdentifier
    }


    static GlobalUniqueIdentifier fetchByLdmNameAndDomainId(String ldmName, Long id) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNameAndDomainId').setLong('id', id).setString("ldmName", ldmName).uniqueResult()
        }

        return globalUniqueIdentifier
    }


    static List<GlobalUniqueIdentifier> fetchByLdmNameAndDomainSurrogateIds(ldmName, surrogateIds) {
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNameAndDomainSurrogateId').setString('ldmName', ldmName).setParameterList('domainSurrogateIds', surrogateIds).list();
        }

        return globalUniqueIdentifierList
    }


    static List<GlobalUniqueIdentifier> fetchByLdmNameAndDomainKeys(ldmName, domainKeys) {
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNameAndDomainKeys').setString('ldmName', ldmName).setParameterList('domainKeys', domainKeys).list();
        }

        return globalUniqueIdentifierList
    }


    static List<GlobalUniqueIdentifier> fetchByLdmNameAndDomainKey(ldmName, domainKey) {
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmNameAndDomainKey').setString('ldmName', ldmName)
                    .setString('domainKey', domainKey).list();
        }

        return globalUniqueIdentifierList
    }

    static List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainKeyLike(ldmName, domainKey) {
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = GlobalUniqueIdentifier.withSession { session ->
            session.getNamedQuery('GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyLike').setString('ldmName', ldmName)
                    .setString('domainKey', '%'+domainKey+'%').list();
        }
        return globalUniqueIdentifierList
    }
}
