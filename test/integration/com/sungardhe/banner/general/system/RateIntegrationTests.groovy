
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


class RateIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(rate_domain_integration_test_data) ENABLED START*/
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
		formContext = ['GTVRATE'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(rate_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidRate() {
		def rate = newValidForCreateRate()
		rate.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull rate.id
	}

	void testCreateInvalidRate() {
		def rate = newInvalidForCreateRate()
		shouldFail(ValidationException) {
            rate.save( failOnError: true, flush: true )		
		}
	}

	void testUpdateValidRate() {
		def rate = newValidForCreateRate()
		rate.save( failOnError: true, flush: true )
        assertNotNull rate.id
        assertEquals 0L, rate.version
        assertEquals i_success_code, rate.code
        assertEquals i_success_description, rate.description
        
		//Update the entity
		rate.description = u_success_description 
		rate.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        rate = Rate.get( rate.id )
        assertEquals 1L, rate?.version
        assertEquals u_success_description, rate.description
	}
	
	void testUpdateInvalidRate() {
		def rate = newValidForCreateRate()
		rate.save( failOnError: true, flush: true )
        assertNotNull rate.id
        assertEquals 0L, rate.version
        assertEquals i_success_code, rate.code
        assertEquals i_success_description, rate.description
        
		//Update the entity with invalid values
		rate.description = u_failure_description 
		shouldFail(ValidationException) {
            rate.save( failOnError: true, flush: true )		
		}
	}

    void testOptimisticLock() { 
		def rate = newValidForCreateRate()
		rate.save( failOnError: true, flush: true )
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVRATE set GTVRATE_VERSION = 999 where GTVRATE_SURROGATE_ID = ?", [ rate.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		rate.description = u_success_description 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            rate.save( failOnError: true, flush: true )
        }
    }
	
	void testDeleteRate() {
		def rate = newValidForCreateRate()
		rate.save( failOnError: true, flush: true )
		def id = rate.id
		assertNotNull id
		rate.delete()
		assertNull Rate.get( id )
	}
	
    void testValidation() {
       def rate = newInvalidForCreateRate()
       assertFalse "Rate could not be validated as expected due to ${rate.errors}", rate.validate()
    }

    void testNullValidationFailure() {
        def rate = new Rate()
        assertFalse "Rate should have failed validation", rate.validate()
        assertErrorsFor rate, 'nullable', 
                                               [ 
                                                 'code', 
                                                 'description'                                                 
                                               ]
    }
    
    
	void testValidationMessages() {
	    def rate = newInvalidForCreateRate()
	    rate.code = null
	    assertFalse rate.validate()
	    assertLocalizedError rate, 'nullable', /.*Field.*code.*of class.*Rate.*cannot be null.*/, 'code'
	    rate.description = null
	    assertFalse rate.validate()
	    assertLocalizedError rate, 'nullable', /.*Field.*description.*of class.*Rate.*cannot be null.*/, 'description'
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

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(rate_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
