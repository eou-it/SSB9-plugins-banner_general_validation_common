/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MaritalStatusDataModelFacadeServiceIntegrationTests extends BaseIntegrationTestCase {

    MaritalStatusDataModelFacadeService maritalStatusDataModelFacadeService

    MaritalStatus i_success_maritalStatus
    Map i_success_input_content
    String i_creation_guid = '11599c85-afe8-4624-9832-106a716624a7'
    String i_update_description = 'Updating the description'


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


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testListV1_WithoutPagination() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        def decorators = maritalStatusDataModelFacadeService.list([:])

        assertNotNull decorators
        assertFalse decorators.isEmpty()
        assertTrue decorators.size() > 0
    }


    @Test
    void testListV1_WithPagination() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        def paginationParams = [max: '4', offset: '0']
        def decorators = maritalStatusDataModelFacadeService.list(paginationParams)

        assertNotNull decorators
        assertFalse decorators.isEmpty()
        assertTrue decorators.size() == 4
    }


    @Test
    void testCountV1() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        assertNotNull i_success_maritalStatus

        assertEquals MaritalStatus.count(), maritalStatusDataModelFacadeService.count([max: 500, offset: 0])
    }


    @Test
    void testGetV1() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        def paginationParams = [max: '1', offset: '0']
        def decorators = maritalStatusDataModelFacadeService.list(paginationParams)
        assertNotNull decorators
        assertFalse decorators.isEmpty()
        assertNotNull decorators[0].guid

        def maritalStatusDetail = maritalStatusDataModelFacadeService.get(decorators[0].guid)
        assertNotNull maritalStatusDetail
        assertNotNull maritalStatusDetail.code
        assertEquals maritalStatusDetail.code, decorators[0].code
        assertNotNull maritalStatusDetail.parentCategory
        assertEquals maritalStatusDetail.parentCategory, decorators[0].parentCategory
        assertNotNull maritalStatusDetail.guid
        assertEquals maritalStatusDetail.guid, decorators[0].guid
        assertNotNull maritalStatusDetail.metadata
        assertEquals maritalStatusDetail.metadata.dataOrigin, decorators[0].metadata.dataOrigin
        assertEquals maritalStatusDetail, decorators[0]
    }


    @Test
    void testCreateV1() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        MaritalStatusDetail maritalStatusDetail = maritalStatusDataModelFacadeService.create(i_success_input_content)

        assertNotNull maritalStatusDetail
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testUpdateV1() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        MaritalStatusDetail maritalStatusDetail = maritalStatusDataModelFacadeService.create(i_success_input_content)
        assertNotNull maritalStatusDetail

        i_success_input_content.put('id', maritalStatusDetail.guid)
        i_success_input_content.put('description', i_update_description)

        maritalStatusDetail = maritalStatusDataModelFacadeService.update(i_success_input_content)

        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_update_description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testUpdateV1_CallsCreate() {
        setContentTypeAndAcceptHeaders("application/vnd.hedtech.integration.v1+json")

        i_success_input_content.put('id', i_creation_guid)

        def maritalStatusDetail = maritalStatusDataModelFacadeService.update(i_success_input_content)

        assertNotNull maritalStatusDetail
        assertEquals i_creation_guid, maritalStatusDetail.guid
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    private void setContentTypeAndAcceptHeaders(String mediaType) {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", mediaType)
        request.addHeader("Content-Type", mediaType)
    }

}
