/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RaceDetail
import net.hedtech.banner.general.system.ldm.v1.RaceParentCategory
import net.hedtech.banner.general.system.ldm.v4.RaceRacialCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "races" resource for LDM
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class RaceCompositeService extends LdmService {

    def raceService
    private static final String PROCESS_CODE = 'HEDM'
    private static final String RACE_LDM_NAME = 'races'
    static final String RACE_PARENT_CATEGORY = "RACE.PARENTCATEGORY"
    static final String RACE_RACIAL_CATEGORY = "RACE.RACIALCATEGORY"
    private static final List<String> VERSIONS = ["v1","v4"]

    List<RaceDetail> list(Map params) {
        List raceDetailList = []
        def version = LdmService.getAcceptVersion(VERSIONS)
        List allowedSortFields = ("v4".equals(version)? ['code', 'title']:['abbreviation', 'title'])

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)

        Map ldmPropertyToDomainPropertyMap = [abbreviation: 'race', title: 'description',code: 'race']
        params.sort = ldmPropertyToDomainPropertyMap[params.sort]
        if("v4".equals(version)){
              fetchRaceDetails(params).each {race ->
                  raceDetailList << new RaceDetail(race[0], GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race[0]?.id)?.guid, race[1]?.translationValue, new Metadata(race[0]?.dataOrigin))
              }
        }else if("v1".equals(version)) {
            List<Race> raceList = raceService.list(params) as List
            raceList.each { race ->
                raceDetailList << new RaceDetail(race, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RACE_LDM_NAME, race.id)?.guid, getLdmRace(race.race), new Metadata(race.dataOrigin))
            }
        }
        return raceDetailList
    }


    Long count(Map params) {
        def version = LdmService.getAcceptVersion(VERSIONS)
        if("v4".equals(version)){
            return  fetchRaceDetails(params,true)
        }else  if("v1".equals(version)) {
            return Race.count()
        }

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
       def raceCategory = getLdmRace(race.race)
        if("v4".equals(LdmService.getAcceptVersion(VERSIONS)) && !raceCategory ){
            throw new ApplicationException("race", new NotFoundException())
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
            def version = LdmService.getAcceptVersion(VERSIONS)
            def category = "v4".equals(version) ? RACE_RACIAL_CATEGORY : RACE_PARENT_CATEGORY
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, category, race)
             if("v4".equals(version)){
                 return  RaceRacialCategory.RACE_RACIAL_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
             }else  if("v1".equals(version)){
               return  RaceParentCategory.RACE_PARENT_CATEGORY.contains(rule?.translationValue) ? rule?.translationValue : null
             }
        }
        return null
    }

    def fetchRaceDetails(def content, def count = false) {
        def query = "from Race r,IntegrationConfiguration i where r.race = i.value and i.settingName = :settingName and i.processCode = :processCode and i.translationValue in (:translationValueList)"
        Race.withSession { session ->
            if (content?.sort && !count) {
                def sort = content.sort
                def order = content.order ?: 'asc'
                query += " order by LOWER(r.$sort) $order"
            } else if(count){
                query = "select count(*) " + query
            }
            def raceQuery = session.createQuery(query).
                    setString('settingName', RACE_RACIAL_CATEGORY).
                    setString('processCode', PROCESS_CODE).
                    setParameterList('translationValueList', RaceRacialCategory.RACE_RACIAL_CATEGORY).
                    setMaxResults(content?.max as Integer).
                    setFirstResult((content?.offset ?: '0') as Integer)
           return count ? raceQuery.uniqueResult() : raceQuery.list()
        }
    }
}
