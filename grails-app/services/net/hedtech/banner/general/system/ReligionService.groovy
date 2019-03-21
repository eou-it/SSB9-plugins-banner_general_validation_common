/** *******************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional

@Transactional
class ReligionService extends ServiceBase {

    GlobalUniqueIdentifierService globalUniqueIdentifierService


    def fetchAllWithGuidByCodeInList(Collection<String> religionCodes, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def rows = []
        if (religionCodes) {
            List entities = []
            Religion.withSession { session ->
                def namedQuery = session.getNamedQuery('Religion.fetchAllWithGuidByCodeInList')
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
                    setString('ldmName', GeneralValidationCommonConstants.RELIGION_LDM_NAME)
                    setParameterList('codes', religionCodes)
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
                rows << [religion: it[0], globalUniqueIdentifier: it[1]]
            }
        }
        return rows
    }


    Religion fetchByGuid(String religionGuid) {
        Religion religion
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RELIGION_LDM_NAME, religionGuid?.toLowerCase()?.trim())
        if (globalUniqueIdentifier) {
            religion = get(globalUniqueIdentifier.domainId)
        }
        return religion
    }

}
