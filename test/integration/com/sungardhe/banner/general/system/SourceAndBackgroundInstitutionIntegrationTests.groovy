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

class SourceAndBackgroundInstitutionIntegrationTests extends BaseIntegrationTestCase {

    def sourceAndBackgroundInstitutionService


    protected void setUp() {
        formContext = ['SHATGRD', 'STVSBGI'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution
        //Test if the generated entity now has an id assigned
        assertNotNull sourceAndBackgroundInstitution.id
    }


    void testUpdateSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution

        assertNotNull sourceAndBackgroundInstitution.id
        groovy.util.GroovyTestCase.assertEquals(0L, sourceAndBackgroundInstitution.version)
        assertEquals("TTTTT", sourceAndBackgroundInstitution.code)
        assertEquals("T", sourceAndBackgroundInstitution.typeIndicator)
        assertEquals("T", sourceAndBackgroundInstitution.srceIndicator)
        assertEquals("TTTTT", sourceAndBackgroundInstitution.description)
        assertEquals("T", sourceAndBackgroundInstitution.ediCapable)
        assertEquals("TTTTT", sourceAndBackgroundInstitution.fice)
        assertEquals(1, sourceAndBackgroundInstitution.voiceResponseMsgNumber)

        //Update the entity
        sourceAndBackgroundInstitution.code = "UUUUU"
        sourceAndBackgroundInstitution.typeIndicator = "U"
        sourceAndBackgroundInstitution.srceIndicator = "U"
        sourceAndBackgroundInstitution.description = "UUUUU"
        sourceAndBackgroundInstitution.ediCapable = "U"
        sourceAndBackgroundInstitution.fice = "UUUUU"
        sourceAndBackgroundInstitution.voiceResponseMsgNumber = 2
        sourceAndBackgroundInstitution.lastModified = new Date()
        sourceAndBackgroundInstitution.lastModifiedBy = "test"
        sourceAndBackgroundInstitution.dataOrigin = "Banner"

        save sourceAndBackgroundInstitution

        sourceAndBackgroundInstitution = SourceAndBackgroundInstitution.get(sourceAndBackgroundInstitution.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), sourceAndBackgroundInstitution?.version
        assertEquals("UUUUU", sourceAndBackgroundInstitution.code)
        assertEquals("U", sourceAndBackgroundInstitution.typeIndicator)
        assertEquals("U", sourceAndBackgroundInstitution.srceIndicator)
        assertEquals("UUUUU", sourceAndBackgroundInstitution.description)
        assertEquals("U", sourceAndBackgroundInstitution.ediCapable)
        assertEquals("UUUUU", sourceAndBackgroundInstitution.fice)
        assertEquals(2, sourceAndBackgroundInstitution.voiceResponseMsgNumber)

    }


    void testOptimisticLock() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVSBGI set STVSBGI_VERSION = 999 where STVSBGI_SURROGATE_ID = ?", [sourceAndBackgroundInstitution.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        sourceAndBackgroundInstitution.code = "UUUUU"
        sourceAndBackgroundInstitution.typeIndicator = "U"
        sourceAndBackgroundInstitution.srceIndicator = "U"
        sourceAndBackgroundInstitution.description = "UUUUU"
        sourceAndBackgroundInstitution.ediCapable = "U"
        sourceAndBackgroundInstitution.fice = "UUUUU"
        sourceAndBackgroundInstitution.voiceResponseMsgNumber = 3
        sourceAndBackgroundInstitution.lastModified = new Date()
        sourceAndBackgroundInstitution.lastModifiedBy = "test"
        sourceAndBackgroundInstitution.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            sourceAndBackgroundInstitution.save(flush:true, failOnError:true)
        }
    }


    void testDeleteSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution
        def id = sourceAndBackgroundInstitution.id
        assertNotNull id
        sourceAndBackgroundInstitution.delete()
        assertNull SourceAndBackgroundInstitution.get(id)
    }


    void testValidation() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        assertTrue "SourceAndBackgroundInstitution could not be validated as expected due to ${sourceAndBackgroundInstitution.errors}", sourceAndBackgroundInstitution.validate()
    }


    void testNullValidationFailure() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution()
        assertFalse "SourceAndBackgroundInstitution should have failed validation", sourceAndBackgroundInstitution.validate()
        assertNoErrorsFor(sourceAndBackgroundInstitution, ['srceIndicator', 'ediCapable', 'fice', 'voiceResponseMsgNumber'])
        assertErrorsFor(sourceAndBackgroundInstitution, 'nullable', ['code', 'description'])
    }


    void testMaxSizeValidationFailures() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution(
                code: 'XXXXXXXX',
                typeIndicator: 'XXX')
        assertFalse "SourceAndBackgroundInstitution should have failed validation", sourceAndBackgroundInstitution.validate()
        assertErrorsFor(sourceAndBackgroundInstitution, 'maxSize', ['code', 'typeIndicator'])
    }


    void testValidationMessages() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()

        sourceAndBackgroundInstitution.code = null
        assertFalse sourceAndBackgroundInstitution.validate()
        assertLocalizedError(sourceAndBackgroundInstitution, 'nullable', /.*Field.*code.*of class.*SourceAndBackgroundInstitution.*cannot be null.*/, 'code')

        sourceAndBackgroundInstitution.description = null
        assertFalse sourceAndBackgroundInstitution.validate()
        assertLocalizedError(sourceAndBackgroundInstitution, 'nullable', /.*Field.*description.*of class.*SourceAndBackgroundInstitution.*cannot be null.*/, 'description')

    }


    private def newSourceAndBackgroundInstitution() {

        def admr = new AdmissionRequest(code: "TTTT", description: "TTTTT", tableName: "TTTTT", voiceResponseMsgNumber: 1, voiceResponseEligIndicator: "T", displayWebIndicator: true, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        admr.save(flush:true, failOnError:true)

        new SourceAndBackgroundInstitution(code: "TTTTT", typeIndicator: "T",
                srceIndicator: "T", description: "TTTTT", ediCapable: "T", fice: "TTTTT",
                voiceResponseMsgNumber: 1, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner", admrCode: admr)
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(sourceandbackgroundinstitution_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
