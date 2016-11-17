/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the TelephoneTypeService model.
 **/
class TelephoneTypeService extends ServiceBase {
    boolean transactional = true

/**
     * fetching TelephoneType details based on code
     * @param code
     * @return
     */
      TelephoneType fetchValidByCode(String code){
        return TelephoneType.fetchByCode(code)
    }

    /**
     * Fetch TelephoneType details for updatable UI select list (read a Select2 widget).
     * @param max
     * @param offset
     * @param searchString
     * @return List of TelephoneType objects
     */
    def fetchUpdateableTelephoneTypeList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = TelephoneType.createCriteria()
        def telephoneTypeList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        telephoneTypeList
    }

    TelephoneType fetchByCode(String code) {
        return TelephoneType.withSession { session ->
            session.getNamedQuery('TelephoneType.fetchByCode').setString('code', code).uniqueResult()
        }
    }

    List fetchAllWithGuidByCodeInList(Collection<String> phoneTypeCodes, int max = 0, int offset = -1) {
        List entities = []
        if (phoneTypeCodes) {
            entities = TelephoneType.withSession { session ->
                def namedQuery = session.getNamedQuery('TelephoneType.fetchAllWithGuidByCodeInList')
                String hql = namedQuery.getQueryString()
                hql += " order by a.id asc"
                namedQuery = session.createQuery(hql)
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME)
                    setParameterList('codes', phoneTypeCodes)
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

    List<TelephoneType> fetchAllByCodeInList(Collection<String> phoneTypeCodes) {
        List entities = []
        if (phoneTypeCodes) {
            entities = TelephoneType.withSession { session ->
                session.getNamedQuery('TelephoneType.fetchAllByCodeInList')
                        .setParameterList('codes', phoneTypeCodes)
                        .list()
            }
        }
        return entities
    }

    List fetchAllWithGuid(int max = 0, int offset = -1) {
        return TelephoneType.withSession { session ->
            def namedQuery = session.getNamedQuery('TelephoneType.fetchAllWithGuid')
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.PHONE_TYPE_LDM_NAME)
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
}
