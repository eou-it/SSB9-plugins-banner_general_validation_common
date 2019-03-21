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

class VisaTypeIntegrationTests extends BaseIntegrationTestCase {

    def visaTypeService


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
    void testCreateVisaType() {
        def visaType = newVisaType()
        save visaType
        //Test if the generated entity now has an id assigned
        assertNotNull visaType.id
    }


    @Test
    void testUpdateVisaType() {
        def visaType = newVisaType()
        save visaType

        assertNotNull visaType.id
        groovy.util.GroovyTestCase.assertEquals(0L, visaType.version)
        assertEquals("TT", visaType.code)
        assertEquals("TTTTT", visaType.description)
        assertEquals("T", visaType.nonResIndicator)
        assertEquals(new Integer(1), visaType.voiceResponseMsgNumber)
        assertEquals(new Integer(2), visaType.statscanCde2)
        assertEquals("TT", visaType.sevisEquiv)

        //Update the entity
        visaType.code = "UU"
        visaType.description = "UUUUU"
        visaType.nonResIndicator = "U"
        visaType.voiceResponseMsgNumber = new Integer(12)
        //  visaType.statscanCde2 = new Integer(22)
        visaType.sevisEquiv = "UU"
        visaType.lastModified = new Date()
        visaType.lastModifiedBy = "test"
        visaType.dataOrigin = "Banner"

        save visaType

        visaType = VisaType.get(visaType.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), visaType?.version
        assertEquals("UU", visaType.code)
        assertEquals("UUUUU", visaType.description)
        assertEquals("U", visaType.nonResIndicator)
        assertEquals(new Integer(12), visaType.voiceResponseMsgNumber)
        //   assertEquals(new Integer(22), visaType.statscanCde2)
        assertEquals("UU", visaType.sevisEquiv)

    }


    @Test
    void testOptimisticLock() {
        def visaType = newVisaType()
        save visaType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVVTYP set STVVTYP_VERSION = 999 where STVVTYP_SURROGATE_ID = ?", [visaType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        visaType.code = "UU"
        visaType.description = "UUUUU"
        visaType.nonResIndicator = "U"
        visaType.voiceResponseMsgNumber = new Integer(2)
        visaType.statscanCde2 = new Integer(22)
        visaType.sevisEquiv = "UU"
        visaType.lastModified = new Date()
        visaType.lastModifiedBy = "test"
        visaType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            visaType.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteVisaType() {
        def visaType = newVisaType()
        save visaType
        def id = visaType.id
        assertNotNull id
        visaType.delete()
        assertNull VisaType.get(id)
    }


    @Test
    void testValidation() {
        def visaType = newVisaType()
        assertTrue "VisaType could not be validated as expected due to ${visaType.errors}", visaType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def visaType = new VisaType()
        assertFalse "VisaType should have failed validation", visaType.validate()
        assertNoErrorsFor(visaType, ['nonResIndicator', 'voiceResponseMsgNumber', 'statscanCde2', 'sevisEquiv'])
        assertErrorsFor(visaType, 'nullable', ['code', 'description'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def visaType = new VisaType(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "VisaType should have failed validation", visaType.validate()
        assertErrorsFor(visaType, 'maxSize', ['code', 'description'])
    }


    private def newVisaType() {
        def admr = new AdmissionRequest(code: "TTTT", description: "TTTTT", tableName: "TTTTT", voiceResponseMsgNumber: 1,
                voiceResponseEligIndicator: "T", displayWebIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        admr.save(flush: true, failOnError: true)

        def visa = new VisaType(code: "TT", description: "TTTTT", nonResIndicator: "T",
                voiceResponseMsgNumber: new Integer(1),
                statscanCde2: new Integer(2), sevisEquiv: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner", admrCode: admr)

        return visa
    }


}
