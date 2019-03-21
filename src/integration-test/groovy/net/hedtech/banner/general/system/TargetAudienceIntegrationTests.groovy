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

class TargetAudienceIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "i_success_description"
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTT"
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "u_success_description"
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = null


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
    void testCreateValidTargetAudience() {
        def targetAudience = newValidForCreateTargetAudience()
        targetAudience.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull targetAudience.id
    }


    @Test
    void testCreateInvalidTargetAudience() {
        def targetAudience = newInvalidForCreateTargetAudience()
        shouldFail(ValidationException) {
            targetAudience.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidTargetAudience() {
        def targetAudience = newValidForCreateTargetAudience()
        targetAudience.save(failOnError: true, flush: true)
        assertNotNull targetAudience.id
        assertEquals 0L, targetAudience.version
        assertEquals i_success_code, targetAudience.code
        assertEquals i_success_description, targetAudience.description

        //Update the entity
        targetAudience.description = u_success_description
        targetAudience.save(failOnError: true, flush: true)

        //Assert for successful update
        targetAudience = TargetAudience.get(targetAudience.id)
        assertEquals 1L, targetAudience?.version
        assertEquals u_success_description, targetAudience.description
    }


    @Test
    void testUpdateInvalidTargetAudience() {
        def targetAudience = newValidForCreateTargetAudience()
        targetAudience.save(failOnError: true, flush: true)
        assertNotNull targetAudience.id
        assertEquals 0L, targetAudience.version
        assertEquals i_success_code, targetAudience.code
        assertEquals i_success_description, targetAudience.description

        //Update the entity with invalid values
        targetAudience.description = u_failure_description
        shouldFail(ValidationException) {
            targetAudience.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def targetAudience = newValidForCreateTargetAudience()
        targetAudience.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVTARG set GTVTARG_VERSION = 999 where GTVTARG_SURROGATE_ID = ?", [targetAudience.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        targetAudience.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            targetAudience.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteTargetAudience() {
        def targetAudience = newValidForCreateTargetAudience()
        targetAudience.save(failOnError: true, flush: true)
        def id = targetAudience.id
        assertNotNull id
        targetAudience.delete()
        assertNull TargetAudience.get(id)
    }


    @Test
    void testValidation() {
        def targetAudience = newInvalidForCreateTargetAudience()
        assertFalse "TargetAudience could not be validated as expected due to ${targetAudience.errors}", targetAudience.validate()
    }


    @Test
    void testNullValidationFailure() {
        def targetAudience = new TargetAudience()
        assertFalse "TargetAudience should have failed validation", targetAudience.validate()
        assertErrorsFor targetAudience, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    private def newValidForCreateTargetAudience() {
        def targetAudience = new TargetAudience(
                code: i_success_code,
                description: i_success_description,
        )
        return targetAudience
    }


    private def newInvalidForCreateTargetAudience() {
        def targetAudience = new TargetAudience(
                code: i_failure_code,
                description: i_failure_description,
        )
        return targetAudience
    }

}
