/*******************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
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

    public List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainKeyInList(String ldmName, List<String> domainKeys) {
        return GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyInList(ldmName, domainKeys)
    }

    public List<GlobalUniqueIdentifier> fetchAllByGuidInList(List<String> guidList) {
        return GlobalUniqueIdentifier.fetchAllByGuidInList(guidList)
    }

    List<GlobalUniqueIdentifier> fetchAllByLdmName(String ldmName) {
        List<GlobalUniqueIdentifier> entities
        if (ldmName) {
            GlobalUniqueIdentifier.withSession { session ->
                def query = session.getNamedQuery('GlobalUniqueIdentifier.fetchByLdmName')
                query.setString('ldmName', ldmName)
                entities = query.list()
            }
        }
        return entities
    }

}
