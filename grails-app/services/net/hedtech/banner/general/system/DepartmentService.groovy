/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
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
 * A transactional service supporting persistence of the Department model. 
 * */
class DepartmentService extends ServiceBase{

    boolean transactional = true

    /**
     * Finds Department for a give departmentCode
     * @param departmentCode
     * @return Department - Entity
     */
    Department fetchByCode(String departmentCode){
        return Department.fetchByCode(departmentCode)
    }

    List fetchAllWithGuidByCodeInList(Collection<String> departmentCodes, int max = 0, int offset = -1) {
        List rows = []
        if (departmentCodes) {
            List entities = Department.withSession { session ->
                def namedQuery = session.createQuery('FROM Department a, GlobalUniqueIdentifier b where a.id = b.domainId and b.ldmName = :ldmName and a.code in :codes')
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.DEPARTMENT_LDM_NAME)
                    setParameterList('codes', departmentCodes)
                    if (max > 0) {
                        setMaxResults(max)
                    }
                    if (offset > -1) {
                        setFirstResult(offset)
                    }
                    list()
                }
            }
            if (entities) {
                entities.each {
                    Map entitiesMap = [:]
                    entitiesMap.department = it[0]
                    entitiesMap.globalUniqueIdentifier = it[1]
                    rows << entitiesMap
                }
            }
        }
        return rows
    }
}
