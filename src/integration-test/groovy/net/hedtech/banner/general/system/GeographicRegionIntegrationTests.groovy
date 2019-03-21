/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.junit.After
import org.junit.Before
import org.junit.Test



/**
 * Integration Tests for the GeographicRegion Domain.
 */
class GeographicRegionIntegrationTests extends BaseIntegrationTestCase{

    //Seed data initialization
    //Success Data for Insert
    def i_success_code = "REGION"
    def i_success_description = "Divides Earth"

    //Failure Data for Insert
    def i_failure_code = "INVALID CODE EXCEEDS TEN CHARACTERS"
    def i_failure_description = "INVALID DESC Which is above thirty characters in length"

    //Success Data for Update
    def u_success_code = "NORTHEAST"
    def u_success_description = "Northeastern United States 1"

    //Failure Data for Update
    def u_failure_code = "NORTHEAST"
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
    void testCreateValidGeographicRegion() {
        def geographicRegion = newValidForCreateGeographicRegion()
        geographicRegion.save(failOnError: true, flush: true)
        assertNotNull geographicRegion.id
        assertEquals i_success_code, geographicRegion.code
        assertEquals i_success_description, geographicRegion.description
    }


    @Test
    void testCreateInValidGeographicRegionCode() {
        def geographicRegion = newInValidCreateGeographicRegionCode()
        //Should fail for Invalid Geographic Region Code
        shouldFail {
            geographicRegion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testCreateInValidGeographicRegionDesc() {
        def geographicRegion = newInValidCreateGeographicRegionDesc()
        //Should fail for Invalid Geographic Region Description
        shouldFail {
            geographicRegion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidGeographicRegion() {
        def geographicRegion = newValidForCreateGeographicRegion()
        geographicRegion.save(failOnError: true, flush: true)
        assertNotNull geographicRegion.id
        assertEquals 0L, geographicRegion.version
        assertEquals i_success_code, geographicRegion.code
        assertEquals i_success_description, geographicRegion.description

        //update the Geographic Region
        geographicRegion.description = u_success_description
        geographicRegion.save(failOnError: true, flush: true)
        geographicRegion = GeographicRegion.get(geographicRegion.id)
        assertEquals 1L, geographicRegion?.version
        assertEquals u_success_description, geographicRegion.description
    }


    @Test
    void testUpdateInValidGeographicRegion() {
        def geographicRegion = newValidForCreateGeographicRegion()
        geographicRegion.save(failOnError: true, flush: true)
        assertNotNull geographicRegion.id
        assertEquals 0L, geographicRegion.version
        assertEquals i_success_code, geographicRegion.code
        assertEquals i_success_description, geographicRegion.description

        //update the Geographic Region
        geographicRegion.description = u_failure_description
        shouldFail {
            geographicRegion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def geographicRegion = newValidForCreateGeographicRegion()
        geographicRegion.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVGEOR set STVGEOR_VERSION = 999 where STVGEOR_SURROGATE_ID = ?", [geographicRegion.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        geographicRegion.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            geographicRegion.save(failOnError: true, flush: true)
        }
    }


    @Test
       void testDeleteGeographicregion() {
           def geographicRegion = newValidForCreateGeographicRegion()
        geographicRegion.save(failOnError: true, flush: true)
           def id = geographicRegion.id
           assertNotNull id
        geographicRegion.delete()
           assertNull GeographicRegion.get(id)
       }


       @Test
       void testValidation() {
           def geographicRegion = newValidForCreateGeographicRegion()
           assertTrue "GeographicRegion could not be validated as expected due to ${geographicRegion.errors}", geographicRegion.validate()
       }


       @Test
       void testNullValidationFailure() {
           def geographicRegion = new GeographicRegion()
           assertFalse "GeographicRegion should have failed validation", geographicRegion.validate()
           assertErrorsFor geographicRegion, 'nullable', ['code', 'description']
       }


       @Test
       void testMaxSizeValidationFailures() {
           def geographicRegion = new GeographicRegion(
                   description: u_failure_description)
           assertFalse "GeographicRegion should have failed validation", geographicRegion.validate()
           assertErrorsFor geographicRegion, 'maxSize', ['description']
       }


    //Private Methods
    private def newValidForCreateGeographicRegion() {
        def geographicRegion = new GeographicRegion(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }


    private def newInValidCreateGeographicRegionCode() {
        def geographicRegion = new GeographicRegion(
                code: i_failure_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }


    private def newInValidCreateGeographicRegionDesc() {
        def geographicRegion = new GeographicRegion(
                code: i_success_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }
}
