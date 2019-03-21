/** *******************************************************************************
 Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * A transactional service supporting persistence of the Race model.
 * */
@Transactional
class RaceService extends ServiceBase {

    GlobalUniqueIdentifierService globalUniqueIdentifierService


    Race fetchByRace(String racialCode) {
        Race race = Race.withSession { session ->
            session.getNamedQuery('Race.fetchByRace').setString('race', racialCode).uniqueResult()
        }
        return race
    }


    List fetchAllWithGuidByRaceInList(Collection<String> raceCodes, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        List entities = []
        if (raceCodes) {
            entities = Race.withSession { session ->
                def namedQuery = session.getNamedQuery('Race.fetchAllWithGuidByRaceInList')
                if (sortField) {
                    def orderBy = " order by a." + sortField
                    if (["asc", "desc"].contains(sortOrder?.trim()?.toLowerCase())) {
                        orderBy += " $sortOrder"
                    }
                    orderBy += ", a.id asc"
                    namedQuery = session.createQuery(namedQuery.getQueryString() + orderBy)
                } else {
                    def orderBy = " order by a.id asc"
                    namedQuery = session.createQuery(namedQuery.getQueryString() + orderBy)
                }

                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.RACE_LDM_NAME)
                    setParameterList('races', raceCodes)
                    if (max > 0) {
                        setMaxResults(max)
                    }
                    if (offset > -1) {
                        setFirstResult(offset)
                    }
                    list()
                }
            }
        }
        return entities
    }


    def fetchAllByRaceInList(Collection<String> raceCodes) {
        List entities = []
        if (raceCodes) {
            entities = Race.withSession { session ->
                session.getNamedQuery('Race.fetchAllByRaceInList')
                        .setParameterList('races', raceCodes)
                        .list()
            }
        }
        return entities
    }


    List fetchAllWithGuid(String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        return Race.withSession { session ->
            def namedQuery = session.getNamedQuery('Race.fetchAllWithGuid')
            if (sortField) {
                def orderBy = " order by a." + sortField
                if (["asc", "desc"].contains(sortOrder?.trim()?.toLowerCase())) {
                    orderBy += " $sortOrder"
                }
                orderBy += ", a.id asc"
                namedQuery = session.createQuery(namedQuery.getQueryString() + orderBy)
            } else {
                def orderBy = " order by a.id asc"
                namedQuery = session.createQuery(namedQuery.getQueryString() + orderBy)
            }
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.RACE_LDM_NAME)
                if (max > 0) {
                    setMaxResults(max)
                }
                if (offset > -1) {
                    setFirstResult(offset)
                }
                list()
            }
        }
    }


    Race fetchByGuid(String raceGuid) {
        Race race
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RACE_LDM_NAME, raceGuid?.toLowerCase()?.trim())
        if (globalUniqueIdentifier) {
            race = get(globalUniqueIdentifier.domainId)
        }
        return race
    }

}
