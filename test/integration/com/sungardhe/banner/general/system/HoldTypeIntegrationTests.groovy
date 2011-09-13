
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


class HoldTypeIntegrationTests extends BaseIntegrationTestCase {

	def holdTypeService
	
	protected void setUp() {
		formContext = ['STVHLDD'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateHoldType() {
		def holdType = newHoldType()
		save holdType
		//Test if the generated entity now has an id assigned		
        assertNotNull holdType.id
	}

	void testUpdateHoldType() {
		def holdType = newHoldType()
		save holdType
       
        assertNotNull holdType.id
        assertEquals 0L, holdType.version
        assertEquals "TT", holdType.code
        assertEquals "Y", holdType.registrationHoldIndicator
        assertEquals "Y", holdType.transactionHoldIndicator
        assertEquals "Y", holdType.gradHoldIndicator
        assertEquals "Y", holdType.gradeHoldIndicator
        assertEquals "TTTTT", holdType.description
        assertEquals "Y", holdType.accountsReceivableHoldIndicator
        assertEquals "Y", holdType.enrollmentVerificationHoldIndicator
        assertEquals 1, holdType.voiceResponseMessageNumber
        assertEquals true, holdType.displayWebIndicator
        assertEquals true, holdType.applicationHoldIndicator
        assertEquals true, holdType.complianceHoldIndicator
        
		//Update the entity
		def testDate = new Date()
		holdType.code = "UU"
		holdType.registrationHoldIndicator = "Y"
		holdType.transactionHoldIndicator = "Y"
		holdType.gradHoldIndicator = "Y"
		holdType.gradeHoldIndicator = "Y"
		holdType.description = "UUUUU"
		holdType.accountsReceivableHoldIndicator = "Y"
		holdType.enrollmentVerificationHoldIndicator = "Y"
		holdType.voiceResponseMessageNumber = 0
		holdType.displayWebIndicator = true
		holdType.applicationHoldIndicator = true
		holdType.complianceHoldIndicator = true
		holdType.lastModified = testDate
		holdType.lastModifiedBy = "test"
		holdType.dataOrigin = "Banner" 
        save holdType
        
        holdType = HoldType.get( holdType.id )
        assertEquals 1L, holdType?.version
        assertEquals "UU", holdType.code
        assertEquals "Y", holdType.registrationHoldIndicator
        assertEquals "Y", holdType.transactionHoldIndicator
        assertEquals "Y", holdType.gradHoldIndicator
        assertEquals "Y", holdType.gradeHoldIndicator
        assertEquals "UUUUU", holdType.description
        assertEquals "Y", holdType.accountsReceivableHoldIndicator
        assertEquals "Y", holdType.enrollmentVerificationHoldIndicator
        assertEquals 0, holdType.voiceResponseMessageNumber
        assertEquals true, holdType.displayWebIndicator
        assertEquals true, holdType.applicationHoldIndicator
        assertEquals true, holdType.complianceHoldIndicator
	}

    void testOptimisticLock() { 
		def holdType = newHoldType()
		save holdType
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVHLDD set STVHLDD_VERSION = 999 where STVHLDD_SURROGATE_ID = ?", [ holdType.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		holdType.code="UU"
		holdType.registrationHoldIndicator="Y"
	/*	holdType.transactionHoldIndicator="Y"
		holdType.gradHoldIndicator="Y"
		holdType.gradeHoldIndicator="Y"
		holdType.description="UUUUU"
		holdType.accountsReceivableHoldIndicator="Y"
		holdType.enrollmentVerificationHoldIndicator="Y"
		holdType.voiceResponseMessageNumber= 0
		holdType.displayWebIndicator=true
		holdType.applicationHoldIndicator=true
		holdType.complianceHoldIndicator=true
		holdType.lastModified= new Date()
		holdType.lastModifiedBy="test"
		holdType.dataOrigin= "Banner"   */
        shouldFail( HibernateOptimisticLockingFailureException ) {
            holdType.save( flush: true )
        }
    }
	
	void testDeleteHoldType() {
		def holdType = newHoldType()
		save holdType
		def id = holdType.id
		assertNotNull id
		holdType.delete()
		assertNull HoldType.get( id )
	}
	
    void testValidation() {
       def holdType = newHoldType()
       assertTrue "HoldType could not be validated as expected due to ${holdType.errors}", holdType.validate()
    }

    void testNullValidationFailure() {
        def holdType = new HoldType()
        assertFalse "HoldType should have failed validation", holdType.validate()
        assertErrorsFor holdType, 'nullable', 
                                               [ 
                                                 'code'
                                               ]
        assertNoErrorsFor holdType,
        									   [ 
             									 'registrationHoldIndicator', 
             									 'transactionHoldIndicator', 
             									 'gradHoldIndicator', 
             									 'gradeHoldIndicator', 
             									 'description', 
             									 'accountsreceivablerHoldIndicator',
             									 'enrollmentVerificationHoldIndicator', 
             									 'voiceResponseMessageNumber',
                                                 'displayWebIndicator',
                                                 'applicationHoldIndicator',
                                                 'complianceHoldIndicator'                                                
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def holdType = new HoldType( 
        registrationHoldIndicator:'XXX',
        transactionHoldIndicator:'XXX',
        gradHoldIndicator:'XXX',
        gradeHoldIndicator:'XXX',
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        accountsReceivableHoldIndicator:'XXX',
        enrollmentVerificationHoldIndicator:'XXX' )
		assertFalse "HoldType should have failed validation", holdType.validate()
		assertErrorsFor holdType, 'maxSize', [ 'registrationHoldIndicator', 'transactionHoldIndicator', 'gradHoldIndicator', 'gradeHoldIndicator', 'description', 'accountsReceivableHoldIndicator', 'enrollmentVerificationHoldIndicator' ]    
    }
  
    
  private def newHoldType() {
     
    def holdType = new HoldType(
    		code: "TT", 
    		registrationHoldIndicator: "Y", 
    		transactionHoldIndicator: "Y", 
    		gradHoldIndicator: "Y", 
    		gradeHoldIndicator: "Y", 
    		description: "TTTTT",
    		accountsReceivableHoldIndicator: "Y", 
    		enrollmentVerificationHoldIndicator: "Y", 
    		voiceResponseMessageNumber: 1,
    		displayWebIndicator: true,
    		applicationHoldIndicator: true,
    		complianceHoldIndicator: true,
            lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner"
        )
        return holdType
    }

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(holdtype_custom_integration_test_methods) ENABLED START*/

    /**
     * A test to exercise 'code' primary key in HoldType
     */
    void testPrimaryKeyOnCode() {
        def holdType = newHoldType()
        def duplicateObj = newHoldType()

        save holdType
        assertNotNull holdType.id

        assertEquals holdType.code, duplicateObj.code

        shouldFail() {
            save duplicateObj
        }
    }

    /**
     * A test to exercise the findBy method for HoldType
     */
    void testFindHoldType() {
        def holdType = newHoldType()
        save holdType

		def holdType2 = HoldType.findByCode("TT")
        assertNotNull holdType2
        assertEquals holdType2.code, "TT"

    }

    /*PROTECTED REGION END*/
}
