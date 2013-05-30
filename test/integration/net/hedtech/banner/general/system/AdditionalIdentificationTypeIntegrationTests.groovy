/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class AdditionalIdentificationTypeIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateAdditionalIdentificationType() {
        def additionalIdentificationType = newAdditionalIdentificationType()
        save additionalIdentificationType
        //Test if the generated entity now has an id assigned
        assertNotNull additionalIdentificationType.id
        groovy.util.GroovyTestCase.assertEquals(0L, additionalIdentificationType.version)
        assertEquals("TTTT", additionalIdentificationType.code)
        assertEquals("TTTTTTTTTT", additionalIdentificationType.description)
        assertEquals("Y", additionalIdentificationType.searchBypass)
    }


    void testUpdateAdditionalIdentificationType() {
        def additionalIdentificationType = newAdditionalIdentificationType()
        save additionalIdentificationType

        assertNotNull additionalIdentificationType.id
        groovy.util.GroovyTestCase.assertEquals(0L, additionalIdentificationType.version)

        //Update the entity
        additionalIdentificationType.code = "UUUU"
        additionalIdentificationType.description = "UUUUUUUUUU"
        additionalIdentificationType.searchBypass = null
        additionalIdentificationType.lastModified = new Date()
        additionalIdentificationType.lastModifiedBy = "test"
        additionalIdentificationType.dataOrigin = "Banner"

        save additionalIdentificationType

        additionalIdentificationType = AdditionalIdentificationType.get(additionalIdentificationType.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), additionalIdentificationType?.version
        assertEquals("UUUU", additionalIdentificationType.code)
        assertEquals("UUUUUUUUUU", additionalIdentificationType.description)
        assertNull(additionalIdentificationType.searchBypass)
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def personType = newAdditionalIdentificationType()

        personType.save(flush: true, failOnError: true)
        personType.refresh()
        assertNotNull "AdditionalIdentificationType should have been saved", personType.id

        // test date values -
        assertEquals date.format(today), date.format(personType.lastModified)
        assertEquals hour.format(today), hour.format(personType.lastModified)
    }


    void testOptimisticLock() {
        def additionalIdentificationType = newAdditionalIdentificationType()
        save additionalIdentificationType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVADID set GTVADID_VERSION = 999 where GTVADID_SURROGATE_ID = ?", [additionalIdentificationType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        additionalIdentificationType.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            additionalIdentificationType.save(flush: true, failOnError: true)
        }
    }


    void testDeleteAdditionalIdentificationType() {
        def additionalIdentificationType = newAdditionalIdentificationType()
        save additionalIdentificationType
        def id = additionalIdentificationType.id
        assertNotNull id
        additionalIdentificationType.delete()
        assertNull AdditionalIdentificationType.get(id)
    }


    void testValidation() {
        def additionalIdentificationType = new AdditionalIdentificationType()
        assertFalse "AdditionalIdentificationType could not be validated as expected due to ${additionalIdentificationType.errors}", additionalIdentificationType.validate()
    }


    void testNullValidationFailure() {
        def additionalIdentificationType = new AdditionalIdentificationType()
        assertFalse "AdditionalIdentificationType should be valid", additionalIdentificationType.validate()
        assertErrorsFor(additionalIdentificationType, 'nullable', ['code', 'description'])
        assertNoErrorsFor(additionalIdentificationType, ['searchBypass'])
    }


    void testMaxSizeValidationFailures() {
        def additionalIdentificationType = new AdditionalIdentificationType(
                code: 'XXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                searchBypass: "TT")
        assertFalse "AdditionalIdentificationType should have failed validation", additionalIdentificationType.validate()
        assertErrorsFor(additionalIdentificationType, 'maxSize', ['code', 'description', 'searchBypass'])
    }


    void testInListValidationFailure() {
        def additionalIdentificationType = newAdditionalIdentificationType()
        additionalIdentificationType.searchBypass = "T"
        assertFalse "AdditionalIdentificationType should have failed validation", additionalIdentificationType.validate()
        assertErrorsFor(additionalIdentificationType, 'inList', ['searchBypass'])
    }


    private def newAdditionalIdentificationType() {
        new AdditionalIdentificationType(code: "TTTT",
                description: "TTTTTTTTTT",
                searchBypass: "Y")
    }

}
