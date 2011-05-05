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
import com.sungardhe.banner.general.system.AcademicYear

/**
 * Integration test for academic year model.
 **/
class AcademicYearIntegrationTests extends BaseIntegrationTestCase {

	def academicYearService
	
	
    protected void setUp() {
        formContext = ['STVACYR'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }
    

	void testCreateAcademicYear() {
		def academicYear = new AcademicYear( code: "TT", description: "TT", sysreqInd: true , lastModified: new Date(),
		                                     lastModifiedBy: "test", dataOrigin: "Banner" )
		
		save academicYear
        assertNotNull academicYear.id
	}
	

	void testUpdateAcademicYear() {
		def academicYear = new AcademicYear( code: "TT", description: "TT", sysreqInd: true , lastModified: new Date(),
		                                     lastModifiedBy: "test", dataOrigin: "Banner" )
		save academicYear
		
		def id = academicYear.id
		def version = academicYear.version
		assertNotNull id
		assertEquals 0L, version
		
		academicYear.description = "updated"
		save academicYear

		academicYear = AcademicYear.get( id )			
		assertNotNull "found must not be null", academicYear
		assertEquals "updated", academicYear.description
		assertEquals 1L, academicYear.version
	}
	

	void testDeleteAcademicYear() {
		def academicYear = new AcademicYear( code: "TT", description: "TT", sysreqInd: true , lastModified: new Date(),
		                                     lastModifiedBy: "test", dataOrigin: "Banner" )
		save academicYear
		
		def id = academicYear.id
		assertNotNull id
		academicYear.delete()
		assertNull AcademicYear.get( id )
	}
	

	void testValidation() {
		def academicYear = new AcademicYear()
		//should not pass validation since none of the required values are provided
		assertFalse academicYear.validate()
		
		academicYear.code = "TT"
		academicYear.description = "TT"
		academicYear.sysreqInd = true
		academicYear.lastModified = new Date()
		academicYear.lastModifiedBy = "test"
        academicYear.dataOrigin = "banner"
		
		assertTrue academicYear.validate()
	}
    
}
