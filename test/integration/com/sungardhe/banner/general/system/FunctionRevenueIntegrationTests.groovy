
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
 Generated: Mon Aug 01 12:03:48 IST 2011 
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException 
import groovy.sql.Sql 
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import grails.validation.ValidationException


class FunctionRevenueIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(functionrevenue_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TT"
	def i_success_description = "i_success_description"
	//Invalid test data (For failure tests)

	def i_failure_code = "TT"
	def i_failure_description = "i_failure_descriptioni_failure_descriptioni_failure_description"
	
	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "TT"
	def u_success_description = "u_success_description"
	//Valid test data (For failure tests)

	def u_failure_code = "TT"
	def u_failure_description = null
	/*PROTECTED REGION END*/
	
	protected void setUp() {
		formContext = ['GTVREVN'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(functionrevenue_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidFunctionRevenue() {
		def functionRevenue = newValidForCreateFunctionRevenue()
		functionRevenue.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull functionRevenue.id
	}

	void testCreateInvalidFunctionRevenue() {
		def functionRevenue = newInvalidForCreateFunctionRevenue()
		shouldFail(ValidationException) {
            functionRevenue.save( failOnError: true, flush: true )		
		}
	}

	void testUpdateValidFunctionRevenue() {
		def functionRevenue = newValidForCreateFunctionRevenue()
		functionRevenue.save( failOnError: true, flush: true )
        assertNotNull functionRevenue.id
        assertEquals 0L, functionRevenue.version
        assertEquals i_success_code, functionRevenue.code
        assertEquals i_success_description, functionRevenue.description
        
		//Update the entity
		functionRevenue.description = u_success_description 
		functionRevenue.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        functionRevenue = FunctionRevenue.get( functionRevenue.id )
        assertEquals 1L, functionRevenue?.version
        assertEquals u_success_description, functionRevenue.description
	}
	
	void testUpdateInvalidFunctionRevenue() {
		def functionRevenue = newValidForCreateFunctionRevenue()
		functionRevenue.save( failOnError: true, flush: true )
        assertNotNull functionRevenue.id
        assertEquals 0L, functionRevenue.version
        assertEquals i_success_code, functionRevenue.code
        assertEquals i_success_description, functionRevenue.description
        
		//Update the entity with invalid values
		functionRevenue.description = u_failure_description 
		shouldFail(ValidationException) {
            functionRevenue.save( failOnError: true, flush: true )		
		}
	}

    void testOptimisticLock() { 
		def functionRevenue = newValidForCreateFunctionRevenue()
		functionRevenue.save( failOnError: true, flush: true )
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVREVN set GTVREVN_VERSION = 999 where GTVREVN_SURROGATE_ID = ?", [ functionRevenue.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		functionRevenue.description = u_success_description 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            functionRevenue.save( failOnError: true, flush: true )
        }
    }
	
	void testDeleteFunctionRevenue() {
		def functionRevenue = newValidForCreateFunctionRevenue()
		functionRevenue.save( failOnError: true, flush: true )
		def id = functionRevenue.id
		assertNotNull id
		functionRevenue.delete()
		assertNull FunctionRevenue.get( id )
	}
	
    void testValidation() {
       def functionRevenue = newInvalidForCreateFunctionRevenue()
       assertFalse "FunctionRevenue could not be validated as expected due to ${functionRevenue.errors}", functionRevenue.validate()
    }

    void testNullValidationFailure() {
        def functionRevenue = new FunctionRevenue()
        assertFalse "FunctionRevenue should have failed validation", functionRevenue.validate()
        assertErrorsFor functionRevenue, 'nullable', 
                                               [ 
                                                 'code', 
                                                 'description'                                                 
                                               ]
    }

  
    
	private def newValidForCreateFunctionRevenue() {
		def functionRevenue = new FunctionRevenue(
			code: i_success_code, 
			description: i_success_description, 
		)
		return functionRevenue
	}

	private def newInvalidForCreateFunctionRevenue() {
		def functionRevenue = new FunctionRevenue(
			code: i_failure_code, 
			description: i_failure_description, 
		)
		return functionRevenue
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionrevenue_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
