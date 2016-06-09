/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.CitizenType
import net.hedtech.banner.general.system.Religion
import net.hedtech.banner.general.system.ldm.v4.PhoneTypeDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for religion composite service.</p>
 */
class ReligionCompositeServiceIntegrationTests extends  BaseIntegrationTestCase {

 def religionCompositeService

    def invalid_resource_guid
    def success_guid
    def invalid_guid

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
      }

    private void initializeDataReferences() {
        invalid_resource_guid=GlobalUniqueIdentifier.findByLdmName('subjects')
        success_guid=GlobalUniqueIdentifier.findByLdmNameAndDomainKeyInList('religions',net.hedtech.banner.general.system.Religion.findAll()?.code)
        invalid_guid=GlobalUniqueIdentifier.findByLdmNameAndDomainKeyNotInList('religions',net.hedtech.banner.general.system.Religion.findAll()?.code)

    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * This test case is checking for ReligionCompositeService count method
     */
    @Test
    void testCount(){
        def expectedCount= religionCompositeService.count()
        assertNotNull expectedCount
        def actualCount= net.hedtech.banner.general.system.Religion.count()
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }
    /**
     * This test case is checking for ReligionCompositeService list method without pagination
     */
    @Test
    void testListWithoutPaginationParams() {
        List religions = religionCompositeService.list([:])
        assertNotNull religions
        assertFalse religions.isEmpty()
        List actualTypes= net.hedtech.banner.general.system.Religion.list(max:'500')
        assertNotNull actualTypes
        assertFalse actualTypes.isEmpty()
        assertTrue religions.code.containsAll(actualTypes.code)
        assertEquals religions.size() , actualTypes.size()
    }

    /**
     * This test case is checking for ReligionCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List religions = religionCompositeService.list(paginationParams)
        assertNotNull religions
        assertFalse religions.isEmpty()
        assertTrue religions.size() == 4
    }

    /**
     * This test case is checking for ReligionCompositeService get method with guid as a null
     */
    @Test
    void testGetWithNullGuid() {
        try {
            religionCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for ReligionCompositeService get method with guid as an empty
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
            religionCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for ReligionCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid(){
        assertNotNull success_guid //success_guid variable is defined at the top of the class
        def  religion= religionCompositeService.get(success_guid?.guid)
        assertNotNull religion
        assertNotNull religion.code
        assertNotNull religion.id
        assertNotNull religion.description
    }

    /**
     * This test case is checking for ReligionCompositeService get method with an invalid guid
     */
    @Test
    void testGetWithInValidGuid(){
        assertNotNull success_guid //success_guid variable is defined at the top of the class
        try {
            religionCompositeService.get(success_guid?.guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testfetchGUIDs(){
        List<String> religionCodes= Religion.findAll(max: 2).code
        Map content=religionCompositeService.fetchGUIDs((religionCodes))
        content.each{ religionStatus ->
            assertTrue religionCodes.contains(religionStatus.key)
        }
    }

}
