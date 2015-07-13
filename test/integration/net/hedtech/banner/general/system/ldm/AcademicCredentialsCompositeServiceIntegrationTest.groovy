/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.Degree
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentialType
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for academic credential composite service.</p>
 */
class AcademicCredentialsCompositeServiceIntegrationTest extends  BaseIntegrationTestCase  {
    
    def  academicCredentialsCompositeService
    
    def success_guid
    def invalid_resource_guid

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid=GlobalUniqueIdentifier.findByLdmName('subjects')
        success_guid=GlobalUniqueIdentifier.findByLdmName('academic-credentials')
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService count method
     */
    @Test
    void testCount(){
        def expectedCount= academicCredentialsCompositeService.count([:])
        assertNotNull expectedCount
        def actualCount= Degree.count()
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService count method with degree type
     */
    @Test
    void testCountByDegreeType(){
        def expectedCount= academicCredentialsCompositeService.count([type:AcademicCredentialType.DEGREE.name().toLowerCase()])
        assertNotNull expectedCount
        def actualCount= Degree.countByDegreeTypeOrDegreeTypeIsNull(AcademicCredentialType.DEGREE.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService count method with honorary type
     */
    @Test
    void testCountByHonoraryType(){
        def expectedCount= academicCredentialsCompositeService.count([type:AcademicCredentialType.HONORARY.name().toLowerCase()])
        assertNotNull expectedCount
        def actualCount= Degree.countByDegreeType(AcademicCredentialType.HONORARY.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService count method with diploma type
     */
    @Test
    void testCountByDiplomaType(){
        def expectedCount= academicCredentialsCompositeService.count([type:AcademicCredentialType.DIPLOMA.name().toLowerCase()])
        assertNotNull expectedCount
        def actualCount= Degree.countByDegreeType(AcademicCredentialType.DIPLOMA.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService count method with certificate type
     */
    @Test
    void testCountByCertificateType(){
        def expectedCount= academicCredentialsCompositeService.count([type:AcademicCredentialType.CERTIFICATE.name().toLowerCase()])
        assertNotNull expectedCount
        def actualCount= Degree.countByDegreeType(AcademicCredentialType.CERTIFICATE.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method passing with invalid sort order
     */
    @Test
    void testListWithInvalidSortOrder(){
        shouldFail(RestfulApiValidationException) {
            def map = [order:'test']
            academicCredentialsCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method passing with invalid sort field
     */
    @Test
    void testListWithInvalidSortField(){
        shouldFail(RestfulApiValidationException) {
            def map = [sort:'code']
            academicCredentialsCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method without pagination
     */
    @Test
    void testListWithoutPaginationParams() {
        List academicCredentials = academicCredentialsCompositeService.list([:])
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertEquals academicCredentials.size() , Degree.list([max:'500']).size()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List academicCredentials = academicCredentialsCompositeService.list(paginationParams)
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertTrue academicCredentials.size() == 4
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with type of degree
     */
    @Test
    void testListWithoutPaginationParamsByDegreeType() {
        List academicCredentials = academicCredentialsCompositeService.list([type: AcademicCredentialType.DEGREE.name().toLowerCase()])
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertEquals academicCredentials.size() , Degree.findAllByDegreeTypeOrDegreeTypeIsNull(AcademicCredentialType.DEGREE.value,[max:'500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with type of honorary
     */
    @Test
    void testListWithoutPaginationParamsByHonoraryType() {
        List academicCredentials = academicCredentialsCompositeService.list([type: AcademicCredentialType.HONORARY.name().toLowerCase()])
        assertNotNull academicCredentials
        assertEquals academicCredentials.size() , Degree.findAllByDegreeType(AcademicCredentialType.HONORARY.value,[max:'500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with type of diploma
     */
    @Test
    void testListWithoutPaginationParamsByDiplomaType() {
        List academicCredentials = academicCredentialsCompositeService.list([type: AcademicCredentialType.DIPLOMA.name().toLowerCase()])
        assertNotNull academicCredentials
        assertEquals academicCredentials.size() , Degree.findAllByDegreeType(AcademicCredentialType.DIPLOMA.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with type of certificate
     */
    @Test
    void testListWithoutPaginationParamsByCertificateType() {
        List academicCredentials = academicCredentialsCompositeService.list([type: AcademicCredentialType.CERTIFICATE.name().toLowerCase()])
        assertNotNull academicCredentials
        assertEquals academicCredentials.size() , Degree.findAllByDegreeType(AcademicCredentialType.CERTIFICATE.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService list method with Invalid type
     */
    @Test
    void testListWithInvalidType() {
        List academicCredentials = academicCredentialsCompositeService.list([type: "INVALID_TYPE"])
        assertNotNull academicCredentials
        assertTrue academicCredentials.isEmpty()
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService get method with guid as a null
     */
    @Test
    void testGetWithNullGuid() {
        try {
            academicCredentialsCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService get method with guid as an empty
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
            academicCredentialsCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService get method with other than academic credentials valid guid
     */
    @Test
    void testGetWithValidGuidAndNonExitsInAcademicCredentials(){
        shouldFail(RestfulApiValidationException) {
            academicCredentialsCompositeService.get(invalid_resource_guid?.guid)//invalid_resource_guid variable is defined at the top of the class
        }
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid(){
       assertNotNull success_guid
       def guid=success_guid?.guid //success_guid variable is defined at the top of the class
       assertNotNull guid
       def  academicCredential= academicCredentialsCompositeService.get(guid)
       assertNotNull academicCredential
       assertNotNull academicCredential.abbreviation
       assertNotNull academicCredential.guid
       assertNotNull academicCredential.type
    }

    /**
     * This test case is checking for AcademicCredentialsCompositeService get method with an invalid guid
     */
    @Test
    void testGetWithInValidGuid(){
        assertNotNull success_guid
        def guid=success_guid?.guid//success_guid variable is defined at the top of the class
        assertNotNull guid
        try {
            academicCredentialsCompositeService.get(guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * <p> Test to check the sort order  and sorting field  by type on AcademicCredentialsCompositeService</p>
     * */
    @Test
    public void testSortOrderByType(){
        params.order='DESC'
        params.sort='type'
        List list = academicCredentialsCompositeService.list(params)
        String tempParam
        list.each{
            academicCredential->
                String type=academicCredential.type
                if(!tempParam){
                    tempParam=type
                }
                assertTrue tempParam.compareTo(type)>0 || tempParam.compareTo(type)==0
                tempParam=type
        }

        params.clear()
        params.order='ASC'
        params.sort='type'
        list = academicCredentialsCompositeService.list(params)
        tempParam=null
        list.each{
            academicCredential->
                String type=academicCredential.type
                if(!tempParam){
                    tempParam=type
                }
                assertTrue tempParam.compareTo(type)<0 || tempParam.compareTo(type)==0
                tempParam=type
        }
    }

   }
