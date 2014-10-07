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

class CertificationOfEligibilityIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidCertificationOfEligibility() {
        def certificationOfEligibility = newValidForCreateCertificationOfEligibility()
        certificationOfEligibility.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull certificationOfEligibility.id
    }


    @Test
    void testUpdateValidCertificationOfEligibility() {
        def certificationOfEligibility = newValidForCreateCertificationOfEligibility()
        certificationOfEligibility.save(failOnError: true, flush: true)
        assertNotNull certificationOfEligibility.id
        assertEquals 0L, certificationOfEligibility.version
        assertEquals "TTTTTT", certificationOfEligibility.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", certificationOfEligibility.description

        //Update the entity
        certificationOfEligibility.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        certificationOfEligibility.save(failOnError: true, flush: true)
        //Assert for sucessful update
        certificationOfEligibility = CertificationOfEligibility.get(certificationOfEligibility.id)
        assertEquals 1L, certificationOfEligibility?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", certificationOfEligibility.description
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def certificationOfEligibility = newValidForCreateCertificationOfEligibility()

        certificationOfEligibility.save(flush: true, failOnError: true)
        certificationOfEligibility.refresh()
        assertNotNull "CertificationOfEligibility should have been saved", certificationOfEligibility.id

        // test date values -
        assertEquals date.format(today), date.format(certificationOfEligibility.lastModified)
        assertEquals hour.format(today), hour.format(certificationOfEligibility.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def certificationOfEligibility = newValidForCreateCertificationOfEligibility()
        certificationOfEligibility.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVCELG set GTVCELG_VERSION = 999 where GTVCELG_SURROGATE_ID = ?", [certificationOfEligibility.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        certificationOfEligibility.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            certificationOfEligibility.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteCertificationOfEligibility() {
        def certificationOfEligibility = newValidForCreateCertificationOfEligibility()
        certificationOfEligibility.save(failOnError: true, flush: true)
        def id = certificationOfEligibility.id
        assertNotNull id
        certificationOfEligibility.delete()
        assertNull CertificationOfEligibility.get(id)
    }


    @Test
    void testValidation() {
        def certificationOfEligibility = new CertificationOfEligibility()
        assertFalse "CertificationOfEligibility could not be validated as expected due to ${certificationOfEligibility.errors}", certificationOfEligibility.validate()
    }


    @Test
    void testNullValidationFailure() {
        def certificationOfEligibility = new CertificationOfEligibility()
        assertFalse "CertificationOfEligibility should have failed validation", certificationOfEligibility.validate()
        assertErrorsFor certificationOfEligibility, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateCertificationOfEligibility() {
        def certificationOfEligibility = new CertificationOfEligibility(
                code: "TTTTTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return certificationOfEligibility
    }
}
