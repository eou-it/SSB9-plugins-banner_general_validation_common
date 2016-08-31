/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the College model.
 * */
class CollegeServiceTests extends BaseIntegrationTestCase {


    def collegeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateCollege() {
        def college = collegeService.create(newCollege("TT"))
        assertNotNull "College returned from collegeService.create has null id", college.id
        assertNotNull "College returned from collegeService.create has null code", college.code
    }


    @Test
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


    @Test
    void testDeleteCollege() {
        def college = collegeService.create(newCollege("TT"))
        assertNotNull "College returned from collegeService.create has null id", college.id

        College collegeDelete = College.findWhere(code: "TT")
        assertNotNull "College with code 'TT' could not be found", collegeDelete
        assertNotNull "College with code 'TT' has null id", collegeDelete?.id

        collegeService.delete(collegeDelete.id)
        assertNull "College ${collegeDelete.id} should have been deleted", college.get(collegeDelete.id)
    }


	// TODO - HRU-5518
    @Ignore
    @Test
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

    @Test
    void testFetchByCode() {
        College actualCollege = collegeService.create(newCollege("TT"))
        assertNotNull actualCollege
        assertNotNull actualCollege.id

        College expectCollege = collegeService.fetchByCode(actualCollege.code)
        assertNotNull expectCollege

        assertEquals actualCollege, expectCollege
    }

    @Test
    void testFetchAllWithGuidByCodeInList() {
        College actualCollege = collegeService.create(newCollege("TT"))
        assertNotNull actualCollege
        assertNotNull actualCollege.id

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.COLLEGE_LDM_NAME, actualCollege.id)
        assertNotNull globalUniqueIdentifier

        List entities = collegeService.fetchAllWithGuidByCodeInList([actualCollege.code])
        assertFalse entities.isEmpty()
        assertEquals entities.size(), 1

        Map entitiesMap = entities[0]
        assertFalse entitiesMap.isEmpty()

        assertEquals actualCollege, entitiesMap.college
        assertEquals globalUniqueIdentifier, entitiesMap.globalUniqueIdentifier
    }

    @Test
    void testFetchAllWithGuidByCodeInListWithPagination() {
        List<College> colleges = collegeService.list(params)
        assertNotNull colleges
        assertFalse colleges.isEmpty()

        College college = colleges[0]
        assertNotNull college.id

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.COLLEGE_LDM_NAME, college.id)
        assertNotNull globalUniqueIdentifier

        List entities = collegeService.fetchAllWithGuidByCodeInList(colleges.code, 1, 0)
        assertFalse entities.isEmpty()
        assertEquals entities.size(), 1

        Map entitiesMap = entities[0]
        assertFalse entitiesMap.isEmpty()

        assertEquals college, entitiesMap.college
        assertEquals globalUniqueIdentifier, entitiesMap.globalUniqueIdentifier
    }

    private College newCollege(String code) {
        new College(code: code, description: "$code description", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
                addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                statisticsCanadianInstitution: "TT", districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
                lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon')
    }

}
