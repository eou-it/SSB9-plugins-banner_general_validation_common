/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.system.Level

/**
 * Integration test for the level model.
 **/
class LevelIntegrationTests extends BaseIntegrationTestCase {

	def levelService	
	
	
	protected void setUp() {
        formContext = ['STVLEVL'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateLevel() {
		def level = new Level(code: "TT", description: "TT", acadInd: true, ceuInd: true, systemReqInd: true, ediEquiv: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		
		if(!level.save()) {
			fail("Could not save Level; LEVEL ERRORS = "+ level.errors );
		}
        assertNotNull( level.id )
	}

	void testUpdateLevel() {
		def level = new Level(code: "TT", description: "TT", acadInd: true, ceuInd: true, systemReqInd: true, ediEquiv: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!level.save(flush: true)) {
			fail("Could not save Level; LEVEL ERRORS = "+ level.errors );
		}
		def id = level.id
		def version = level.version
		assertNotNull(id)
		assertEquals( 0L, version)

		level.description = "updated"
		
		if(!level.save(flush:true)) {
			fail("Could not update Level; LEVEL ERRORS = "+ level.errors );
		}
		level = Level.get(id)	
		
		assertNotNull("found must not be null", level)
		assertEquals("updated", level.description)
		assertEquals( 1, level.version )
	}

	void testDeleteLevel() {
		def level = new Level(code: "TT", description: "TT", acadInd: true, ceuInd: true, systemReqInd: true, ediEquiv: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!level.save(flush: true)) {
			fail("Could not save Level; LEVEL ERRORS = "+ level.errors );
		}
		def id = level.id
		assertNotNull(id)
		level.delete();
		def found = Level.get(id)	
		assertNull(found)
	}

	void testValidation() {
		def level = new Level()
		//should not pass validation since none of the required values are provided
		assertFalse(level.validate())
		level.code= "TT"
		level.description= "TT"
		level.acadInd= true
		level.ceuInd= true
		level.systemReqInd= true
		
		level.ediEquiv= "TT"
		level.lastModified = new Date()
		level.lastModifiedBy = "test"
            level.dataOrigin = "banner"

		//should pass this time
		assertTrue(level.validate())
	}
}
