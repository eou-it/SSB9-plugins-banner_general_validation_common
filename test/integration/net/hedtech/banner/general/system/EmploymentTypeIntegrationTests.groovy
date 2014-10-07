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

class EmploymentTypeIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidEmploymentType() {
        def employmentType = newValidForCreateEmploymentType()
        employmentType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull employmentType.id
    }


    @Test
    void testUpdateValidEmploymentType() {
        def employmentType = newValidForCreateEmploymentType()
        employmentType.save(failOnError: true, flush: true)
        assertNotNull employmentType.id
        assertEquals 0L, employmentType.version
        assertEquals "TTT", employmentType.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", employmentType.description

        //Update the entity
        employmentType.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        employmentType.save(failOnError: true, flush: true)
        //Assert for sucessful update
        employmentType = EmploymentType.get(employmentType.id)
        assertEquals 1L, employmentType?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", employmentType.description
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def employmentType = newValidForCreateEmploymentType()

        employmentType.save(flush: true, failOnError: true)
        employmentType.refresh()
        assertNotNull "EmploymentType should have been saved", employmentType.id

        // test date values -
        assertEquals date.format(today), date.format(employmentType.lastModified)
        assertEquals hour.format(today), hour.format(employmentType.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def employmentType = newValidForCreateEmploymentType()
        employmentType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVEMPT set STVEMPT_VERSION = 999 where STVEMPT_SURROGATE_ID = ?", [employmentType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        employmentType.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            employmentType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteEmploymentType() {
        def employmentType = newValidForCreateEmploymentType()
        employmentType.save(failOnError: true, flush: true)
        def id = employmentType.id
        assertNotNull id
        employmentType.delete()
        assertNull EmploymentType.get(id)
    }


    @Test
    void testValidation() {
        def employmentType = new EmploymentType()
        assertFalse "EmploymentType could not be validated as expected due to ${employmentType.errors}", employmentType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def employmentType = new EmploymentType()
        assertFalse "EmploymentType should have failed validation", employmentType.validate()
        assertErrorsFor employmentType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateEmploymentType() {
        def employmentType = new EmploymentType(
                code: "TTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return employmentType
    }
}
