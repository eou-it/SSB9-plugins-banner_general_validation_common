/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * A transactional service supporting persistence of the InstitutionalDescriptionService model.
 * */
@Transactional
class InstitutionalDescriptionService extends ServiceBase {

    CurrencyConversionService currencyConversionService
    IsoCodeService isoCodeService


    def InstitutionalDescription findByKey() {
        return InstitutionalDescription.fetchByKey()
    }


    String getInstitutionBaseCurrencyCode() {
        return findByKey().baseCurrCode
    }


    String getInstitutionTimeZoneID() {
        return findByKey().timeZoneID
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


    String getDatabaseSessionTimeZone() {
        String timeZoneId
        InstitutionalDescription.withSession { session ->
            String sql = "select sessiontimezone from dual"
            timeZoneId = session.createSQLQuery(sql).uniqueResult()
        }
        return timeZoneId
    }

}
