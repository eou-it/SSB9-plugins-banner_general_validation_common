/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm
import net.hedtech.banner.service.ServiceBase
/**
 * Service class for GlobalUniqueIdentifier responsible for
 * interacting with the Entity GlobalUniqueIdentifier, and
 */
class GlobalUniqueIdentifierService extends ServiceBase {

    public static final String API ="api"

    /**
     * fetch GlobalUniqueIdentifier info based on ldmName and guid
     * @param ldmName
     * @param guid
     * @return GlobalUniqueIdentifier
     */
     GlobalUniqueIdentifier fetchByLdmNameAndGuid(String ldmName, String guid) {
       return  GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, guid)
    }

    /**
     * fetch GlobalUniqueIdentifier info based on ldmName and domainId
     * @param ldmName
     * @param domainId
     * @return GlobalUniqueIdentifier
     */
    GlobalUniqueIdentifier  fetchByLdmNameAndDomainId(String ldmName, Long domainId){
        return  GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(ldmName, domainId)
    }
}
