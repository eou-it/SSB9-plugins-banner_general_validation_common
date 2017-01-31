/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.finance.util.FinanceCommonUtility

/**
 * Service class for FinanceCurrencyCompositeService.
 *
 */
class FinanceBankCompositeService {
    def bankService
    /**
     * The service method to find Bank code list by the Bank code and effective date.
     * @param attrs attributes map which holds Bank code and effective date.
     * @param pagingParams pagination parameters
     * @return list of Bank.
     */
    def findBankCodeListByEffectiveDateAndBankCode( Map attrs, Map pagingParams ) {
        def inputMap = [searchParam: attrs?.searchParam?.toUpperCase()]
        List<Bank> bankList = []
        FinanceCommonUtility.applyWildCard( inputMap, true, true )
        bankList = bankService.findBankListByEffectiveDateAndBankCode( attrs?.effectiveDate, inputMap.searchParam, pagingParams )
        if (!bankList) {
            LoggerUtility.warn( log, 'Error: Bank code not found.' )
            throw new ApplicationException( BankService, new BusinessLogicValidationException( GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE, [] ) )
        }
        bankList.collect() {
            [   id                  : it.id,
                version             : it.version,
                    bankCode            : it.bank,
                    bankAccountName     : it.bankAccountName,
                    chartOfAccounts     : it.chartOfAccounts ? it.chartOfAccounts : '',
                    currencyConversion : it.currencyConversion ? it.currencyConversion : ''
            ]
        }
    }
}
