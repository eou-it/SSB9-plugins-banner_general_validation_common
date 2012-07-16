
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
 Banner Automator Version: 1.26
 Generated: Thu Aug 11 12:05:30 EDT 2011
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import java.text.SimpleDateFormat
import grails.validation.ValidationException


class InitialsIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(initials_domain_integration_test_data) ENABLED START*/
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
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['STVINIT'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(initials_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidInitials() {
		def initials = newValidForCreateInitials()
		initials.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull initials.id
	}

	void testCreateInvalidInitials() {
		def initials = newInvalidForCreateInitials()
        initials.code = null
		shouldFail(ValidationException) {
            initials.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidInitials() {
		def initials = newValidForCreateInitials()
		initials.save( failOnError: true, flush: true )
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
		initials.save( failOnError: true, flush: true )
		//Assert for sucessful update
        initials = Initials.get( initials.id )
        assertEquals 1L, initials?.version
        assertEquals u_success_description, initials.description
        assertEquals u_success_title1, initials.title1
        assertEquals u_success_title2, initials.title2
        assertEquals u_success_emailAddress, initials.emailAddress
	}

	void testUpdateInvalidInitials() {
		def initials = newValidForCreateInitials()
		initials.save( failOnError: true, flush: true )
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
            initials.save( failOnError: true, flush: true )
		}
	}

    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
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

    void testOptimisticLock() {
		def initials = newValidForCreateInitials()
		initials.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVINIT set STVINIT_VERSION = 999 where STVINIT_SURROGATE_ID = ?", [ initials.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		initials.description = u_success_description
		initials.title1 = u_success_title1
		initials.title2 = u_success_title2
		initials.emailAddress = u_success_emailAddress
        shouldFail( HibernateOptimisticLockingFailureException ) {
            initials.save( failOnError: true, flush: true )
        }
    }

	void testDeleteInitials() {
		def initials = newValidForCreateInitials()
		initials.save( failOnError: true, flush: true )
		def id = initials.id
		assertNotNull id
		initials.delete()
		assertNull Initials.get( id )
	}

    void testValidation() {
       def initials = newInvalidForCreateInitials()
       initials.code = null
       assertFalse "Initials could not be validated as expected due to ${initials.errors}", initials.validate()
    }

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

    void testMaxSizeValidationFailures() {
        def initials = new Initials(
        title2:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        emailAddress:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "Initials should have failed validation", initials.validate()
		assertErrorsFor initials, 'maxSize', [ 'title2', 'emailAddress' ]
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

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(initials_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
