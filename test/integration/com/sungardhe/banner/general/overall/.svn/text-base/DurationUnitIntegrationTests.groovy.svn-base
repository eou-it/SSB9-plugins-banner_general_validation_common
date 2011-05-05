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

/**
 * Integration test for the duration unit code model.
 **/
class DurationUnitIntegrationTests extends BaseIntegrationTestCase {

	def durationUnitCodeService
	
	
	protected void setUp() {
        formContext = ['SPAIDEN'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }
    
    
	void testCreateDurationUnitCode() {
		def durationUnitCode = new DurationUnit( code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1 ,
		                                             lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		
        save durationUnitCode
        assertNotNull( durationUnitCode.id )
	}
	

	void testUpdateDurationUnitCode() {
		def durationUnitCode = new DurationUnit( code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1 ,
		                                             lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		                                             
        save durationUnitCode
		def id = durationUnitCode.id
		assertNotNull id
		assertEquals 0L, durationUnitCode.version

		durationUnitCode.description = "updated"
        save durationUnitCode
        
		durationUnitCode = DurationUnit.get( id )
		assertNotNull "found must not be null", durationUnitCode
		assertEquals "updated", durationUnitCode.description
		assertEquals 1, durationUnitCode.version
	}
	

	void testDeleteDurationUnitCode() {
		def durationUnitCode = new DurationUnit( code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1 ,
		                                             lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		                                             
        save durationUnitCode
		def id = durationUnitCode.id
		assertNotNull id
		
		durationUnitCode.delete();
		assertNull DurationUnit.get(id)
	}
	

	void testValidation() {
		def durationUnitCode = new DurationUnit()
		//should not pass validation since none of the required values are provided
		assertFalse( durationUnitCode.validate() )

        durationUnitCode = new DurationUnit( code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1 ,
                                                 lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		//should pass this time
		assertTrue durationUnitCode.validate()
	}
	
}
