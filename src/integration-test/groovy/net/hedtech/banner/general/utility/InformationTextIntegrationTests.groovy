/*********************************************************************************
 Copyright 2010-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

package net.hedtech.banner.general.utility

import net.hedtech.banner.general.system.SystemUtility
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class InformationTextIntegrationTests extends BaseIntegrationTestCase {
    def i_success_pageName = "TT"
    def i_success_label = "TT"
    def i_success_textType = "T"
    def i_success_sequenceNumber = 1
    def i_success_persona = "TTTT"
    def i_success_startDate = new Date()
    def i_success_endDate = new Date()
    def i_success_text = "TTTTT"
    def i_success_locale = "#"
    def i_success_sourceIndicator = "B"
    def i_success_comment = "TTTT"

    //Invalid test data (For failure tests)
    def i_failure_pageName = "TT" * 201
    def i_failure_label = "TT" * 201
    def i_failure_textType = "#"
    def i_failure_sequenceNumber = 1
    def i_failure_persona = "TTTT"
    def i_failure_startDate = new Date()
    def i_failure_endDate = new Date()
    def i_failure_text = "TTTTT"
    def i_failure_locale = "#"
    def i_failure_sourceIndicator = "U"
    def i_failure_comment = "TTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_pageName = "WW"
    def u_success_label = "WW"
    def u_success_textType = "P"
    def u_success_sequenceNumber = 1
    def u_success_persona = "TTTT"
    def u_success_startDate = new Date()
    def u_success_endDate = new Date()
    def u_success_text = "TTTTT"
    def u_success_locale = "#"
    def u_success_sourceIndicator = "L"
    def u_success_comment = "TTTT"

    //Valid test data (For failure tests)
    def u_failure_pageName = "TT" * 201
    def u_failure_label = "TT" * 201
    def u_failure_textType = "#"
    def u_failure_sequenceNumber = 1
    def u_failure_persona = "TTTT"
    def u_failure_startDate = new Date()
    def u_failure_endDate = new Date()
    def u_failure_text = "TTTTT"
    def u_failure_locale = "#"
    def u_failure_sourceIndicator = "U"
    def u_failure_comment = "TTTT"


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateValidInformationText( ) {
        def informationText = newValidForCreateInformationText()
        informationText.save( failOnError: true, flush: true )
        //Test if the generated entity now has an id assigned
        assertNotNull informationText.id
        assertNotNull informationText.dataOrigin
        assertNotNull informationText.lastModified
        assertNotNull informationText.lastModifiedBy
    }


    @Test
    void testCreateInvalidInformationText( ) {
        def informationText = newInvalidForCreateInformationText()
        informationText.pageName = i_failure_pageName
        try {
            informationText.save( failOnError: true, flush: true )
            fail( "This should have failed as pageName exceeded maxSize" )
        } catch ( ValidationException ) {
            assertErrorsFor informationText, 'maxSize', ['pageName']
        }
    }


    @Test
    void testUpdateValidInformationText( ) {
        def informationText = newValidForCreateInformationText()
        informationText.save( failOnError: true, flush: true )
        assertNotNull informationText.id
        assertEquals 0L, informationText.version
        assertEquals i_success_pageName, informationText.pageName
        assertEquals i_success_label, informationText.label
        assertEquals i_success_textType, informationText.textType
        assertEquals i_success_sequenceNumber, informationText.sequenceNumber
        assertEquals i_success_persona, informationText.persona
        assertEquals i_success_startDate, informationText.startDate
        assertEquals i_success_endDate, informationText.endDate
        assertEquals i_success_text, informationText.text
        assertEquals i_success_locale, informationText.locale
        assertEquals i_success_sourceIndicator, informationText.sourceIndicator
        assertEquals i_success_comment, informationText.comment

        //Update the entity
        informationText.pageName = u_success_pageName
        informationText.label = u_success_label
        informationText.textType = u_success_textType
        informationText.sequenceNumber = u_success_sequenceNumber
        informationText.persona = u_success_persona
        informationText.startDate = u_success_startDate
        informationText.endDate = u_success_endDate
        informationText.text = u_success_text
        informationText.locale = u_success_locale
        informationText.sourceIndicator = u_success_sourceIndicator
        informationText.comment = u_success_comment
        informationText.save( failOnError: true, flush: true )

        //Assert for sucessful update
        informationText = InformationText.get( informationText.id )
        assertEquals 1L, informationText?.version
        assertEquals u_success_pageName, informationText.pageName
        assertEquals u_success_label, informationText.label
        assertEquals u_success_textType, informationText.textType
        assertEquals u_success_sequenceNumber, informationText.sequenceNumber
        assertEquals u_success_persona, informationText.persona
        assertEquals u_success_startDate, informationText.startDate
        assertEquals u_success_endDate, informationText.endDate
        assertEquals u_success_text, informationText.text
        assertEquals u_success_locale, informationText.locale
        assertEquals u_success_sourceIndicator, informationText.sourceIndicator
        assertEquals u_success_comment, informationText.comment
    }


    @Test
    void testUpdateInvalidInformationText( ) {
        def informationText = newValidForCreateInformationText()
        informationText.save( failOnError: true, flush: true )
        assertNotNull informationText.id
        assertEquals 0L, informationText.version

        //Update the entity with invalid values
        informationText.pageName = u_failure_pageName
        try {
            informationText.save( failOnError: true, flush: true )
            fail( "This should have failed as pageName exceeded maxSize" )
        } catch ( ValidationException ) {
            assertErrorsFor informationText, 'maxSize', ['pageName']
        }
    }


    @Test
    void testDates( ) {
        def time = new SimpleDateFormat( 'HHmmss' )
        def hour = new SimpleDateFormat( 'HH' )
        def date = new SimpleDateFormat( 'yyyy-M-d' )
        def today = new Date()

        def informationText = newValidForCreateInformationText()
        informationText.startDate = new Date()
        informationText.endDate = new Date()

        informationText.save( flush: true, failOnError: true )
        informationText.refresh()
        assertNotNull informationText.id

        // test date values -
        assertEquals date.format( today ), date.format( informationText.lastModified )
        assertEquals hour.format( today ), hour.format( informationText.lastModified )

        assertEquals time.format( informationText.startDate ), "000000"
        assertEquals time.format( informationText.endDate ), "000000"

    }


    @Test
    void testOptimisticLock( ) {
        def informationText = newValidForCreateInformationText()
        informationText.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GURINFO set GURINFO_VERSION = 999 where GURINFO_SURROGATE_ID = ?", [informationText.id] )
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        informationText.pageName = u_success_pageName
        informationText.textType = u_success_textType
        shouldFail( HibernateOptimisticLockingFailureException ) {
            informationText.save( failOnError: true, flush: true )
        }
    }


    @Test
    void testDeleteInformationText( ) {
        def informationText = newValidForCreateInformationText()
        informationText.save( failOnError: true, flush: true )
        def id = informationText.id
        assertNotNull id
        informationText.delete()
        assertNull InformationText.get( id )
    }


    @Test
    void testNullValidationFailure( ) {
        def informationText = new InformationText()
        assertFalse informationText.validate()
        assertErrorsFor informationText, 'nullable',
                [
                        'pageName',
                        'label',
                        'sequenceNumber',
                        'persona',
                        'textType',
                        'locale'
                ]
        assertNoErrorsFor informationText,
                [
                        'text',
                        'comment',
                        'sourceIndicator',
                        'startDate',
                        'endDate',
                ]
    }


    @Test
    void testMaxSizeValidationFailures( ) {
        def informationText = new InformationText(
                pageName: 'X' * 201,
                label: 'X' * 201,
                textType: 'X' * 201,
                persona: 'X' * 301,
                text: 'X' * 4001,
                locale: 'X' * 201,
                comment: 'X' * 300001
        )
        assertFalse informationText.validate()
        assertErrorsFor informationText, 'maxSize', ['pageName', 'label', 'textType', 'persona', 'text', 'locale', 'comment']
    }


    @Test
    void testInListValidationFailures( ) {
        def informationText = new InformationText(
                sourceIndicator: 'A',
                textType: 'X'
        )
        assertFalse informationText.validate()
        assertErrorsFor informationText, 'inList', ['sourceIndicator','textType']
    }


    @Test
    void testMaxValidationFailures( ) {
        def informationText = new InformationText(
                sequenceNumber: 100000
        )
        assertFalse informationText.validate()
        assertErrorsFor informationText, 'max', ['sequenceNumber']
    }


    @Test
    void testMinValidationFailures( ) {
        def informationText = new InformationText(
                sequenceNumber: -100
        )
        assertFalse informationText.validate()
        assertErrorsFor informationText, 'min', ['sequenceNumber']
    }


    @Test
    void testFetchInfoTextByRolesLargeList() {
        def roleList = []
        for (counter in 1..1100 ) {
            roleList << "TEST-ROLE-${counter}".toString()
        }
        roleList << "STUDENT"

        assertTrue roleList.size() > 1000
        assertTrue roleList instanceof List
        def rolePartitions = SystemUtility.splitList(roleList, 1000)
        assertTrue rolePartitions.size() > 1

        def response = InformationText.findAllWhere(pageName: "REGISTRATION", locale: 'en_US')
        def infoTexts = InformationText.fetchInfoTextByRoles('REGISTRATION',roleList, ['en_US'])
        assertEquals infoTexts.size(), response.size()
    }


    @Test
    void testFetchInfoTextByRolesSmallList() {
        def roleList = []
        for (counter in 1..100 ) {
            roleList << "TEST-ROLE-${counter}".toString()
        }
        roleList << "STUDENT"

        assertTrue roleList.size() > 100
        assertTrue roleList instanceof List
        def rolePartitions = SystemUtility.splitList(roleList, 100)
        assertTrue rolePartitions.size() > 1

        def response = InformationText.findAllWhere(pageName: "REGISTRATION", locale: 'en_US')
        def infoTexts = InformationText.fetchInfoTextByRoles('REGISTRATION',roleList, ['en_US'])
        assertEquals infoTexts.size(), response.size()
    }


    @Test
    void testFetchInfoTextByRoleAndLabelLargeList() {
        def roleList = []
        for (counter in 1..1100 ) {
            roleList << "TEST-ROLE-${counter}".toString()
        }
        roleList << "STUDENT"

        assertTrue roleList.size() > 1000
        assertTrue roleList instanceof List
        def rolePartitions = SystemUtility.splitList(roleList, 1000)
        assertTrue rolePartitions.size() > 1

        def regLabel = 'registration.search.info.tooltip'
        def response = InformationText.findAllWhere(pageName: "REGISTRATION", locale: 'en_US', label: regLabel)
        def infoTexts = InformationText.fetchInfoTextByRolesAndLabel('REGISTRATION',roleList, ['en_US'], regLabel)
        assertEquals infoTexts.size(), response.size()
    }


    @Test
    void testFetchInfoTextByRoleAndLabelSmallList() {
        def roleList = []
        for (counter in 1..100 ) {
            roleList << "TEST-ROLE-${counter}".toString()
        }
        roleList << "STUDENT"

        assertTrue roleList.size() > 100
        assertTrue roleList instanceof List
        def rolePartitions = SystemUtility.splitList(roleList, 100)
        assertTrue rolePartitions.size() > 1

        def regLabel = 'registration.search.info.tooltip'
        def response = InformationText.findAllWhere(pageName: "REGISTRATION", locale: 'en_US', label: regLabel)
        def infoTexts = InformationText.fetchInfoTextByRolesAndLabel('REGISTRATION',roleList, ['en_US'], regLabel)
        assertEquals infoTexts.size(), response.size()
    }


    private def newValidForCreateInformationText( ) {
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


    private def newInvalidForCreateInformationText( ) {
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
}
