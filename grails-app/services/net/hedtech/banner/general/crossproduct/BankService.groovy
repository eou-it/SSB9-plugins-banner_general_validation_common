/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.util.LoggerUtility

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
     * The service method to find Bank code list by the Bank code and effective date.
     * @param attrs attributes map which holds Bank code and effective date.
     * @param pagingParams pagination parameters
     * @return list of Bank.
     */
    def findBankListByEffectiveDateAndBankCode(effectiveDate, searchParam, pagingParams ) {
        def bankList = Bank.fetchByBankCodeList( effectiveDate, searchParam, pagingParams ).list
        if (!bankList) {
            LoggerUtility.warn( log, 'Error: Bank code not found.' )
            throw new ApplicationException( BankService, new BusinessLogicValidationException( GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE, [] ) )
        }
        return bankList
    }
}
