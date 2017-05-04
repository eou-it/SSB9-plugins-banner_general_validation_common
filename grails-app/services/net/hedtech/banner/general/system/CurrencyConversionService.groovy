/*******************************************************************************
Copyright 2017 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class CurrencyConversionService extends ServiceBase {

    boolean transactional = true

    CurrencyConversion findByCurrencyConversion(String currencyConversion) {
        def entity
        if(currencyConversion) {
            entity = CurrencyConversion.withSession { session ->
                session.getNamedQuery('CurrencyConversion.findByCurrencyConversion').setString('currencyConversion', currencyConversion).uniqueResult()
            }
        }
        return entity
    }

    Collection<CurrencyConversion> findByCurrencyConversionInList(Collection<String> currencyConversions) {
        Collection<CurrencyConversion> entities = []
        if(currencyConversions) {
            entities = CurrencyConversion.withSession { session ->
                session.getNamedQuery('CurrencyConversion.findByCurrencyConversionInList').setParameterList('currencyConversions', currencyConversions).list()
            }
        }
        return entities
    }
}
