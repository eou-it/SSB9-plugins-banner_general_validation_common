/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system;

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration tests for <code>DisabilityAssistance</code>.
 */
class DisabilityAssistanceIntegrationTests  extends BaseIntegrationTestCase {
    
	def disabilityAssistanceService;
	
	
	protected void setUp() {
        formContext = [ 'STVMDEQ' ] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }
	
	
	void testCreate() {		
		def disabilityAssistance = new DisabilityAssistance( code: "zz", description:"unit-test", lastModified: new Date(),
		lastModifiedBy: "psykes", dataOrigin: "Banner" )
		
		if(!disabilityAssistance.save()) {
			println("DISABILITY ERRORS = " + disabilityAssistance.errors)
			fail("Could not save disabilityAssistance");
		}
		disabilityAssistance.save()
		println("DISABILITY ID = "+disabilityAssistance.id)		
	}
	

	void testUpdate() {
		
		def disabilityAssistance = new DisabilityAssistance( code: "yy", description:"unit-test", lastModified: new Date(),
		lastModifiedBy: "psykes", dataOrigin: "Banner" )
		
		if(!disabilityAssistance.save()) {
			println(disabilityAssistance.errors)
			fail("Could not save disabilityAssistance");
		}
		
		def id = disabilityAssistance.id
		println("ID FROM UPDATE TEST = "+id)		
		disabilityAssistance.description = "updated"
		
		if(!disabilityAssistance.save()) {
			println(disabilityAssistance.errors)
			fail("Could not update disabilityAssistance");
		}
		
		def found = DisabilityAssistance.get(id)
		assertNotNull("found must not be null", found)		
		assertEquals("updated", found.description)
	}
	

	void testDelete() {
		
		def disabilityAssistance = new DisabilityAssistance( code: "xx", description:"unit-test", lastModified: new Date(),
		lastModifiedBy: "psykes", dataOrigin: "Banner" )
		
		if(!disabilityAssistance.save()) {
			println(disabilityAssistance.errors)
			fail("Could not save disabilityAssistance");
		}
		
		def id = disabilityAssistance.id
		disabilityAssistance.delete()

		def found = DisabilityAssistance.get(id)		
		assertNull(found)
	}
	

	void testValidation() {
		
		def disabilityAssistance = new DisabilityAssistance()
		
		//should not pass validation since code and description are not provided
		assertFalse(disabilityAssistance.validate())
		
		disabilityAssistance.code = "AA"
		disabilityAssistance.description = "unit test"
		disabilityAssistance.lastModified = new Date()
		disabilityAssistance.lastModifiedBy = "psykes"
		disabilityAssistance.dataOrigin = "banner"
		
		//should pass this time
		assertTrue(disabilityAssistance.validate())
		
		disabilityAssistance.code = "AAA"
		
		//should fail validation since code can not be more than 2 characters
		assertFalse(disabilityAssistance.validate())		
	}
	

}
