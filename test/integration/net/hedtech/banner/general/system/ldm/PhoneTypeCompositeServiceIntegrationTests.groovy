/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.system.TelephoneTypeService
import net.hedtech.banner.general.system.ldm.v4.PhoneTypeDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for phone type composite service.</p>
 */
class PhoneTypeCompositeServiceIntegrationTests extends  BaseIntegrationTestCase {

    def phoneTypeCompositeService
    TelephoneTypeService telephoneTypeService

    def invalid_resource_guid
    def i_success_input_content

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid=GlobalUniqueIdentifier.findByLdmName('subjects')
        i_success_input_content = [code: 'KKR', description: 'Test Description', phoneType:"billing"]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * This test case is checking for PhoneTypeCompositeService count method
     */
    @Test
    void testCount(){
        def actualCount = phoneTypeCompositeService.count().toInteger()
        assertNotNull actualCount

        Map<String, String> bannerPhoneTypeToHedmPhoneTypeMap = phoneTypeCompositeService.getBannerPhoneTypeToHedmV6PhoneTypeMap()
        assertFalse bannerPhoneTypeToHedmPhoneTypeMap.isEmpty()

        List<TelephoneType> phoneTypeList = telephoneTypeService.fetchAllByCodeInList(bannerPhoneTypeToHedmPhoneTypeMap.keySet())
        assertFalse phoneTypeList.isEmpty()
        assertEquals phoneTypeList.size(), actualCount
    }


    /**
     * This test case is checking for PhoneTypeCompositeService list method without pagination
     */
    @Test
    void testListWithoutPaginationParams() {
        List phoneTypes = phoneTypeCompositeService.list(params)
        assertFalse phoneTypes.isEmpty()

        Map<String, String> bannerPhoneTypeToHedmPhoneTypeMap = phoneTypeCompositeService.getBannerPhoneTypeToHedmV6PhoneTypeMap()
        assertFalse bannerPhoneTypeToHedmPhoneTypeMap.isEmpty()

        List entities = telephoneTypeService.fetchAllWithGuidByCodeInList(bannerPhoneTypeToHedmPhoneTypeMap.keySet(), 500, 0)
        assertFalse entities.isEmpty()

        assertEquals phoneTypes.size(), entities.size()
        Iterator it1 = phoneTypes.iterator()
        Iterator it2 = entities.iterator()

        while (it1.hasNext() && it2.hasNext()) {
            PhoneTypeDecorator phoneTypeDecorator = it1.next()
            List actualEntities = it2.next()
            TelephoneType telephoneType = actualEntities.getAt(0)
            assertEquals telephoneType.code, phoneTypeDecorator.code
            assertEquals telephoneType.description, phoneTypeDecorator.description
            GlobalUniqueIdentifier globalUniqueIdentifier = actualEntities.getAt(1)
            assertEquals globalUniqueIdentifier.guid, phoneTypeDecorator.id
            assertEquals phoneTypeDecorator.phoneType, bannerPhoneTypeToHedmPhoneTypeMap.get(telephoneType.code)
        }
    }

