/** *******************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class MaritalStatusV6CompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    MaritalStatusV6CompositeService maritalStatusV6CompositeService
    def globalUniqueIdentifierService

    MaritalStatus i_success_maritalStatus
    Map i_success_input_content
    def i_creation_guid = '11599c85-afe8-4624-9832-106a716624a7'
    def i_update_description = 'Updating the description'
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'


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
    void testList_WithoutPagination() {
        def decorators = maritalStatusV6CompositeService.list([:])
        assertNotNull decorators
        assertFalse decorators.isEmpty()
        assertTrue decorators.size() > 0
    }


    @Test
    void testCount() {
        assertNotNull i_success_maritalStatus

        def translationValues = MaritalStatusCategory.values().versionToEnumMap[GeneralValidationCommonConstants.VERSION_V6]
        int totalCount = IntegrationConfiguration.countByTranslationValueInListAndSettingNameAndValueInList(translationValues,
                GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY,
                MaritalStatus.findAll().code)

        assertEquals totalCount, maritalStatusV6CompositeService.count([max: 500, offset: 0])
    }


    @Test
    void testGet_InvalidGuid() {
        try {
            maritalStatusV6CompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGet_OrphanedGuid() {
        globalUniqueIdentifierService.create([guid     : i_creation_guid,
                                              ldmName  : 'marital-status',
                                              domainId : 99999999999,
                                              domainKey: 'Y'])
        try {
            maritalStatusV6CompositeService.get(i_creation_guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGet_NullGuid() {
        try {
            maritalStatusV6CompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testCreate() {
        i_success_input_content.put('maritalCategory', 'divorced')

        def dataMapForSingle = maritalStatusV6CompositeService.create(i_success_input_content)
        MaritalStatusDetail maritalStatusDetail = maritalStatusV6CompositeService.createMaritalStatusDataModel(dataMapForSingle)

        assertNotNull maritalStatusDetail
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testCreate_NoCode() {
        i_success_input_content.remove('code')
        try {
            maritalStatusV6CompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }


    @Test
    void testCreate_NoDesc() {
        i_success_input_content.remove('description')
        try {
            maritalStatusV6CompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "description.required"
        }
    }


    @Test
    void testCreate_WithAlreadyExistCodeV4Header() {
        i_success_input_content.put('maritalCategory', 'divorced')
        i_success_input_content?.code = i_success_maritalStatus?.code

        try {
            maritalStatusV6CompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists"
        }
    }


    @Test
    void testUpdate() {
        i_success_input_content.put('id', GlobalUniqueIdentifier.findByDomainKeyAndLdmName(i_success_maritalStatus?.code, GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)?.guid)

        def dataMapForSingle = maritalStatusV6CompositeService.update(i_success_input_content)
        def maritalStatusDetail = maritalStatusV6CompositeService.createMaritalStatusDataModel(dataMapForSingle)

        assertEquals i_success_maritalStatus?.code, maritalStatusDetail.code
        assertEquals i_success_maritalStatus?.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }


    @Test
    void testUpdate_CallsCreate() {
        i_success_input_content.put('maritalCategory', 'divorced')
        i_success_input_content.put('id', i_creation_guid)

        def dataMapForSingle = maritalStatusV6CompositeService.update(i_success_input_content)
        MaritalStatusDetail maritalStatusDetail = maritalStatusV6CompositeService.createMaritalStatusDataModel(dataMapForSingle)

        assertNotNull maritalStatusDetail
        assertEquals i_creation_guid, maritalStatusDetail.guid
        assertEquals i_success_input_content.code, maritalStatusDetail.code
        assertEquals i_success_input_content.description, maritalStatusDetail.description
        assertEquals i_success_input_content.metadata.dataOrigin, maritalStatusDetail.dataOrigin
    }

}
