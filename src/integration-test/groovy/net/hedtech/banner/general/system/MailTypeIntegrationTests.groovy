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
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class MailTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTT"
    def i_success_description = "i_success_description"
    def i_success_displayWebIndicator = true
    //Invalid test data (For failure tests)

    def i_failure_code = "TTT"
    def i_failure_description = null
    def i_failure_displayWebIndicator = true

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTT"
    def u_success_description = "u_success_description"
    def u_success_displayWebIndicator = true
    //Valid test data (For failure tests)

    def u_failure_code = "TTT"
    def u_failure_description = null
    def u_failure_displayWebIndicator = true


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
    void testCreateValidMailType() {
        def mailType = newValidForCreateMailType()
        mailType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull mailType.id
    }


    @Test
    void testCreateInvalidMailType() {
        def mailType = newInvalidForCreateMailType()
        shouldFail(ValidationException) {
            mailType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidMailType() {
        def mailType = newValidForCreateMailType()
        mailType.save(failOnError: true, flush: true)
        assertNotNull mailType.id
        assertEquals 0L, mailType.version
        assertEquals i_success_code, mailType.code
        assertEquals i_success_description, mailType.description
        assertEquals i_success_displayWebIndicator, mailType.displayWebIndicator

        //Update the entity
        mailType.description = u_success_description
        mailType.displayWebIndicator = u_success_displayWebIndicator
        mailType.save(failOnError: true, flush: true)
        //Assert for sucessful update
        mailType = MailType.get(mailType.id)
        assertEquals 1L, mailType?.version
        assertEquals u_success_description, mailType.description
        assertEquals u_success_displayWebIndicator, mailType.displayWebIndicator
    }


    @Test
    void testUpdateInvalidMailType() {
        def mailType = newValidForCreateMailType()
        mailType.save(failOnError: true, flush: true)
        assertNotNull mailType.id
        assertEquals 0L, mailType.version
        assertEquals i_success_code, mailType.code
        assertEquals i_success_description, mailType.description
        assertEquals i_success_displayWebIndicator, mailType.displayWebIndicator

        //Update the entity with invalid values
        mailType.description = u_failure_description
        mailType.displayWebIndicator = u_failure_displayWebIndicator
        shouldFail(ValidationException) {
            mailType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def mailType = newValidForCreateMailType()
        mailType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVMAIL set GTVMAIL_VERSION = 999 where GTVMAIL_SURROGATE_ID = ?", [mailType.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        mailType.description = u_success_description
        mailType.displayWebIndicator = u_success_displayWebIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            mailType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteMailType() {
        def mailType = newValidForCreateMailType()
        mailType.save(failOnError: true, flush: true)
        def id = mailType.id
        assertNotNull id
        mailType.delete()
        assertNull MailType.get(id)
    }


    @Test
    void testValidation() {
        def mailType = newInvalidForCreateMailType()
        assertFalse "MailType could not be validated as expected due to ${mailType.errors}", mailType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def mailType = new MailType()
        assertFalse "MailType should have failed validation", mailType.validate()
        assertErrorsFor mailType, 'nullable',
                [
                        'code',
                        'description',
                        'displayWebIndicator'
                ]
    }




    private def newValidForCreateMailType() {
        def mailType = new MailType(
                code: i_success_code,
                description: i_success_description,
                displayWebIndicator: i_success_displayWebIndicator,
        )
        return mailType
    }


    private def newInvalidForCreateMailType() {
        def mailType = new MailType(
                code: i_failure_code,
                description: i_failure_description,
                displayWebIndicator: i_failure_displayWebIndicator,
        )
        return mailType
    }

}
