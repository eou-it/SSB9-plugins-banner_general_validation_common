/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
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
