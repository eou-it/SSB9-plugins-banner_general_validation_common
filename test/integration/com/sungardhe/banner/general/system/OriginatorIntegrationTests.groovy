
/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
 
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase 
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


class OriginatorIntegrationTests extends BaseIntegrationTestCase {

	def originatorService
	
	protected void setUp() {
		formContext = ['STVORIG'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateOriginator() {
		def originator = newOriginator()
		save originator
		//Test if the generated entity now has an id assigned		
        assertNotNull originator.id
	}

	void testUpdateOriginator() {
		def originator = newOriginator()
		save originator
       
        assertNotNull originator.id
        assertEquals 0L, originator.version
        assertEquals "TTTT", originator.code
        assertEquals "TTTTT", originator.description
        
		//Update the entity
		def testDate = new Date()
		originator.code = "UUUU"
		originator.description = "UUUUU"
		originator.lastModified = testDate
		originator.lastModifiedBy = "test"
		originator.dataOrigin = "Banner" 
        save originator
        
        originator = Originator.get( originator.id )
        assertEquals 1L, originator?.version
        assertEquals "UUUU", originator.code
        assertEquals "UUUUU", originator.description
	}

    void testOptimisticLock() { 
		def originator = newOriginator()
		save originator
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVORIG set STVORIG_VERSION = 999 where STVORIG_SURROGATE_ID = ?", [ originator.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		originator.code="UUUU"
		originator.description="UUUUU"
		originator.lastModified= new Date()
		originator.lastModifiedBy="test"
		originator.dataOrigin= "Banner" 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            originator.save( flush: true )
        }
    }
	
	void testDeleteOriginator() {
		def originator = newOriginator()
		save originator
		def id = originator.id
		assertNotNull id
		originator.delete()
		assertNull Originator.get( id )
	}
	
    void testValidation() {
       def originator = newOriginator()
       assertTrue "Originator could not be validated as expected due to ${originator.errors}", originator.validate()
    }

    void testNullValidationFailure() {
        def originator = new Originator()
        assertFalse "Originator should have failed validation", originator.validate()
        assertErrorsFor originator, 'nullable', 
                                               [ 
                                                 'code'                                                 
                                               ]
        assertNoErrorsFor originator,
        									   [ 
             									 'description'                                                 
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def originator = new Originator( 
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "Originator should have failed validation", originator.validate()
		assertErrorsFor originator, 'maxSize', [ 'description' ]    
    }
    
	void testValidationMessages() {
	    def originator = newOriginator()
	    originator.code = null
	    assertFalse originator.validate()
	    assertLocalizedError originator, 'nullable', /.*Field.*code.*of class.*Originator.*cannot be null.*/, 'code'
	}
  
    
  private def newOriginator() {
     
    def originator = new Originator(
    		code: "TTTT", 
    		description: "TTTTT", 
            lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner"
        )
        return originator
    }

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(originator_custom_integration_test_methods) ENABLED START*/

    /**
     * A test to exercise 'code' primary key in HoldType
     */
    void testPrimaryKeyOnCode() {
        def originator = newOriginator()
        def duplicateObj = newOriginator()

        save originator
        assertNotNull originator.id

        assertEquals originator.code, duplicateObj.code

        shouldFail() {
            save duplicateObj
        }
    }

    /**
     * A test to exercise the findBy method for Originator
     */
    void testFindOriginator() {
        def originator = newOriginator()
        save originator
        
		def originator2 = Originator.findByCode("TTTT")
        assertNotNull originator2
        assertEquals originator2.code, "TTTT"
    }

    /*PROTECTED REGION END*/
}
