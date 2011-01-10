
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
 * Integration test for the College model.
 **/

class CollegeServiceTests extends BaseIntegrationTestCase {

	def CollegeService


	protected void setUp() {
        formContext = ['STVDIVS'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
       super.tearDown()
    }

	void testCreateCollege() {
        def college = new College( code: "TT", description: "TT", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
          addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, statisticsCanadianInstitution: "TT",
          districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
          lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon' )
		college = CollegeService.create(college)
        assertNotNull "College ID is null in College Create Service Test", college.id
        assertNotNull "College Code is null in College Create Service Test", college.code
	}

	void testUpdateCollege() {
        def college = new College( code: "TT", description: "TT", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
          addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, statisticsCanadianInstitution: "TT",
          districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
          lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon' )
	    college = CollegeService.create(college)

        College collegeUpdate = College.findWhere(code: "TT")
        assertNotNull "College ID is null in College Update Service Test", collegeUpdate.id

        collegeUpdate.description = "ZZ"
        collegeUpdate = CollegeService.update(collegeUpdate)
        assertEquals "ZZ", collegeUpdate.description
	}

	void testDeleteCollege() {
        def college = new College( code: "TT", description: "TT", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
		    addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, statisticsCanadianInstitution: "TT",
		    districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
		    lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon' ) 
	    college = CollegeService.create(college)
        assertNotNull "College ID is null in College Delete Service Test", college.id

        College collegeDelete = College.findWhere(code: "TT")
        assertNotNull "College ID is null in College Delete Service Test", collegeDelete.id

        CollegeService.delete(collegeDelete.id)
        assertNull "College should have been deleted in College Delete Service Test", college.get(collegeDelete.id)
	}

}