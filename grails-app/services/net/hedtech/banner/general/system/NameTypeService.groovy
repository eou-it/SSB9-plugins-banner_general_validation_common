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

    List fetchAllWithGuidByCodeInList(Collection<String> nameTypeCodes, int max = 0, int offset = -1) {
        List entities = []
        if (nameTypeCodes) {
            entities = NameType.withSession { session ->
                def namedQuery = session.getNamedQuery('NameType.fetchAllWithGuidByCodeInList')
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME)
                    setParameterList('codes', nameTypeCodes)
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

    List<NameType> fetchAllByCodeInList(Collection<String> nameTypeCodes) {
        List entities = []
        if (nameTypeCodes) {
            entities = NameType.withSession { session ->
                def namedQuery = session.getNamedQuery('NameType.fetchAllByCodeInList')
                namedQuery.with {
                    setParameterList('codes', nameTypeCodes)
                    list()
                }
            }
        }
        return entities
    }

    List fetchAllWithGuid(int max=0, int offset=-1) {
        return NameType.withSession { session ->
            def namedQuery = session.getNamedQuery('NameType.fetchAllWithGuid')
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME)
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
