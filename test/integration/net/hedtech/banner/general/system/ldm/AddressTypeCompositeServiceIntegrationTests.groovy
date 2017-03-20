/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.AddressTypeService
import net.hedtech.banner.general.system.ldm.v6.AddressTypeDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration Test cases for AddressTypeCompositeService.</p>
 */
class AddressTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    AddressTypeCompositeService addressTypeCompositeService
    AddressTypeService addressTypeService

    def invalid_resource_guid
    private String i_success_code = 'BU'
    private String i_failure_ldmName = 'subjects'
    private String invalid_guid_errorMessage = 'NotFoundException'
    Map i_success_input_content


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid = GlobalUniqueIdentifier.findByLdmName(i_failure_ldmName)
        i_success_input_content = [code: 'XY', description: 'Test Description', addressType: "Home"]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Test to check the AddressTypeCompositeService count method
     */
    @Test
    public void testCount() {
        def actualCount = addressTypeCompositeService.count().toInteger()
        assertNotNull actualCount

        Map<String, String> bannerAddressTypeToHedmAddressTypeMap = addressTypeCompositeService.getBannerAddressTypeToHedmV6AddressTypeMap()
        assertFalse bannerAddressTypeToHedmAddressTypeMap.isEmpty()

        List<AddressType> addressTypeList = addressTypeService.fetchAllByCodeInList(bannerAddressTypeToHedmAddressTypeMap.keySet())
        assertFalse addressTypeList.isEmpty()
        assertEquals addressTypeList.size(), actualCount
    }

    /**
     * Test to check the AddressTypeCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '3', offset: '0']
        List addressTypes = addressTypeCompositeService.list(paginationParams)
        assertNotNull addressTypes
        assertFalse addressTypes.isEmpty()
        assertTrue addressTypes.size() > 0
    }

    /**
     * Test to check the AddressTypeCompositeService list method without pagination
     */
    @Test
    void testListWithoutParams() {
        List addressTypes = addressTypeCompositeService.list(params)
        assertFalse addressTypes.isEmpty()

        Map<String, String> bannerAddressTypeToHedmAddressTypeMap = addressTypeCompositeService.getBannerAddressTypeToHedmV6AddressTypeMap()
        assertFalse bannerAddressTypeToHedmAddressTypeMap.isEmpty()

        List entities = addressTypeService.fetchAllWithGuidByCodeInList(bannerAddressTypeToHedmAddressTypeMap.keySet(), 500, 0)
        assertFalse entities.isEmpty()

        assertEquals addressTypes.size(), entities.size()
        Iterator it1 = addressTypes.iterator()
        Iterator it2 = entities.iterator()

        while (it1.hasNext() && it2.hasNext()) {
            AddressTypeDecorator addressTypeDecorator = it1.next()
            LinkedHashMap actualEntities = it2.next()
            AddressType addressType = actualEntities.addressType
            assertEquals addressType.code, addressTypeDecorator.code
            assertEquals addressType.description, addressTypeDecorator.description
            GlobalUniqueIdentifier globalUniqueIdentifier = actualEntities.globalUniqueIdentifier
            assertEquals globalUniqueIdentifier.guid, addressTypeDecorator.id
            assertEquals addressTypeDecorator.addressType, bannerAddressTypeToHedmAddressTypeMap.get(addressType.code)
        }
    }

    @Test
    public void testOffsetCriteria() {
        List<AddressTypeDecorator> addressTypeDecoratorList = addressTypeCompositeService.list(params)
        assertNotNull addressTypeDecoratorList
        params.offset = '2'
        List newAddressTypeDecoratorList = addressTypeCompositeService.list(params)
        assertNotNull newAddressTypeDecoratorList

        AddressTypeDecorator addressTypeDecorator = addressTypeDecoratorList.get(2)
        assertNotNull addressTypeDecorator
        AddressTypeDecorator addressTypeDetailsWithOffset = newAddressTypeDecoratorList.get(0)
        assertNotNull addressTypeDecorator

        assertEquals addressTypeDecorator.id, addressTypeDetailsWithOffset.id
        assertEquals addressTypeDecorator.code, addressTypeDetailsWithOffset.code
        assertEquals addressTypeDecorator.description, addressTypeDetailsWithOffset.description
    }

    /**
     * Test to check AddressTypeCompositeService get method with invalid guid
     */
    @Test
    void testGetWithInvalidGuid() {
        try {
            addressTypeCompositeService.get("xxxxx")
        } catch (ApplicationException e) {
            assertApplicationException e, invalid_guid_errorMessage
        }
    }

    /**
     * Test to check the  AddressTypeCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid() {

        List list = addressTypeCompositeService.list([max: '1'])
        assertFalse list.isEmpty()
        AddressTypeDecorator addressTypeDecorator = list.get(0)
        assertNotNull addressTypeDecorator

        AddressTypeDecorator getAddressType = addressTypeCompositeService.get(addressTypeDecorator.id)
        assertNotNull getAddressType
        assertEquals addressTypeDecorator.code, getAddressType.code
        assertEquals addressTypeDecorator.description, getAddressType.description
        assertEquals addressTypeDecorator.id, getAddressType.id
        assertEquals addressTypeDecorator.addressType, getAddressType.addressType
    }

    /**
     * Test Case to check the get method with a valid guid that is not mapped to GORICCR table with respect to the address-types of person or organization
     */
    @Test
    void testGetWithNotMappedGuid() {
        AddressTypeDecorator createAddressType = addressTypeCompositeService.create(i_success_input_content)
        assertNotNull createAddressType
        try {
            addressTypeCompositeService.get(createAddressType.id)
        } catch (ApplicationException ae) {
            assertApplicationException ae, invalid_guid_errorMessage
        }
    }

    /**
     * Test to check the create method of AddressTypeCompositeService with valid request payload
     */
    @Test
    void testCreateAddressType() {
        AddressTypeDecorator addressType = addressTypeCompositeService.create(i_success_input_content)
        assertNotNull addressType
        assertEquals i_success_input_content.code, addressType.code
        assertEquals i_success_input_content.description, addressType.description
    }

    /**
     * Test to check the AddressTypeCompositeService create method without mandatory code in the request payload
     */
    @Test
    void testCreateAddressTypeWithoutMandatoryCode() {
        i_success_input_content.remove('code')
        try {
            addressTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }

    /**
     * Test to check the AddressTypeCompositeService create method with existing code in the request payload
     */
    @Test
    void testCreateAddressTypeExistingCode() {
        i_success_input_content.code = i_success_code
        try {
            addressTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    /**
     * Test to update the address-Type with a valid request payload
     * */
    @Test
    void testUpdateAddressType() {
        AddressTypeDecorator addressTypeDecorator = addressTypeCompositeService.create(i_success_input_content)
        assertNotNull addressTypeDecorator
        assertNotNull addressTypeDecorator.id
        assertEquals i_success_input_content.code, addressTypeDecorator.code
        assertEquals i_success_input_content.description, addressTypeDecorator.description
        Map i_update_address_type = updateAddressTypeMap(addressTypeDecorator.id)
        def o_success_AddressType_update = addressTypeCompositeService.update(i_update_address_type)
        assertNotNull o_success_AddressType_update
        assertEquals o_success_AddressType_update.id, i_update_address_type.id
        assertEquals o_success_AddressType_update.code, i_update_address_type.code
        assertEquals o_success_AddressType_update.description, i_update_address_type.description
    }

    /**
     * Test to update the address-Type with Invalid Guid
     * */
    @Test
    void testUpdateAddressTypeWithInvalidGuid() {
        AddressTypeDecorator addressTypeDecorator = addressTypeCompositeService.create(i_success_input_content)
        assertNotNull addressTypeDecorator
        assertNotNull addressTypeDecorator.id
        assertEquals i_success_input_content.code, addressTypeDecorator.code
        assertEquals i_success_input_content.description, addressTypeDecorator.description
        Map i_update_address_type = updateAddressTypeMap(null)
        shouldFail(ApplicationException) {
            addressTypeCompositeService.update(i_update_address_type)
        }
    }

    /**
     * Test to update the address-Type with non existing Guid and Code for AddressTypeCompositeService create method invocation
     * */
    @Test
    void testUpdateAddressTypeWithCreateForNewCodeAndGuid() {
        i_success_input_content.put("id", "test-guid")
        AddressTypeDecorator addressTypeDecorator = addressTypeCompositeService.update(i_success_input_content)
        assertNotNull addressTypeDecorator
        assertNotNull addressTypeDecorator.id
        assertEquals i_success_input_content.id, addressTypeDecorator.id
        assertEquals i_success_input_content.code, addressTypeDecorator.code
        assertEquals i_success_input_content.description, addressTypeDecorator.description
    }

    /**
     * Test to check the AddressTypeCompositeService update method with existing code and new Guid in the request payload
     */
    @Test
    void testUpdateAddressTypeWithCreateForNewGuidAndExistingCode() {
        i_success_input_content.put("id", "test-guid")
        i_success_input_content.code = i_success_code
        try {
            addressTypeCompositeService.update(i_success_input_content)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "exists.message"
        }
    }


    @Test
    void testGetAddressTypeCodeToGuidMap() {
        AddressTypeDecorator addressType = addressTypeCompositeService.create(i_success_input_content)
        assertNotNull addressType
        assertNotNull addressType.id
        Map<String, String> addressTypeCodeToGuidMap = addressTypeCompositeService.getAddressTypeCodeToGuidMap([addressType.code])
        assertFalse addressTypeCodeToGuidMap.isEmpty()
        assertTrue addressTypeCodeToGuidMap.containsKey(addressType.code)
        assertEquals addressType.id, addressTypeCodeToGuidMap.get(addressType.code)
    }

    @Test
    void testGetBannerAddressTypeToHedmV6AddressTypeMap() {
        AddressType addressType = AddressType.findByCode(i_success_code)
        assertNotNull addressType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.ADDRESS_TYPE_SETTING_NAME_V6, addressType.code)
        assertNotNull intConf
        def map = addressTypeCompositeService.getBannerAddressTypeToHedmV6AddressTypeMap()
        assertNotNull map
        assertTrue map.containsKey(addressType.code)
        assertEquals map.get(addressType.code), HedmAddressType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V6]
    }

    @Test
    void testGetBannerAddressTypeToHedmV3AddressTypeMap() {
        AddressType addressType = AddressType.findByCode(i_success_code)
        assertNotNull addressType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.ADDRESS_TYPE_SETTING_NAME_V3, addressType.code)
        assertNotNull intConf
        def map = addressTypeCompositeService.getBannerAddressTypeToHedmV3AddressTypeMap()
        assertNotNull map
        assertTrue map.containsKey(addressType.code)
        assertEquals map.get(addressType.code), HedmAddressType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V3).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V3]

    }


    private Map updateAddressTypeMap(id) {
        Map params = [id: id, code: 'XY', description: 'Description Test', addressType: "Test"]
        return params
    }

}
