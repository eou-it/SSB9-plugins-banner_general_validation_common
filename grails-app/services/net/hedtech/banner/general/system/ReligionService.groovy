package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

class ReligionService extends ServiceBase {

    boolean transactional = true


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


}