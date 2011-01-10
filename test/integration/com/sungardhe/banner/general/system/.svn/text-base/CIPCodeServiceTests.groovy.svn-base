
/*******************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/

package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the CIP code model.
 **/

class CIPCodeServiceTests extends BaseIntegrationTestCase {

	def CIPCodeService


	protected void setUp() {
        formContext = ['STVCIPC'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
       super.tearDown()
    }

	void testCreateCIPCode() {
		def cipCode = new CIPCode( code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT" ,
		                           lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
      
		cipCode = CIPCodeService.create(cipCode)
        assertNotNull "CIP Code ID is null in CIP Code Create Service Test", cipCode.id
        assertNotNull "CIP Code Code is null in CIP Code Create Service Test", cipCode.code
	}
 
	void testUpdateCIPCode() {
		def cipCode = new CIPCode( code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT" ,
		                           lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
	    cipCode = CIPCodeService.create(cipCode)

        CIPCode cipCodeUpdate = CIPCode.findWhere(code: "TT")
        assertNotNull "CIP Code ID is null in CIP Code Update Service Test", cipCodeUpdate.id

        cipCodeUpdate.description = "ZZ"
        cipCodeUpdate = CIPCodeService.update(cipCodeUpdate)
        assertEquals "ZZ", cipCodeUpdate.description
	}


	void testDeleteCIPCode() {
		def cipCode = new CIPCode( code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT" ,
		                           lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
        cipCode = CIPCodeService.create(cipCode)
        assertNotNull "CIP Code ID is null in CIP Code Delete Service Test", cipCode.id

        CIPCode cipCodeDelete = CIPCode.findWhere(code: "TT")
        CIPCodeService.delete(cipCodeDelete.id)

        assertNull "CIP Code should have been deleted in CIP Code Delete Service Test", cipCode.get(cipCodeDelete.id)

	}

}