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

class AdmissionRequestIntegrationTests extends BaseIntegrationTestCase {

    def admissionRequestService


    protected void setUp() {
        formContext = ['STVADMR'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateAdmissionRequest() {
        def admissionRequest = newAdmissionRequest()
        save admissionRequest
        //Test if the generated entity now has an id assigned
        assertNotNull admissionRequest.id
    }


    void testUpdateAdmissionRequest() {
        def admissionRequest = newAdmissionRequest()
        save admissionRequest

        assertNotNull admissionRequest.id
        groovy.util.GroovyTestCase.assertEquals(0L, admissionRequest.version)
        assertEquals("TTTT", admissionRequest.code)
        assertEquals("TTTTT", admissionRequest.description)
        assertEquals("TTTTT", admissionRequest.tableName)
        assertEquals(1, admissionRequest.voiceResponseMsgNumber)
        assertEquals("T", admissionRequest.voiceResponseEligIndicator)
        assertTrue admissionRequest.displayWebIndicator

        //Update the entity
        admissionRequest.code = "UUUU"
        admissionRequest.description = "UUUUU"
        admissionRequest.tableName = "UUUUU"
        admissionRequest.voiceResponseMsgNumber = 8
        admissionRequest.voiceResponseEligIndicator = "U"
        admissionRequest.displayWebIndicator = false
        admissionRequest.lastModified = new Date()
        admissionRequest.lastModifiedBy = "test"
        admissionRequest.dataOrigin = "Banner"

        save admissionRequest

        admissionRequest = AdmissionRequest.get(admissionRequest.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), admissionRequest?.version
        assertEquals("UUUU", admissionRequest.code)
        assertEquals("UUUUU", admissionRequest.description)
        assertEquals("UUUUU", admissionRequest.tableName)
        assertEquals(8, admissionRequest.voiceResponseMsgNumber)
        assertEquals("U", admissionRequest.voiceResponseEligIndicator)
        groovy.util.GroovyTestCase.assertEquals(false, admissionRequest.displayWebIndicator)

    }


    void testOptimisticLock() {
        def admissionRequest = newAdmissionRequest()
        save admissionRequest

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVADMR set STVADMR_VERSION = 999 where STVADMR_SURROGATE_ID = ?", [admissionRequest.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        admissionRequest.code = "UUUU"
        admissionRequest.description = "UUUUU"
        admissionRequest.tableName = "UUUUU"
        admissionRequest.voiceResponseMsgNumber = 9
        admissionRequest.voiceResponseEligIndicator = "U"
        admissionRequest.displayWebIndicator = false
        admissionRequest.lastModified = new Date()
        admissionRequest.lastModifiedBy = "test"
        admissionRequest.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            admissionRequest.save(flush:true, failOnError:true)
        }
    }


    void testDeleteAdmissionRequest() {
        def admissionRequest = newAdmissionRequest()
        save admissionRequest
        def id = admissionRequest.id
        assertNotNull id
        admissionRequest.delete()
        assertNull AdmissionRequest.get(id)
    }


    void testValidation() {
        def admissionRequest = newAdmissionRequest()
        assertTrue "AdmissionRequest could not be validated as expected due to ${admissionRequest.errors}", admissionRequest.validate()
    }


    void testNullValidationFailure() {
        def admissionRequest = new AdmissionRequest(
                code: null,
                description: null,
                displayWebIndicator: null)
        assertFalse "AdmissionRequest should have failed validation", admissionRequest.validate()
        assertNoErrorsFor(admissionRequest, ['tableName', 'voiceResponseMsgNumber', 'voiceResponseEligIndicator'])
        assertErrorsFor(admissionRequest, 'nullable', ['code', 'description', 'displayWebIndicator'])
    }


    void testMaxSizeValidationFailures() {
        def admissionRequest = new AdmissionRequest(
                code: 'XXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                displayWebIndicator: true)
        assertFalse "AdmissionRequest should have failed validation", admissionRequest.validate()
        assertErrorsFor(admissionRequest, 'maxSize', ['code', 'description'])
    }





    private def newAdmissionRequest() {
        new AdmissionRequest(code: "TTTT", description: "TTTTT", tableName: "TTTTT", voiceResponseMsgNumber: 1, voiceResponseEligIndicator: "T", displayWebIndicator: true, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(admissionrequest_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
