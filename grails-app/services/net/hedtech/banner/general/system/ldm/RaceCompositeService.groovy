/*********************************************************************************
 Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
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
    def list(Map params) {
        String version = LdmService.getAcceptVersion(VERSIONS)
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        if (version < GeneralValidationCommonConstants.VERSION_V6) {
            List<String> allowedSortFields = [GeneralValidationCommonConstants.TITLE]
            if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
                allowedSortFields << GeneralValidationCommonConstants.CODE
            } else {
                allowedSortFields << GeneralValidationCommonConstants.ABBREVIATION
            }
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            RestfulApiValidationUtility.validateSortOrder(params.order)

            Map ldmPropertyToDomainPropertyMap = [abbreviation: GeneralValidationCommonConstants.RACE,
                                                  title       : GeneralValidationCommonConstants.DESCRIPTION,
                                                  code        : GeneralValidationCommonConstants.RACE]
            params.sort = ldmPropertyToDomainPropertyMap[params.sort]
        }
        Map<String, String> bannerRaceCodeHedmRacialCategoryMap
        def entities
        params.offset = params.offset ?: '0'
        if (version.equals(GeneralValidationCommonConstants.VERSION_V4)) {
            bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV4RacialCategoryMap()
            entities = raceService.fetchAllWithGuidByRaceInList(bannerRaceCodeHedmRacialCategoryMap.keySet(), params.sort, params.order, params.max as int, params.offset as int)
        } else {
            if (version.equals(GeneralValidationCommonConstants.VERSION_V1)) {
                bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV1RacialCategoryMap()
            } else {
                params.sort = null
                params.order = null
                bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV6RacialCategoryMap()
            }
            entities = raceService.fetchAllWithGuid(params.sort, params.order, params.max as int, params.offset as int)
        }
        return createDecorators(bannerRaceCodeHedmRacialCategoryMap, entities)
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
            return getBannerRaceCodHedmV4RacialCategoryMap().size()
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
        String version = LdmService.getAcceptVersion(VERSIONS)
        Map<String, String> bannerRaceCodeHedmRacialCategoryMap
        if (version.equals(GeneralValidationCommonConstants.VERSION_V4)) {
            bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV4RacialCategoryMap()
        } else {
            if (version.equals(GeneralValidationCommonConstants.VERSION_V1)) {
                bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV1RacialCategoryMap()
            } else {
                bannerRaceCodeHedmRacialCategoryMap = getBannerRaceCodHedmV6RacialCategoryMap()
            }
        }
        if (GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(VERSIONS)) && !bannerRaceCodeHedmRacialCategoryMap.containsKey(race.race)) {
            throw new ApplicationException(GeneralValidationCommonConstants.RACE, new NotFoundException())
        }
        return createDecorators(bannerRaceCodeHedmRacialCategoryMap, [[race, globalUniqueIdentifier]])[0]
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
            String version = LdmService.getAcceptVersion(VERSIONS)

            String settingName
            if (GeneralValidationCommonConstants.VERSION_V1.equals(version)) {
                settingName = GeneralValidationCommonConstants.RACE_PARENT_CATEGORY
            } else if (GeneralValidationCommonConstants.VERSION_V4.equals(version)) {
                settingName = GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY
            } else {
                settingName = GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY_V6
            }

            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, settingName, raceCode)

            UsRacialCategory usRacialCategory = UsRacialCategory.getByString(rule?.translationValue, version)
            if (usRacialCategory) {
                hedmRaceCategory = usRacialCategory.versionToEnumMap[version]
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


    def getBannerRaceCodHedmV1RacialCategoryMap() {
        return getBannerRaceCodHedmRacialCategoryMap(GeneralValidationCommonConstants.RACE_PARENT_CATEGORY, GeneralValidationCommonConstants.VERSION_V1)
    }


    def getBannerRaceCodHedmV4RacialCategoryMap() {
        return getBannerRaceCodHedmRacialCategoryMap(GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY, GeneralValidationCommonConstants.VERSION_V4)
    }


    def getBannerRaceCodHedmV6RacialCategoryMap() {
        return getBannerRaceCodHedmRacialCategoryMap(GeneralValidationCommonConstants.RACE_RACIAL_CATEGORY_V6, GeneralValidationCommonConstants.VERSION_V6)
    }


    private def getBannerRaceCodHedmRacialCategoryMap(String settingName, String version) {
        Map<String, String> bannerRaceCodHedmRacialCategoryMap = [:]
        List<IntegrationConfiguration> integrationConfigurationList = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (integrationConfigurationList) {
            List<Race> entities = raceService.fetchAllByRaceInList(integrationConfigurationList.value)
            integrationConfigurationList.each {
                UsRacialCategory hedmRacialCategory = UsRacialCategory.getByString(it.translationValue, version)
                if (entities.race.contains(it.value) && hedmRacialCategory) {
                    bannerRaceCodHedmRacialCategoryMap.put(it.value, hedmRacialCategory.versionToEnumMap[version])
                }
            }
        }
        if (bannerRaceCodHedmRacialCategoryMap.isEmpty()) {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerRaceCodHedmRacialCategoryMap
    }


    private def createDecorators(Map<String, String> bannerRaceCodeHedmRacialCategoryMap, def entities) {
        def decorators = []
        entities.each {
            Race race = it.getAt(0)
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)

            if (LdmService.getAcceptVersion(VERSIONS) < GeneralValidationCommonConstants.VERSION_V6) {
                Metadata metadata
                if (LdmService.getAcceptVersion(VERSIONS).equals(GeneralValidationCommonConstants.VERSION_V1)) {
                    metadata = new Metadata(race.dataOrigin)
                }
                decorators << new RaceDetail(race, globalUniqueIdentifier?.guid, bannerRaceCodeHedmRacialCategoryMap.get(race.race), metadata)

            } else {
                decorators << new RaceDetailV6(race, globalUniqueIdentifier?.guid, bannerRaceCodeHedmRacialCategoryMap.get(race.race), null)

            }
        }
        return decorators
    }

}
