/** *****************************************************************************
 © 2014 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * AcademicLevelCompositeServiceIntegrationTests.
 *
 * Date: 7/24/14
 * Time: 4:33 PM
 */
class AcademicLevelCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicLevelCompositeService

    Level i_success_level

    def i_success_code = "TZ"
    def i_success_description = "Test Description"
    def i_success_academicIndicator = true
    def i_success_continuingEducationIndicator = true

    def i_success_systemRequiredIndicator = true
    def i_success_voiceResponseMessageNumber = 1

    def i_success_electronicDataInterchangeEquivalent = "TT"


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        i_success_level = Level.findByCode('LW')
    }


    void testListWithoutPaginationParams() {
        List academicLevels = academicLevelCompositeService.list([:])
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()
        assertTrue academicLevels.code.contains(i_success_level.code)
    }


    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List academicLevels = academicLevelCompositeService.list(paginationParams)
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()
        assertTrue academicLevels.code.contains(i_success_level.code)
    }


    void testCount() {
        assertNotNull i_success_level
        assertTrue academicLevelCompositeService.count() > 0
    }


    void testGetInvalidGuid() {
        try {
            academicLevelCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }



    void testGetInvalidNonExistentAcademicLevel() {
        Level level = newValidForCreateLevel()
        save level
        assertNotNull level.id
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByAcademicLevelId(level.id)
        assertNotNull academicLevel
        assertNotNull academicLevel.guid
        assertEquals academicLevel.id, level.id

        level.delete(flush: true)
        assertNull level.get(academicLevel.id)

        try {
            academicLevelCompositeService.get(academicLevel.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List academicLevels = academicLevelCompositeService.list(paginationParams)
        assertNotNull academicLevels
        assertFalse academicLevels.isEmpty()

        assertNotNull academicLevels[0].guid
        def academicLevel = academicLevelCompositeService.get(academicLevels[0].guid)
        assertNotNull academicLevel
        assertEquals academicLevels[0].guid, academicLevel.guid
        assertEquals academicLevels[0].code, academicLevel.code
    }


    void testFetchByAcademicLevelId() {
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByAcademicLevelId(i_success_level.id)
        assertNotNull academicLevel
        assertEquals i_success_level.id, academicLevel.id
        assertEquals i_success_level.code, academicLevel.code
        assertEquals i_success_level.description, academicLevel.description
    }


    void testFetchByAcademicLevelIdInvalid() {
        assertNull academicLevelCompositeService.fetchByAcademicLevelId(null)
    }


    void testFetchByAcademicLevel() {
        AcademicLevel academicLevel = academicLevelCompositeService.fetchByAcademicLevel(i_success_level.code)
        assertNotNull academicLevel
        assertEquals i_success_level.id, academicLevel.id
        assertEquals i_success_level.code, academicLevel.code
        assertEquals i_success_level.description, academicLevel.description
    }



    void testFetchByAcademicLevelInvalid() {
        assertNull academicLevelCompositeService.fetchByAcademicLevel(null)
        assertNull academicLevelCompositeService.fetchByAcademicLevel('A1')
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
