/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class MeetingTypeIntegrationTests extends BaseIntegrationTestCase {

    def meetingTypeService


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
    void testCreateMeetingType() {
        def meetingType = newMeetingType()
        save meetingType
        //Test if the generated entity now has an id assigned
        assertNotNull meetingType.id
    }


    @Test
    void testUpdateMeetingType() {
        def meetingType = newMeetingType()
        save meetingType

        assertNotNull meetingType.id
        assertEquals 0L, meetingType.version
        assertEquals "TTTT", meetingType.code
        assertEquals "TTTTT", meetingType.description
        assertEquals true, meetingType.systemRequiredIndicator
        assertEquals 1, meetingType.voiceResponseMsgNumber, 0

        //Update the entity
        meetingType.code = "UUUU"
        meetingType.description = "UUUUU"
        meetingType.systemRequiredIndicator = false
        meetingType.voiceResponseMsgNumber = 0
        meetingType.lastModified = new Date()
        meetingType.lastModifiedBy = "test"
        meetingType.dataOrigin = "Banner"
        save meetingType

        meetingType = MeetingType.get(meetingType.id)
        assertEquals 1L, meetingType?.version
        assertEquals "UUUU", meetingType.code
        assertEquals "UUUUU", meetingType.description
        assertEquals false, meetingType.systemRequiredIndicator
        assertEquals 0, meetingType.voiceResponseMsgNumber, 0

    }


    @Test
    void testOptimisticLock() {
        def meetingType = newMeetingType()
        save meetingType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVMTYP set GTVMTYP_VERSION = 999 where GTVMTYP_SURROGATE_ID = ?", [meetingType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        meetingType.code = "UUUU"
        meetingType.description = "UUUUU"
        meetingType.systemRequiredIndicator = false
        meetingType.voiceResponseMsgNumber = 0
        meetingType.lastModified = new Date()
        meetingType.lastModifiedBy = "test"
        meetingType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            meetingType.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteMeetingType() {
        def meetingType = newMeetingType()
        save meetingType
        def id = meetingType.id
        assertNotNull id
        meetingType.delete()
        assertNull MeetingType.get(id)
    }


    @Test
    void testValidation() {
        def meetingType = newMeetingType()
        assertTrue "MeetingType could not be validated as expected due to ${meetingType.errors}", meetingType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def meetingType = new MeetingType()
        assertFalse "MeetingType should have failed validation", meetingType.validate()
        assertErrorsFor meetingType, 'nullable', ['code', 'description', 'systemRequiredIndicator']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def meetingType = new MeetingType(
                code: 'XXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "MeetingType should have failed validation", meetingType.validate()
        assertErrorsFor meetingType, 'maxSize', ['code', 'description']
    }


    private def newMeetingType() {
        new MeetingType(code: "TTTT",
                description: "TTTTT",
                systemRequiredIndicator: true,
                voiceResponseMsgNumber: 1,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner")
    }


}
