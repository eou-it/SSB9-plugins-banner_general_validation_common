/** *****************************************************************************
 © 2015 Ellucian.  All Rights Reserved.
 ****************************************************************************** */
package net.hedtech.banner.general.lettergeneration.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.lettergeneration.PopulationSelectionBase
import net.hedtech.banner.general.lettergeneration.ldm.v2.PersonFilter
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "person-filters" resource
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class PersonFilterCompositeService {

    def populationSelectionBaseService
    public static final String LDM_NAME = 'person-filters'

    private HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation       : 'selection'
    ]


    /**
     * GET /api/person-filters
     *
     * @param params Request parameters
     * @return
     */
    List<PersonFilter> list(Map params){

        List<PersonFilter> personFilters = []
        List allowedSortFields = ['abbreviation']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = ldmFieldToBannerDomainPropertyMap[params.sort]

        List<PopulationSelectionBase> populationSelectionBases = populationSelectionBaseService.list(params) as List
        populationSelectionBases?.each{
            personFilters << new PersonFilter(it, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, it.id)?.guid, new Metadata(it.dataOrigin))
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
     */    Long count() {
        return PopulationSelectionBase.count()
    }


    /**
     * GET /api/person-filters/<guid>
     *
     * @param guid
     * @return
     */
    PersonFilter get(String guid){
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("personFilter", new NotFoundException())
        }

        PopulationSelectionBase populationSelectionBase = PopulationSelectionBase.get(globalUniqueIdentifier.domainId)
        if (!populationSelectionBase) {
            throw new ApplicationException("personFilter", new NotFoundException())
        }

        return new PersonFilter(populationSelectionBase, globalUniqueIdentifier.guid, new Metadata(populationSelectionBase.dataOrigin));
    }
}
