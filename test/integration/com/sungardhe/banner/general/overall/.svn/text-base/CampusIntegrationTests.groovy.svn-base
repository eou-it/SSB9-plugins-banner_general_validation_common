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
import com.sungardhe.banner.general.system.Campus

/**
 * Integration tests for the Campus model.  
 **/
class CampusIntegrationTests extends BaseIntegrationTestCase {

	def campusService
	
	protected void setUp() {
		formContext = [ 'STVCAMP' ] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}
	

	protected void tearDown() {
		super.tearDown()
	}
	

	void testCreateCampus() {
		def campus = new Campus( code: "TT", description: "TT", districtIdentifierCode: "TT" , 
		                         lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		
	    assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
        assertNotNull campus.id
	}
	

	void testUpdateCampus() {
		def campus = new Campus( code: "TT", description: "TT", districtIdentifierCode: "TT", 
		                         lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		                         
        save campus
		def id = campus.id
		def version = campus.version
		assertNotNull id
		assertEquals 0L, version

		campus.description = "updated"		
        save campus
        
		def found = Campus.get( id )	
		assertNotNull "found must not be null", found
		assertEquals "updated", found.description
		assertEquals 1L, found.version
	}


	void testDeleteCampus() {
		def campus = new Campus( code: "TT", description: "TT", districtIdentifierCode: "TT" , 
		                         lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		                         
        assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
		def id = campus.id
		assertNotNull id
		campus.delete()
		assertNull( Campus.get( id ) )
	}
	
    
}
