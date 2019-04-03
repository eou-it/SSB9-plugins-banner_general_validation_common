/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import net.hedtech.banner.exceptions.ApplicationException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class FinanceBankCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    FinanceBankCompositeService financeBankCompositeService

    def i_success_bank = "TT"
    def i_success_currencyConversion
    def i_success_effectiveDate = new Date() - 10
    def i_success_nextChangeDate = new Date() + 10
    def i_success_bankPidm = 1
    def i_success_accountNumber = "TTTTT"
    def i_success_accountName = "TTTTT"
    def i_success_achStatus = "A"
    def i_success_statusIndicator = "A"
    def i_success_termDate = new Date() + 10
    def i_success_chartOfAccounts = "#"
    def i_success_accountCash = "TTTTT"
    def i_success_accountInterfund = "TTTTT"
    def i_success_fund = "TTTTT"
    def i_success_achDestinationNumber = "TTTTT"
    def i_success_achOriginalNumber = "TTTTT"
    def i_success_achDestinationName = "TTTTT"
    def i_success_achShortOriginalName = "TTTTT"
    def i_success_companyType = "#"
    def i_success_companyId = "TTTTT"
    def i_success_achOriginalName = "TTTTT"
    def i_success_bankRoutNumber = "TTTTT"
    def i_success_achFileNumber = 1

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testFetchByBankCodeListWithNullSearch() {
        try{
            Bank getBankDetails = financeBankCompositeService.findBankCodeListByEffectiveDateAndBankCode([effectiveDate:new Date() , searchParam: null, coaCode:i_success_chartOfAccounts] , [max:10, offset:0])
           fail 'should fail this' + GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE
        }catch (ApplicationException ae) {
            assertApplicationException ae, GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE
        }
    }

    @Test
    void testFetchByBankCodeBySearch() {
        Bank bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        bank.refresh()
        assertNotNull bank.id

        Bank getBankDetails = financeBankCompositeService.findBankCodeListByEffectiveDateAndBankCode([effectiveDate: new Date(), searchParam: i_success_bank, coaCode:i_success_chartOfAccounts] , [max:10, offset:0])
        assertNotNull getBankDetails
    }

    @Test
    void testFetchByBankCodeByInvalidSearch() {
        try{
            Bank getBankDetails = financeBankCompositeService.findBankCodeListByEffectiveDateAndBankCode([effectiveDate: new Date() , searchParam:'XYZC', coaCode:'B'] , [max:10, offset:0])
            fail 'should fail this' + GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE
        }catch (ApplicationException ae) {
            assertApplicationException ae, GeneralValidationCommonConstants.ERROR_MSG_MISSING_BANK_CODE
        }
    }

    private Bank newValidForCreateBank() {
        Bank bank = new Bank(
                bank: i_success_bank,
                effectiveDate: i_success_effectiveDate,
                nextChangeDate: i_success_nextChangeDate,
                bankPidm: i_success_bankPidm,
                bankAccountNumber: i_success_accountNumber,
                bankAccountName: i_success_accountName,
                achStatus: i_success_achStatus,
                statusIndicator: i_success_statusIndicator,
                terminationDate: i_success_termDate,
                chartOfAccounts: i_success_chartOfAccounts,
                accountCash: i_success_accountCash,
                accountInterfund: i_success_accountInterfund,
                fund: i_success_fund,
                bankOriginatingRoutingNumber: i_success_bankRoutNumber,
                achDestinationRoutingNumber: i_success_achDestinationNumber,
                achOriginatingRoutingNumber: i_success_achOriginalNumber,
                achDestinationName: i_success_achDestinationName,
                achOriginatingName: i_success_achOriginalName,
                achOriginatingNameShort: i_success_achShortOriginalName,
                companyType: i_success_companyType,
                companyId: i_success_companyId,
                achFileNumber: i_success_achFileNumber,
                currencyConversion: i_success_currencyConversion,
        )
        return bank
    }
}
