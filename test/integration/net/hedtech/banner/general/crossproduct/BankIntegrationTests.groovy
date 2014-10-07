/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.crossproduct
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class BankIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance

    //Valid test data (For success tests)
    def i_success_bank = "TT"
    def i_success_currencyConversion
    def i_success_effectiveDate = new Date()
    def i_success_nextChangeDate = new Date()
    def i_success_bankPidm = 1
    def i_success_accountNumber = "TTTTT"
    def i_success_accountName = "TTTTT"
    def i_success_achStatus = "A"
    def i_success_statusIndicator = "A"
    def i_success_termDate = new Date()
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

    //Invalid test data (For failure tests)
    def i_failure_bank = "######"
    def i_failure_currencyConversion
    def i_failure_effectiveDate = new Date()
    def i_failure_nextChangeDate = new Date()
    def i_failure_bankPidm = 1
    def i_failure_accountNumber = "TTTTT"
    def i_failure_accountName = "TTTTT"
    def i_failure_achStatus = "#"
    def i_failure_statusIndicator = "A"
    def i_failure_termDate = new Date()
    def i_failure_chartOfAccounts = "#"
    def i_failure_accountCash = "TTTTT"
    def i_failure_accountInterfund = "TTTTT"
    def i_failure_fund = "TTTTT"
    def i_failure_achDestinationNumber = "TTTTT"
    def i_failure_achOriginalNumber = "TTTTT"
    def i_failure_achDestinationName = "TTTTT"
    def i_failure_achShortOriginalName = "TTTTT"
    def i_failure_companyType = "#"
    def i_failure_companyId = "TTTTT"
    def i_failure_achOriginalName = "TTTTT"
    def i_failure_bankRoutNumber = "TTTTT"
    def i_failure_achFileNumber = 1

    //Test data for creating updating domain instance

    //Valid test data (For success tests)
    def u_success_bank = "RR"
    def u_success_currencyConversion
    def u_success_effectiveDate = new Date()
    def u_success_nextChangeDate = new Date()
    def u_success_bankPidm = 1
    def u_success_accountNumber = "TTTTT"
    def u_success_accountName = "TTTTT"
    def u_success_achStatus = "I"
    def u_success_statusIndicator = "A"
    def u_success_termDate = new Date()
    def u_success_chartOfAccounts = "#"
    def u_success_accountCash = "TTTTT"
    def u_success_accountInterfund = "TTTTT"
    def u_success_fund = "TTTTT"
    def u_success_achDestinationNumber = "TTTTT"
    def u_success_achOriginalNumber = "TTTTT"
    def u_success_achDestinationName = "TTTTT"
    def u_success_achShortOriginalName = "TTTTT"
    def u_success_companyType = "#"
    def u_success_companyId = "TTTTT"
    def u_success_achOriginalName = "TTTTT"
    def u_success_bankRoutNumber = "TTTTT"
    def u_success_achFileNumber = 1

    //Valid test data (For failure tests)
    def u_failure_bank = "RRRRRR"
    def u_failure_currencyConversion
    def u_failure_effectiveDate = new Date()
    def u_failure_nextChangeDate = new Date()
    def u_failure_bankPidm = 1
    def u_failure_accountNumber = "TTTTT"
    def u_failure_accountName = "TTTTT"
    def u_failure_achStatus = "#"
    def u_failure_statusIndicator = "A"
    def u_failure_termDate = new Date()
    def u_failure_chartOfAccounts = "#"
    def u_failure_accountCash = "TTTTT"
    def u_failure_accountInterfund = "TTTTT"
    def u_failure_fund = "TTTTT"
    def u_failure_achDestinationNumber = "TTTTT"
    def u_failure_achOriginalNumber = "TTTTT"
    def u_failure_achDestinationName = "TTTTT"
    def u_failure_achShortOriginalName = "TTTTT"
    def u_failure_companyType = "#"
    def u_failure_companyId = "TTTTT"
    def u_failure_achOriginalName = "TTTTT"
    def u_failure_bankRoutNumber = "TTTTT"
    def u_failure_achFileNumber = 1


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
    void testCreateValidBank() {
        def bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull bank.id
    }


    @Test
    void testCreateInvalidBank() {
        def bank = newInvalidForCreateBank()
        shouldFail(ValidationException) {
            bank.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidBank() {
        def bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        assertNotNull bank.id
        assertEquals 0L, bank.version
        assertEquals i_success_effectiveDate, bank.effectiveDate
        assertEquals i_success_nextChangeDate, bank.nextChangeDate
        assertEquals i_success_bankPidm, bank.bankPidm
        assertEquals i_success_accountNumber, bank.bankAccountNumber
        assertEquals i_success_accountName, bank.bankAccountName
        assertEquals i_success_achStatus, bank.achStatus
        assertEquals i_success_statusIndicator, bank.statusIndicator
        assertEquals i_success_termDate, bank.terminationDate
        assertEquals i_success_chartOfAccounts, bank.chartOfAccounts
        assertEquals i_success_accountCash, bank.accountCash
        assertEquals i_success_accountInterfund, bank.accountInterfund
        assertEquals i_success_fund, bank.fund
        assertEquals i_success_achDestinationNumber, bank.achDestinationRoutingNumber
        assertEquals i_success_achOriginalNumber, bank.achOriginatingRoutingNumber
        assertEquals i_success_achDestinationName, bank.achDestinationName
        assertEquals i_success_achShortOriginalName, bank.achOriginatingNameShort
        assertEquals i_success_companyType, bank.companyType
        assertEquals i_success_companyId, bank.companyId
        assertEquals i_success_achOriginalName, bank.achOriginatingName
        assertEquals i_success_bankRoutNumber, bank.bankOriginatingRoutingNumber
        assertEquals i_success_achFileNumber, bank.achFileNumber

        //Update the entity
        bank.bankPidm = u_success_bankPidm
        bank.bankAccountNumber = u_success_accountNumber
        bank.bankAccountName = u_success_accountName
        bank.achStatus = u_success_achStatus
        bank.statusIndicator = u_success_statusIndicator
        bank.terminationDate = u_success_termDate
        bank.chartOfAccounts = u_success_chartOfAccounts
        bank.accountCash = u_success_accountCash
        bank.accountInterfund = u_success_accountInterfund
        bank.fund = u_success_fund
        bank.achDestinationRoutingNumber = u_success_achDestinationNumber
        bank.achOriginatingRoutingNumber = u_success_achOriginalNumber
        bank.achDestinationName = u_success_achDestinationName
        bank.achOriginatingNameShort = u_success_achShortOriginalName
        bank.companyType = u_success_companyType
        bank.companyId = u_success_companyId
        bank.achOriginatingName = u_success_achOriginalName
        bank.bankOriginatingRoutingNumber = u_success_bankRoutNumber
        bank.achFileNumber = u_success_achFileNumber

        bank.currencyConversion = u_success_currencyConversion
        bank.save(failOnError: true, flush: true)

        //Assert for successful update
        bank = Bank.get(bank.id)
        assertEquals 1L, bank?.version
        assertEquals u_success_bankPidm, bank.bankPidm
        assertEquals u_success_accountNumber, bank.bankAccountNumber
        assertEquals u_success_accountName, bank.bankAccountName
        assertEquals u_success_achStatus, bank.achStatus
        assertEquals u_success_statusIndicator, bank.statusIndicator
        assertEquals u_success_termDate, bank.terminationDate
        assertEquals u_success_chartOfAccounts, bank.chartOfAccounts
        assertEquals u_success_accountCash, bank.accountCash
        assertEquals u_success_accountInterfund, bank.accountInterfund
        assertEquals u_success_fund, bank.fund
        assertEquals u_success_achDestinationNumber, bank.achDestinationRoutingNumber
        assertEquals u_success_achOriginalNumber, bank.achOriginatingRoutingNumber
        assertEquals u_success_achDestinationName, bank.achDestinationName
        assertEquals u_success_achShortOriginalName, bank.achOriginatingNameShort
        assertEquals u_success_companyType, bank.companyType
        assertEquals u_success_companyId, bank.companyId
        assertEquals u_success_achOriginalName, bank.achOriginatingName
        assertEquals u_success_bankRoutNumber, bank.bankOriginatingRoutingNumber
        assertEquals u_success_achFileNumber, bank.achFileNumber
    }


    @Test
    void testUpdateInvalidBank() {
        def bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        assertNotNull bank.id
        assertEquals 0L, bank.version
        assertEquals i_success_effectiveDate, bank.effectiveDate
        assertEquals i_success_nextChangeDate, bank.nextChangeDate
        assertEquals i_success_bankPidm, bank.bankPidm
        assertEquals i_success_accountNumber, bank.bankAccountNumber
        assertEquals i_success_accountName, bank.bankAccountName
        assertEquals i_success_achStatus, bank.achStatus
        assertEquals i_success_statusIndicator, bank.statusIndicator
        assertEquals i_success_termDate, bank.terminationDate
        assertEquals i_success_chartOfAccounts, bank.chartOfAccounts
        assertEquals i_success_accountCash, bank.accountCash
        assertEquals i_success_accountInterfund, bank.accountInterfund
        assertEquals i_success_fund, bank.fund
        assertEquals i_success_achDestinationNumber, bank.achDestinationRoutingNumber
        assertEquals i_success_achOriginalNumber, bank.achOriginatingRoutingNumber
        assertEquals i_success_achDestinationName, bank.achDestinationName
        assertEquals i_success_achShortOriginalName, bank.achOriginatingNameShort
        assertEquals i_success_companyType, bank.companyType
        assertEquals i_success_companyId, bank.companyId
        assertEquals i_success_achOriginalName, bank.achOriginatingName
        assertEquals i_success_bankRoutNumber, bank.bankOriginatingRoutingNumber
        assertEquals i_success_achFileNumber, bank.achFileNumber

        //Update the entity with invalid values
        bank.bankPidm = u_failure_bankPidm
        bank.bankAccountNumber = u_failure_accountNumber
        bank.bankAccountName = u_failure_accountName
        bank.achStatus = u_failure_achStatus
        bank.statusIndicator = u_failure_statusIndicator
        bank.terminationDate = u_failure_termDate
        bank.chartOfAccounts = u_failure_chartOfAccounts
        bank.accountCash = u_failure_accountCash
        bank.accountInterfund = u_failure_accountInterfund
        bank.fund = u_failure_fund
        bank.achDestinationRoutingNumber = u_failure_achDestinationNumber
        bank.achOriginatingRoutingNumber = u_failure_achOriginalNumber
        bank.achDestinationName = u_failure_achDestinationName
        bank.achOriginatingNameShort = u_failure_achShortOriginalName
        bank.companyType = u_failure_companyType
        bank.companyId = u_failure_companyId
        bank.achOriginatingName = u_failure_achOriginalName
        bank.bankOriginatingRoutingNumber = u_failure_bankRoutNumber
        bank.achFileNumber = u_failure_achFileNumber

        bank.currencyConversion = u_failure_currencyConversion
        shouldFail(ValidationException) {
            bank.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def bank = newValidForCreateBank()

        def newEffectiveDate = new Date()
        def newNextChangeDate = new Date() + 10
        def newTerminationDate = new Date() - 10

        bank.effectiveDate = newEffectiveDate
        bank.nextChangeDate = newNextChangeDate
        bank.terminationDate = newTerminationDate

        bank.save(flush: true, failOnError: true)
        bank.refresh()
        assertNotNull "Bank should have been saved", bank.id

        // test date values -
        assertEquals date.format(today), date.format(bank.lastModified)
        assertEquals hour.format(today), hour.format(bank.lastModified)

        assertEquals date.format(bank.effectiveDate), date.format(newEffectiveDate)
        assertEquals date.format(bank.nextChangeDate), date.format(newNextChangeDate)
        assertEquals date.format(bank.terminationDate), date.format(newTerminationDate)
    }


    @Test
    void testOptimisticLock() {
        def bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GXVBANK set GXVBANK_VERSION = 999 where GXVBANK_SURROGATE_ID = ?", [bank.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        bank.bankPidm = u_success_bankPidm
        bank.bankAccountNumber = u_success_accountNumber
        bank.bankAccountName = u_success_accountName
        bank.achStatus = u_success_achStatus
        bank.statusIndicator = u_success_statusIndicator
        bank.terminationDate = u_success_termDate
        bank.chartOfAccounts = u_success_chartOfAccounts
        bank.accountCash = u_success_accountCash
        bank.accountInterfund = u_success_accountInterfund
        bank.fund = u_success_fund
        bank.achDestinationRoutingNumber = u_success_achDestinationNumber
        bank.achOriginatingRoutingNumber = u_success_achOriginalNumber
        bank.achDestinationName = u_success_achDestinationName
        bank.achOriginatingNameShort = u_success_achShortOriginalName
        bank.companyType = u_success_companyType
        bank.companyId = u_success_companyId
        bank.achOriginatingName = u_success_achOriginalName
        bank.bankOriginatingRoutingNumber = u_success_bankRoutNumber
        bank.achFileNumber = u_success_achFileNumber
        shouldFail(HibernateOptimisticLockingFailureException) {
            bank.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteBank() {
        def bank = newValidForCreateBank()
        bank.save(failOnError: true, flush: true)
        def id = bank.id
        assertNotNull id
        bank.delete()
        assertNull Bank.get(id)
    }


    @Test
    void testValidation() {
        def bank = newInvalidForCreateBank()
        assertFalse "Bank could not be validated as expected due to ${bank.errors}", bank.validate()
    }


    @Test
    void testNullValidationFailure() {
        def bank = new Bank()
        assertFalse "Bank should have failed validation", bank.validate()
        assertErrorsFor bank, 'nullable',
                [
                        'effectiveDate',
                        'nextChangeDate',
                        'bankPidm',
                        'bankAccountNumber',
                        'bankAccountName',
                        'achStatus',
                        'statusIndicator',
                        'bank'
                ]
        assertNoErrorsFor bank,
                [
                        'terminationDate',
                        'chartOfAccounts',
                        'accountCash',
                        'accountInterfund',
                        'fund',
                        'achDestinationRoutingNumber',
                        'achOriginatingRoutingNumber',
                        'achDestinationName',
                        'achOriginatingNameShort',
                        'companyType',
                        'companyId',
                        'achOriginatingName',
                        'bankOriginatingRoutingNumber',
                        'achFileNumber',
                        'currencyConversion'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def bank = new Bank(
                chartOfAccounts: 'XXX',
                accountCash: 'XXXXXXXX',
                accountInterfund: 'XXXXXXXX',
                fund: 'XXXXXXXX',
                achDestinationRoutingNumber: 'XXXXXXXXXXXX',
                achOriginatingRoutingNumber: 'XXXXXXXXXXXX',
                achDestinationName: 'XXXXXXXXXXXXXXXXXXXXXXXXX',
                achOriginatingNameShort: 'XXXXXXXXXXXXXXXXX',
                companyType: 'XXX',
                companyId: 'XXXXXXXXXXX',
                achOriginatingName: 'XXXXXXXXXXXXXXXXXXXXXXXXX',
                bankOriginatingRoutingNumber: 'XXXXXXXXXXX')
        assertFalse "Bank should have failed validation", bank.validate()
        assertErrorsFor bank, 'maxSize', ['chartOfAccounts', 'accountCash', 'accountInterfund', 'fund', 'companyType', 'companyId',
                'bankOriginatingRoutingNumber',
                'achDestinationName', 'achDestinationRoutingNumber',
                'achOriginatingName', 'achOriginatingRoutingNumber', 'achOriginatingNameShort']
    }


    private def newValidForCreateBank() {
        def bank = new Bank(
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


    private def newInvalidForCreateBank() {
        def bank = new Bank(
                bank: i_failure_bank,
                effectiveDate: i_failure_effectiveDate,
                nextChangeDate: i_failure_nextChangeDate,
                bankPidm: i_failure_bankPidm,
                bankAccountNumber: i_failure_accountNumber,
                bankAccountName: i_failure_accountName,
                achStatus: i_failure_achStatus,
                statusIndicator: i_failure_statusIndicator,
                terminationDate: i_failure_termDate,
                chartOfAccounts: i_failure_chartOfAccounts,
                accountCash: i_failure_accountCash,
                accountInterfund: i_failure_accountInterfund,
                fund: i_failure_fund,
                bankOriginatingRoutingNumber: i_failure_bankRoutNumber,
                achDestinationRoutingNumber: i_failure_achDestinationNumber,
                achOriginatingRoutingNumber: i_failure_achOriginalNumber,
                achDestinationName: i_failure_achDestinationName,
                achOriginatingName: i_failure_achOriginalName,
                achOriginatingNameShort: i_failure_achShortOriginalName,
                companyType: i_failure_companyType,
                companyId: i_failure_companyId,
                achFileNumber: i_failure_achFileNumber,
                currencyConversion: i_failure_currencyConversion,
        )
        return bank
    }


}
