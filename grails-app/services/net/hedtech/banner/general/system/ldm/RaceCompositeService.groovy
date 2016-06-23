/*********************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
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
import net.hedtech.banner.general.system.ldm.v6.RaceDetailV6
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "races" resource for LDM
 */
@Transactional
class RaceCompositeService extends LdmService {

    def raceService

    private static
    final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1, GeneralValidationCommonConstants.VERSION_V4, GeneralValidationCommonConstants.VERSION_V6]

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
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        if (!GeneralValidationCommonConstants.VERSION_V6.equals(version)) {
            List allowedSortFields = (GeneralValidationCommonConstants.VERSION_V4.equals(version) ? [GeneralValidationCommonConstants.CODE, GeneralValidationCommonConstants.TITLE] : [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE])
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            RestfulApiValidationUtility.validateSortOrder(params.order)
        }

        Map ldmPropertyToDomainPropertyMap = [abbreviation: GeneralValidationCommonConstants.RACE, title: GeneralValidationCommonConstants.DESCRIPTION, code: GeneralValidationCommonConstants.RACE]
        params.sort = ldmPropertyToDomainPropertyMap[params.sort]
        if (GeneralValidationCommonConstants.VERSION_V6.equals(version)) {
            params.order = null
            params.sort = null
            raceService.list(params).each { race ->
                raceDetailList << new RaceDetailV6(race, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), null)
            }
        } else if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
            raceService.fetchRaceDetails(params).each { race ->
                raceDetailList << new RaceDetail(race[0], globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.RACE_LDM_NAME, race[0]?.id)?.guid, race[1]?.translationValue, null)
            }
        } else {
            raceService.list(params).each { race ->
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
        if (GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS))) {
            return raceService.fetchRaceDetailsCount()
        } else {
            return raceService.count(params)
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
        if (GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) && !raceCategory) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }
        if (GeneralValidationCommonConstants.VERSION_V6.equals(LdmService.getAcceptVersion(VERSIONS))) {
            return new RaceDetailV6(race, globalUniqueIdentifier.guid, raceCategory, null);
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


    def getLdmRace(String raceCode) {
        String hedmRaceCategory
        if (raceCode != null) {
            def version = LdmService.getAcceptVersion(VERSIONS)

            String settingName
            if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
                settingName = GeneralValidationCommonConstants.RACE_PARENT_CATEGORY
            } else if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
                settingName = GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY
            } else {
                settingName = GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY_V6
            }

            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, settingName, raceCode)

            UsRacialCategory usRacialCategory = UsRacialCategory.getByString(rule?.translationValue)
            if (usRacialCategory) {
                if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
                    hedmRaceCategory = usRacialCategory.v1
                } else if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
                    hedmRaceCategory = usRacialCategory.v4
                } else {
                    hedmRaceCategory = usRacialCategory.v6
                }
            }
        }
        return hedmRaceCategory
    }

    /**
     * POST /api/races
     *
     * @param content Request body
     */
    def create(content) {
        def version = LdmService.getContentTypeVersion(VERSIONS)
        validateRequest(content, version)
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


    private void validateRequest(content, version) {
        if (!content.race) {
            def parameterValue = GeneralValidationCommonConstants.VERSION_V4.equals(version) ? GeneralValidationCommonConstants.CODE.capitalize() : GeneralValidationCommonConstants.ABBREVIATION.capitalize()
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, [parameterValue]))
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


    public Map fetchGuids(Set<String> raceCodes) {
        Map content = [:]
        def result
        String hql = '''select r.race, b.guid from Race r, GlobalUniqueIdentifier b WHERE r.race in :raceCodes and b.ldmName = :ldmName and r.race = b.domainKey'''
        Race.withSession { session ->
            def query = session.createQuery(hql).setString('ldmName', GeneralValidationCommonConstants.RACE_LDM_NAME)
            query.setParameterList('raceCodes', raceCodes)
            result = query.list()
        }
        result.each { race ->
            content.put(race[0], race[1])
        }
        return content
    }

}
