/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.LocationTypeView
import net.hedtech.banner.general.system.ldm.v4.LocationType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration Test cases for LocationTypeCompositeService.</p>
 */
class LocationTypeCompositeServiceIntegrationTests extends  BaseIntegrationTestCase {

    def locationTypeCompositeService

    def invalid_resource_guid
    def success_guid
    def invalid_guid
    private String i_success_locationType = 'billing'
    private String i_failure_locationType = 'testFailure'
    private String i_success_description = 'Business'
    private String i_success_code = 'BU'
    private String i_failure_ldmName = 'subjects'
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'
    private String invalid_guid_errorMessage = 'NotFoundException'
    private String locationTypes = 'location-types'
    Map i_success_input_content


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid = GlobalUniqueIdentifier.findByLdmName(i_failure_ldmName)
        success_guid = GlobalUniqueIdentifier.findByLdmNameAndDomainKeyInList(locationTypes, LocationTypeView.findAll()?.code)
        invalid_guid = GlobalUniqueIdentifier.findByLdmNameAndDomainKeyNotInList(locationTypes, LocationTypeView.findAll()?.code)
        i_success_input_content = [code: 'XY', description: 'Test Description',type:[person:[locationType:"Test"]]]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Test to check the LocationTypeCompositeService count method
     */
    @Test
    void testViewCount() {
        def expectedLocationTypesCount = locationTypeCompositeService.count([:])
        assertNotNull expectedLocationTypesCount
        def actualLocationTypesCount = LocationTypeView.count()
        assertNotNull actualLocationTypesCount
        assertEquals expectedLocationTypesCount, actualLocationTypesCount
    }

    /**
     * Test to check the LocationTypeCompositeService list method with valid sort and order field
     */
    @Test
    void testListWithValidSortAndOrderField() {
        def params = [order: 'ASC', sort: 'code']
        def locationTypeList = locationTypeCompositeService.list(params)
        assertNotNull locationTypeList
        assertNotNull locationTypeList.toString()
        assertTrue locationTypeList.locationType.contains(i_success_locationType)
        assertTrue locationTypeList.description.contains(i_success_description)
        assertTrue locationTypeList.code.contains(i_success_code)
        assertFalse locationTypeList.locationType.contains(i_failure_locationType)
    }

    /**
     * Test to check the LocationTypeCompositeService list method with invalid order field
     */
    @Test
    void testListWithInvalidSortOrder() {
        shouldFail(RestfulApiValidationException) {
            def map = [order: 'test']
            locationTypeCompositeService.list(map)
        }
    }

    /**
     * Test to check the LocationTypeCompositeService list method with invalid sort field
     */
    @Test
    void testListWithInvalidSortField() {
        shouldFail(RestfulApiValidationException) {
            def map = [sort: 'test']
            locationTypeCompositeService.list(map)
        }
    }

    /**
     * Test to check the LocationTypeCompositeService list method with invalid sort field and message
     */
    @Test
    void testListWithInvalidSortFieldWithException() {
        try {
            def map = [sort: 'test']
            locationTypeCompositeService.list(map)
            fail()
        } catch (RestfulApiValidationException e) {
            assertEquals 400, e.getHttpStatusCode()
            assertEquals invalid_sort_orderErrorMessage , e.messageCode.toString()
        }
    }

    /**
     * Test to check the LocationTypeCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '5', offset: '0']
        List locationTypes = locationTypeCompositeService.list(paginationParams)
        assertTrue locationTypes.locationType.contains(i_success_locationType)
        assertNotNull locationTypes
        assertFalse locationTypes.isEmpty()
        assertTrue locationTypes.size() == 5
    }

    /**
     * Test to check the LocationTypeCompositeService list method without pagination
     */
    @Test
    void testListWithoutParams() {
        List locationTypes = locationTypeCompositeService.list([:])
        assertNotNull locationTypes
        assertFalse locationTypes.isEmpty()
        List actualTypes = LocationTypeView.list(max: '500')
        assertNotNull actualTypes
        assertFalse actualTypes.isEmpty()
        assertTrue locationTypes.code.containsAll(actualTypes.code)
        assertEquals locationTypes.size(), actualTypes.size()
    }

    /**
     * Test to check LocationTypeCompositeService get method with invalid guid
     */
    @Test
    void testGetWithInvalidGuid() {
        try {
            locationTypeCompositeService.get(null)
        } catch (ApplicationException e) {
            assertApplicationException e, invalid_guid_errorMessage
        }
    }

