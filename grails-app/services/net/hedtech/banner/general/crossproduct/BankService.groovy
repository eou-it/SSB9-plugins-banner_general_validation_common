/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.service.ServiceBase


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
}
