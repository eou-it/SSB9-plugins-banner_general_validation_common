/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class InitialsIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    def i_success_title1 = "TTTTT"
    def i_success_title2 = "TTTTT"
    def i_success_emailAddress = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTT"
    def i_failure_description = "TTTTT"
    def i_failure_title1 = "TTTTT"
    def i_failure_title2 = "TTTTT"
    def i_failure_emailAddress = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTT"
    def u_success_description = "WWWWW"
    def u_success_title1 = "WWWW"
    def u_success_title2 = "WWWW"
    def u_success_emailAddress = "WWWWW"
    //Valid test data (For failure tests)

    def u_failure_code = "TTTT"
    def u_failure_description = "TTTTT"
    def u_failure_title1 = "TTTTT"
    def u_failure_title2 = "TTTTT"
    def u_failure_emailAddress = "TTTTT"


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
    void testCreateValidInitials() {
        def initials = newValidForCreateInitials()
        initials.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull initials.id
    }


	@Test
    void testCreateInvalidInitials() {
        def initials = newInvalidForCreateInitials()
        initials.code = null
        shouldFail(ValidationException) {
            initials.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testUpdateValidInitials() {
        def initials = newValidForCreateInitials()
        initials.save(failOnError: true, flush: true)
        assertNotNull initials.id
        assertEquals 0L, initials.version
        assertEquals i_success_code, initials.code
        assertEquals i_success_description, initials.description
        assertEquals i_success_title1, initials.title1
        assertEquals i_success_title2, initials.title2
        assertEquals i_success_emailAddress, initials.emailAddress

        //Update the entity
        initials.description = u_success_description
        initials.title1 = u_success_title1
        initials.title2 = u_success_title2
        initials.emailAddress = u_success_emailAddress
        initials.save(failOnError: true, flush: true)
        //Assert for sucessful update
        initials = Initials.get(initials.id)
        assertEquals 1L, initials?.version
        assertEquals u_success_description, initials.description
        assertEquals u_success_title1, initials.title1
        assertEquals u_success_title2, initials.title2
        assertEquals u_success_emailAddress, initials.emailAddress
    }


	@Test
    void testUpdateInvalidInitials() {
        def initials = newValidForCreateInitials()
        initials.save(failOnError: true, flush: true)
        assertNotNull initials.id
        assertEquals 0L, initials.version
        assertEquals i_success_code, initials.code
        assertEquals i_success_description, initials.description
        assertEquals i_success_title1, initials.title1
        assertEquals i_success_title2, initials.title2
        assertEquals i_success_emailAddress, initials.emailAddress

        //Update the entity with invalid values
        initials.code = null
        initials.description = u_failure_description
        initials.title1 = u_failure_title1
        initials.title2 = u_failure_title2
        initials.emailAddress = u_failure_emailAddress
        shouldFail(ValidationException) {
            initials.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def initials = newValidForCreateInitials()



        initials.save(flush: true, failOnError: true)
        initials.refresh()
        assertNotNull "Initials should have been saved", initials.id

        // test date values -
        assertEquals date.format(today), date.format(initials.lastModified)
        assertEquals hour.format(today), hour.format(initials.lastModified)


    }


	@Test
    void testOptimisticLock() {
        def initials = newValidForCreateInitials()
        initials.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVINIT set STVINIT_VERSION = 999 where STVINIT_SURROGATE_ID = ?", [initials.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        initials.description = u_success_description
        initials.title1 = u_success_title1
        initials.title2 = u_success_title2
        initials.emailAddress = u_success_emailAddress
        shouldFail(HibernateOptimisticLockingFailureException) {
            initials.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDeleteInitials() {
        def initials = newValidForCreateInitials()
        initials.save(failOnError: true, flush: true)
        def id = initials.id
        assertNotNull id
        initials.delete()
        assertNull Initials.get(id)
    }


	@Test
    void testValidation() {
        def initials = newInvalidForCreateInitials()
        initials.code = null
        assertFalse "Initials could not be validated as expected due to ${initials.errors}", initials.validate()
    }


	@Test
    void testNullValidationFailure() {
        def initials = new Initials()
        assertFalse "Initials should have failed validation", initials.validate()
        assertErrorsFor initials, 'nullable',
                [
                        'code',
                        'description',
                        'title1'
                ]
        assertNoErrorsFor initials,
                [
                        'title2',
                        'emailAddress'
                ]
    }


	@Test
    void testMaxSizeValidationFailures() {
        def initials = new Initials(
                title2: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                emailAddress: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "Initials should have failed validation", initials.validate()
        assertErrorsFor initials, 'maxSize', ['title2', 'emailAddress']
    }



    private def newValidForCreateInitials() {
        def initials = new Initials(
                code: i_success_code,
                description: i_success_description,
                title1: i_success_title1,
                title2: i_success_title2,
                emailAddress: i_success_emailAddress,
        )
        return initials
    }


    private def newInvalidForCreateInitials() {
        def initials = new Initials(
                code: i_failure_code,
                description: i_failure_description,
                title1: i_failure_title1,
                title2: i_failure_title2,
                emailAddress: i_failure_emailAddress,
        )
        return initials
    }


}
