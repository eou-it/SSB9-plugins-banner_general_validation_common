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
            title       : 'selection'
    ]


    List<PersonFilter> list(Map params){

        List<PersonFilter> personFilters = []
        List allowedSortFields = ['title']

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


    Long count() {
        return PopulationSelectionBase.count()
    }


    PersonFilter get(String guid){
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(PopulationSelectionBase.class.simpleName)))
        }

        PopulationSelectionBase populationSelectionBase = PopulationSelectionBase.get(globalUniqueIdentifier.domainId)
        if (!populationSelectionBase) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(PopulationSelectionBase.class.simpleName)))
        }

        return new PersonFilter(populationSelectionBase, globalUniqueIdentifier.guid, new Metadata(populationSelectionBase.dataOrigin));
    }
}
