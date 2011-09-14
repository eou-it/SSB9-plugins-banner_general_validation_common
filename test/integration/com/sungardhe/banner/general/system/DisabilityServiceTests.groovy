
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

package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Disability model.
 **/

class DisabilityServiceTests extends BaseIntegrationTestCase {

	def DisabilityService


	protected void setUp() {
        formContext = ['STVDISA'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
       super.tearDown()
    }

	void testCreateDisability() {
        def disability = new Disability(code: "TT", description: "TT" , lastModified: new Date(),
		   lastModifiedBy: "test", dataOrigin: "Banner" )
		disability = DisabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Create Service Test", disability.id
        assertNotNull "Disability Code is null in Disability Create Service Test", disability.code
	}

	void testUpdateDisability() {
        def disability = new Disability(code: "TT", description: "TT" , lastModified: new Date(),
		   lastModifiedBy: "test", dataOrigin: "Banner" )
	    disability = DisabilityService.create(disability)

        Disability disabilityUpdate = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Update Service Test", disabilityUpdate.id

        disabilityUpdate.description = "ZZ"
        disabilityUpdate = DisabilityService.update(disabilityUpdate)
        assertEquals "ZZ", disabilityUpdate.description
	}

	void testDeleteDisability() {
        def disability = new Disability(code: "TT", description: "TT" , lastModified: new Date(),
		   lastModifiedBy: "test", dataOrigin: "Banner" )
	    disability = DisabilityService.create(disability)
        assertNotNull "Disability ID is null in Disability Delete Service Test", disability.id

        Disability disabilityDelete = Disability.findWhere(code: "TT")
        assertNotNull "Disability ID is null in Disability Delete Service Test", disabilityDelete.id

        DisabilityService.delete(disabilityDelete.id)
        assertNull "Disability should have been deleted in Disability Delete Service Test", disability.get(disabilityDelete.id)
	}

}