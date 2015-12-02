/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm
import net.hedtech.banner.service.ServiceBase
/**
 * Service class for GlobalUniqueIdentifier responsible for
 * interacting with the Entity GlobalUniqueIdentifier, and
 */
class GlobalUniqueIdentifierService extends ServiceBase {

    public static final String API ="api"

    public List<GlobalUniqueIdentifier> fetchAllByLdmNameAndDomainKeyLike(ldmName, domainKey){
        log.trace("fetchAllByLdmNameAndDomainKeyLike Begin with ldmName = ${ldmName} and domainKey = ${domainKey}")
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList
        globalUniqueIdentifierList = GlobalUniqueIdentifier.fetchAllByLdmNameAndDomainKeyLike(ldmName, domainKey)
        log.trace("fetchAllByLdmNameAndDomainKeyLike End")
        return globalUniqueIdentifierList
    }

}
