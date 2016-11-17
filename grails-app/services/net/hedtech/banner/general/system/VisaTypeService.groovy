package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase

class VisaTypeService extends ServiceBase {

    boolean transactional = true

    def fetchAllWithGuid(int max = 0, int offset = -1) {
        def rows = []
        def entities = []
        VisaType.withSession { session ->
            def namedQuery = session.getNamedQuery('VisaType.fetchAllWithGuid')
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.VISA_TYPES_LDM_NAME)
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
            rows << [visaType: it[0], globalUniqueIdentifier: it[1]]
        }
        return rows
    }

    def fetchAllWithGuidByCodeInList(Collection<String> visaTypeCodes, int max = 0, int offset = -1) {
        def rows = []
        if (visaTypeCodes) {
            def entities = []
            VisaType.withSession { session ->
                def namedQuery = session.getNamedQuery('VisaType.fetchAllWithGuidByCodeInList')
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.VISA_TYPES_LDM_NAME)
                    setParameterList('codes', visaTypeCodes)
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
                rows << [visaType: it[0], globalUniqueIdentifier: it[1]]
            }
        }
        return rows
    }

}
