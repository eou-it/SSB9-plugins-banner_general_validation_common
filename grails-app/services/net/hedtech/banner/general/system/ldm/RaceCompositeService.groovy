/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.general.system.ldm.v1.RaceParentCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "ethnicities" resource for LDM
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class RaceCompositeService {

    def raceService
    private static final String RACE_LDM_NAME = 'races'

    List<RaceDetail> list(Map params) {
        List raceDetailList = []
        List allowedSortFields = ['abbreviation', 'title']

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        Map ldmPropertyToDomainPropertyMap = [abbreviation: 'race', title: 'description']
        params.sort = ldmPropertyToDomainPropertyMap[params.sort]
        List<Race> raceList = raceService.list(params) as List
        raceList.each { race ->
            raceDetailList << new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race))
        }
        return raceDetailList
    }


    Long count() {
        return Race.count()
    }


    RaceDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(RACE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(Race.class.simpleName)))
        }

        Race race = Race.get(globalUniqueIdentifier.domainId)
        if (!race) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(Race.class.simpleName)))
        }

        return new RaceDetail(race, globalUniqueIdentifier.guid, getLdmRace(race.race));
    }


    RaceDetail fetchByRaceId(Long raceId) {
        Race race = raceService.get(raceId) as Race
        return new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, raceId)?.guid, getLdmRace(race.race))
    }


    RaceDetail fetchByRaceCode(String raceCode) {
        RaceDetail raceDetail = null
        if (raceCode) {
            Race race = Race.findByRace(raceCode)
            if (!race) {
                return raceDetail
            }
            raceDetail = new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race))
        }
        return raceDetail
    }

    //TODO: Move this function to common place
    // Return LDM enumeration value for the corresponding race code.
    def getLdmRace(def race) {
        if (race != null) {
            return RaceParentCategory.WHITE.value
        }
        return null
    }
}
