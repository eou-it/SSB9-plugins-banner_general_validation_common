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
import com.sungardhe.banner.general.system.TermType

/**
 * Integration test for the 'term type' model.
 **/
class TermTypeIntegrationTests extends BaseIntegrationTestCase {


	
	protected void setUp() {
        formContext = [ 'STVTERM' ] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateTermType() {
		def termType = new TermType(code: "T", description: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		
		if(!termType.save()) {
			fail("Could not save TermType; TERMTYPE ERRORS = "+ termType.errors );
		}
        assertNotNull( termType.id )
	}

	void testUpdateTermType() {
		def termType = new TermType(code: "T", description: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!termType.save(flush: true)) {
			fail("Could not save TermType; TERMTYPE ERRORS = "+ termType.errors );
		}
		def id = termType.id
		def version = termType.version
		assertNotNull(id)
		assertEquals( 0L, version)

		termType.description = "updated"
		
		if(!termType.save(flush:true)) {
			fail("Could not update TermType; TERMTYPE ERRORS = "+ termType.errors );
		}
		termType = TermType.get(id)	
		
		assertNotNull("found must not be null", termType)
		assertEquals("updated", termType.description)
		assertEquals( 1, termType.version )
	}

	void testDeleteTermType() {
		def termType = new TermType(code: "T", description: "TT" , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!termType.save(flush: true)) {
			fail("Could not save TermType; TERMTYPE ERRORS = "+ termType.errors );
		}
		def id = termType.id
		assertNotNull(id)
		termType.delete();
		def found = TermType.get(id)	
		assertNull(found)
	}

	void testValidation() {
		def termType = new TermType()
		//should not pass validation since none of the required values are provided
		assertFalse(termType.validate())

        // Setup a valid termType
        termType = new TermType(code: "T", description: "TT" , lastModified: new Date(),
		    lastModifiedBy: "test", dataOrigin: "Banner" )
		//should pass this time
		assertTrue(termType.validate())
	}
    
    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(termtype_custom_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