    /**
     * This test case is checking for PhoneTypeCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List phoneTypes = phoneTypeCompositeService.list(paginationParams)
        assertNotNull phoneTypes
        assertFalse phoneTypes.isEmpty()
        assertTrue phoneTypes.size() == 4
    }

    @Test
    public void testOffsetCriteria(){
        List<PhoneTypeDecorator> phoneTypeDetailses = phoneTypeCompositeService.list(params)
        assertNotNull phoneTypeDetailses
        params.offset='2'
        List phoneTypeCompositeServiceNew = phoneTypeCompositeService.list(params)
        assertNotNull phoneTypeCompositeServiceNew

        PhoneTypeDecorator phoneTypeDetails = phoneTypeDetailses.get(2)
        assertNotNull phoneTypeDetails
        PhoneTypeDecorator phoneTypeDetailsWithOffset = phoneTypeCompositeServiceNew.get(0)
        assertNotNull phoneTypeDetails

        assertEquals phoneTypeDetails.id,phoneTypeDetailsWithOffset.id
        assertEquals phoneTypeDetails.code,phoneTypeDetailsWithOffset.code
        assertEquals phoneTypeDetails.description,phoneTypeDetailsWithOffset.description

    }


    /**
     * This test case is checking for PhoneTypeCompositeService get method with guid as a null
     */
    @Test
    void testGetWithNullGuid() {
        try {
            phoneTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for PhoneTypeCompositeService get method with guid as an empty
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
            phoneTypeCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for PhoneTypeCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid(){
        List list = phoneTypeCompositeService.list([max:'1'])
        assertFalse list.isEmpty()
        PhoneTypeDecorator phoneTypeDetails = list.get(0)
        assertNotNull phoneTypeDetails

        PhoneTypeDecorator newPhoneTypeDetails = phoneTypeCompositeService.get(phoneTypeDetails.id)
        assertNotNull newPhoneTypeDetails
        assertEquals phoneTypeDetails.code,newPhoneTypeDetails.code
        assertEquals phoneTypeDetails.description,newPhoneTypeDetails.description
        assertEquals phoneTypeDetails.id,newPhoneTypeDetails.id
        assertEquals newPhoneTypeDetails.phoneType, phoneTypeDetails.phoneType
    }

   /**
     * This test case is checking for PhoneTypeCompositeService get method with an invalid guid
     */
    @Test
    void testGetWithInValidGuid(){
        try {
            phoneTypeCompositeService.get('invalid_guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     *Test Case to check the get method with a valid guid that is not mapped to GORICCR table with respect to the phone-types of person or organization
     */
    @Test
    void testGetWithNotMappedGuid(){
        PhoneTypeDecorator phoneType = phoneTypeCompositeService.create(i_success_input_content)
        assertNotNull phoneType
        try {
            phoneTypeCompositeService.get(phoneType.id)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testCreate() {
        PhoneTypeDecorator phoneType = phoneTypeCompositeService.create(i_success_input_content)
        assertNotNull phoneType
        assertEquals phoneType.code, i_success_input_content.code
        assertEquals phoneType.description, i_success_input_content.description
        assertNotNull phoneType.id
    }

    @Test
    void testCreateWithId(){
        i_success_input_content.id = 'TEST_GUID'
        PhoneTypeDecorator phoneType = phoneTypeCompositeService.create(i_success_input_content)
        assertNotNull phoneType
        assertNotNull phoneType.id
        assertEquals phoneType.code, i_success_input_content.code
        assertEquals phoneType.description, i_success_input_content.description
        assertEquals phoneType.id, i_success_input_content.id.trim().toLowerCase()
    }

    @Test
    void testCreateWithExistsCode(){
        i_success_input_content.code = 'MA'
        try{
            phoneTypeCompositeService.create(i_success_input_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "exists.message"
        }
    }

    @Test
    void testCreateWithoutMandatoryCode(){
        i_success_input_content.remove('code')
        try{
            phoneTypeCompositeService.create(i_success_input_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "code.required.message"
        }
    }

    @Test
    void testUpdate(){
       PhoneTypeDecorator i_phoneType = phoneTypeCompositeService.create(i_success_input_content)
       assertNotNull i_phoneType
       assertNotNull i_phoneType.id
        Map u_success_input_content = i_success_input_content.clone()
       u_success_input_content.code = 'TESTC'
       u_success_input_content.id = i_phoneType.id
       u_success_input_content.description = 'Test phone type description'
       PhoneTypeDecorator u_phoneType = phoneTypeCompositeService.update(u_success_input_content)
       assertNotNull u_phoneType
       assertEquals u_phoneType.code, i_phoneType.code
       assertEquals u_phoneType.description, u_success_input_content.description
       assertEquals u_phoneType.id, u_success_input_content.id
    }

    @Test
    void testUpdateNullGuid() {
        i_success_input_content.put('id', '')
        try{
            phoneTypeCompositeService.update(i_success_input_content)
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testUpdateNonExistsGuid() {
        i_success_input_content.put('id', 'TEST')
        PhoneTypeDecorator u_phoneType = phoneTypeCompositeService.update(i_success_input_content)
        assertNotNull u_phoneType
        assertEquals u_phoneType.code, i_success_input_content.code
        assertEquals u_phoneType.description, i_success_input_content.description
        assertEquals u_phoneType.id, i_success_input_content.id.toLowerCase()
    }

    @Test
    void testGetPhoneTypeCodeToGuidMap() {
        PhoneTypeDecorator phoneType = phoneTypeCompositeService.create(i_success_input_content)
        assertNotNull phoneType
        assertNotNull phoneType.id
        Map<String,String> phoneTypeCodeToGuidMap = phoneTypeCompositeService.getPhoneTypeCodeToGuidMap([phoneType.code])
        assertFalse phoneTypeCodeToGuidMap.isEmpty()
        assertTrue phoneTypeCodeToGuidMap.containsKey(phoneType.code)
        assertEquals phoneType.id, phoneTypeCodeToGuidMap.get(phoneType.code)
    }

    @Test
    void testGetBannerPhoneTypeToHedmV6PhoneTypeMap() {
        TelephoneType telephoneType = TelephoneType.findByCode("HOME")
        assertNotNull telephoneType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PHONE_TYPE_SETTING_NAME_V6, telephoneType.code)
        assertNotNull intConf
        def map = phoneTypeCompositeService.getBannerPhoneTypeToHedmV6PhoneTypeMap()
        assertNotNull map
        assertTrue map.containsKey(telephoneType.code)
        assertEquals map.get(telephoneType.code), HedmPhoneType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V6]
    }

    @Test
    void testGetBannerPhoneTypeToHedmV3PhoneTypeMap() {
        TelephoneType telephoneType = TelephoneType.findByCode("HOME")
        assertNotNull telephoneType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PHONE_TYPE_SETTING_NAME_V3, telephoneType.code)
        assertNotNull intConf
        def map = phoneTypeCompositeService.getBannerPhoneTypeToHedmV3PhoneTypeMap()
        assertNotNull map
        assertTrue map.containsKey(telephoneType.code)
        assertEquals map.get(telephoneType.code), HedmPhoneType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V3).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V3]
    }

}
