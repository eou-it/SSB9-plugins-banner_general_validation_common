/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.utility

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import java.text.SimpleDateFormat

class InformationTextUtilityIntegrationTests extends BaseIntegrationTestCase {

    def selfServiceBannerAuthenticationProvider


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }

    void testSingleValueKeyWithBaseline() {
        createBaselineWithSingleValueKey()
        setAuthentication()
        def informationText = InformationTextUtility.getMessage("TESTPAGE", "key1")
        String expectedText = "Baseline text no 0"
        assertEquals(expectedText, informationText)
        logout()
    }

    void testMultipleValuesKeyWithBaseline() {
        createBaselineTestDataWithNotNullDate()
        setAuthentication()
        def informationText = InformationTextUtility.getMessages("TESTPAGE")
        String value1 = "Baseline text no 0\nBaseline text no 1\nBaseline text no 2\nBaseline text no 3"
        String value2 = "Baseline second text no 0\nBaseline second text no 1\nBaseline second text no 2\nBaseline second text no 3"
        assertEquals(value1, informationText.key1)
        assertEquals(value2, informationText.key2)
        logout()
    }

    void testSingleKeyWithoutValue() {
        setAuthentication()
        def informationText = InformationTextUtility.getMessage("TESTPAGE", "key1")
        String expectedText = "key1"
        assertEquals(expectedText, informationText)
        logout()
    }

    void testSingleValueKeyWithLocal() {
        createLocalWithSingleValueKey()
        setAuthentication()
        def informationText = InformationTextUtility.getMessage("TESTPAGE", "key1")
        String expectedText = "Local text no 0"
        assertEquals(expectedText, informationText)
        logout()
    }

    void testMultipleValuesKeyWithLocalNullDate() {
        createBaselineTestDataWithNotNullDate()
        createLocalTestDataWithNullDate()
        setAuthentication()
        def informationText = InformationTextUtility.getMessages("TESTPAGE")
        String expectedText = ""
        assertEquals(expectedText, informationText.key1)
        assertEquals(expectedText, informationText.key2)
        logout()
    }

    void testMultipleValuesKeyWithLocalNotNullDate() {
        createBaselineTestDataWithNotNullDate()
        createLocalTestDataWithNotNullDate()
        setAuthentication()
        def informationText = InformationTextUtility.getMessages("TESTPAGE")
        String value1 = "Local text no 0\nLocal text no 1\nLocal text no 2\nLocal text no 3"
        String value2 = "Local second text no 0\nLocal second text no 1\nLocal second text no 2\nLocal second text no 3"
        assertEquals(value1, informationText.key1)
        assertEquals(value2, informationText.key2)
        logout()
    }

    void testMultipleValuesKeyWithLocalSingleNullDate() {
        createBaselineTestDataWithNotNullDate()
        createLocalTestDataWithSingleNullDate()
        setAuthentication()
        def informationText = InformationTextUtility.getMessage("TESTPAGE", "key1")
        String expectedText = "Local text no 0\nLocal text no 1\nLocal text no 2"
        assertEquals(expectedText, informationText)
        logout()
    }

    void testLocalWithFutureStartDate() {
        createBaselineTestDataWithNotNullDate()
        createLocalTestDataWithFutureStartDate()
        setAuthentication()
        def informationText = InformationTextUtility.getMessage("TESTPAGE", "key1")
        String expectedText = "Baseline text no 0\nBaseline text no 1\nBaseline text no 2\nBaseline text no 3"
        assertEquals(expectedText, informationText)
        logout()
    }


    private def newValidForCreateInformationText() {
        def informationText = new InformationText(
                pageName: i_success_pageName,
                label: i_success_label,
                textType: i_success_textType,
                sequenceNumber: i_success_sequenceNumber,
                persona: i_success_persona,
                startDate: i_success_startDate,
                endDate: i_success_endDate,
                text: i_success_text,
                locale: i_success_locale,
                sourceIndicator: i_success_sourceIndicator,
                comment: i_success_comment
        )
        return informationText
    }


    private def newInvalidForCreateInformationText() {
        def informationText = new InformationText(
                pageName: i_failure_pageName,
                label: i_failure_label,
                textType: i_failure_textType,
                sequenceNumber: i_failure_sequenceNumber,
                persona: i_failure_persona,
                startDate: i_failure_startDate,
                endDate: i_failure_endDate,
                text: i_failure_text,
                locale: i_failure_locale,
                sourceIndicator: i_failure_sourceIndicator,
                comment: i_failure_comment
        )
        return informationText
    }

    private def setAuthentication() {
        def auth = selfServiceBannerAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken('HOSWEB002', '111111'))
        SecurityContextHolder.getContext().setAuthentication(auth)
    }

    private def createBaselineWithSingleValueKey() {
        createInfoTextTestData("B", "Baseline text no", "key1", new Date(), new Date(), 1)
    }

    private def createBaselineTestDataWithNotNullDate() {
        createInfoTextTestData("B", "Baseline text no", "key1", new Date(), new Date())
        createInfoTextTestData("B", "Baseline second text no", "key2", new Date(), new Date())
    }

    private def createLocalWithSingleValueKey() {
        createInfoTextTestData("L", "Local text no", "key1", new Date(), new Date(), 1)
    }

    def createLocalTestDataWithNotNullDate() {
        createInfoTextTestData("L", "Local text no", "key1", new Date(), new Date())
        createInfoTextTestData("L", "Local second text no", "key2", new Date(), new Date())
    }

    def createLocalTestDataWithFutureStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        def startDate = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, 5)
        def endDate = calendar.getTime()
        createInfoTextTestData("L", "Local text no", "key1", startDate, endDate)

    }

    def createLocalTestDataWithNullDate() {
        createInfoTextTestData("L", "Local text no", "key1")
        createInfoTextTestData("L", "Local text no", "key2")
    }

    def createLocalTestDataWithSingleNullDate() {
        createInfoTextTestData("L", "Local text no", "key1", new Date(), new Date(), 4, true)
    }

    def createInfoTextTestData(sourceIndicator, text, label, startDate = null, endDate = null, recordsSize = 4, singleNullDateIndicator = false) {
        def pageName = "TESTPAGE"
        def textType = "Label"
        def sequenceNumber = 1
        def persona = "STUDENT"
        def locale = "EN_US"
        def comment = "Test data"
        recordsSize.times {
            if (singleNullDateIndicator) {
                if (it == recordsSize - 1) {
                    startDate = null
                    endDate = null
                }
            }
            new InformationText(
                    pageName: pageName,
                    label: label,
                    textType: textType,
                    sequenceNumber: sequenceNumber++,
                    persona: persona,
                    startDate: startDate,
                    endDate: endDate,
                    text: text + " " + it,
                    locale: locale,
                    sourceIndicator: sourceIndicator,
                    comment: comment
            ).save(failOnError: true, flush: true)

        }
    }

}


