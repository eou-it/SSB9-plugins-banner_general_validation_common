/** *****************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the InstitutionalDescriptionService model.
 * */
class InstitutionalDescriptionService extends ServiceBase {

    boolean transactional = true

    CurrencyConversionService currencyConversionService
    IsoCodeService isoCodeService


    def InstitutionalDescription findByKey() {
        return InstitutionalDescription.fetchByKey()
    }


    String getInstitutionBaseCurrencyCode() {
        return findByKey().baseCurrCode
    }


    String getISO4217CurrencyCodeForInstitutionBaseCurrency() {
        CurrencyConversion currencyConversion = currencyConversionService.findByCurrencyConversion(getInstitutionBaseCurrencyCode())
        String isoCurrencyCode = currencyConversion?.standardCodeIso
        if (!isoCurrencyCode || !isoCodeService.isValidISO4217CurrencyCode(isoCurrencyCode)) {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("currency.not.mapped", null))
        }
        return isoCurrencyCode
    }


    String getDatabaseUtcOffset() {
        String utcOffset
        InstitutionalDescription.withSession { session ->
            String sql = "select dbtimezone from dual"
            utcOffset = session.createSQLQuery(sql).uniqueResult()
        }
        return utcOffset
    }


    String getSessionTimeZone() {
        String timeZoneId
        InstitutionalDescription.withSession { session ->
            String sql = "select sessiontimezone from dual"
            timeZoneId = session.createSQLQuery(sql).uniqueResult()
        }
        return timeZoneId
    }

}
