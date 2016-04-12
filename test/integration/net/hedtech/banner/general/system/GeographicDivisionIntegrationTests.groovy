/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


/**
 * Integration Tests for the GeographicDivision Domain.
 */
class GeographicDivisionIntegrationTests extends BaseIntegrationTestCase{

    //Seed data initialization
    //Success Data for Insert
    def i_success_code = "NORTHHEMIS"
    def i_success_description = "Northern Hemisphere"

    //Failure Data for Insert
    def i_failure_code = "INVALID CODE EXCEEDS TEN CHARACTERS"
    def i_failure_description = "INVALID DESC EXCEEDS THIRTY CHARACTERS AND STILL FAILING OVER"

    //Success Data for Update
    def u_success_code = "NORTHHEM"
    def u_success_description = "Northern Hemisphere 12"

    //Failure Data for Update
    def u_failure_code = "CENTRAL"
    def u_failure_description = "INVALID DESC Which is above thirty characters in length"


    @Before
    public void setUp(){
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown(){
        super.tearDown()
    }


    @Test
    void testCreateValidGeographicDivision() {
        def geographicDivision = newValidForCreateGeographicDivision()
        geographicDivision.save(failOnError: true, flush: true)
        assertNotNull geographicDivision.id
        assertEquals i_success_code, geographicDivision.code
        assertEquals i_success_description, geographicDivision.description
    }


    @Test
    void testCreateInValidGeographicDivisionCode() {
        def geographicDivision = newInValidCreateGeographicDivisionCode()
        //Should fail for Invalid Geographic Division Code
        shouldFail {
            geographicDivision.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testCreateInValidGeographicDivisionDesc() {
        def geographicDivision = newInValidCreateGeographicDivisionDesc()
        //Should fail for Invalid Geographic Division Description
        shouldFail {
            geographicDivision.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidGeographicDivision() {
        def geographicDivision = newValidForCreateGeographicDivision()
        geographicDivision.save(failOnError: true, flush: true)
        assertNotNull geographicDivision.id
        assertEquals 0L, geographicDivision.version
        assertEquals i_success_code, geographicDivision.code
        assertEquals i_success_description, geographicDivision.description

        //update the Geographic Division
        geographicDivision.description = u_success_description
        geographicDivision.save(failOnError: true, flush: true)
        geographicDivision = GeographicDivision.get(geographicDivision.id)
        assertEquals 1L, geographicDivision?.version
        assertEquals u_success_description, geographicDivision.description
    }


    @Test
    void testUpdateInValidGeographicDivision() {
        def geographicDivision = newValidForCreateGeographicDivision()
        geographicDivision.save(failOnError: true, flush: true)
        assertNotNull geographicDivision.id
        assertEquals 0L, geographicDivision.version
        assertEquals i_success_code, geographicDivision.code
        assertEquals i_success_description, geographicDivision.description

        //update the Geographic Division
        geographicDivision.description = u_failure_description
        shouldFail {
            geographicDivision.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def geographicDivision = newValidForCreateGeographicDivision()
        geographicDivision.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVGEOD set STVGEOD_VERSION = 999 where STVGEOD_SURROGATE_ID = ?", [geographicDivision.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        geographicDivision.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            geographicDivision.save(failOnError: true, flush: true)
        }
    }


    @Test
       void testDeleteGeographicDivision() {
           def geographicDivision = newValidForCreateGeographicDivision()
        geographicDivision.save(failOnError: true, flush: true)
           def id = geographicDivision.id
           assertNotNull id
        geographicDivision.delete()
           assertNull GeographicDivision.get(id)
       }


       @Test
       void testValidation() {
           def geographicDivision = newValidForCreateGeographicDivision()
           assertTrue "GeographicDivision could not be validated as expected due to ${geographicDivision.errors}", geographicDivision.validate()
       }


       @Test
       void testNullValidationFailure() {
           def geographicDivision = new GeographicDivision()
           assertFalse "GeographicDivision should have failed validation", geographicDivision.validate()
           assertErrorsFor geographicDivision, 'nullable', ['code', 'description']
       }


       @Test
       void testMaxSizeValidationFailures() {
           def geographicDivision = new GeographicDivision(
                   description: u_failure_description)
           assertFalse "GeographicDivision should have failed validation", geographicDivision.validate()
           assertErrorsFor geographicDivision, 'maxSize', ['description']
       }


    //Private Methods
    private def newValidForCreateGeographicDivision() {
        def geographicDivision = new GeographicDivision(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }


    private def newInValidCreateGeographicDivisionCode() {
        def geographicDivision = new GeographicDivision(
                code: i_failure_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }


    private def newInValidCreateGeographicDivisionDesc() {
        def geographicDivision = new GeographicDivision(
                code: i_success_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }
}
