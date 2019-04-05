/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class VisaIssuingAuthorityIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateValidVisaIssuingAuthority() {
        def visaIssuingAuthority = newValidForCreateVisaIssuingAuthority()
        visaIssuingAuthority.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull visaIssuingAuthority.id
    }


    @Test
    void testUpdateValidVisaIssuingAuthority() {
        def visaIssuingAuthority = newValidForCreateVisaIssuingAuthority()
        visaIssuingAuthority.save(failOnError: true, flush: true)
        assertNotNull visaIssuingAuthority.id
        assertEquals 0L, visaIssuingAuthority.version
        assertEquals "TTTTTT", visaIssuingAuthority.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", visaIssuingAuthority.description

        //Update the entity
        visaIssuingAuthority.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        visaIssuingAuthority.save(failOnError: true, flush: true)
        //Assert for sucessful update
        visaIssuingAuthority = VisaIssuingAuthority.get(visaIssuingAuthority.id)
        assertEquals 1L, visaIssuingAuthority?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", visaIssuingAuthority.description
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def visaIssuingAuthority = newValidForCreateVisaIssuingAuthority()

        visaIssuingAuthority.save(flush: true, failOnError: true)
        visaIssuingAuthority.refresh()
        assertNotNull "VisaIssuingAuthority should have been saved", visaIssuingAuthority.id

        // test date values -
        assertEquals date.format(today), date.format(visaIssuingAuthority.lastModified)
        assertEquals hour.format(today), hour.format(visaIssuingAuthority.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def visaIssuingAuthority = newValidForCreateVisaIssuingAuthority()
        visaIssuingAuthority.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVVISS set GTVVISS_VERSION = 999 where GTVVISS_SURROGATE_ID = ?", [visaIssuingAuthority.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        visaIssuingAuthority.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            visaIssuingAuthority.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteVisaIssuingAuthority() {
        def visaIssuingAuthority = newValidForCreateVisaIssuingAuthority()
        visaIssuingAuthority.save(failOnError: true, flush: true)
        def id = visaIssuingAuthority.id
        assertNotNull id
        visaIssuingAuthority.delete()
        assertNull VisaIssuingAuthority.get(id)
    }


    @Test
    void testValidation() {
        def visaIssuingAuthority = new VisaIssuingAuthority()
        assertFalse "VisaIssuingAuthority could not be validated as expected due to ${visaIssuingAuthority.errors}", visaIssuingAuthority.validate()
    }


    @Test
    void testNullValidationFailure() {
        def visaIssuingAuthority = new VisaIssuingAuthority()
        assertFalse "VisaIssuingAuthority should have failed validation", visaIssuingAuthority.validate()
        assertErrorsFor visaIssuingAuthority, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateVisaIssuingAuthority() {
        def visaIssuingAuthority = new VisaIssuingAuthority(
                code: "TTTTTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return visaIssuingAuthority
    }

}
