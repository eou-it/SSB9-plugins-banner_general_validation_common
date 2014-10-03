/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import java.text.SimpleDateFormat
import grails.validation.ValidationException


class CommonMatchingSourceIntegrationTests extends BaseIntegrationTestCase {

    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_longDescription = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTT"
    def i_failure_description = "TTTTT"
    def i_failure_longDescription = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "UPDATED"
    def u_success_longDescription = "TTTTT"
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = "This description will fail because it is too long for the column limit of 60 characters"
    def u_failure_longDescription = "TTTTT"


	@Before
	public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	@After
	public void tearDown() {
        super.tearDown()
    }


	@Test
    void testCreateValidCommonMatchingSource() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull commonMatchingSource.id
    }


	@Test
    void testUpdateValidCommonMatchingSource() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(failOnError: true, flush: true)
        assertNotNull commonMatchingSource.id
        assertEquals 0L, commonMatchingSource.version
        assertEquals i_success_code, commonMatchingSource.code
        assertEquals i_success_description, commonMatchingSource.description
        assertEquals i_success_longDescription, commonMatchingSource.longDescription

        //Update the entity
        commonMatchingSource.description = u_success_description
        commonMatchingSource.longDescription = u_success_longDescription
        commonMatchingSource.save(failOnError: true, flush: true)
        //Assert for successful update
        commonMatchingSource = CommonMatchingSource.get(commonMatchingSource.id)
        assertEquals 1L, commonMatchingSource?.version
        assertEquals u_success_description, commonMatchingSource.description
        assertEquals u_success_longDescription, commonMatchingSource.longDescription
    }


	@Test
    void testUpdateInvalidCommonMatchingSource() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(failOnError: true, flush: true)
        assertNotNull commonMatchingSource.id
        assertEquals 0L, commonMatchingSource.version
        assertEquals i_success_code, commonMatchingSource.code
        assertEquals i_success_description, commonMatchingSource.description
        assertEquals i_success_longDescription, commonMatchingSource.longDescription

        //Update the entity with invalid values
        commonMatchingSource.description = u_failure_description
        commonMatchingSource.longDescription = u_failure_longDescription
        shouldFail(ValidationException) {
            commonMatchingSource.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(flush: true, failOnError: true)
        commonMatchingSource.refresh()
        assertNotNull "CommonMatchingSource should have been saved", commonMatchingSource.id

        // test date values -
        assertEquals date.format(today), date.format(commonMatchingSource.lastModified)
        assertEquals hour.format(today), hour.format(commonMatchingSource.lastModified)
    }


	@Test
    void testOptimisticLock() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVCMSC set GTVCMSC_VERSION = 999 where GTVCMSC_SURROGATE_ID = ?", [commonMatchingSource.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        commonMatchingSource.description = u_success_description
        commonMatchingSource.longDescription = u_success_longDescription
        shouldFail(HibernateOptimisticLockingFailureException) {
            commonMatchingSource.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDeleteCommonMatchingSource() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        commonMatchingSource.save(failOnError: true, flush: true)
        def id = commonMatchingSource.id
        assertNotNull id
        commonMatchingSource.delete()
        assertNull CommonMatchingSource.get(id)
    }


	@Test
    void testValidation() {
        def commonMatchingSource = newValidForCreateCommonMatchingSource()
        assertTrue "CommonMatchingSource could not be validated as expected due to ${commonMatchingSource.errors}", commonMatchingSource.validate()
    }


	@Test
    void testNullValidationFailure() {
        def commonMatchingSource = new CommonMatchingSource()
        assertFalse "CommonMatchingSource should have failed validation", commonMatchingSource.validate()
        assertErrorsFor commonMatchingSource, 'nullable',
                [
                        'code',
                        'description'
                ]
        assertNoErrorsFor commonMatchingSource,
                [
                        'longDescription'
                ]
    }


	@Test
    void testMaxSizeValidationFailures() {
        def commonMatchingSource = new CommonMatchingSource(
                longDescription: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "CommonMatchingSource should have failed validation", commonMatchingSource.validate()
        assertErrorsFor commonMatchingSource, 'maxSize', ['longDescription']
    }


    private def newValidForCreateCommonMatchingSource() {
        def commonMatchingSource = new CommonMatchingSource(
                code: i_success_code,
                description: i_success_description,
                longDescription: i_success_longDescription,
        )
        return commonMatchingSource
    }

}
