/*******************************************************************************
 Copyright 2013-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.crossproduct.Bank
import net.hedtech.banner.testing.BaseIntegrationTestCase

class CurrencyConversionServiceIntegrationTests extends BaseIntegrationTestCase {

    def currencyConversionService

    //Test data for creating new domain instance

    //Valid test data (For success tests)
    def i_success_currencyConversion = "TT"
    def i_success_rateEffectiveDate = new Date() - 1
    def i_success_rateNextChangeDate = new Date() + 1
    def i_success_rateTermDate = new Date() - 2
    def i_success_title = "TTTTT"
    def i_success_statusIndicator = "I"
    def i_success_accountsPayableAccount = "TTTTT"
    def i_success_nation = "150"
    def i_success_conversionType = "P"
    def i_success_exchAccount = "TTTTT"
    def i_success_disabilityAgentPidm = 1
    def i_success_accountsPayableAcct2 = "TTTTT"
    def i_success_exchAcct2 = "TTTTT"
    def i_success_standardCodeIso = "TTT"

    //Invalid test data (For failure tests)
    def i_failure_currencyConversion = null
    def i_failure_rateEffectiveDate = new Date() - 1
    def i_failure_rateNextChangeDate = new Date() + 1
    def i_failure_rateTermDate = new Date() - 2
    def i_failure_title = "TTTTT"
    def i_failure_statusIndicator = "I"
    def i_failure_accountsPayableAccount = "TTTTT"
    def i_failure_nation = "TTTTT"
    def i_failure_conversionType = "#"
    def i_failure_exchAccount = "TTTTT"
    def i_failure_disabilityAgentPidm = 1
    def i_failure_accountsPayableAcct2 = "TTTTT"
    def i_failure_exchAcct2 = "TTTTT"
    def i_failure_standardCodeIso = "TTT"
    def i_failure_bank = null

    //Test data for creating updating domain instance

    //Valid test data (For success tests)
    def u_success_currencyConversion = "##"
    def u_success_rateEffectiveDate = new Date() - 10
    def u_success_rateNextChangeDate = new Date() + 10
    def u_success_rateTermDate = new Date() - 20
    def u_success_title = "TTTTT"
    def u_success_statusIndicator = "I"
    def u_success_accountsPayableAccount = "TTTTT"
    def u_success_nation = "155"
    def u_success_conversionType = "D"
    def u_success_exchAccount = "TTTTT"
    def u_success_disabilityAgentPidm = 1
    def u_success_accountsPayableAcct2 = "TTTTT"
    def u_success_exchAcct2 = "TTTTT"
    def u_success_standardCodeIso = "TTT"

    //Valid test data (For failure tests)
    def u_failure_currencyConversion = "##"
    def u_failure_rateEffectiveDate = new Date()
    def u_failure_rateNextChangeDate = new Date()
    def u_failure_rateTermDate = new Date()
    def u_failure_title = "TTTTT"
    def u_failure_statusIndicator = "I"
    def u_failure_accountsPayableAccount = "TTTTT"
    def u_failure_nation = "TTTTT"
    def u_failure_conversionType = "#"
    def u_failure_exchAccount = "TTTTT"
    def u_failure_disabilityAgentPidm = 1
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
    void testCurrencyConversionValidCreate() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversionService.create(currencyConversion)
        assertNotNull "CurrencyConversion ID is null in CurrencyConversion Service Tests Create", currencyConversion.id
        assertNotNull "CurrencyConversion currencyConversion is null in CurrencyConversion Service Tests", currencyConversion.currencyConversion
        assertNotNull currencyConversion.version
        assertNotNull currencyConversion.dataOrigin
        assertNotNull currencyConversion.lastModifiedBy
        assertNotNull currencyConversion.lastModified
    }


    @Test
    void testCurrencyConversionInvalidCreate() {
        def currencyConversion = newInvalidForCreateCurrencyConversion()
        shouldFail(ApplicationException) {
            currencyConversionService.create(currencyConversion)
        }
    }


    @Test
    void testCurrencyConversionValidUpdate() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversionService.create(currencyConversion)
        assertNotNull "CurrencyConversion ID is null in CurrencyConversion Service Tests Create", currencyConversion.id
        assertNotNull "CurrencyConversion currencyConversion is null in CurrencyConversion Service Tests", currencyConversion.currencyConversion
        assertNotNull currencyConversion.version
        assertNotNull currencyConversion.dataOrigin
        assertNotNull currencyConversion.lastModifiedBy
        assertNotNull currencyConversion.lastModified

        //Update the entity with new values
        currencyConversion.rateTerminationDate = u_success_rateTermDate
        currencyConversion.title = u_success_title
        currencyConversion.statusIndicator = u_success_statusIndicator
        currencyConversion.accountsPayableAccount = u_success_accountsPayableAccount
        currencyConversion.nation = u_success_nation
        currencyConversion.conversionType = u_success_conversionType
        currencyConversion.exchangeAccount = u_success_exchAccount
        currencyConversion.disbursingAgentPidm = u_success_disabilityAgentPidm
        currencyConversion.accountsPayableAccount2 = u_success_accountsPayableAcct2
        currencyConversion.exchangeAccount2 = u_success_exchAcct2
        currencyConversion.standardCodeIso = u_success_standardCodeIso

        currencyConversionService.update(currencyConversion)

        //test the values
        assertEquals u_success_rateTermDate, currencyConversion.rateTerminationDate
        assertEquals u_success_title, currencyConversion.title
        assertEquals u_success_statusIndicator, currencyConversion.statusIndicator
        assertEquals u_success_accountsPayableAccount, currencyConversion.accountsPayableAccount
        assertEquals u_success_nation, currencyConversion.nation
        assertEquals u_success_conversionType, currencyConversion.conversionType
        assertEquals u_success_exchAccount, currencyConversion.exchangeAccount
        assertEquals u_success_disabilityAgentPidm, currencyConversion.disbursingAgentPidm
        assertEquals u_success_accountsPayableAcct2, currencyConversion.accountsPayableAccount2
        assertEquals u_success_exchAcct2, currencyConversion.exchangeAccount2
        assertEquals u_success_standardCodeIso, currencyConversion.standardCodeIso
    }


    @Test
    void testCurrencyConversionInvalidUpdate() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversionService.create(currencyConversion)
        assertNotNull "CurrencyConversion ID is null in CurrencyConversion Service Tests Create", currencyConversion.id
        assertNotNull "CurrencyConversion currencyConversion is null in CurrencyConversion Service Tests", currencyConversion.currencyConversion
        assertNotNull currencyConversion.version
        assertNotNull currencyConversion.dataOrigin
        assertNotNull currencyConversion.lastModifiedBy
        assertNotNull currencyConversion.lastModified

        //Update the entity with new invalid values
        currencyConversion.rateTerminationDate = u_failure_rateTermDate
        currencyConversion.title = u_failure_title
        currencyConversion.statusIndicator = u_failure_statusIndicator
        currencyConversion.accountsPayableAccount = u_failure_accountsPayableAccount
        currencyConversion.nation = u_failure_nation
        currencyConversion.conversionType = u_failure_conversionType
        currencyConversion.exchangeAccount = u_failure_exchAccount
        currencyConversion.disbursingAgentPidm = u_failure_disabilityAgentPidm
        currencyConversion.accountsPayableAccount2 = u_failure_accountsPayableAcct2
        currencyConversion.exchangeAccount2 = u_failure_exchAcct2
        currencyConversion.standardCodeIso = u_failure_standardCodeIso

        shouldFail(ApplicationException) {
            currencyConversion = currencyConversionService.update(currencyConversion)
        }
    }


    @Test
    void testCurrencyConversionDelete() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversionService.create(currencyConversion)
        assertNotNull "CurrencyConversion ID is null in CurrencyConversion Service Tests Create", currencyConversion.id
        def id = currencyConversion.id
        currencyConversionService.delete(currencyConversion)
        assertNull "CurrencyConversion should have been deleted", currencyConversion.get(id)
    }


    @Test
    void testReadOnly() {
        def currencyConversion = newValidForCreateCurrencyConversion()
        currencyConversionService.create(currencyConversion)
        assertNotNull "CurrencyConversion ID is null in CurrencyConversion Service Tests Create", currencyConversion.id

        currencyConversion.currencyConversion = u_success_currencyConversion
        currencyConversion.rateEffectiveDate = u_success_rateEffectiveDate
        currencyConversion.rateNextChangeDate = u_success_rateNextChangeDate
        try {
            currencyConversionService.update(currencyConversion)
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    private def newValidForCreateCurrencyConversion() {
        def newBank = new Bank(bank: "##",
                effectiveDate: new Date() - 100,
                bankPidm: 1,
                bankAccountName: '#####',
                bankAccountNumber: '#####',
                achStatus: "A",
                statusIndicator: "A",
                nextChangeDate: new Date() + 100,
                currencyConversion: null)
        save newBank

        def currencyConversion = new CurrencyConversion(
                currencyConversion: i_success_currencyConversion,
                rateEffectiveDate: i_success_rateEffectiveDate,
                rateNextChangeDate: i_success_rateNextChangeDate,
                rateTerminationDate: i_success_rateTermDate,
                title: i_success_title,
                statusIndicator: i_success_statusIndicator,
                accountsPayableAccount: i_success_accountsPayableAccount,
                nation: i_success_nation,
                conversionType: i_success_conversionType,
                exchangeAccount: i_success_exchAccount,
                disbursingAgentPidm: i_success_disabilityAgentPidm,
                accountsPayableAccount2: i_success_accountsPayableAcct2,
                exchangeAccount2: i_success_exchAcct2,
                standardCodeIso: i_success_standardCodeIso,
                bank: newBank.bank
        )
        return currencyConversion
    }


    private def newInvalidForCreateCurrencyConversion() {
        def currencyConversion = new CurrencyConversion(
                currencyConversion: i_failure_currencyConversion,
                rateEffectiveDate: i_failure_rateEffectiveDate,
                rateNextChangeDate: i_failure_rateNextChangeDate,
                rateTerminationDate: i_failure_rateTermDate,
                title: i_failure_title,
                statusIndicator: i_failure_statusIndicator,
                accountsPayableAccount: i_failure_accountsPayableAccount,
                nation: i_failure_nation,
                conversionType: i_failure_conversionType,
                exchangeAccount: i_failure_exchAccount,
                disbursingAgentPidm: i_failure_disabilityAgentPidm,
                accountsPayableAccount2: i_failure_accountsPayableAcct2,
                exchangeAccount2: i_failure_exchAcct2,
                standardCodeIso: i_failure_standardCodeIso,
                bank: i_failure_bank
        )
        return currencyConversion
    }


}
