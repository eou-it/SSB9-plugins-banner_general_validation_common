/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the TelephoneTypeService model.
 **/
class TelephoneTypeService extends ServiceBase {
    boolean transactional = true

    TelephoneType fetchByCode(String code) {
        return TelephoneType.withSession { session ->
            session.getNamedQuery('TelephoneType.fetchByCode').setString('code', code).uniqueResult()
        }
    }

    List fetchAllWithGuidInList(Collection<String> phoneTypeCodes, int max = 0, int offset = -1) {
        List entities = []
        if (phoneTypeCodes) {
            entities = TelephoneType.withSession { session ->
                def namedQuery = session.getNamedQuery('TelephoneType.fetchAllWithGuidInList')
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
