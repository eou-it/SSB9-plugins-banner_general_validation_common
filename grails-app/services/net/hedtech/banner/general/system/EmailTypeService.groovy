/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
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


class EmailTypeService extends ServiceBase {
    boolean transactional = true


    List fetchAllWithGuidByCodeInList(Collection<String> emailTypeCodes, int max = 0, int offset = -1) {
        List entities = []
        if (emailTypeCodes) {
            entities = EmailType.withSession { session ->
                def namedQuery = session.getNamedQuery('EmailType.fetchAllWithGuidByCodeInList')
                namedQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
                    setParameterList('codes', emailTypeCodes)
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

    List<EmailType> fetchAllByCodeInList(Collection<String> emailTypeCodes) {
        List entities = []
        if (emailTypeCodes) {
            entities = EmailType.withSession { session ->
                session.getNamedQuery('EmailType.fetchAllByCodeInList')
                        .setParameterList('codes', emailTypeCodes)
                        .list()
            }
        }
        return entities
    }

    List fetchAllWithGuid(int max=0, int offset=-1) {
        return EmailType.withSession { session ->
            def namedQuery = session.getNamedQuery('EmailType.fetchAllWithGuid')
            namedQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
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

    EmailType fetchByCode(String code){
        EmailType emailType
        if(code){
            emailType = EmailType.findByCode(code)
        }
        return emailType
    }


}
