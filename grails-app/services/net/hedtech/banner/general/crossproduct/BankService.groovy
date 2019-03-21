/*******************************************************************************
 Copyright 2016-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.service.ServiceBase
import org.apache.commons.lang.StringUtils
import grails.gorm.transactions.Transactional

@Transactional
class BankService extends ServiceBase {

    /**
     * Provide Bank object for specified ruleClassCode
     * @param ruleClassCode , effectiveDate
     * @return RuleClass
     */
    Bank fetchByBankCode(String bankCode, Date effectiveDate) {
        return Bank.fetchByBankCode(bankCode, effectiveDate)
    }

    /**
     * The service method to find Bank code list by the Bank code and effective date.
     * @param attrs attributes map which holds Bank code and effective date.
     * @param pagingParams pagination parameters
     * @return list of Bank.
     */
    def findBankListByEffectiveDateAndBankCode(effectiveDate, searchParam, pagingParams, coaCode ) {
        def bankList = Bank.fetchByBankCodeList( effectiveDate, getLikeFormattedFilter(searchParam?.toUpperCase()), pagingParams, getLikeFormattedFilter(coaCode?.toUpperCase()) ).list
        return bankList
    }

    /**
     *
     * @param filter
     * @return
     */
    private static getLikeFormattedFilter(String filter){
        def filterText
        if (StringUtils.isBlank( filter )) {
            filterText = "%"
        } else if (!(filter =~ /%/)) {
            filterText = "%" + filter.toUpperCase() + "%"
        } else {
            filterText = filter.toUpperCase()
        }

        return filterText
    }
    /**
     * Get bank name
     * @param code
     * @param effectiveDate
     * @return
     */
    def getBankTitle(code, effectiveDate){
        return Bank.getBankTitle(code, effectiveDate);
    }
}
