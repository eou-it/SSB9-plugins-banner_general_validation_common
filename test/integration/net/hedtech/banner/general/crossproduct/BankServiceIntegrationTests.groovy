/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct

import net.hedtech.banner.general.overall.BankService
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class BankServiceIntegrationTests extends BaseIntegrationTestCase {
    BankService bankService

    //Valid test data (For success tests)
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
    void testFetchByBankCode() {
        Bank bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull bank.id

        Bank getBankDetails = bankService.fetchByBankCode(i_success_bank, new Date())
        assertNotNull getBankDetails.id
        assertEquals getBankDetails.effectiveDate, bank.effectiveDate
        assertEquals getBankDetails.nextChangeDate, bank.nextChangeDate
        assertEquals getBankDetails.bankPidm, bank.bankPidm
        assertEquals getBankDetails.bankAccountNumber, bank.bankAccountNumber
        assertEquals getBankDetails.bankAccountName, bank.bankAccountName
        assertEquals getBankDetails.achStatus, bank.achStatus
        assertEquals getBankDetails.statusIndicator, bank.statusIndicator
        assertEquals getBankDetails.terminationDate, bank.terminationDate
        assertEquals getBankDetails.chartOfAccounts, bank.chartOfAccounts
        assertEquals getBankDetails.accountCash, bank.accountCash
        assertEquals getBankDetails.accountInterfund, bank.accountInterfund
        assertEquals getBankDetails.fund, bank.fund
        assertEquals getBankDetails.achDestinationRoutingNumber, bank.achDestinationRoutingNumber
        assertEquals getBankDetails.achOriginatingRoutingNumber, bank.achOriginatingRoutingNumber
        assertEquals getBankDetails.achDestinationName, bank.achDestinationName
        assertEquals getBankDetails.achOriginatingNameShort, bank.achOriginatingNameShort
        assertEquals getBankDetails.companyType, bank.companyType
        assertEquals getBankDetails.companyId, bank.companyId
        assertEquals getBankDetails.achOriginatingName, bank.achOriginatingName
        assertEquals getBankDetails.bankOriginatingRoutingNumber, bank.bankOriginatingRoutingNumber
        assertEquals getBankDetails.achFileNumber, bank.achFileNumber
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
