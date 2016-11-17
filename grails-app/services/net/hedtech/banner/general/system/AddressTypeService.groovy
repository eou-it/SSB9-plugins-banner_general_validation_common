/** *****************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the AddressType model.
 **/
class AddressTypeService extends ServiceBase {

    boolean transactional = true

    /**
     * fetch AddressType based on code
     * @param code
     * @return AddressType
     */
    AddressType fetchByCode(String code) {
        AddressType addressType = AddressType.withSession { session ->
            session.getNamedQuery('AddressType.fetchByCode').setString(GeneralValidationCommonConstants.CODE, code).uniqueResult()
        }
        return addressType
    }


    List fetchAllWithGuidByCodeInList(Collection<String> addressTypeCodes, int max = 0, int offset = -1) {
        def rows = []
        if (addressTypeCodes) {
            List entities = []
            AddressType.withSession { session ->
                def namedQuery = session.getNamedQuery('AddressType.fetchAllWithGuidByCodeInList')
                String hql = namedQuery.getQueryString()
                hql += " order by a.id asc"
                namedQuery = session.createQuery(hql)
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME)
                    setParameterList('codes', addressTypeCodes)
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
                rows << [addressType: it[0], globalUniqueIdentifier: it[1]]
            }
        }
        return rows
    }

    List<AddressType> fetchAllByCodeInList(Collection<String> addressTypeCodes) {
        List entities = []
        if (addressTypeCodes) {
            AddressType.withSession { session ->
                def namedQuery = session.getNamedQuery('AddressType.fetchAllByCodeInList')
                namedQuery.with {
                    setParameterList('codes', addressTypeCodes)
                    entities = list()
                }
            }
        }
        return entities
    }

    List fetchAllWithGuid(int max = 0, int offset = -1) {
        return AddressType.withSession { session ->
            def namedQuery = session.getNamedQuery('AddressType.fetchAllWithGuid')
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME)
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
