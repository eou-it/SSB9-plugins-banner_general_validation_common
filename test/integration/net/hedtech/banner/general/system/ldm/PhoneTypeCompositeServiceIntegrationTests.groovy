/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.v4.PhoneTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for phone type composite service.</p>
 */
class PhoneTypeCompositeServiceIntegrationTests extends  BaseIntegrationTestCase {

 def phoneTypeCompositeService

    def invalid_resource_guid
    def success_guid
    def invalid_guid
    def i_success_input_content
    
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid=GlobalUniqueIdentifier.findByLdmName('subjects')
        success_guid=GlobalUniqueIdentifier.findByLdmNameAndDomainKeyInList('phone-types',net.hedtech.banner.general.system.PhoneType.findAll()?.code)
        invalid_guid=GlobalUniqueIdentifier.findByLdmNameAndDomainKeyNotInList('phone-types',net.hedtech.banner.general.system.PhoneType.findAll()?.code)
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
        def expectedCount= phoneTypeCompositeService.count()
        assertNotNull expectedCount
        def actualCount= net.hedtech.banner.general.system.PhoneType.count()
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }


    /**
     * This test case is checking for PhoneTypeCompositeService list method without pagination
     */
    @Test
    void testListWithoutPaginationParams() {
        List phoneTypes = phoneTypeCompositeService.list([:])
        assertNotNull phoneTypes
        assertFalse phoneTypes.isEmpty()
        List actualTypes= net.hedtech.banner.general.system.PhoneType.list(max:'500')
        assertNotNull actualTypes
        assertFalse actualTypes.isEmpty()
        assertTrue phoneTypes.code.containsAll(actualTypes.code)
        assertEquals phoneTypes.size() , actualTypes.size()
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
        assertNotNull success_guid //success_guid variable is defined at the top of the class
        def  phoneType= phoneTypeCompositeService.get(success_guid?.guid)
        assertNotNull phoneType
        assertNotNull phoneType.code
        assertNotNull phoneType.id
        assertNotNull phoneType.phoneType
    }

   /**
     * This test case is checking for PhoneTypeCompositeService get method with an invalid guid
     */
    @Test
    void testGetWithInValidGuid(){
        assertNotNull success_guid //success_guid variable is defined at the top of the class
        try {
            phoneTypeCompositeService.get(success_guid?.guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     *Test Case to check the get method with a valid guid that is not mapped to GORICCR table with respect to the phone-types of person or organization
     */
    @Test
    void testGetWithNotMappedGuid(){
        assertNotNull invalid_guid //success_guid variable is defined at the top of the class
        try {
            phoneTypeCompositeService.get(invalid_guid?.guid)
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
       def u_success_input_content = i_success_input_content.clone()
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
}