    /**
     * Test to check LocationTypeCompositeService get method with empty guid value
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
            locationTypeCompositeService.get("")
        } catch (ApplicationException e) {
            assertApplicationException e, invalid_guid_errorMessage
        }
    }

    /**
     * Test to check LocationTypeCompositeService get method with other than location types valid guid
     */
    @Test
    void testGetWithValidNonExistingLocationTypeGuid() {
        assertNotNull invalid_resource_guid
        try {
            locationTypeCompositeService.get(invalid_resource_guid.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * Test to check the  LocationTypeCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid() {
        assertNotNull success_guid
        def guid = success_guid?.guid
        assertNotNull guid
        def locationType = locationTypeCompositeService.get(guid)
        assertNotNull locationType
        assertNotNull locationType.toString()
        assertNotNull locationType
        assertNotNull locationType.code
        assertNotNull locationType.id
        assertNotNull locationType.type
        assertNotNull locationType.value
    }

    /**
     * Test Case to check the get method with a valid guid that is not mapped to GORICCR table with respect to the location-types of person or organization
     */
    @Test
    void testGetWithNotMappedGuid() {
        assertNotNull invalid_guid
        def guid = invalid_guid?.guid
        assertNotNull guid
        try {
            locationTypeCompositeService.get(guid)
        } catch (ApplicationException e) {
            assertApplicationException e, invalid_guid_errorMessage
        }
    }

    /**
     * Test to check the sort by code on LocationTypeCompositeService
     * */
    @Test
    public void testSortByCode(){
        params.order='ASC'
        params.sort='code'
        List list = locationTypeCompositeService.list(params)
        assertNotNull list
        def tempParam=null
        list.each{
            location->
                String code=location.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='DESC'
        params.sort='code'
        list = locationTypeCompositeService.list(params)
        assertNotNull list
        tempParam=null
        list.each{
            location->
                String code=location.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }
    }

    /**
     * Test to check the create method of LocationTypeCompositeService with valid request payload
     */
    @Test
    void testCreateLocationType() {
        LocationType locationType = locationTypeCompositeService.create(i_success_input_content)
        assertNotNull locationType
        assertEquals i_success_input_content.code, locationType.code
        assertEquals i_success_input_content.description, locationType.description
    }

    /**
     * Test to check the LocationTypeCompositeService create method without mandatory code in the request payload
     */
    @Test
    void testCreateLocationTypeWithoutMandatoryCode() {
        i_success_input_content.remove('code')
        try {
            locationTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }

    /**
     * Test to check the LocationTypeCompositeService create method with existing code in the request payload
     */
    @Test
    void testCreateLocationTypeExistingCode() {
        i_success_input_content.code=i_success_code
        try {
            locationTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    /**
     * Test to update the Location-Type with a valid request payload
     * */
    @Test
    void testUpdateLocationType() {
        LocationType locationType = locationTypeCompositeService.create(i_success_input_content)
        assertNotNull locationType
        assertNotNull locationType.id
        assertEquals i_success_input_content.code, locationType.code
        assertEquals i_success_input_content.description, locationType.description
        Map update_content = updateLocationTypeMap(locationType.id)
        def o_success_LocationType_update = locationTypeCompositeService.update(update_content)
        assertNotNull o_success_LocationType_update
        assertEquals o_success_LocationType_update.id, update_content.id
        assertEquals o_success_LocationType_update.code, update_content.code
        assertEquals o_success_LocationType_update.description, update_content.description
    }

    /**
     * Test to update the Location-Type with Invalid Guid
     * */
    @Test
    void testUpdateLocationTypeWithInvalidGuid() {
        LocationType locationType = locationTypeCompositeService.create(i_success_input_content)
        assertNotNull locationType
        assertNotNull locationType.id
        assertEquals i_success_input_content.code, locationType.code
        assertEquals i_success_input_content.description, locationType.description
        Map update_content = updateLocationTypeMap(null)
        shouldFail(ApplicationException) {
            locationTypeCompositeService.update(update_content)
        }
    }

    /**
     * Test to update the Location-Type with non existing Guid and Code for LocationTypeCompositeService create method invocation
     * */
    @Test
    void testUpdateLocationTypeWithCreateForNewCodeAndGuid() {
        i_success_input_content.put("id","test-guid")
        LocationType locationType = locationTypeCompositeService.update(i_success_input_content)
        assertNotNull locationType
        assertNotNull locationType.id
        assertEquals i_success_input_content.id, locationType.id
        assertEquals i_success_input_content.code, locationType.code
        assertEquals i_success_input_content.description, locationType.description
    }

    /**
     * Test to check the LocationTypeCompositeService update method with existing code and new Guid in the request payload
     */
    @Test
    void testUpdateLocationTypeWithCreateForNewGuidAndExistingCode() {
        i_success_input_content.put("id","test-guid")
        i_success_input_content.code=i_success_code
        try {
            locationTypeCompositeService.update(i_success_input_content)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    private Map updateLocationTypeMap(id) {
        Map params = [id: id,code: 'XY', description: 'Description Test',type:[person:[locationType:"Test"]]]
        return params
    }

}