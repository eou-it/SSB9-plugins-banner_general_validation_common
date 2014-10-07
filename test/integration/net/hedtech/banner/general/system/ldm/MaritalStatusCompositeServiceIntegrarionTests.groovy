/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class MaritalStatusCompositeServiceIntegrarionTests extends BaseIntegrationTestCase {

    MaritalStatus i_success_maritalStatus
    def maritalStatusCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        i_success_maritalStatus = MaritalStatus.findByCode('M')
    }

    @Test
    void testListWithoutPaginationParams() {
        List maritalStatuses = maritalStatusCompositeService.list([:])
        assertNotNull maritalStatuses
        assertFalse maritalStatuses.isEmpty()
        assertTrue maritalStatuses.size() > 0
    }

    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List maritalStatuses = maritalStatusCompositeService.list(paginationParams)
        assertNotNull maritalStatuses
        assertFalse maritalStatuses.isEmpty()
        assertTrue maritalStatuses.size() == 4
    }

    @Test
    void testCount() {
        assertNotNull i_success_maritalStatus
        assertEquals MaritalStatus.count(), maritalStatusCompositeService.count()
    }

    @Test
    void testGetInvalidGuid() {
        try {
            maritalStatusCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetNullGuid() {
        try {
            maritalStatusCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def maritalStatusDetails = maritalStatusCompositeService.list(paginationParams)
        assertNotNull maritalStatusDetails
        assertFalse maritalStatusDetails.isEmpty()

        assertNotNull maritalStatusDetails[0].guid
        def maritalStatusDetail = maritalStatusCompositeService.get(maritalStatusDetails[0].guid)
        assertNotNull maritalStatusDetail
        assertNotNull maritalStatusDetail.code
        assertEquals maritalStatusDetail.code, maritalStatusDetails[0].code
        assertNotNull maritalStatusDetail.parentCategory
        assertEquals maritalStatusDetail.parentCategory, maritalStatusDetails[0].parentCategory
        assertNotNull maritalStatusDetail.guid
        assertEquals maritalStatusDetail.guid, maritalStatusDetails[0].guid
        assertNotNull maritalStatusDetail.metadata
        assertEquals maritalStatusDetail.metadata.dataOrigin, maritalStatusDetails[0].metadata.dataOrigin
    }

    @Test
    void testFetchByGradingModeIdInvalid() {
        try {
            maritalStatusCompositeService.fetchByMaritalStatusId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testFetchByGradingModeId() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusId(i_success_maritalStatus.id)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals i_success_maritalStatus.dataOrigin, maritalStatusDetail.metadata.dataOrigin
        assertEquals maritalStatusCompositeService.getLdmMaritalStatus(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
    }

    @Test
    void testFetchByGradingModeInvalid() {
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode(null)
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode('Q')
    }

    @Test
    void testFetchByGradingMode() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(i_success_maritalStatus.code)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals i_success_maritalStatus.dataOrigin, maritalStatusDetail.metadata.dataOrigin
        assertEquals maritalStatusCompositeService.getLdmMaritalStatus(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
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
