/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.lettergeneration.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.lettergeneration.PopulationSelectionExtract
import net.hedtech.banner.general.lettergeneration.ldm.v2.PersonFilter
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "person-filters" resource
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class PersonFilterCompositeService {

    public static final String LDM_NAME = 'person-filters'
    private static final String DOMAIN_KEY_DELIMITER = '-^'

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

        // As only one record is inserted in GLBEXTR for application,selection, creatorId and userId combination, can't rely on domain surrogate id. Hence, domain key
        def domainKeyParts = splitDomainKey(globalUniqueIdentifier.domainKey)

        def pidms = PopulationSelectionExtract.fetchAllPidmsByApplicationSelectionCreatorIdLastModifiedBy(domainKeyParts.application, domainKeyParts.selection, domainKeyParts.creatorId, domainKeyParts.lastModifiedBy)

        if (!pidms) {
            throw new ApplicationException("personFilter", new NotFoundException())
        }

        return new PersonFilter(domainKeyParts.selection, globalUniqueIdentifier.guid, globalUniqueIdentifier.domainKey, new Metadata(globalUniqueIdentifier.dataOrigin));
    }


    private def fetchPersonFilterByCriteria(Map content, boolean count = false) {
        log.trace "fetchPersonFilterByCriteria:Begin"
        def result

        def searchFilters = QueryBuilder.createFilters(content)
        def filtersAllowedWithCriterion = ["abbreviation"]
        def allowedOperators = [Operators.CONTAINS]
        RestfulApiValidationUtility.validateCriteria(searchFilters, filtersAllowedWithCriterion, allowedOperators)

        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria

        String query = ""
        if (count) {
            query = "select count(a) from PopulationSelectionExtract a where 1=1 "
        } else {
            query = "select distinct a.application, a.selection, a.creatorId, a.lastModifiedBy from PopulationSelectionExtract a where 1=1"
        }

        Map namedParams = [:]
        if (params.containsKey("abbreviation")) {
            // filter[index][field]=abbreviation
            retainSingleCriterionForFilter(criteria, "abbreviation")
            query += """ and lower(a.selection) like lower(:selection) """
            namedParams.put("selection", params.abbreviation)
        } else if (content.containsKey("abbreviation")) {
            query += """ and lower(a.selection) like lower(:selection) """
            namedParams.put("selection", (!content.abbreviation.trim().contains("%")) ? "%${content.abbreviation.trim()}%" : content.abbreviation.trim())
        }

        if (count) {
            query += "group by a.application, a.selection, a.creatorId, a.lastModifiedBy"
            result = PopulationSelectionExtract.executeQuery(query, namedParams)?.size()
        } else {
            Integer max = content.max.trim().toInteger()
            Integer offset = content.offset?.trim()?.toInteger() ?: 0
            if (content.sort) {
                String sort = content.sort.trim()
                String order = content.order?.trim() ?: "asc"
                query += """ order by $sort $order """
            }

            result = PopulationSelectionExtract.executeQuery(query, namedParams, [max: max, offset: offset])
        }

        log.trace "fetchPersonFilterByCriteria:End"
        return result
    }


    private void retainSingleCriterionForFilter(def criteria, String filterName) {
        def col = criteria.findAll { it.key == filterName }
        if (col.size() > 1) {
            // More than one criterion for filter
            criteria.removeAll { it.key == filterName }
            criteria << col[col.size() - 1]
        }
    }


    def splitDomainKey(String domainKey) {
        def domainKeyParts = [:]

        if (domainKey) {
            List tokens = domainKey.tokenize(DOMAIN_KEY_DELIMITER)
            if (tokens.size() < 4) {
                throw new ApplicationException('PersonFilterCompositeService', new BusinessLogicValidationException("invalid.domain.key.message", ["BusinessLogicValidationException"]))
            }

            domainKeyParts << [application: tokens[0]]
            log.debug("application: ${tokens[0]}")

            domainKeyParts << [selection: tokens[1]]
            log.debug("selection: ${tokens[1]}")

            domainKeyParts << [creatorId: tokens[2]]
            log.debug("creatorId: ${tokens[2]}")

            domainKeyParts << [lastModifiedBy: tokens[3]]
            log.debug("lastModifiedBy: ${tokens[3]}")
        }

        return domainKeyParts
    }


}
