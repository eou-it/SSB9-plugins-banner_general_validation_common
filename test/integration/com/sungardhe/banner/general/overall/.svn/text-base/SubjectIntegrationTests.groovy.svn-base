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
import com.sungardhe.banner.general.system.Subject

/**
 * Integration test for the 'subject' model.
 **/
class SubjectIntegrationTests extends BaseIntegrationTestCase {

	
	protected void setUp() {
        formContext = [ 'STVSUBJ' ] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateSubject() {
		def subject = new Subject(code: "TT", description: "TT", vrMsgNo: 1, dispWebInd: true , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		
		if(!subject.save()) {
			fail("Could not save Subject; SUBJECT ERRORS = "+ subject.errors );
		}
        assertNotNull( subject.id )
	}

	void testUpdateSubject() {
		def subject = new Subject(code: "TT", description: "TT", vrMsgNo: 1, dispWebInd: true , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!subject.save(flush: true)) {
			fail("Could not save Subject; SUBJECT ERRORS = "+ subject.errors );
		}
		def id = subject.id
		def version = subject.version
		assertNotNull(id)
		assertEquals( 0L, version)

		subject.description = "updated"
		
		if(!subject.save(flush:true)) {
			fail("Could not update Subject; SUBJECT ERRORS = "+ subject.errors );
		}
		subject = Subject.get(id)	
		
		assertNotNull("found must not be null", subject)
		assertEquals("updated", subject.description)
		assertEquals( 1, subject.version )
	}

	void testDeleteSubject() {
		def subject = new Subject(code: "TT", description: "TT", vrMsgNo: 1, dispWebInd: true , lastModified: new Date(),
		lastModifiedBy: "test", dataOrigin: "Banner" )
		if(!subject.save(flush: true)) {
			fail("Could not save Subject; SUBJECT ERRORS = "+ subject.errors );
		}
		def id = subject.id
		assertNotNull(id)
		subject.delete();
		def found = Subject.get(id)	
		assertNull(found)
	}

	void testValidation() {
		def subject = new Subject()
		//should not pass validation since none of the required values are provided
		assertFalse(subject.validate())

        // Setup the valid subject
        subject = new Subject(code: "TT", description: "TT", vrMsgNo: 1, dispWebInd: true , lastModified: new Date(),
		    lastModifiedBy: "test", dataOrigin: "Banner" )
        
		//should pass this time
		assertTrue(subject.validate())
	}

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(subject_custom_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
