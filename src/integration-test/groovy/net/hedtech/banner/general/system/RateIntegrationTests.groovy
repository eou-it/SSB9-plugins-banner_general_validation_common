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
class RateIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidRate() {
        def rate = newValidForCreateRate()
        rate.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull rate.id
    }


    @Test
    void testCreateInvalidRate() {
        def rate = newInvalidForCreateRate()
        shouldFail(ValidationException) {
            rate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidRate() {
        def rate = newValidForCreateRate()
        rate.save(failOnError: true, flush: true)
        assertNotNull rate.id
        assertEquals 0L, rate.version
        assertEquals i_success_code, rate.code
        assertEquals i_success_description, rate.description

        //Update the entity
        rate.description = u_success_description
        rate.save(failOnError: true, flush: true)

        //Assert for successful update
        rate = Rate.get(rate.id)
        assertEquals 1L, rate?.version
        assertEquals u_success_description, rate.description
    }


    @Test
    void testUpdateInvalidRate() {
        def rate = newValidForCreateRate()
        rate.save(failOnError: true, flush: true)
        assertNotNull rate.id
        assertEquals 0L, rate.version
        assertEquals i_success_code, rate.code
        assertEquals i_success_description, rate.description

        //Update the entity with invalid values
        rate.description = u_failure_description
        shouldFail(ValidationException) {
            rate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def rate = newValidForCreateRate()
        rate.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVRATE set GTVRATE_VERSION = 999 where GTVRATE_SURROGATE_ID = ?", [rate.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        rate.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            rate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteRate() {
        def rate = newValidForCreateRate()
        rate.save(failOnError: true, flush: true)
        def id = rate.id
        assertNotNull id
        rate.delete()
        assertNull Rate.get(id)
    }


    @Test
    void testValidation() {
        def rate = newInvalidForCreateRate()
        assertFalse "Rate could not be validated as expected due to ${rate.errors}", rate.validate()
    }


    @Test
    void testNullValidationFailure() {
        def rate = new Rate()
        assertFalse "Rate should have failed validation", rate.validate()
        assertErrorsFor rate, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateRate() {
        def rate = new Rate(
                code: i_success_code,
                description: i_success_description,
        )
        return rate
    }


    private def newInvalidForCreateRate() {
        def rate = new Rate(
                code: i_failure_code,
                description: i_failure_description,
        )
        return rate
    }

}
