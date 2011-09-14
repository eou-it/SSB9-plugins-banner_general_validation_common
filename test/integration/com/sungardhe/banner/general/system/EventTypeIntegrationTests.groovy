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
/**
 Banner Automator Version: 0.1.1
 Generated: Tue May 10 23:42:29 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


class EventTypeIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(eventtype_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = null
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "XXXX"
    def u_success_description = "XXXX"
    //Valid test data (For failure tests)

    def u_failure_code = "1234567890"
    def u_failure_description = "1234567890123456789012345678901234567890"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['STVETYP'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidEventType() {
        def eventType = newValidForCreateEventType()
        save eventType
        //Test if the generated entity now has an id assigned
        assertNotNull eventType.id
    }


    void testUpdateValidEventType() {
        def eventType = newValidForCreateEventType()
        save eventType
        assertNotNull eventType.id
        assertEquals 0L, eventType.version
        assertEquals i_success_code, eventType.code
        assertEquals i_success_description, eventType.description

        //Update the entity
        eventType.description = u_success_description
        save eventType
        //Asset for sucessful update
        eventType = EventType.get(eventType.id)
        assertEquals 1L, eventType?.version
        assertEquals u_success_description, eventType.description
    }



    void testOptimisticLock() {
        def eventType = newValidForCreateEventType()
        save eventType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVETYP set STVETYP_VERSION = 999 where STVETYP_SURROGATE_ID = ?", [eventType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        eventType.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            eventType.save(failOnError: true, flush: true)
        }
    }


    void testDeleteEventType() {
        def eventType = newValidForCreateEventType()
        save eventType
        def id = eventType.id
        assertNotNull id
        eventType.delete()
        assertNull EventType.get(id)
    }



    void testNullValidationFailure() {
        def eventType = new EventType()
        assertFalse "EventType should have failed validation", eventType.validate()
        assertErrorsFor eventType, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor eventType,
                [
                        'description'
                ]
    }


    void testMaxSizeValidationFailures() {
        def eventType = new EventType( code:u_failure_code,
                description: u_failure_description)
        assertFalse "EventType should have failed validation", eventType.validate()
        assertErrorsFor eventType, 'maxSize', ['description','code']
    }



    private def newValidForCreateEventType() {
        def eventType = new EventType(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return eventType
    }


    private def newInvalidForCreateEventType() {
        def eventType = new EventType(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return eventType
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(eventtype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
