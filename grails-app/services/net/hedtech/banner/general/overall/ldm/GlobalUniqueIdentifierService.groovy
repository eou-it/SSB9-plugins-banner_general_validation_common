/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.service.ServiceBase
import org.springframework.transaction.annotation.Transactional

/**
 * Service class for GlobalUniqueIdentifier responsible for
 * interacting with the Entity GlobalUniqueIdentifier, and
 */
@Transactional
class GlobalUniqueIdentifierService extends ServiceBase {

    static final int MAX = 10
    static final int OFFSET = 10
    static final String API ="api"

    /**
     * Finds GlobalUniqueIdentifier for a given guid
     * @param guid
     * @return GlobalUniqueIdentifier Entity
     */
    @Transactional(readOnly = true)
    GlobalUniqueIdentifier findByGuid(guid){
       return  GlobalUniqueIdentifier.findByGuid(guid)
    }

    /**
     * Finds Guid for given ldmName and domainId
     * @param ldmName
     * @param domainId
     * @return String - guid
     */
    @Transactional(readOnly = true)
    String fetchByLdmNameAndDomainId(ldmName, domainId){
        return  GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(ldmName, domainId)?.guid
    }
}
