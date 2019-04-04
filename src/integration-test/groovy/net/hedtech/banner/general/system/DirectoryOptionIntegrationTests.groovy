/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class DirectoryOptionIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_systemRequiredIndicator = true
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTTTTTTTTT"
    def i_failure_description = "TTTTT"
    def i_failure_systemRequiredIndicator = true

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "XXXXXXXXXXXX"
    def u_success_systemRequiredIndicator = true
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = "TTTTTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    def u_failure_systemRequiredIndicator = true


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
    void testCreateValidDirectoryOption() {
        def directoryOption = newValidForCreateDirectoryOption()
        directoryOption.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned		
        assertNotNull directoryOption.id
    }


    @Test
    void testCreateInvalidDirectoryOption() {
        def directoryOption = newInvalidForCreateDirectoryOption()
        shouldFail(ValidationException) {
            directoryOption.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidDirectoryOption() {
        def directoryOption = newValidForCreateDirectoryOption()
        directoryOption.save(failOnError: true, flush: true)
        assertNotNull directoryOption.id
        assertEquals 0L, directoryOption.version
        assertEquals i_success_code, directoryOption.code
        assertEquals i_success_description, directoryOption.description
        assertEquals i_success_systemRequiredIndicator, directoryOption.systemRequiredIndicator

        //Update the entity
        directoryOption.description = u_success_description
        directoryOption.systemRequiredIndicator = u_success_systemRequiredIndicator
        directoryOption.save(failOnError: true, flush: true)
        //Assert for sucessful update        
        directoryOption = DirectoryOption.get(directoryOption.id)
        assertEquals 1L, directoryOption?.version
        assertEquals u_success_description, directoryOption.description
        assertEquals u_success_systemRequiredIndicator, directoryOption.systemRequiredIndicator
    }


    @Test
    void testUpdateInvalidDirectoryOption() {
        def directoryOption = newValidForCreateDirectoryOption()
        directoryOption.save(failOnError: true, flush: true)
        assertNotNull directoryOption.id
        assertEquals 0L, directoryOption.version
        assertEquals i_success_code, directoryOption.code
        assertEquals i_success_description, directoryOption.description
        assertEquals i_success_systemRequiredIndicator, directoryOption.systemRequiredIndicator

        //Update the entity with invalid values
        directoryOption.description = u_failure_description
        directoryOption.systemRequiredIndicator = u_failure_systemRequiredIndicator
        shouldFail(ValidationException) {
            directoryOption.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def directoryOption = newValidForCreateDirectoryOption()



        directoryOption.save(flush: true, failOnError: true)
        directoryOption.refresh()
        assertNotNull "DirectoryOption should have been saved", directoryOption.id

        // test date values -    	
        assertEquals date.format(today), date.format(directoryOption.lastModified)
        assertEquals hour.format(today), hour.format(directoryOption.lastModified)


    }


    @Test
    void testOptimisticLock() {
        def directoryOption = newValidForCreateDirectoryOption()
        directoryOption.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVDIRO set GTVDIRO_VERSION = 999 where GTVDIRO_SURROGATE_ID = ?", [directoryOption.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        directoryOption.description = u_success_description
        directoryOption.systemRequiredIndicator = u_success_systemRequiredIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            directoryOption.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteDirectoryOption() {
        def directoryOption = newValidForCreateDirectoryOption()
        directoryOption.save(failOnError: true, flush: true)
        def id = directoryOption.id
        assertNotNull id
        directoryOption.delete()
        assertNull DirectoryOption.get(id)
    }


    @Test
    void testValidation() {
        def directoryOption = newInvalidForCreateDirectoryOption()
        assertFalse "DirectoryOption could not be validated as expected due to ${directoryOption.errors}", directoryOption.validate()
    }


    @Test
    void testNullValidationFailure() {
        def directoryOption = new DirectoryOption()
        assertFalse "DirectoryOption should have failed validation", directoryOption.validate()
        assertErrorsFor directoryOption, 'nullable',
                [
                        'code',
                        'description',
                        'systemRequiredIndicator'
                ]
    }


    private def newValidForCreateDirectoryOption() {
        def directoryOption = new DirectoryOption(
                code: i_success_code,
                description: i_success_description,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
        )
        return directoryOption
    }


    private def newInvalidForCreateDirectoryOption() {
        def directoryOption = new DirectoryOption(
                code: i_failure_code,
                description: i_failure_description,
                systemRequiredIndicator: i_failure_systemRequiredIndicator,
        )
        return directoryOption
    }
}
