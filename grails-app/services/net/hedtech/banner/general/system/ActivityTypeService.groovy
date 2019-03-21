/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional

@Transactional
class ActivityTypeService extends ServiceBase {


    List fetchAllWithGuidByCodeInList(Collection<String> codes, int max = 0, int offset = -1) {
        def rows = []
        if (codes) {
            List entities = []
            ActivityType.withSession { session ->
                def namedQuery = session.getNamedQuery('ActivityType.fetchAllWithGuidByCodeInList')
                namedQuery.setParameter('activityType', GeneralValidationCommonConstants.ACTIVITY_TYPE_LDM_NAME)
                namedQuery.with {
                    setParameterList('codes', codes)
                    if (max > 0) {
                        setMaxResults(max)
                    }
                    if (offset > -1) {
                        setFirstResult(offset)
                    }
                    entities = list()
                }
            }
            entities?.each {
                rows << [activityType: it[0], globalUniqueIdentifier: it[1]]
            }
        }
        return rows
    }

    List<ActivityType> fetchAllByCodeInList(Collection<String> codes) {
        List entities = []
        if (codes) {
            entities = ActivityType.withSession { session ->
                def namedQuery = session.getNamedQuery('ActivityType.fetchAllByCodeInList')
                namedQuery.with {
                    setParameterList('codes', codes)
                    list()
                }
            }
        }
        return entities
    }

    List fetchAllWithGuid(int max = 0, int offset = -1) {
        def rows = []
        def entities = ActivityType.withSession { session ->
            def namedQuery = session.getNamedQuery('ActivityType.fetchAllWithGuid')
            namedQuery.setParameter('activityType', GeneralValidationCommonConstants.ACTIVITY_TYPE_LDM_NAME)
            namedQuery.with {
                if (max > 0) {
                    setMaxResults(max)
                }
                if (offset > -1) {
                    setFirstResult(offset)
                }
                list()
            }
        }
        entities?.each {
            rows << [activityType: it[0], globalUniqueIdentifier: it[1]]
        }
        return rows
    }

}
