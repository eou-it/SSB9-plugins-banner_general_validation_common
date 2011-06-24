
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
 Generated: Fri Feb 11 16:39:35 EST 2011 
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql 
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


class CountyIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(county_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "270"
	def i_success_description = "Otsego County"

	//Invalid test data (For failure tests)
    def i_failure_code = "INVALID"
	def i_failure_description = "Albany County"
	
	//Test data for creating updating domain instance
	//Valid test data (For success tests)
    def u_success_code = "270"
	def u_success_description = "Otsego"

	//Valid test data (For failure tests)
    def u_failure_code = "270"
	def u_failure_description = "Otsego County Description Too Long for Field"
	/*PROTECTED REGION END*/
	
	protected void setUp() {
		formContext = ['STVCNTY'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(county_domain_integration_test_data_initialization) ENABLED START*/
		/*PROTECTED REGION END*/
	}
	
	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidCounty() {
		def county = newValidForCreateCounty()
		county.save(failOnError: true, flush: true)
		//Test if the generated entity now has an id assigned		
        assertNotNull county.id
	}

	void testCreateInvalidCounty() {
		def county = newInvalidForCreateCounty()
		shouldFail {
            county.save(failOnError: true, flush: true)
        }
	}

	void testUpdateValidCounty() {
		def county = newValidForCreateCounty()
		county.save(failOnError: true, flush: true)
        assertNotNull county.id
        assertEquals 0L, county.version
        assertEquals i_success_code, county.code
        assertEquals i_success_description, county.description
        
		//Update the entity
		county.description = u_success_description 
        county.save(failOnError: true, flush: true)
		//Assert for successful update
        county = County.get( county.id )
        assertEquals 1L, county?.version
        assertEquals u_success_description, county.description
	}
	
	void testUpdateInvalidCounty() {
		def county = newValidForCreateCounty()
		county.save(failOnError: true, flush: true)
        assertNotNull county.id
        assertEquals 0L, county.version
        assertEquals i_success_code, county.code
        assertEquals i_success_description, county.description
        
		//Update the entity with invalid values
		county.description = u_failure_description 
		shouldFail {
            county.save(failOnError: true, flush: true)
        }
	}

    void testOptimisticLock() { 
		def county = newValidForCreateCounty()
		county.save(failOnError: true, flush: true)
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVCNTY set STVCNTY_VERSION = 999 where STVCNTY_SURROGATE_ID = ?", [ county.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		county.description = u_success_description 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            county.save( failOnError: true, flush: true )
        }
    }
	
	void testDeleteCounty() {
		def county = newValidForCreateCounty()
		county.save(failOnError: true, flush: true)
		def id = county.id
		assertNotNull id
		county.delete()
		assertNull County.get( id )
	}
	
    void testValidation() {
        def county = newValidForCreateCounty()
        assertTrue "County could not be validated as expected due to ${county.errors}", county.validate()
    }

    void testNullValidationFailure() {
        def county = new County()
        assertFalse "County should have failed validation", county.validate()
        assertErrorsFor county, 'nullable', 
                                               [ 
                                                 'code'                                                 
                                               ]
        assertNoErrorsFor county,
        									   [ 
             									 'description'                                                 
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def county = new County( 
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "County should have failed validation", county.validate()
		assertErrorsFor county, 'maxSize', [ 'description' ]    
    }
    
	void testValidationMessages() {
	    def county = newInvalidForCreateCounty()
	    county.code = null
	    assertFalse county.validate()
	    assertLocalizedError county, 'nullable', /.*Field.*code.*of class.*County.*cannot be null.*/, 'code'
	}
  
    
	private def newValidForCreateCounty() {
		def county = new County(
			code: i_success_code, 
			description: i_success_description, 
        	lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner"
	    )
		return county
	}

	private def newInvalidForCreateCounty() {
		def county = new County(
			code: i_failure_code, 
			description: i_failure_description, 
        	lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner"
		)
		return county
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(county_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
