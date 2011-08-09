
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
 Generated: Thu Aug 04 14:06:16 EDT 2011 
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException 
import groovy.sql.Sql 
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import grails.validation.ValidationException


class EmailTypeIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(emailtype_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTT"
	def i_success_description = "TTTTT"
	def i_success_displayWebIndicator = true
	def i_success_urlIndicator = true    
	//Invalid test data (For failure tests)

	def i_failure_code = null
	def i_failure_description = null
	def i_failure_displayWebIndicator = null
	def i_failure_urlIndicator = null
	
	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "TTTT"
	def u_success_description = "TTTTT"
	def u_success_displayWebIndicator = true
	def u_success_urlIndicator = true	
	//Valid test data (For failure tests)

	def u_failure_code = null
	def u_failure_description = null
	def u_failure_displayWebIndicator = null
	def u_failure_urlIndicator = null
	/*PROTECTED REGION END*/
	
	protected void setUp() {
		formContext = ['GTVEMAL'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(emailtype_domain_integration_test_data_initialization) ENABLED START*/
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

	void testCreateValidEmailType() {
		def emailType = newValidForCreateEmailType()
		emailType.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull emailType.id
	}

	void testCreateInvalidEmailType() {
		def emailType = newInvalidForCreateEmailType()
		shouldFail(ValidationException) {
            emailType.save( failOnError: true, flush: true )		
		}
	}

	void testUpdateValidEmailType() {
		def emailType = newValidForCreateEmailType()
		emailType.save( failOnError: true, flush: true )
        assertNotNull emailType.id
        assertEquals 0L, emailType.version
        assertEquals i_success_code, emailType.code
        assertEquals i_success_description, emailType.description
        assertEquals i_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals i_success_urlIndicator, emailType.urlIndicator
        
		//Update the entity
		emailType.description = u_success_description 
		emailType.displayWebIndicator = u_success_displayWebIndicator 
		emailType.urlIndicator = u_success_urlIndicator 
		emailType.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        emailType = EmailType.get( emailType.id )
        // assertEquals 1L, emailType?.version
        assertEquals u_success_description, emailType.description
        assertEquals u_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals u_success_urlIndicator, emailType.urlIndicator
	}
	
	void testUpdateInvalidEmailType() {
		def emailType = newValidForCreateEmailType()
		emailType.save( failOnError: true, flush: true )
        assertNotNull emailType.id
        assertEquals 0L, emailType.version
        assertEquals i_success_code, emailType.code
        assertEquals i_success_description, emailType.description
        assertEquals i_success_displayWebIndicator, emailType.displayWebIndicator
        assertEquals i_success_urlIndicator, emailType.urlIndicator
        
		//Update the entity with invalid values
		emailType.description = u_failure_description 
		emailType.displayWebIndicator = u_failure_displayWebIndicator 
		emailType.urlIndicator = u_failure_urlIndicator 
		shouldFail(ValidationException) {
            emailType.save( failOnError: true, flush: true )		
		}
	}
	
	void testDeleteEmailType() {
		def emailType = newValidForCreateEmailType()
		emailType.save( failOnError: true, flush: true )
		def id = emailType.id
		assertNotNull id
		emailType.delete()
		assertNull EmailType.get( id )
	}
	
    void   testValidation() {
       def emailType = newInvalidForCreateEmailType()
       assertFalse "EmailType could not be validated as expected due to ${emailType.errors}", emailType.validate()
    }

    void testNullValidationFailure() {
        def emailType = new EmailType()
        assertFalse "EmailType should have failed validation", emailType.validate()
        assertErrorsFor emailType, 'nullable', 
                                               [ 
                                                 'code', 
                                                 'displayWebIndicator', 
                                                 'urlIndicator'                                                 
                                               ]
        assertNoErrorsFor emailType,
        									   [ 
             									 'description'                                                 
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def emailType = new EmailType( 
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "EmailType should have failed validation", emailType.validate()
		assertErrorsFor emailType, 'maxSize', [ 'description' ]    
    }
    
	void testValidationMessages() {
	    def emailType = newInvalidForCreateEmailType()
	    emailType.code = null
	    assertFalse emailType.validate()
	    assertLocalizedError emailType, 'nullable', /.*Field.*code.*of class.*EmailType.*cannot be null.*/, 'code'
	    emailType.displayWebIndicator = null
	    assertFalse emailType.validate()
	    assertLocalizedError emailType, 'nullable', /.*Field.*displayWebIndicator.*of class.*EmailType.*cannot be null.*/, 'displayWebIndicator'
	    emailType.urlIndicator = null
	    assertFalse emailType.validate()
	    assertLocalizedError emailType, 'nullable', /.*Field.*urlIndicator.*of class.*EmailType.*cannot be null.*/, 'urlIndicator'
	}
  
    
	private def newValidForCreateEmailType() {
		def emailType = new EmailType(
			code: i_success_code, 
			description: i_success_description, 
			displayWebIndicator: i_success_displayWebIndicator, 
			urlIndicator: i_success_urlIndicator, 
		)
		return emailType
	}

	private def newInvalidForCreateEmailType() {
		def emailType = new EmailType(
			code: i_failure_code, 
			description: i_failure_description, 
			displayWebIndicator: i_failure_displayWebIndicator, 
			urlIndicator: i_failure_urlIndicator, 
		)
		return emailType
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(emailtype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
