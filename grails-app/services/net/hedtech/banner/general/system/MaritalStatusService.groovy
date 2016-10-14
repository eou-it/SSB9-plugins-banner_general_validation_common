/*********************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

/**
 * A transactional service supporting persistence of the Level model.
 *
 */
class MaritalStatusService extends ServiceBase {

    boolean transactional = true

    /**
     * fetching marital status details based on code
     * @param code
     * @return
     */
    MaritalStatus fetchByCode(String code) {
        MaritalStatus maritalStatus
        if (code) {
            maritalStatus = MaritalStatus.withSession { session ->
                session.getNamedQuery('MaritalStatus.fetchByCode').setString('code', code).uniqueResult()
            }
        }
        return maritalStatus
    }


    def fetchAllWithGuid(String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def rows = []
        List entities = []
        MaritalStatus.withSession { session ->
            def namedQuery = session.getNamedQuery('MaritalStatus.fetchAllWithGuid')
            String hql = namedQuery.getQueryString()
            String orderBy
            if (sortField) {
                orderBy = " order by a.$sortField ${sortOrder ?: ''} , a.id asc"
            } else {
                orderBy = " order by a.id ${sortOrder ?: ''}"
            }
            hql += orderBy
            namedQuery = session.createQuery(hql)
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)
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
            rows << [maritalStatus: it[0], globalUniqueIdentifier: it[1]]
        }
        return rows
    }


    def fetchAllWithGuidByCodeInList(Collection<String> maritalStatusCodes, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def rows = []
        if (maritalStatusCodes) {
            List entities = []
            MaritalStatus.withSession { session ->
                def namedQuery = session.getNamedQuery('MaritalStatus.fetchAllWithGuidByCodeInList')
                String hql = namedQuery.getQueryString()
                String orderBy
                if (sortField) {
                    orderBy = " order by a.$sortField ${sortOrder ?: ''} , a.id asc"
                } else {
                    orderBy = " order by a.id ${sortOrder ?: ''}"
                }
                hql += orderBy
                namedQuery = session.createQuery(hql)
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)
                    setParameterList('codes', maritalStatusCodes)
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
                rows << [maritalStatus: it[0], globalUniqueIdentifier: it[1]]
            }
        }
        return rows
    }


    List<MaritalStatus> fetchAllByCodeInList(Collection<String> maritalStatusCodes) {
        List entities = []
        if (maritalStatusCodes) {
            MaritalStatus.withSession { session ->
                def namedQuery = session.getNamedQuery('MaritalStatus.fetchAllByCodeInList')
                namedQuery.with {
                    setParameterList('codes', maritalStatusCodes)
                    entities = list()
                }
            }
        }
        return entities
    }

}
