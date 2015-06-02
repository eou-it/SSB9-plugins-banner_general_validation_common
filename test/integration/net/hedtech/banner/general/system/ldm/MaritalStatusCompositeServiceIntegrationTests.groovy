/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class MaritalStatusCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    MaritalStatus i_success_maritalStatus
    Map i_success_input_content
    def i_creation_guid = '11599c85-afe8-4624-9832-106a716624a7'
    def i_update_description = 'Updating the description'

    def maritalStatusCompositeService
    def globalUniqueIdentifierService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }


    private void initializeDataReferences() {
        i_success_maritalStatus = MaritalStatus.findByCode('M')
        i_success_input_content = [code: 'Y', metadata: [dataOrigin: 'Banner'], description: 'The Y code']
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
    void testGetOrphanedGuid() {
        globalUniqueIdentifierService.create([guid     : i_creation_guid,
                                              ldmName  : 'marital-status',
                                              domainId : 99999999999,
                                              domainKey: 'Y'])
        try {
            maritalStatusCompositeService.get(i_creation_guid)
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
        assertNotNull maritalStatusDetail.toString()
        assertNotNull maritalStatusDetail.code
        assertEquals maritalStatusDetail.code, maritalStatusDetails[0].code
        assertNotNull maritalStatusDetail.parentCategory
        assertEquals maritalStatusDetail.parentCategory, maritalStatusDetails[0].parentCategory
        assertNotNull maritalStatusDetail.guid
        assertEquals maritalStatusDetail.guid, maritalStatusDetails[0].guid
        assertNotNull maritalStatusDetail.metadata
        assertEquals maritalStatusDetail.metadata.dataOrigin, maritalStatusDetails[0].metadata.dataOrigin
        assertEquals maritalStatusDetail, maritalStatusDetails[0]
    }


    @Test
    void testFetchByMaritalStatusIdNull() {
        def result = maritalStatusCompositeService.fetchByMaritalStatusId(null)
        assertNull result
    }


    @Test
    void testFetchByMaritalStatusId() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusId(i_success_maritalStatus.id)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals i_success_maritalStatus.dataOrigin, maritalStatusDetail.metadata.dataOrigin
        assertEquals maritalStatusCompositeService.getHeDMEnumeration(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
    }


    @Test
    void testFetchByMaritalStatusInvalid() {
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode(null)
        assertNull maritalStatusCompositeService.fetchByMaritalStatusCode('Q')
    }


    @Test
    void testFetchByMaritalStatus() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(i_success_maritalStatus.code)
        assertNotNull maritalStatusDetail
        assertEquals i_success_maritalStatus.id, maritalStatusDetail.id
        assertEquals i_success_maritalStatus.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus.description, maritalStatusDetail.description
        assertEquals i_success_maritalStatus.dataOrigin, maritalStatusDetail.metadata.dataOrigin
        assertEquals maritalStatusCompositeService.getHeDMEnumeration(i_success_maritalStatus.code), maritalStatusDetail.parentCategory
    }


    @Test
    void testGetLdmMaritalStatus() {
        def result = maritalStatusCompositeService.getHeDMEnumeration('S')
        assertNotNull result
        assertEquals result, 'Single'

    }


    @Test
    void testGetLdmMaritalStatusNull() {
        def result = maritalStatusCompositeService.getHeDMEnumeration(null)
        assertNull result
    }


    @Test
    void testGetLdmMaritalStatusInvalidTranslation() {
        def result = maritalStatusCompositeService.getHeDMEnumeration('X')
        assertNull result
    }


    @Test
    void testCreate() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.create(i_success_input_content)
        assertNotNull maritalStatusDetail
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testCreateNoCode() {
        i_success_input_content.remove('code')
        try {
            MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }


    @Test
    void testCreateNoDesc() {
        i_success_input_content.remove('description')
        try {
            MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "description.required"
        }
    }


    @Test
    void testUpdateCreate() {
        i_success_input_content.put('id', i_creation_guid)
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.update(i_success_input_content)
        assertNotNull maritalStatusDetail
        assertEquals i_creation_guid, maritalStatusDetail.guid
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testUpdate() {
        MaritalStatusDetail maritalStatusDetail = maritalStatusCompositeService.create(i_success_input_content)
        assertNotNull maritalStatusDetail
        i_success_input_content.put('id', maritalStatusDetail.guid)
        i_success_input_content.put('description', i_update_description)
        maritalStatusDetail = maritalStatusCompositeService.update(i_success_input_content)
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_update_description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }

}
