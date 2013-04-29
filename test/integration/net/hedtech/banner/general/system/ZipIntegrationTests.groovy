/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class ZipIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidZip() {
        def zip = newValidForCreateZip()
        zip.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull zip.id
    }


    void testCreateInvalidZip() {
        def zip = newInvalidForCreateZip()
        shouldFail(ValidationException) {
            zip.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidZip() {
        def zip = newValidForCreateZip()
        zip.save(failOnError: true, flush: true)
        assertNotNull zip.id
        assertEquals 0L, zip.version
        assertEquals "123456789012345678901234567890", zip.code
        assertEquals "12345678901234567890123456789012345678901234567890", zip.city

        //Update the entity
        zip.code = "UPDATE789012345678901234567890"
        zip.city = "UPDATE78901234567890123456789012345678901234567890"
        zip.state = State.findWhere(code: "NJ")
        zip.county = County.findWhere(code: "002")
        zip.nation = Nation.findWhere(code: "2")
        zip.save(failOnError: true, flush: true)

        //Assert for sucessful update
        zip = Zip.get(zip.id)
        assertEquals 1L, zip?.version
    }


    void testUpdateInvalidZip() {
        def zip = newValidForCreateZip()
        zip.save(failOnError: true, flush: true)
        assertNotNull zip.id
        assertEquals 0L, zip.version
        assertEquals "123456789012345678901234567890", zip.code
        assertEquals "12345678901234567890123456789012345678901234567890", zip.city

        //Update the entity with invalid values
        zip.code = null
        zip.city = null
        shouldFail(ValidationException) {
            zip.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()
        def zip = newValidForCreateZip()

        zip.save(flush: true, failOnError: true)
        zip.refresh()
        assertNotNull "Zip should have been saved", zip.id

        // test date values -
        assertEquals date.format(today), date.format(zip.lastModified)
        assertEquals hour.format(today), hour.format(zip.lastModified)
    }


    void testOptimisticLock() {
        def zip = newValidForCreateZip()
        zip.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVZIPC set GTVZIPC_VERSION = 999 where GTVZIPC_SURROGATE_ID = ?", [zip.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        zip.code = "UPDATE789012345678901234567890"
        shouldFail(HibernateOptimisticLockingFailureException) {
            zip.save(failOnError: true, flush: true)
        }
    }


    void testDeleteZip() {
        def zip = newValidForCreateZip()
        zip.save(failOnError: true, flush: true)
        def id = zip.id
        assertNotNull id
        zip.delete()
        assertNull Zip.get(id)
    }


    void testValidation() {
        def zip = newInvalidForCreateZip()
        assertFalse "Zip could not be validated as expected due to ${zip.errors}", zip.validate()
    }


    void testNullValidationFailure() {
        def zip = new Zip()
        assertFalse "Zip should have failed validation", zip.validate()
        assertErrorsFor zip, 'nullable',
                [
                        'code',
                        'city'
                ]
        assertNoErrorsFor zip,
                [
                        'state',
                        'nation',
                        'county'
                ]
    }


    private def newValidForCreateZip() {
        def zip = new Zip(
                code: "123456789012345678901234567890",
                city: "12345678901234567890123456789012345678901234567890",
                state: State.findWhere(code: "PA"),
                county: County.findWhere(code: "001"),
                nation: Nation.findWhere(code: "1"),
        )
        return zip
    }


    private def newInvalidForCreateZip() {
        def zip = new Zip(
                code: "123456789012345678901234567890FAIL",
                city: "12345678901234567890123456789012345678901234567890FAIL",
                state: null,
                nation: null,
                county: null,
        )
        return zip
    }

}
