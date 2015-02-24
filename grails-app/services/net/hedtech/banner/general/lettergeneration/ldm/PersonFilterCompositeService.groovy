/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.lettergeneration.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.lettergeneration.PopulationSelectionExtract
import net.hedtech.banner.general.lettergeneration.ldm.v2.PersonFilter
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "person-filters" resource
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class PersonFilterCompositeService {

    public static final String LDM_NAME = 'person-filters'

    private HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'selection'
    ]

    /**
     * GET /api/person-filters
     *
     * @param params Request parameters
     * @return
     */
    List<PersonFilter> list(Map params) {
        List<PersonFilter> personFilters = []

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        List allowedSortFields = ['abbreviation']
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = ldmFieldToBannerDomainPropertyMap[params.sort]

        def populationSelectionExtracts = fetchPersonFilterByCriteria(params)

        populationSelectionExtracts?.each {
            String GORGUID_DOMAIN_KEY = "${it[0]}-^${it[1]}-^${it[2]}-^${it[3]}"
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(LDM_NAME, GORGUID_DOMAIN_KEY)
            personFilters << new PersonFilter(it[1], globalUniqueIdentifier?.guid, globalUniqueIdentifier?.domainKey, new Metadata(globalUniqueIdentifier?.dataOrigin))
        }

        return personFilters
    }

    /**
     * GET /api/person-filters
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true)
    def count(Map params) {
        log.trace "count:Begin"
        log.debug "Request parameters: ${params}"
        int total = 0

        total = fetchPersonFilterByCriteria(params, true)

        log.trace "count:End: $total"
        return total
    }

    /**
     * GET /api/person-filters/<guid>
     *
     * @param guid
     * @return
     */
    PersonFilter get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("personFilter", new NotFoundException())
        }

        PopulationSelectionExtract populationSelectionExtract = PopulationSelectionExtract.get(globalUniqueIdentifier.domainId)
        if (!populationSelectionExtract) {
            throw new ApplicationException("personFilter", new NotFoundException())
        }

        return new PersonFilter(populationSelectionExtract.selection, globalUniqueIdentifier.guid, globalUniqueIdentifier.domainKey, new Metadata(globalUniqueIdentifier.dataOrigin));
    }


    private def fetchPersonFilterByCriteria(Map content, boolean count = false) {
        log.trace "fetchPersonFilterByCriteria:Begin"
        def result
        String queryCriteria = ""

        if (count) {
            queryCriteria = "select count(a) from PopulationSelectionExtract a where 1=1 group by a.application, a.selection, a.creatorId, a.lastModifiedBy"
            result = PopulationSelectionExtract.executeQuery(queryCriteria)?.size()
        } else {
            queryCriteria = "select distinct a.application, a.selection, a.creatorId, a.lastModifiedBy from PopulationSelectionExtract a where 1=1"
            Integer max = content.max.trim().toInteger()
            Integer offset = content.offset?.trim()?.toInteger() ?: 0
            if (content.sort) {
                String sort = content.sort.trim()
                String order = content.order?.trim() ?: "asc"
                queryCriteria += """ order by $sort $order """
            }
            result = PopulationSelectionExtract.executeQuery(queryCriteria, [max: max, offset: offset])
        }

        log.trace "fetchPersonFilterByCriteria:End"
        return result
    }


}
