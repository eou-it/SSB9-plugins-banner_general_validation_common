/** *****************************************************************************
 Copyright 2013-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.crossproduct.Bank
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class CurrencyConversionIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance

    //Valid test data (For success tests)
    def i_success_currencyConversion = "LIR"
    def i_success_rateEffectiveDate = new Date()
    def i_success_rateNextChangeDate = new Date()
    def i_success_rateTerminationDate = new Date()
    def i_success_title = "TTTTT"
    def i_success_statusIndicator = "I"
    def i_success_accountsPayableAccount = "TTTTT"
    def i_success_nation = "150"
    def i_success_conversionType = "P"
    def i_success_exchAccount = "TTTTT"
    def i_success_disbAgentPidm = 1
    def i_success_accountsPayableAcct2 = "TTTTT"
    def i_success_exchAcct2 = "TTTTT"
    def i_success_standardCodeIso = "TTT"

    //Invalid test data (For failure tests)
    def i_failure_currencyConversion = null
    def i_failure_rateEffectiveDate = new Date()
    def i_failure_rateNextChangeDate = new Date()
    def i_failure_rateTerminationDate = new Date()
    def i_failure_title = "TTTTT"
    def i_failure_statusIndicator = "I"
    def i_failure_accountsPayableAccount = "TTTTT"
    def i_failure_nation = "TTTTT"
    def i_failure_conversionType = "#"
    def i_failure_exchAccount = "TTTTT"
    def i_failure_disbAgentPidm = 1
    def i_failure_accountsPayableAcct2 = "TTTTT"
    def i_failure_exchAcct2 = "TTTTT"
    def i_failure_standardCodeIso = "TTT"

    //Test data for creating updating domain instance

    //Valid test data (For success tests)
    def u_success_currencyConversion = "YEN"
    def u_success_rateEffectiveDate = new Date()
    def u_success_rateNextChangeDate = new Date()
    def u_success_rateTerminationDate = new Date()
    def u_success_title = "TTTTT"
    def u_success_statusIndicator = "I"
    def u_success_accountsPayableAccount = "TTTTT"
    def u_success_nation = "155"
    def u_success_conversionType = "D"
    def u_success_exchAccount = "TTTTT"
    def u_success_disbAgentPidm = 1
    def u_success_accountsPayableAcct2 = "TTTTT"
    def u_success_exchAcct2 = "TTTTT"
    def u_success_standardCodeIso = "TTT"

    //Valid test data (For failure tests)
    def u_failure_currencyConversion = null
    def u_failure_rateEffectiveDate = new Date()
    def u_failure_rateNextChangeDate = new Date()
    def u_failure_rateTerminationDate = new Date()
    def u_failure_title = "TTTTT"
    def u_failure_statusIndicator = "I"
    def u_failure_accountsPayableAccount = "TTTTT"
    def u_failure_nation = "TTTTT"
    def u_failure_conversionType = "#"
    def u_failure_exchAccount = "TTTTT"
    def u_failure_disbAgentPidm = 1
    def u_failure_accountsPayableAcct2 = "TTTTT"
    def u_failure_exchAcct2 = "TTTTT"
    def u_failure_standardCodeIso = "TTT"


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
    void testCreateValidCurrencyConversion() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversion.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull currencyConversion.id
    }


    @Test
    void testCreateInvalidCurrencyConversion() {
        def currencyConversion = newInvalidForCreateCurrencyConversion()
        shouldFail(ValidationException) {
            currencyConversion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidCurrencyConversion() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversion.save(failOnError: true, flush: true)
        assertNotNull currencyConversion.id
        assertEquals 0L, currencyConversion.version
        assertEquals i_success_rateEffectiveDate, currencyConversion.rateEffectiveDate
        assertEquals i_success_rateNextChangeDate, currencyConversion.rateNextChangeDate
        assertEquals i_success_rateTerminationDate, currencyConversion.rateTerminationDate
        assertEquals i_success_title, currencyConversion.title
        assertEquals i_success_statusIndicator, currencyConversion.statusIndicator
        assertEquals i_success_accountsPayableAccount, currencyConversion.accountsPayableAccount
        assertEquals i_success_nation, currencyConversion.nation
        assertEquals i_success_conversionType, currencyConversion.conversionType
        assertEquals i_success_exchAccount, currencyConversion.exchangeAccount
        assertEquals i_success_disbAgentPidm, currencyConversion.disbursingAgentPidm
        assertEquals i_success_accountsPayableAcct2, currencyConversion.accountsPayableAccount2
        assertEquals i_success_exchAcct2, currencyConversion.exchangeAccount2
        assertEquals i_success_standardCodeIso, currencyConversion.standardCodeIso

        //Assert for successful update
        def currencyConversion1
        currencyConversion1 = CurrencyConversion.get(currencyConversion.id)
        currencyConversion1.conversionType = u_success_conversionType
        currencyConversion1.save()
        currencyConversion1 = CurrencyConversion.get(currencyConversion.id)
        assertEquals currencyConversion1.conversionType, u_success_conversionType
    }


    @Test
    void testUpdateInvalidCurrencyConversion() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversion.save(failOnError: true, flush: true)
        assertNotNull currencyConversion.id
        assertEquals 0L, currencyConversion.version
        assertEquals i_success_rateEffectiveDate, currencyConversion.rateEffectiveDate
        assertEquals i_success_rateNextChangeDate, currencyConversion.rateNextChangeDate
        assertEquals i_success_rateTerminationDate, currencyConversion.rateTerminationDate
        assertEquals i_success_title, currencyConversion.title
        assertEquals i_success_statusIndicator, currencyConversion.statusIndicator
        assertEquals i_success_accountsPayableAccount, currencyConversion.accountsPayableAccount
        assertEquals i_success_nation, currencyConversion.nation
        assertEquals i_success_conversionType, currencyConversion.conversionType
        assertEquals i_success_exchAccount, currencyConversion.exchangeAccount
        assertEquals i_success_disbAgentPidm, currencyConversion.disbursingAgentPidm
        assertEquals i_success_accountsPayableAcct2, currencyConversion.accountsPayableAccount2
        assertEquals i_success_exchAcct2, currencyConversion.exchangeAccount2
        assertEquals i_success_standardCodeIso, currencyConversion.standardCodeIso

        //Update the entity with invalid values
        currencyConversion.rateTerminationDate = u_failure_rateTerminationDate
        currencyConversion.title = u_failure_title
        currencyConversion.statusIndicator = u_failure_statusIndicator
        currencyConversion.accountsPayableAccount = u_failure_accountsPayableAccount
        currencyConversion.nation = u_failure_nation
        currencyConversion.conversionType = u_failure_conversionType
        currencyConversion.exchangeAccount = u_failure_exchAccount
        currencyConversion.disbursingAgentPidm = u_failure_disbAgentPidm
        currencyConversion.accountsPayableAccount2 = u_failure_accountsPayableAcct2
        currencyConversion.exchangeAccount2 = u_failure_exchAcct2
        currencyConversion.standardCodeIso = u_failure_standardCodeIso

        shouldFail(ValidationException) {
            currencyConversion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def currencyConversion = newValidForCreateCurrencyConversion()

        def rateEffectiveDate = new Date()
        def rateNextChangeDate = new Date() + 10
        def rateTerminationDate = new Date() - 10

        currencyConversion.rateEffectiveDate = rateEffectiveDate
        currencyConversion.rateNextChangeDate = rateNextChangeDate
        currencyConversion.rateTerminationDate = rateTerminationDate
        currencyConversion.save(flush: true, failOnError: true)
        currencyConversion.refresh()
        assertNotNull "CurrencyConversion should have been saved", currencyConversion.id

        // test date values -
        assertEquals date.format(today), date.format(currencyConversion.lastModified)
        assertEquals hour.format(today), hour.format(currencyConversion.lastModified)

        assertEquals date.format(currencyConversion.rateEffectiveDate), date.format(rateEffectiveDate)
        assertEquals date.format(currencyConversion.rateNextChangeDate), date.format(rateNextChangeDate)
        assertEquals date.format(currencyConversion.rateTerminationDate), date.format(rateTerminationDate)
    }


    @Test
    void testOptimisticLock() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversion.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVCURR set GTVCURR_VERSION = 999 where GTVCURR_SURROGATE_ID = ?", [currencyConversion.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        currencyConversion.rateTerminationDate = u_success_rateTerminationDate
        currencyConversion.title = u_success_title
        currencyConversion.statusIndicator = u_success_statusIndicator
        currencyConversion.accountsPayableAccount = u_success_accountsPayableAccount
        currencyConversion.nation = u_success_nation
        currencyConversion.conversionType = u_success_conversionType
        currencyConversion.exchangeAccount = u_success_exchAccount
        currencyConversion.disbursingAgentPidm = u_success_disbAgentPidm
        currencyConversion.accountsPayableAccount2 = u_success_accountsPayableAcct2
        currencyConversion.exchangeAccount2 = u_success_exchAcct2
        currencyConversion.standardCodeIso = u_success_standardCodeIso
        shouldFail(HibernateOptimisticLockingFailureException) {
            currencyConversion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteCurrencyConversion() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversion.save(failOnError: true, flush: true)
        def id = currencyConversion.id
        assertNotNull id
        currencyConversion.delete()
        assertNull CurrencyConversion.get(id)
    }


    @Test
    void testValidation() {
        def currencyConversion = newInvalidForCreateCurrencyConversion()
        assertFalse "CurrencyConversion could not be validated as expected due to ${currencyConversion.errors}", currencyConversion.validate()
    }


    @Test
    void testNullValidationFailure() {
        def currencyConversion = new CurrencyConversion()
        assertFalse "CurrencyConversion should have failed validation", currencyConversion.validate()
        assertErrorsFor currencyConversion, 'nullable',
                [
                        'rateEffectiveDate',
                        'rateNextChangeDate',
                        'title',
                        'statusIndicator',
                        'currencyConversion'
                ]
        assertNoErrorsFor currencyConversion,
                [
                        'rateTerminationDate',
                        'accountsPayableAccount',
                        'nation',
                        'conversionType',
                        'exchangeAccount',
                        'disbursingAgentPidm',
                        'accountsPayableAccount2',
                        'exchangeAccount2',
                        'standardCodeIso',
                        'bank'
                ]
    }


    @Test
    void testFetchCurrentCurrencyConversion() {
        def currencyConversion = CurrencyConversion.fetchCurrentCurrencyConversion("USD")
        assertNotNull(currencyConversion)
    }


    @Test
    void testMaxSizeValidationFailures() {
        def currencyConversion = new CurrencyConversion(
                currencyConversion: 'XXXXX',
                title: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                accountsPayableAccount: 'XXXXXXXX',
                nation: 'XXXXXXX',
                conversionType: 'XXX',
                exchangeAccount: 'XXXXXXXX',
                accountsPayableAccount2: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                exchangeAccount2: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                standardCodeIso: 'XXXXX')
        assertFalse "CurrencyConversion should have failed validation", currencyConversion.validate()
        assertErrorsFor currencyConversion, 'maxSize', ['currencyConversion', 'title', 'accountsPayableAccount', 'nation', 'conversionType',
                'exchangeAccount', 'accountsPayableAccount2', 'exchangeAccount2', 'standardCodeIso']
    }


    @Test
    void testInListValidationFailures() {
        def currencyConversion = new CurrencyConversion(
                statusIndicator: "#",
                conversionType: "#")
        assertFalse "CurrencyConversion should have failed validation", currencyConversion.validate()
        assertErrorsFor currencyConversion, 'inList', [
                'statusIndicator',
                'conversionType']
    }


    private def newValidForCreateCurrencyConversion() {

        def currencyConversion = new CurrencyConversion(
                currencyConversion: i_success_currencyConversion,
                rateEffectiveDate: i_success_rateEffectiveDate,
                rateNextChangeDate: i_success_rateNextChangeDate,
                rateTerminationDate: i_success_rateTerminationDate,
                title: i_success_title,
                statusIndicator: i_success_statusIndicator,
                accountsPayableAccount: i_success_accountsPayableAccount,
                nation: i_success_nation,
                conversionType: i_success_conversionType,
                exchangeAccount: i_success_exchAccount,
                disbursingAgentPidm: i_success_disbAgentPidm,
                accountsPayableAccount2: i_success_accountsPayableAcct2,
                exchangeAccount2: i_success_exchAcct2,
                standardCodeIso: i_success_standardCodeIso,
                bank: "##"
        )
        return currencyConversion
    }


    private def newInvalidForCreateCurrencyConversion() {
        def currencyConversion = new CurrencyConversion(
                currencyConversion: i_failure_currencyConversion,
                rateEffectiveDate: i_failure_rateEffectiveDate,
                rateNextChangeDate: i_failure_rateNextChangeDate,
                rateTerminationDate: i_failure_rateTerminationDate,
                title: i_failure_title,
                statusIndicator: i_failure_statusIndicator,
                accountsPayableAccount: i_failure_accountsPayableAccount,
                nation: i_failure_nation,
                conversionType: i_failure_conversionType,
                exchangeAccount: i_failure_exchAccount,
                disbursingAgentPidm: i_failure_disbAgentPidm,
                accountsPayableAccount2: i_failure_accountsPayableAcct2,
                exchangeAccount2: i_failure_exchAcct2,
                standardCodeIso: i_failure_standardCodeIso,
                bank: null
        )
        return currencyConversion
    }

}
