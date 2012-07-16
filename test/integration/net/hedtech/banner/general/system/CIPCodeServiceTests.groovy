
/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

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