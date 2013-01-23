/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:43:24 EDT 2011
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import grails.validation.ValidationException


class RatingIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(rating_domain_integration_test_data) ENABLED START*/
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
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		//initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(rating_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)

		//Invalid test data (For failure tests)

		//Valid test data (For success tests)

		//Valid test data (For failure tests)

		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidRating() {
		def rating = newValidForCreateRating()
		save rating
		//Test if the generated entity now has an id assigned
        assertNotNull rating.id
	}

	void testCreateInvalidRating() {
		def rating = newInvalidForCreateRating()
        rating.code = null
		shouldFail(ValidationException) {
            rating.save( failOnError: true, flush: true )
		}
	}

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
		//Asset for sucessful update
        rating = Rating.get( rating.id )
        assertEquals 1L, rating?.version
        assertEquals u_success_description, rating.description
	}

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
            rating.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def rating = newValidForCreateRating()
		save rating

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVRTNG set GTVRTNG_VERSION = 999 where GTVRTNG_SURROGATE_ID = ?", [ rating.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		rating.description = u_success_description
        shouldFail( HibernateOptimisticLockingFailureException ) {
            rating.save( failOnError: true, flush: true )
        }
    }

	void testDeleteRating() {
		def rating = newValidForCreateRating()
		save rating
		def id = rating.id
		assertNotNull id
		rating.delete()
		assertNull Rating.get( id )
	}

    void testValidation() {
       def rating = newInvalidForCreateRating()
       assertTrue "Rating could not be validated as expected due to ${rating.errors}", rating.validate()
    }

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

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(rating_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
