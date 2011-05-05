/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.general.system.MedicalCondition
import com.sungardhe.banner.testing.BaseIntegrationTestCase


/**
 * Integration tests for <code>MedicalCondition</code>.
 * 
 */
class MedicalConditionIntegrationTests extends BaseIntegrationTestCase {

	def medicalConditionService


	protected void setUp() {
        formContext = ['STVMEDI'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	protected void tearDown() throws Exception {
		super.tearDown()
	}
	
	
	void testCreate() {	
		def medCond = newMedicalCondition() 
		save medCond
		assertNotNull medCond.id
	}
	
	
	void testUpdate() {
		
		def medCond = newMedicalCondition() 
		
		save medCond	
		def id = medCond.id
		
		medCond.description = "updated"
		save medCond
		
		def found = MedicalCondition.get( id )	
		assertNotNull "found must not be null", found
		assertEquals "updated", found.description
		assertEquals 1L, found.version
	}
	
	
	void testDelete() {
		
		def medCond = newMedicalCondition() 
		save medCond
		
		def id = medCond.id
		
		medCond.delete()		
		def found = MedicalCondition.get( id )		
		assertNull found 
	}
	
	
	/**
	 * Tests validation of constraints. 
	 */
	void testValidation() {
		
		def medicalCondition = new MedicalCondition()		
		assertFalse medicalCondition.validate() // missing required code and description
		
		medicalCondition.code = "AA"
		medicalCondition.description = "unit test"
		medicalCondition.lastModified = new Date()
		medicalCondition.lastModifiedBy = "horizon"				
		assertTrue medicalCondition.validate()
		
		medicalCondition.code = "AAAAAAAAAAAAAAAA"		
		assertFalse medicalCondition.validate()		
	}


    void testList() {

        def originalSize = MedicalCondition.count()
        def medCond = newMedicalCondition()
		save medCond
        assertTrue( MedicalCondition.count() == originalSize + 1 )
    }


    private def newMedicalCondition() {
        new MedicalCondition( code: "zz", description:"unit-test", 
		                      lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner" )
    }
  
}
