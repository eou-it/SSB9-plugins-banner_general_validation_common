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


class ParticipantTypeIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(participanttype_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTTT"
	def i_success_description = "TTTTT"
	//Invalid test data (For failure tests)

	def i_failure_code = "FFFFF"
	def i_failure_description = "FFFFF"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "USSSS"
	def u_success_description = "USSSS"
	//Valid test data (For failure tests)

	def u_failure_code = "UFFFF"
	def u_failure_description = "UFFFF"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		//initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(participanttype_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidParticipantType() {
		def participantType = newValidForCreateParticipantType()
		save participantType
		//Test if the generated entity now has an id assigned
        assertNotNull participantType.id
	}

	void testCreateInvalidParticipantType() {
		def participantType = newInvalidForCreateParticipantType()
        participantType.code = null
		shouldFail(ValidationException) {
            participantType.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidParticipantType() {
		def participantType = newValidForCreateParticipantType()
		save participantType
        assertNotNull participantType.id
        assertEquals 0L, participantType.version
        assertEquals i_success_code, participantType.code
        assertEquals i_success_description, participantType.description

		//Update the entity
		participantType.description = u_success_description
        participantType.save( failOnError: true, flush: true )
		//Asset for sucessful update
        participantType = ParticipantType.get( participantType.id )
        assertEquals 1L, participantType?.version
        assertEquals u_success_description, participantType.description
	}

	void testUpdateInvalidParticipantType() {
		def participantType = newValidForCreateParticipantType()
		save participantType
        assertNotNull participantType.id
        assertEquals 0L, participantType.version
        assertEquals i_success_code, participantType.code
        assertEquals i_success_description, participantType.description

		//Update the entity with invalid values
		participantType.description = u_failure_description
        participantType.code = null
		shouldFail(ValidationException) {
            participantType.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def participantType = newValidForCreateParticipantType()
		participantType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVPTYP set GTVPTYP_VERSION = 999 where GTVPTYP_SURROGATE_ID = ?", [ participantType.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		participantType.description = u_success_description
        shouldFail( HibernateOptimisticLockingFailureException ) {
            participantType.save( failOnError: true, flush: true )
        }
    }

	void testDeleteParticipantType() {
		def participantType = newValidForCreateParticipantType()
		save participantType
		def id = participantType.id
		assertNotNull id
		participantType.delete()
		assertNull ParticipantType.get( id )
	}

    void testValidation() {
       def participantType = newInvalidForCreateParticipantType()
       assertTrue "ParticipantType could not be validated as expected due to ${participantType.errors}", participantType.validate()
    }

    void testNullValidationFailure() {
        def participantType = new ParticipantType()
        assertFalse "ParticipantType should have failed validation", participantType.validate()
        assertErrorsFor participantType, 'nullable',
                                               [
                                                 'code',
                                                 'description'
                                               ]
    }


	private def newValidForCreateParticipantType() {
		def participantType = new ParticipantType(
			code: i_success_code,
			description: i_success_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return participantType
	}

	private def newInvalidForCreateParticipantType() {
		def participantType = new ParticipantType(
			code: i_failure_code,
			description: i_failure_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return participantType
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(participanttype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
