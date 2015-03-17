/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.general.system.ldm.v1.RaceParentCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * Service used to support "races" resource for LDM
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class RaceCompositeService extends LdmService {

    def raceService
    private static final String RACE_LDM_NAME = 'races'
    static final String PROCESS_CODE = "LDM"
    static final String RACE_PARENT_CATEGORY = "RACE.PARENTCATEGORY"

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
            raceDetailList << new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
        }
        return raceDetailList
    }


    Long count() {
        return Race.count()
    }


    RaceDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(RACE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("race", new NotFoundException())
        }

        Race race = Race.get(globalUniqueIdentifier.domainId)
        if (!race) {
            throw new ApplicationException("race", new NotFoundException())
        }

        return new RaceDetail(race, globalUniqueIdentifier.guid, getLdmRace(race.race), new Metadata(race.dataOrigin));
    }


    RaceDetail fetchByRaceId(Long raceId) {
        if (null == raceId) {
            return null
        }
        Race race = raceService.get(raceId) as Race
        if (!race) {
            return null
        }
        return new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, raceId)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
    }


    RaceDetail fetchByRaceCode(String raceCode) {
        RaceDetail raceDetail = null
        if (raceCode) {
            Race race = Race.findByRace(raceCode)
            if (!race) {
                return raceDetail
            }
            raceDetail = new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
        }
        return raceDetail
    }

    def getLdmRace(def race) {
        if (race != null) {
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, RACE_PARENT_CATEGORY, race)
            return RaceParentCategory.RACE_PARENT_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
        }
        return null
    }
}
