/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Name Type model.
 * Providing restriction on create , update and delete on Name Type model.
 * */
class NameTypeService extends ServiceBase {

    boolean transactional = true

    /**
     * fetch list of Name types
     * @param params
     * @return
     */
    List fetchAll(Map params) {
        return NameType.fetchAll(params)
    }

    /**
     * fetch Namme type data bases on guid
     * @param guid
     * @return
     */
    def fetchByGuid(String guid) {
        return NameType.fetchByGuid(guid)
    }


    Map fetchGUIDs(List<String> nameTypeCodes) {
        def codeToGuidMap = [:]
        if (nameTypeCodes) {
            def result
            String hql = ''' select a.code, b.guid
                             from NameType a, GlobalUniqueIdentifier b
                             where a.code in :nameTypeCodes
                             and b.ldmName = :ldmName
                             and a.code = b.domainKey '''
            NameType.withSession { session ->
                def query = session.createQuery(hql)
                query.setString('ldmName', GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME)
                query.setParameterList('nameTypeCodes', nameTypeCodes)
                result = query.list()
            }
            result.each {
                codeToGuidMap.put(it[0], it[1])
            }
        }
        return codeToGuidMap
    }

}
