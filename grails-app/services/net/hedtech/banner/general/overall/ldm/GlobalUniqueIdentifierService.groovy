/*******************************************************************************
 Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.service.ServiceBase

/**
 * Service class for GlobalUniqueIdentifier responsible for
 * interacting with the Entity GlobalUniqueIdentifier, and
 */
class GlobalUniqueIdentifierService extends ServiceBase {

    public static final String API = "api"

    /**
     * fetch GlobalUniqueIdentifier info based on ldmName and guid
     * @param ldmName
     * @param guid
     * @return GlobalUniqueIdentifier
     */
    GlobalUniqueIdentifier fetchByLdmNameAndGuid(String ldmName, String guid) {
        return GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, guid)
    }

    /**
     * fetch GlobalUniqueIdentifier info based on ldmName and domainId
     * @param ldmName
     * @param domainId
     * @return GlobalUniqueIdentifier
     */
    GlobalUniqueIdentifier fetchByLdmNameAndDomainId(String ldmName, Long domainId) {
        return GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(ldmName, domainId)
    }

    /**
     * fetch GlobalUniqueIdentifier lists based on ldmName
     * @param ldmName
     * @return Long
     */
    @Deprecated
    List<GlobalUniqueIdentifier> fetchByLdmName(String ldmName) {
        return GlobalUniqueIdentifier.fetchByLdmName(ldmName)
    }

    /**
     * fetch the total count based on the LDM Name
     * @param ldmName
     * @return Long
     */
    Long fetchCountByLdmName(String ldmName) {
        return GlobalUniqueIdentifier.fetchCountByLdmName(ldmName)
    }

    /**
     * fetch GlobalUniqueIdentifier info bases on LdmName and DomainKey
     * @param ldmName
     * @param domainKey
     * @return
     */
    GlobalUniqueIdentifier fetchByDomainKeyAndLdmName(domainKey, ldmName) {
        return GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(domainKey, ldmName)

    }

    /**
     * fetch GlobalUniqueIdentifier info bases on guid
     * @param guid
     * @return
     */
    GlobalUniqueIdentifier fetchByLdmNamesAndGuid(guid) {
        return GlobalUniqueIdentifier.fetchByLdmNamesAndGuid(guid)

    }


    public List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainKeyLike(ldmName, domainKey) {
        log.trace("fetchAllByLdmNameAndDomainKeyLike Begin with ldmName = ${ldmName} and domainKey = ${domainKey}")
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList
        globalUniqueIdentifierList = GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyLike(ldmName, domainKey)
        log.trace("fetchAllByLdmNameAndDomainKeyLike End")
        return globalUniqueIdentifierList
    }


    public List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainKeyInList(String ldmName, List<String> domainKeys, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        List<GlobalUniqueIdentifier> entities = []

        GlobalUniqueIdentifier.withSession { session ->
            def query = session.getNamedQuery('GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyInList')
            String hql = query.getQueryString()
            sortOrder = sortOrder ?: 'asc'
            if (sortField) {
                hql += " order by $sortField $sortOrder" + ", id $sortOrder"
            } else {
                hql += " order by id $sortOrder"
            }

            query = session.createQuery(hql)
            query.setString('ldmName', ldmName)
            query.setParameterList('domainKeys', domainKeys)
            if (max > 0) {
                query.setMaxResults(max)
            }
            if (offset > -1) {
                query.setFirstResult(offset)
            }
            entities = query.list()
        }
        return entities
    }


    public List<GlobalUniqueIdentifier> fetchAllByGuidInList(List<String> guidList) {
        return GlobalUniqueIdentifier.fetchAllByGuidInList(guidList)
    }


    def fetchAllByLdmName(String ldmName, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def entities = []
        if (ldmName) {
            GlobalUniqueIdentifier.withSession { session ->
                def query = session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmName')
                String hql = query.getQueryString()
                if (sortField) {
                    hql += " order by $sortField"
                } else {
                    hql += " order by id"
                }
                if (sortOrder) {
                    hql += " $sortOrder"
                }
                query = session.createQuery(hql)
                query.setString('ldmName', ldmName)
                if (max > 0) {
                    query.setMaxResults(max)
                }
                if (offset > -1) {
                    query.setFirstResult(offset)
                }
                entities = query.list()
            }
        }
        return entities
    }


    List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainIds(String ldmName, List<Long> domainIds) {
        return GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainIds(ldmName, domainIds)
    }

}
