/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class BackgroundInstitutionCharacteristicIntegrationTests extends BaseIntegrationTestCase {

    private static final String validUpdateCharacteristicCode = "Z"

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
    void testCreateBackgroundInstitutionCharacteristic() {
        def backgroundInstitutionCharacteristic = newBackgroundInstitutionCharacteristic()
        save backgroundInstitutionCharacteristic
        //Test if the generated entity now has an id assigned
        assertNotNull backgroundInstitutionCharacteristic.id
    }


    @Test
    void testUpdateBackgroundInstitutionCharacteristic() {
        def backgroundInstitutionCharacteristic = newBackgroundInstitutionCharacteristic()
        save backgroundInstitutionCharacteristic

        assertNotNull backgroundInstitutionCharacteristic.id
        groovy.util.GroovyTestCase.assertEquals(0L, backgroundInstitutionCharacteristic.version)
        assertEquals("T", backgroundInstitutionCharacteristic.code)
        assertEquals("TTTTT", backgroundInstitutionCharacteristic.description)

        //Update the entity
        backgroundInstitutionCharacteristic.code = validUpdateCharacteristicCode
        backgroundInstitutionCharacteristic.description = "UUUUU"
        backgroundInstitutionCharacteristic.lastModified = new Date()
        backgroundInstitutionCharacteristic.lastModifiedBy = "test"
        backgroundInstitutionCharacteristic.dataOrigin = "Banner"

        save backgroundInstitutionCharacteristic

        backgroundInstitutionCharacteristic = BackgroundInstitutionCharacteristic.get(backgroundInstitutionCharacteristic.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), backgroundInstitutionCharacteristic?.version
        assertEquals(validUpdateCharacteristicCode, backgroundInstitutionCharacteristic.code)
        assertEquals("UUUUU", backgroundInstitutionCharacteristic.description)

    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def backgroundInstitutionCharacteristic = newBackgroundInstitutionCharacteristic()

        backgroundInstitutionCharacteristic.save(flush: true, failOnError: true)
        backgroundInstitutionCharacteristic.refresh()
        assertNotNull "BackgroundInstitutionCharacteristic should have been saved", backgroundInstitutionCharacteristic.id

        // test date values -
        assertEquals date.format(today), date.format(backgroundInstitutionCharacteristic.lastModified)
        assertEquals hour.format(today), hour.format(backgroundInstitutionCharacteristic.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def backgroundInstitutionCharacteristic = newBackgroundInstitutionCharacteristic()
        save backgroundInstitutionCharacteristic

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVBCHR set STVBCHR_VERSION = 999 where STVBCHR_SURROGATE_ID = ?", [backgroundInstitutionCharacteristic.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        backgroundInstitutionCharacteristic.code = "U"
        backgroundInstitutionCharacteristic.description = "UUUUU"
        backgroundInstitutionCharacteristic.lastModified = new Date()
        backgroundInstitutionCharacteristic.lastModifiedBy = "test"
        backgroundInstitutionCharacteristic.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            backgroundInstitutionCharacteristic.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteBackgroundInstitutionCharacteristic() {
        def backgroundInstitutionCharacteristic = newBackgroundInstitutionCharacteristic()
        save backgroundInstitutionCharacteristic
        def id = backgroundInstitutionCharacteristic.id
        assertNotNull id
        backgroundInstitutionCharacteristic.delete()
        assertNull BackgroundInstitutionCharacteristic.get(id)
    }


    @Test
    void testValidation() {
        def backgroundInstitutionCharacteristic = new BackgroundInstitutionCharacteristic()
        assertFalse "BackgroundInstitutionCharacteristic could not be validated as expected due to ${backgroundInstitutionCharacteristic.errors}", backgroundInstitutionCharacteristic.validate()
    }


    @Test
    void testNullValidationFailure() {
        def backgroundInstitutionCharacteristic = new BackgroundInstitutionCharacteristic()
        assertFalse "BackgroundInstitutionCharacteristic should have failed validation", backgroundInstitutionCharacteristic.validate()
        assertErrorsFor(backgroundInstitutionCharacteristic, 'nullable', ['code'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def backgroundInstitutionCharacteristic = new BackgroundInstitutionCharacteristic(
                code: 'XXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "BackgroundInstitutionCharacteristic should have failed validation", backgroundInstitutionCharacteristic.validate()
        assertErrorsFor(backgroundInstitutionCharacteristic, 'maxSize', ['code', 'description'])
    }


    private def newBackgroundInstitutionCharacteristic() {
        new BackgroundInstitutionCharacteristic(code: "T", description: "TTTTT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

}
