/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class LegacyIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateLegacy() {
        def legacy = newLegacy()
        legacy.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull legacy.id
        assertNotNull legacy.id
        assertEquals 0L, legacy.version
        assertEquals "T", legacy.code
        assertEquals "TTTTTTTTTT", legacy.description
    }


    void testUpdateLegacy() {
        def legacy = newLegacy()
        legacy.save(failOnError: true, flush: true)
        assertNotNull legacy.id
        assertEquals 0L, legacy.version
        assertEquals "T", legacy.code
        assertEquals "TTTTTTTTTT", legacy.description

        //Update the entity
        legacy.description = "UUUUUUUUUU"
        legacy.save(failOnError: true, flush: true)
        //Assert for successful update
        legacy = Legacy.get(legacy.id)
        assertEquals 1L, legacy?.version
        assertEquals "UUUUUUUUUU", legacy.description
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def legacy = newLegacy()

        legacy.save(flush: true, failOnError: true)
        legacy.refresh()
        assertNotNull "Legacy should have been saved", legacy.id

        // test date values -
        assertEquals date.format(today), date.format(legacy.lastModified)
        assertEquals hour.format(today), hour.format(legacy.lastModified)
    }


    void testOptimisticLock() {
        def legacy = newLegacy()
        legacy.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVLGCY set STVLGCY_VERSION = 999 where STVLGCY_SURROGATE_ID = ?", [legacy.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        legacy.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            legacy.save(failOnError: true, flush: true)
        }
    }


    void testDeleteLegacy() {
        def legacy = newLegacy()
        legacy.save(failOnError: true, flush: true)
        def id = legacy.id
        assertNotNull id
        legacy.delete()
        assertNull Legacy.get(id)
    }


    void testValidation() {
        def legacy = new Legacy()
        assertFalse "Legacy could not be validated as expected due to ${legacy.errors}", legacy.validate()
    }


    void testNullValidationFailure() {
        def legacy = new Legacy()
        assertFalse "Legacy should have failed validation", legacy.validate()
        assertErrorsFor legacy, 'nullable', ['code']
        assertNoErrorsFor legacy, ['description']
    }


    void testMaxSizeValidationFailures() {
        def legacy = new Legacy(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT')

        assertFalse "Legacy should have failed validation", legacy.validate()
        assertErrorsFor legacy, 'maxSize', ['code', 'description']
    }


    private def newLegacy() {
        def legacy = new Legacy(
                code: "T",
                description: "TTTTTTTTTT"
        )
        return legacy
    }

}
