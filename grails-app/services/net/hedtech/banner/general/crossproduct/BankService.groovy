/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.general.crossproduct.Bank

class BankService extends ServiceBase {
    boolean transactional = true

    /**
     * Provide Bank object for specified ruleClassCode
     * @param ruleClassCode , effectiveDate
     * @return RuleClass
     */
    Bank fetchByBankCode(String bankCode, Date effectiveDate) {
        return Bank.fetchByBankCode(bankCode, effectiveDate)
    }

    /**
     * The service method to find Currency code list by the Bank code and effective date.
     * @param attrs attributes map which holds Bank code and effective date.
     * @param pagingParams pagination parameters
     * @return list of Bank.
     */
    def findBankCodeListByEffectiveDateAndBankCode( Map attrs, Map pagingParams ) {
        LoggerUtility.info( log, "Input parameters for findBankCodeListByEffectiveDateAndBankCode :" + attrs )
        def inputMap = [searchParam: attrs?.searchParam?.toUpperCase()]
        FinanceCommonUtility.applyWildCard( inputMap, true, true )
        List<Bank> bankList = []
        bankList = Bank.fetchByBankCodeList( attrs?.effectiveDate, inputMap.searchParam, pagingParams ).list
        if (!bankList) {
            LoggerUtility.warn( log, 'Error: Bank code not found.' )
            throw new ApplicationException( BankService, new BusinessLogicValidationException( GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE, [] ) )
        }
        bankList.collect() {
            [id                  : it.id,
             version             : it.version,
             bankCode            : it.bank,
             bankAccountName     : it.bankAccountName,
             chartOfAccounts     : it.chartOfAccounts,
             currencyConversion : it.currencyConversion
            ]
        }
    }
}
