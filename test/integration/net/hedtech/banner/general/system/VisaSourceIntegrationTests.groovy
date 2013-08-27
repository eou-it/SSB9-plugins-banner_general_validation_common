/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class VisaSourceIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull visaSource.id
    }


    void testUpdateValidVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        assertNotNull visaSource.id
        assertEquals 0L, visaSource.version
        assertEquals "TTTTTT", visaSource.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", visaSource.description

        //Update the entity
        visaSource.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        visaSource.save(failOnError: true, flush: true)
        //Assert for sucessful update
        visaSource = VisaSource.get(visaSource.id)
        assertEquals 1L, visaSource?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", visaSource.description
    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def visaSource = newValidForCreateVisaSource()

        visaSource.save(flush: true, failOnError: true)
        visaSource.refresh()
        assertNotNull "VisaSource should have been saved", visaSource.id

        // test date values -
        assertEquals date.format(today), date.format(visaSource.lastModified)
        assertEquals hour.format(today), hour.format(visaSource.lastModified)
    }


    void testOptimisticLock() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSRCE set GTVSRCE_VERSION = 999 where GTVSRCE_SURROGATE_ID = ?", [visaSource.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        visaSource.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            visaSource.save(failOnError: true, flush: true)
        }
    }


    void testDeleteVisaSource() {
        def visaSource = newValidForCreateVisaSource()
        visaSource.save(failOnError: true, flush: true)
        def id = visaSource.id
        assertNotNull id
        visaSource.delete()
        assertNull VisaSource.get(id)
    }


    void testValidation() {
        def visaSource = new VisaSource()
        assertFalse "VisaSource could not be validated as expected due to ${visaSource.errors}", visaSource.validate()
    }


    void testNullValidationFailure() {
        def visaSource = new VisaSource()
        assertFalse "VisaSource should have failed validation", visaSource.validate()
        assertErrorsFor visaSource, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateVisaSource() {
        def visaSource = new VisaSource(
                code: "TTTTTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return visaSource
    }
}
