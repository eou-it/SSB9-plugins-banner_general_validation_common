/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.general.system.ldm.v1.RaceParentCategory
import net.hedtech.banner.general.system.ldm.v4.RaceRacialCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "races" resource for LDM
 */
@Transactional
class RaceCompositeService extends LdmService {

    def raceService

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1,GeneralValidationCommonConstants.VERSION_V4]

    /**
     * GET /api/races
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true)
    List<RaceDetail> list(Map params) {
        List raceDetailList = []
        def version = LdmService.getAcceptVersion(VERSIONS)
        List allowedSortFields = (GeneralValidationCommonConstants.VERSION_V4.equals(version)? [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE]:[GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE])

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        Map ldmPropertyToDomainPropertyMap = [abbreviation: GeneralValidationCommonConstants.RACE, title: GeneralValidationCommonConstants.DESCRIPTION,code: GeneralValidationCommonConstants.RACE]
        params.sort = ldmPropertyToDomainPropertyMap[params.sort]
        if(GeneralValidationCommonConstants.VERSION_V4.equals(version)){
            raceService.fetchRaceDetails(params).each {race ->
                  raceDetailList << new RaceDetail(race[0], globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race[0]?.id)?.guid, race[1]?.translationValue, new Metadata(race[0]?.dataOrigin))
              }
        }else {
            List<Race> raceList = raceService.list(params) as List
            raceList.each { race ->
                raceDetailList << new RaceDetail(race, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
            }
        }
        return raceDetailList
    }

    /**
     * GET /api/races
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    Long count(Map params) {
        if(GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS))){
            return  raceService.fetchRaceDetails(params,true)
        }else {
            return raceService.count()
        }

    }

    /**
     * GET /api/races/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    RaceDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RACE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }

        Race race = raceService.get(globalUniqueIdentifier.domainId)
        if (!race) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }
       def raceCategory = getLdmRace(race.race)
        if(GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) && !raceCategory ){
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }

        return new RaceDetail(race, globalUniqueIdentifier.guid, raceCategory, new Metadata(race.dataOrigin));
    }


    RaceDetail fetchByRaceId(Long raceId) {
        if (null == raceId) {
            return null
        }
        Race race = raceService.get(raceId) as Race
        if (!race) {
            return null
        }
        return new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, raceId)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
    }


    RaceDetail fetchByRaceCode(String raceCode) {
        RaceDetail raceDetail = null
        if (raceCode) {
            Race race = Race.findByRace(raceCode)
            if (!race) {
                return raceDetail
            }
            raceDetail = new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
        }
        return raceDetail
    }

    def getLdmRace(def race) {
        if (race != null) {
            def version = LdmService.getAcceptVersion(VERSIONS)
            def category = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY : GeneralValidationCommonConstants.RACE_PARENT_CATEGORY
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, category, race)
             if(GeneralValidationCommonConstants.VERSION_V4.equals(version)){
                 return  RaceRacialCategory.RACE_RACIAL_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
             }else {
               return  RaceParentCategory.RACE_PARENT_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
             }
        }
        return null
    }



    /**
     * POST /api/races
     *
     * @param content Request body
     */
    def create(content) {
        def version = LdmService.getContentTypeVersion(VERSIONS)
        validateRequest(content,version)
        Race race = raceService.fetchByRace(content.race.trim())
        if (race) {
            def messageCode = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS : GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new BusinessLogicValidationException(messageCode, null))
        }

        race = bindRaces(new Race(), content)

        String guid = content.guid?.trim()?.toLowerCase()
        if (guid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(race.id, guid, GeneralValidationCommonConstants.RACE_LDM_NAME)
        } else {
            guid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race.id)?.guid
        }
        def racialCategory = GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) ? content.racialCategory : content.parentCategory
        return new RaceDetail(race, guid, racialCategory, new Metadata(race.dataOrigin));
    }

    private void validateRequest(content,version) {
        if (!content.race) {
            def parameterValue = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.CODE.capitalize() : GeneralValidationCommonConstants.ABBREVIATION.capitalize()
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED,[parameterValue]))
        }
        if (!content.description) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_DESCRIPTION_REQUIRED, null))
        }
    }

    // not setting up the GORRACE_RRAC_CODE, GORRACE_EDI_EQUIV, GORRACE_LMS_EQUIV
    def bindRaces(Race race, Map content) {
        setDataOrigin(race, content)
        bindData(race, content, [:])
        raceService.createOrUpdate(race)
    }

    /**
     * PUT /api/races/<guid>
     *
     * @param content Request body
     */
    def update(content) {
        String guid = content.id?.trim()?.toLowerCase()
        if (!guid) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RACE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            if (!content.guid) {
                content.guid = guid
            }
            //Per strategy when a GUID was provided, the create should happen.
            return create(content)
        }
        Race race = raceService.get(globalUniqueIdentifier.domainId)
        if (!race) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }
        // Should not allow to update Race.code as it is read-only
        if (race.race != content.race?.trim()) {
            content.race = race.race
        }
        race = bindRaces(race, content)
        def racialCategory = getLdmRace(race.race)
        return new RaceDetail(race, guid, racialCategory, new Metadata(race.dataOrigin));
    }

}
