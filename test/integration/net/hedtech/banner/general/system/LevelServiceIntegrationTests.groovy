/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

/**
 * Integration test for the level model.
 * */
class LevelServiceIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TT"
    def i_success_description = "TTTTT"
    def i_success_academicIndicator = true
    def i_success_continuingEducationIndicator = true

    def i_success_systemRequiredIndicator = true
    def i_success_voiceResponseMessageNumber = 1

    def i_success_electronicDataInterchangeEquivalent = "TT"

    def levelService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @Test
    void testFetchByCodeInListNullList() {
        assertEquals([], levelService.fetchAllByCodeInList(null))
    }

    @Test
    void testFetchByCodeInListEmptyList() {
        assertEquals([], levelService.fetchAllByCodeInList([]))
    }

    @Test
    void testFetchByCodeInList() {
        newValidForCreateLevel().save()
        List<Level> levelList = levelService.fetchAllByCodeInList([i_success_code])
        assertEquals(1, levelList.size())
        assertEquals([i_success_code], levelList.code)
    }

    private def newValidForCreateLevel() {
        def level = new Level(
                code: i_success_code,
                description: i_success_description,
                acadInd: i_success_academicIndicator,
                ceuInd: i_success_continuingEducationIndicator,
                systemReqInd: i_success_systemRequiredIndicator,
                vrMsgNo: i_success_voiceResponseMessageNumber,
                ediEquiv: i_success_electronicDataInterchangeEquivalent,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return level
    }
}
