/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.query.DynamicFinder
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
class LevelService extends ServiceBase{

    boolean transactional = true

    /**
     * fetching Level details based on code
     * @param code
     * @return
     */
    Level fetchByCode(String code){
        return Level.fetchByCode(code)
    }

    List<Level> fetchAllByCodeInList(List<String> codes){
        return Level.fetchAllByCodeInList(codes)
    }

    List fetchAllWithGuidByCodeInList(Collection<String> levelCodes, int max = 0, int offset = -1) {
        List rows = []
        if (levelCodes) {
            List entities = Level.withSession { session ->
                def namedQuery = session.getNamedQuery('Level.fetchAllWithGuidByCodeInList')
                namedQuery.with {
                    setParameterList('codes', levelCodes)
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
                Map entitiesMap = [level: it[0], globalUniqueIdentifier: it[1]]
                rows.add(entitiesMap)
            }
        }
        return rows
    }


    def fetchAllWithGuidByGuidInList(Collection<String> guids) {
        List rows = []
        if (guids) {
            List<GlobalUniqueIdentifier> globalUniqueIdentifiers = GlobalUniqueIdentifier.fetchAllByGuidInList(guids.unique())
            rows = fetchAllWithGuidByCodeInList(globalUniqueIdentifiers?.domainKey)
        }
        return rows
    }

    def fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {

        Map params = [:]
        List criteria = []
        Map pagingAndSortParams = [:]

        sortOrder = sortOrder ?: 'asc'
        if (sortField) {
            pagingAndSortParams.sortCriteria = [
                    ["sortColumn": sortField, "sortDirection": sortOrder],
                    ["sortColumn": "id", "sortDirection": "asc"]
            ]
        } else {
            pagingAndSortParams.sortColumn = "id"
            pagingAndSortParams.sortDirection = sortOrder
        }

        if (max > 0) {
            pagingAndSortParams.max = max
        }
        if (offset > -1) {
            pagingAndSortParams.offset = offset
        }

        return getDynamicFinderForFetchAllByCriteria().find([params: params, criteria: criteria], pagingAndSortParams)
    }

    private DynamicFinder getDynamicFinderForFetchAllByCriteria() {
        def query = """ FROM Level a """
        return new DynamicFinder(Level.class, query, "a")
    }
}