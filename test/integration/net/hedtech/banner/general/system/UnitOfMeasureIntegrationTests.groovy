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

class UnitOfMeasureIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateUnitOfMeasure() {
        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull unitOfMeasure.id
        assertNotNull unitOfMeasure.id
        assertEquals 0L, unitOfMeasure.version
        assertEquals "TTTT", unitOfMeasure.code
        assertEquals "TTTTTTTTTT", unitOfMeasure.description
    }


    @Test
    void testUpdateUnitOfMeasure() {
        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)
        assertNotNull unitOfMeasure.id
        assertEquals 0L, unitOfMeasure.version
        assertEquals "TTTT", unitOfMeasure.code

        //Update the entity
        unitOfMeasure.description = "UUUUUUUUUU"
        unitOfMeasure.save(failOnError: true, flush: true)
        //Assert for successful update
        unitOfMeasure = UnitOfMeasure.get(unitOfMeasure.id)
        assertEquals 1L, unitOfMeasure?.version
        assertEquals "UUUUUUUUUU", unitOfMeasure.description
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def unitOfMeasure = newUnitOfMeasure()

        unitOfMeasure.save(flush: true, failOnError: true)
        unitOfMeasure.refresh()
        assertNotNull "UnitOfMeasure should have been saved", unitOfMeasure.id

        // test date values -
        assertEquals date.format(today), date.format(unitOfMeasure.lastModified)
        assertEquals hour.format(today), hour.format(unitOfMeasure.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVUOMS set GTVUOMS_VERSION = 999 where GTVUOMS_SURROGATE_ID = ?", [unitOfMeasure.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        unitOfMeasure.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            unitOfMeasure.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteUnitOfMeasure() {
        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)
        def id = unitOfMeasure.id
        assertNotNull id
        unitOfMeasure.delete()
        assertNull UnitOfMeasure.get(id)
    }


    @Test
    void testValidation() {
        def unitOfMeasure = new UnitOfMeasure()
        assertFalse "UnitOfMeasure could not be validated as expected due to ${unitOfMeasure.errors}", unitOfMeasure.validate()
    }


    @Test
    void testNullValidationFailure() {
        def unitOfMeasure = new UnitOfMeasure()
        assertFalse "UnitOfMeasure should have failed validation", unitOfMeasure.validate()
        assertErrorsFor unitOfMeasure, 'nullable', ['code', 'description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def unitOfMeasure = new UnitOfMeasure(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT')

        assertFalse "UnitOfMeasure should have failed validation", unitOfMeasure.validate()
        assertErrorsFor unitOfMeasure, 'maxSize', ['code', 'description']
    }


    private def newUnitOfMeasure() {
        def unitOfMeasure = new UnitOfMeasure(
                code: "TTTT",
                description: "TTTTTTTTTT"
        )
        return unitOfMeasure
    }

}
