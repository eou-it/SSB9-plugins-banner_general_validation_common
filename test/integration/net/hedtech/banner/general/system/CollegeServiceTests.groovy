/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the College model.
 * */
class CollegeServiceTests extends BaseIntegrationTestCase {


    def collegeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateCollege() {
        def college = collegeService.create(newCollege("TT"))
        assertNotNull "College returned from collegeService.create has null id", college.id
        assertNotNull "College returned from collegeService.create has null code", college.code
    }


    void testUpdateCollege() {
        def college = collegeService.create(newCollege("TT"))
        assertNotNull college.id
        def collegeUpdate = College.findWhere(code: "TT")
        assertNotNull "College with code 'TT' could not be found", collegeUpdate
        assertNotNull "College with code 'TT' has null id", collegeUpdate.id

        collegeUpdate.description = "ZZ"
        collegeUpdate = collegeService.update(collegeUpdate)
        assertEquals "ZZ", collegeUpdate.description
    }


    void testDeleteCollege() {
        def college = collegeService.create(newCollege("TT"))
        assertNotNull "College returned from collegeService.create has null id", college.id

        College collegeDelete = College.findWhere(code: "TT")
        assertNotNull "College with code 'TT' could not be found", collegeDelete
        assertNotNull "College with code 'TT' has null id", collegeDelete?.id

        collegeService.delete(collegeDelete.id)
        assertNull "College ${collegeDelete.id} should have been deleted", college.get(collegeDelete.id)
    }


    void testSaveInvalid() {
        try {
            // note: this will be invalid for both code and description, resulting in two errors
            collegeService.create(newCollege('TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT'))
            fail("Was able to save an invalid college!")
        } catch (ApplicationException e) {
            def returnMap = e.returnMap(message)
            assertTrue "Return map not as expected but was: ${returnMap.message}",
                    returnMap.message ==~ /.*The College cannot be saved, as it contains errors.*/
            assertFalse returnMap.success

            // note that validation exceptions, unlike most others, may hold many localized error messages that may be presented to a user
            assertEquals 2L, returnMap.errors?.size()
            if (returnMap.errors instanceof List) {
                assertFieldErrorContent(returnMap.errors, [fieldName: "code", modelName: "net.hedtech.banner.general.system.College",
                        exactMessage: "The college code is too long, it must be no more than 2 characters",
                        rejectedValue: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"])

                assertFieldErrorContent(returnMap.errors, [fieldName: "description", modelName: "net.hedtech.banner.general.system.College",
                        partialMessage: "exceeds the maximum size of",
                        rejectedValue: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT description"])
            }
            assertNotNull returnMap.underlyingErrorMessage // this is the underlying exception's message (which is not appropriate to display to a user)
        }
    }


    private College newCollege(String code) {
        new College(code: code, description: "$code description", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
                addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                statisticsCanadianInstitution: "TT", districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
                lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon')
    }

}
