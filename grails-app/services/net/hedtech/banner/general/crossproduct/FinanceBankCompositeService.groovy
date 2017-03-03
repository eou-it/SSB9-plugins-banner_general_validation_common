/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants


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
        List<Bank> bankList = []
        bankList = bankService.findBankListByEffectiveDateAndBankCode( attrs?.effectiveDate, attrs?.searchParam, pagingParams, attrs?.coaCode )
        if (!bankList) {
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
