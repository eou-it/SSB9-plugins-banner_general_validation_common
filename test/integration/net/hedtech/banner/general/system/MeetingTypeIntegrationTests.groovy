/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class MeetingTypeIntegrationTests extends BaseIntegrationTestCase {

    def meetingTypeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateMeetingType() {
        def meetingType = newMeetingType()
        save meetingType
        //Test if the generated entity now has an id assigned
        assertNotNull meetingType.id
    }


    void testUpdateMeetingType() {
        def meetingType = newMeetingType()
        save meetingType

        assertNotNull meetingType.id
        assertEquals 0L, meetingType.version
        assertEquals "TTTT", meetingType.code
        assertEquals "TTTTT", meetingType.description
        assertEquals true, meetingType.systemRequiredIndicator
        assertEquals 1, meetingType.voiceResponseMsgNumber

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
        assertEquals 0, meetingType.voiceResponseMsgNumber

    }


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
            meetingType.save(flush:true, failOnError:true)
        }
    }


    void testDeleteMeetingType() {
        def meetingType = newMeetingType()
        save meetingType
        def id = meetingType.id
        assertNotNull id
        meetingType.delete()
        assertNull MeetingType.get(id)
    }


    void testValidation() {
        def meetingType = newMeetingType()
        assertTrue "MeetingType could not be validated as expected due to ${meetingType.errors}", meetingType.validate()
    }


    void testNullValidationFailure() {
        def meetingType = new MeetingType()
        assertFalse "MeetingType should have failed validation", meetingType.validate()
        assertErrorsFor meetingType, 'nullable', ['code', 'description', 'systemRequiredIndicator']
    }


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(meetingtype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
