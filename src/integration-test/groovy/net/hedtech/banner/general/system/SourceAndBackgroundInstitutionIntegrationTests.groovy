/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class SourceAndBackgroundInstitutionIntegrationTests extends BaseIntegrationTestCase {

    def sourceAndBackgroundInstitutionService


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
    void testCreateSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution
        //Test if the generated entity now has an id assigned
        assertNotNull sourceAndBackgroundInstitution.id
    }


    @Test
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


    @Test
    void testOptimisticLock() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVSBGI set STVSBGI_VERSION = 999 where STVSBGI_SURROGATE_ID = ?", [sourceAndBackgroundInstitution.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
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
            sourceAndBackgroundInstitution.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        save sourceAndBackgroundInstitution
        def id = sourceAndBackgroundInstitution.id
        assertNotNull id
        sourceAndBackgroundInstitution.delete()
        assertNull SourceAndBackgroundInstitution.get(id)
    }


    @Test
    void testValidation() {
        def sourceAndBackgroundInstitution = newSourceAndBackgroundInstitution()
        assertTrue "SourceAndBackgroundInstitution could not be validated as expected due to ${sourceAndBackgroundInstitution.errors}", sourceAndBackgroundInstitution.validate()
    }


    @Test
    void testNullValidationFailure() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution()
        assertFalse "SourceAndBackgroundInstitution should have failed validation", sourceAndBackgroundInstitution.validate()
        assertNoErrorsFor(sourceAndBackgroundInstitution, ['srceIndicator', 'ediCapable', 'fice', 'voiceResponseMsgNumber'])
        assertErrorsFor(sourceAndBackgroundInstitution, 'nullable', ['code', 'description'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution(
                code: 'XXXXXXXX',
                typeIndicator: 'XXX')
        assertFalse "SourceAndBackgroundInstitution should have failed validation", sourceAndBackgroundInstitution.validate()
        assertErrorsFor(sourceAndBackgroundInstitution, 'maxSize', ['code', 'typeIndicator'])
    }


    @Test
    void testFetchBySomeSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitutionList = SourceAndBackgroundInstitution.fetchBySomeSourceAndBackgroundInstitution()
        assertTrue sourceAndBackgroundInstitutionList.list.size() > 5

        sourceAndBackgroundInstitutionList = SourceAndBackgroundInstitution.fetchBySomeSourceAndBackgroundInstitution("152600")
        assertEquals sourceAndBackgroundInstitutionList.list.size(), 1
        assertEquals "152600", sourceAndBackgroundInstitutionList.list[0].code
    }


    private def newSourceAndBackgroundInstitution() {
        def admr = new AdmissionRequest(code: "TTTT", description: "TTTTT", tableName: "TTTTT", voiceResponseMsgNumber: 1, voiceResponseEligIndicator: "T", displayWebIndicator: true, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        admr.save(flush: true, failOnError: true)

        new SourceAndBackgroundInstitution(code: "TTTTT", typeIndicator: "T",
                srceIndicator: "T", description: "TTTTT", ediCapable: "T", fice: "TTTTT",
                voiceResponseMsgNumber: 1, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner", admrCode: admr)
    }


}
