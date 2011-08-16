
/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 1.24
 Generated: Mon Aug 01 14:09:09 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import grails.validation.ValidationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class RsvpIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(rsvp_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTTT"
	def i_success_description = "i_success_description"
	def i_success_planToAttendenceIndicator = true
	//Invalid test data (For failure tests)

	def i_failure_code = "TTTTT"
	def i_failure_description = null
	def i_failure_planToAttendenceIndicator = true

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "TTTTT"
	def u_success_description = "u_success_description"
	def u_success_planToAttendenceIndicator = true
	//Valid test data (For failure tests)

	def u_failure_code = "TTTTT"
	def u_failure_description = null
	def u_failure_planToAttendenceIndicator = true
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GTVRSVP'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		//initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(rsvp_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidRsvp() {
		def rsvp = newValidForCreateRsvp()
		rsvp.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull rsvp.id
	}

	void testCreateInvalidRsvp() {
		def rsvp = newInvalidForCreateRsvp()
		shouldFail(ValidationException) {
            rsvp.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidRsvp() {
		def rsvp = newValidForCreateRsvp()
		rsvp.save( failOnError: true, flush: true )
        assertNotNull rsvp.id
        assertEquals 0L, rsvp.version
        assertEquals i_success_code, rsvp.code
        assertEquals i_success_description, rsvp.description
        assertEquals i_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator

		//Update the entity
		rsvp.description = u_success_description
		rsvp.planToAttendenceIndicator = u_success_planToAttendenceIndicator
		rsvp.save( failOnError: true, flush: true )
		//Assert for sucessful update
        rsvp = Rsvp.get( rsvp.id )
        assertEquals 1L, rsvp?.version
        assertEquals u_success_description, rsvp.description
        assertEquals u_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator
	}

	void testUpdateInvalidRsvp() {
		def rsvp = newValidForCreateRsvp()
		rsvp.save( failOnError: true, flush: true )
        assertNotNull rsvp.id
        assertEquals 0L, rsvp.version
        assertEquals i_success_code, rsvp.code
        assertEquals i_success_description, rsvp.description
        assertEquals i_success_planToAttendenceIndicator, rsvp.planToAttendenceIndicator

		//Update the entity with invalid values
		rsvp.description = u_failure_description
		rsvp.planToAttendenceIndicator = u_failure_planToAttendenceIndicator
		shouldFail(ValidationException) {
            rsvp.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def rsvp = newValidForCreateRsvp()
		rsvp.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVRSVP set GTVRSVP_VERSION = 999 where GTVRSVP_SURROGATE_ID = ?", [ rsvp.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		rsvp.description = u_success_description
		rsvp.planToAttendenceIndicator = u_success_planToAttendenceIndicator
        shouldFail( HibernateOptimisticLockingFailureException ) {
            rsvp.save( failOnError: true, flush: true )
        }
    }

	void testDeleteRsvp() {
		def rsvp = newValidForCreateRsvp()
		rsvp.save( failOnError: true, flush: true )
		def id = rsvp.id
		assertNotNull id
		rsvp.delete()
		assertNull Rsvp.get( id )
	}

    void testValidation() {
       def rsvp = newInvalidForCreateRsvp()
       assertFalse "Rsvp could not be validated as expected due to ${rsvp.errors}", rsvp.validate()
    }

    void testNullValidationFailure() {
        def rsvp = new Rsvp()
        assertFalse "Rsvp should have failed validation", rsvp.validate()
        assertErrorsFor rsvp, 'nullable',
                                               [
                                                 'code',
                                                 'description',
                                                 'planToAttendenceIndicator'
                                               ]
    }


	void testValidationMessages() {
	    def rsvp = newInvalidForCreateRsvp()
	    rsvp.code = null
	    assertFalse rsvp.validate()
	    assertLocalizedError rsvp, 'nullable', /.*Field.*code.*of class.*Rsvp.*cannot be null.*/, 'code'
	    rsvp.description = null
	    assertFalse rsvp.validate()
	    assertLocalizedError rsvp, 'nullable', /.*Field.*description.*of class.*Rsvp.*cannot be null.*/, 'description'
	    rsvp.planToAttendenceIndicator = null
	    assertFalse rsvp.validate()
	    assertLocalizedError rsvp, 'nullable', /.*Field.*planToAttendenceIndicator.*of class.*Rsvp.*cannot be null.*/, 'planToAttendenceIndicator'
	}


	private def newValidForCreateRsvp() {
		def rsvp = new Rsvp(
			code: i_success_code,
			description: i_success_description,
			planToAttendenceIndicator: i_success_planToAttendenceIndicator,
		)
		return rsvp
	}

	private def newInvalidForCreateRsvp() {
		def rsvp = new Rsvp(
			code: i_failure_code,
			description: i_failure_description,
			planToAttendenceIndicator: i_failure_planToAttendenceIndicator,
		)
		return rsvp
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(rsvp_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
