/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.LocationTypeView
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
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
    private String i_success_translationValue = 'billing'
    private String i_failure_translationValue = 'testFailure'
    private String i_success_description = 'Business'
    private String i_success_code = 'BU'
    private String i_failure_ldmName = 'subjects'
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'
    private String invalid_guid_errorMessage = 'NotFoundException'
    private String locationTypes = 'location-types'


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
        assertTrue locationTypeList.translationValue.contains(i_success_translationValue)
        assertTrue locationTypeList.description.contains(i_success_description)
        assertTrue locationTypeList.code.contains(i_success_code)
        assertFalse locationTypeList.translationValue.contains(i_failure_translationValue)
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
        assertTrue locationTypes.translationValue.contains(i_success_translationValue)
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
        shouldFail(RestfulApiValidationException) {
            locationTypeCompositeService.get(invalid_resource_guid?.guid)
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
        assertNotNull locationType.code
        assertNotNull locationType.id
        assertNotNull locationType.type
        assertNotNull locationType.value
    }

    /**
     * Test to check  LocationTypeCompositeService get method with an valid guid but not mapped goriccr
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
}