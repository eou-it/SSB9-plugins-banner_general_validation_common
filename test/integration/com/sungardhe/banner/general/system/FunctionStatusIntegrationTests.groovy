/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Thu May 12 23:12:04 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class FunctionStatusIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(functionstatus_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "#"
	def i_success_description = "TTTTT"
	def i_success_activeIndicator = null
	//Invalid test data (For failure tests)

	def i_failure_code = null
	def i_failure_description = null
	def i_failure_activeIndicator = "E"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "#"
	def u_success_description = "TTTTT"
	def u_success_activeIndicator = "Y"
	//Valid test data (For failure tests)

	def u_failure_code = "1234567890"
	def u_failure_description = "1234567890123456789012345678901234567890"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GTVFSTA'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}


	protected void tearDown() {
		super.tearDown()
	}


	void testCreateValidFunctionStatus() {
		def functionStatus = newValidForCreateFunctionStatus()
		save functionStatus
		//Test if the generated entity now has an id assigned
        assertNotNull functionStatus.id
	}



	void testUpdateValidFunctionStatus() {
		def functionStatus = newValidForCreateFunctionStatus()
		save functionStatus
        assertNotNull functionStatus.id
        assertEquals 0L, functionStatus.version
        assertEquals i_success_code, functionStatus.code
        assertEquals i_success_description, functionStatus.description
        assertEquals i_success_activeIndicator, functionStatus.activeIndicator

		//Update the entity
		functionStatus.description = u_success_description
		functionStatus.activeIndicator = u_success_activeIndicator
        save functionStatus
		//Asset for sucessful update
        functionStatus = FunctionStatus.get( functionStatus.id )
        assertEquals 1L, functionStatus?.version
        assertEquals u_success_description, functionStatus.description
        assertEquals u_success_activeIndicator, functionStatus.activeIndicator
	}



    void testOptimisticLock() {
		def functionStatus = newValidForCreateFunctionStatus()
		save functionStatus

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVFSTA set GTVFSTA_VERSION = 999 where GTVFSTA_SURROGATE_ID = ?", [ functionStatus.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		functionStatus.description = u_success_description
		functionStatus.activeIndicator = u_success_activeIndicator
        shouldFail( HibernateOptimisticLockingFailureException ) {
            functionStatus.save( failOnError: true, flush: true )
        }
    }

	void testDeleteFunctionStatus() {
		def functionStatus = newValidForCreateFunctionStatus()
		save functionStatus
		def id = functionStatus.id
		assertNotNull id
		functionStatus.delete()
		assertNull FunctionStatus.get( id )
	}



    void testNullValidationFailure() {
        def functionStatus = new FunctionStatus()
        assertFalse "FunctionStatus should have failed validation", functionStatus.validate()
        assertErrorsFor functionStatus, 'nullable',
                                               [
                                                 'code',
                                                 'description'
                                               ]
        assertNoErrorsFor functionStatus,
        									   [
             									 'activeIndicator'
											   ]
    }


    void testMaxSizeValidationFailures() {
        def functionStatus = new FunctionStatus(code:u_failure_code, description: u_failure_description, activeIndicator:'XXX' )
		assertFalse "FunctionStatus should have failed validation", functionStatus.validate()
		assertErrorsFor functionStatus, 'maxSize', [ 'activeIndicator', 'code', 'description' ]
    }


	void testValidationMessages() {
	    def functionStatus = newInvalidForCreateFunctionStatus()
	    assertFalse functionStatus.validate()
	    assertLocalizedError functionStatus, 'nullable', /.*Field.*description.*of class.*FunctionStatus.*cannot be null.*/, 'description'
        assertLocalizedError functionStatus, 'nullable', /.*Field.*code.*of class.*FunctionStatus.*cannot be null.*/, 'code'
        functionStatus.code=u_failure_code
        functionStatus.description=u_failure_description
        assertFalse functionStatus.validate()
        String expectedDescriptionErrorMessage = "Field description of class com.sungardhe.banner.general.system.FunctionStatus with value 1234567890123456789012345678901234567890 exceeds the maximum size of 30"
        String expectedCodeErrorMessage = "Field code of class com.sungardhe.banner.general.system.FunctionStatus with value 1234567890 exceeds the maximum size of 1"
        assertEquals expectedCodeErrorMessage, getErrorMessage( functionStatus.errors.getFieldError( "code" ) )
        assertEquals expectedDescriptionErrorMessage, getErrorMessage( functionStatus.errors.getFieldError( "description" ) )
	}


	private def newValidForCreateFunctionStatus() {
		def functionStatus = new FunctionStatus(
			code: i_success_code,
			description: i_success_description,
			activeIndicator: i_success_activeIndicator,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return functionStatus
	}

	private def newInvalidForCreateFunctionStatus() {
		def functionStatus = new FunctionStatus(
			code: i_failure_code,
			description: i_failure_description,
			activeIndicator: i_failure_activeIndicator,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return functionStatus
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionstatus_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
