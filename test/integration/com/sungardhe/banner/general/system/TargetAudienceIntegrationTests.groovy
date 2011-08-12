
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


class TargetAudienceIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(targetaudience_domain_integration_test_data) ENABLED START*/
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
		formContext = ['GTVTARG'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(targetaudience_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidTargetAudience() {
		def targetAudience = newValidForCreateTargetAudience()
		targetAudience.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull targetAudience.id
	}

	void testCreateInvalidTargetAudience() {
		def targetAudience = newInvalidForCreateTargetAudience()
		shouldFail(ValidationException) {
            targetAudience.save( failOnError: true, flush: true )		
		}
	}

	void testUpdateValidTargetAudience() {
		def targetAudience = newValidForCreateTargetAudience()
		targetAudience.save( failOnError: true, flush: true )
        assertNotNull targetAudience.id
        assertEquals 0L, targetAudience.version
        assertEquals i_success_code, targetAudience.code
        assertEquals i_success_description, targetAudience.description
        
		//Update the entity
		targetAudience.description = u_success_description 
		targetAudience.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        targetAudience = TargetAudience.get( targetAudience.id )
        assertEquals 1L, targetAudience?.version
        assertEquals u_success_description, targetAudience.description
	}
	
	void testUpdateInvalidTargetAudience() {
		def targetAudience = newValidForCreateTargetAudience()
		targetAudience.save( failOnError: true, flush: true )
        assertNotNull targetAudience.id
        assertEquals 0L, targetAudience.version
        assertEquals i_success_code, targetAudience.code
        assertEquals i_success_description, targetAudience.description
        
		//Update the entity with invalid values
		targetAudience.description = u_failure_description 
		shouldFail(ValidationException) {
            targetAudience.save( failOnError: true, flush: true )		
		}
	}

    void testOptimisticLock() { 
		def targetAudience = newValidForCreateTargetAudience()
		targetAudience.save( failOnError: true, flush: true )
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVTARG set GTVTARG_VERSION = 999 where GTVTARG_SURROGATE_ID = ?", [ targetAudience.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		targetAudience.description = u_success_description 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            targetAudience.save( failOnError: true, flush: true )
        }
    }
	
	void testDeleteTargetAudience() {
		def targetAudience = newValidForCreateTargetAudience()
		targetAudience.save( failOnError: true, flush: true )
		def id = targetAudience.id
		assertNotNull id
		targetAudience.delete()
		assertNull TargetAudience.get( id )
	}
	
    void testValidation() {
       def targetAudience = newInvalidForCreateTargetAudience()
       assertFalse "TargetAudience could not be validated as expected due to ${targetAudience.errors}", targetAudience.validate()
    }

    void testNullValidationFailure() {
        def targetAudience = new TargetAudience()
        assertFalse "TargetAudience should have failed validation", targetAudience.validate()
        assertErrorsFor targetAudience, 'nullable', 
                                               [ 
                                                 'code', 
                                                 'description'                                                 
                                               ]
    }
    
    
	void testValidationMessages() {
	    def targetAudience = newInvalidForCreateTargetAudience()
	    targetAudience.code = null
	    assertFalse targetAudience.validate()
	    assertLocalizedError targetAudience, 'nullable', /.*Field.*code.*of class.*TargetAudience.*cannot be null.*/, 'code'
	    targetAudience.description = null
	    assertFalse targetAudience.validate()
	    assertLocalizedError targetAudience, 'nullable', /.*Field.*description.*of class.*TargetAudience.*cannot be null.*/, 'description'
	}
  
    
	private def newValidForCreateTargetAudience() {
		def targetAudience = new TargetAudience(
			code: i_success_code, 
			description: i_success_description, 
		)
		return targetAudience
	}

	private def newInvalidForCreateTargetAudience() {
		def targetAudience = new TargetAudience(
			code: i_failure_code, 
			description: i_failure_description, 
		)
		return targetAudience
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(targetaudience_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
