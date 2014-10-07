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

class RatingIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "TT"
    def i_failure_description = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "WW"
    def u_success_description = "WWWWW"
    //Valid test data (For failure tests)

    def u_failure_code = "TT"
    def u_failure_description = "TTTTT"


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
    void testCreateValidRating() {
        def rating = newValidForCreateRating()
        save rating
        //Test if the generated entity now has an id assigned
        assertNotNull rating.id
    }


    @Test
    void testCreateInvalidRating() {
        def rating = newInvalidForCreateRating()
        rating.code = null
        shouldFail(ValidationException) {
            rating.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidRating() {
        def rating = newValidForCreateRating()
        save rating
        assertNotNull rating.id
        assertEquals 0L, rating.version
        assertEquals i_success_code, rating.code
        assertEquals i_success_description, rating.description

        //Update the entity
        rating.description = u_success_description
        save rating

        //Asset for successful update
        rating = Rating.get(rating.id)
        assertEquals 1L, rating?.version
        assertEquals u_success_description, rating.description
    }


    @Test
    void testUpdateInvalidRating() {
        def rating = newValidForCreateRating()
        save rating
        assertNotNull rating.id
        assertEquals 0L, rating.version
        assertEquals i_success_code, rating.code
        assertEquals i_success_description, rating.description

        //Update the entity with invalid values
        rating.description = u_failure_description
        rating.code = null
        shouldFail(ValidationException) {
            rating.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def rating = newValidForCreateRating()
        save rating

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVRTNG set GTVRTNG_VERSION = 999 where GTVRTNG_SURROGATE_ID = ?", [rating.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        rating.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            rating.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteRating() {
        def rating = newValidForCreateRating()
        save rating
        def id = rating.id
        assertNotNull id
        rating.delete()
        assertNull Rating.get(id)
    }


    @Test
    void testValidation() {
        def rating = newInvalidForCreateRating()
        assertTrue "Rating could not be validated as expected due to ${rating.errors}", rating.validate()
    }


    @Test
    void testNullValidationFailure() {
        def rating = new Rating()
        assertFalse "Rating should have failed validation", rating.validate()
        assertErrorsFor rating, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateRating() {
        def rating = new Rating(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return rating
    }


    private def newInvalidForCreateRating() {
        def rating = new Rating(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return rating
    }


}
