
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
import com.sungardhe.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import grails.validation.ValidationException


class FunctionTypeIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(functiontype_domain_integration_test_data) ENABLED START*/
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
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GTVFTYP'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	//	initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(functiontype_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidFunctionType() {
		def functionType = newValidForCreateFunctionType()
		functionType.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull functionType.id
	}

	void testCreateInvalidFunctionType() {
		def functionType = newInvalidForCreateFunctionType()
		shouldFail(ValidationException) {
            functionType.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidFunctionType() {
		def functionType = newValidForCreateFunctionType()
		functionType.save( failOnError: true, flush: true )
        assertNotNull functionType.id
        assertEquals 0L, functionType.version
        assertEquals i_success_code, functionType.code
        assertEquals i_success_description, functionType.description

		//Update the entity
		functionType.description = u_success_description
		functionType.save( failOnError: true, flush: true )
		//Assert for sucessful update
        functionType = FunctionType.get( functionType.id )
        assertEquals 1L, functionType?.version
        assertEquals u_success_description, functionType.description
	}

	void testUpdateInvalidFunctionType() {
		def functionType = newValidForCreateFunctionType()
		functionType.save( failOnError: true, flush: true )
        assertNotNull functionType.id
        assertEquals 0L, functionType.version
        assertEquals i_success_code, functionType.code
        assertEquals i_success_description, functionType.description

		//Update the entity with invalid values
		functionType.description = u_failure_description
		shouldFail(ValidationException) {
            functionType.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def functionType = newValidForCreateFunctionType()
		functionType.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVFTYP set GTVFTYP_VERSION = 999 where GTVFTYP_SURROGATE_ID = ?", [ functionType.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		functionType.description = u_success_description
        shouldFail( HibernateOptimisticLockingFailureException ) {
            functionType.save( failOnError: true, flush: true )
        }
    }

	void testDeleteFunctionType() {
		def functionType = newValidForCreateFunctionType()
		functionType.save( failOnError: true, flush: true )
		def id = functionType.id
		assertNotNull id
		functionType.delete()
		assertNull FunctionType.get( id )
	}

    void testValidation() {
       def functionType = newInvalidForCreateFunctionType()
       assertFalse "FunctionType could not be validated as expected due to ${functionType.errors}", functionType.validate()
    }

    void testNullValidationFailure() {
        def functionType = new FunctionType()
        assertFalse "FunctionType should have failed validation", functionType.validate()
        assertErrorsFor functionType, 'nullable',
                                               [
                                                 'code',
                                                 'description'
                                               ]
    }




	private def newValidForCreateFunctionType() {
		def functionType = new FunctionType(
			code: i_success_code,
			description: i_success_description,
		)
		return functionType
	}

	private def newInvalidForCreateFunctionType() {
		def functionType = new FunctionType(
			code: i_failure_code,
			description: i_failure_description,
		)
		return functionType
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functiontype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
