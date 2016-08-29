/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase


/**
 * A transactional service supporting persistence of the College model.
 **/
class CollegeService extends ServiceBase {

    boolean transactional = true

    /**
     * Finds College for a give collegeCode
     * @param collegeCode
     * @return College - Entity
     */
    College fetchByCode(collegeCode) {
        College.findByCode(collegeCode)

    }

    List fetchAllWithGuidByCodeInList(Collection<String> collegeCodes, int max = 0, int offset = -1) {
        List rows = []
        if (collegeCodes) {
            List entities = College.withSession { session ->
                def namedQuery = session.createQuery('FROM College a, GlobalUniqueIdentifier b where a.id = b.domainId and b.ldmName = :ldmName and a.code in :codes')
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.COLLEGE_LDM_NAME)
                    setParameterList('codes', collegeCodes)
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
                    entitiesMap.college = it[0]
                    entitiesMap.globalUniqueIdentifier = it[1]
                    rows << entitiesMap
                }
            }
        }
        return rows
    }

}
