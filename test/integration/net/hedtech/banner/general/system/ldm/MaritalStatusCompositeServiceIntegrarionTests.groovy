/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.testing.BaseIntegrationTestCase


class MaritalStatusCompositeServiceIntegrarionTests extends BaseIntegrationTestCase {

    MaritalStatus i_success_maritalStatus
    def maritalStatusCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        i_success_maritalStatus = MaritalStatus.findByCode('M')
    }


    void testListWithoutPaginationParams() {
        List maritalStatuses = maritalStatusCompositeService.list([:])
        assertNotNull maritalStatuses
        assertFalse maritalStatuses.isEmpty()
        assertTrue maritalStatuses.size() > 0
    }


    void testList() {
        def paginationParams = [max: '4', offset: '0']
        List maritalStatuses = maritalStatusCompositeService.list(paginationParams)
        assertNotNull maritalStatuses
        assertFalse maritalStatuses.isEmpty()
        assertTrue maritalStatuses.size() == 4
    }


    void testCount() {
        assertNotNull i_success_maritalStatus
        assertTrue maritalStatusCompositeService.count() > 0
    }


    void testGetInvalidGuid() {
        try {
            maritalStatusCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def maritalStatusDetails = maritalStatusCompositeService.list(paginationParams)
        assertNotNull maritalStatusDetails
        assertFalse maritalStatusDetails.isEmpty()

        assertNotNull maritalStatusDetails[0].guid
        def maritalStatusDetail = maritalStatusCompositeService.get(maritalStatusDetails[0].guid)
        assertNotNull maritalStatusDetail
        assertEquals maritalStatusDetails[0], maritalStatusDetail
    }


    void testFetchByGradingModeIdInvalid() {
        try {
            maritalStatusCompositeService.fetchByMaritalStatusId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testFetchByGradingModeId() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusId(i_success_maritalStatus.id)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals getLdmMaritalStatus(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
    }


    void testFetchByGradingModeInvalid() {
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode(null)
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode('Q')
    }


    void testFetchByGradingMode() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(i_success_maritalStatus.code)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals getLdmMaritalStatus(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
    }

    //TODO: Move this function to common place
    // Return LDM enumeration value for this marital status code.
    def getLdmMaritalStatus(def maritalStatus) {
        if (maritalStatus != null) {
            switch (maritalStatus) {
                case "S":
                    return MaritalStatusParentCategory.SINGLE.value
                case "M":
                    return MaritalStatusParentCategory.MARRIED.value
                case "D":
                    return MaritalStatusParentCategory.DIVORCED.value
                case "W":
                    return MaritalStatusParentCategory.WIDOWED.value
                case "P":
                    return MaritalStatusParentCategory.SEPARATED.value
                case "R":
                    return MaritalStatusParentCategory.MARRIED.value
                default:
                    return null
            }
        }
        return null
    }
}
