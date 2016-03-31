/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AcademicCredential
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
class AcademicCredentialCompositeServiceIntegrationTest extends  BaseIntegrationTestCase  {
    
    def academicCredentialCompositeService
    def supplementalDataService
    
    def success_guid
    def invalid_resource_guid
    def i_success_content

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        invalid_resource_guid=GlobalUniqueIdentifier.findByLdmName('subjects')
        success_guid=GlobalUniqueIdentifier.findByLdmName('academic-credentials')
        i_success_content = [code: 'TEST',description:'Test Title',type:'degree', metadata: [dataOrigin: 'Banner']]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService count method
     */
    @Test
    void testCount(){
        def expectedCount= academicCredentialCompositeService.count([:])
        assertNotNull expectedCount
        def actualCount= Degree.count()
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService count method with degree type
     */
    @Test
    void testCountByDegreeType(){
        def expectedCount= academicCredentialCompositeService.count([type:AcademicCredentialType.DEGREE.value])
        assertNotNull expectedCount
        def actualCount= AcademicCredential.countByType(AcademicCredentialType.DEGREE.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService count method with honorary type
     */
    @Test
    void testCountByHonoraryType(){
        def expectedCount= academicCredentialCompositeService.count([type:AcademicCredentialType.HONORARY.value])
        assertNotNull expectedCount
        def actualCount= AcademicCredential.countByType(AcademicCredentialType.HONORARY.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService count method with diploma type
     */
    @Test
    void testCountByDiplomaType(){
        def expectedCount= academicCredentialCompositeService.count([type:AcademicCredentialType.DIPLOMA.value])
        assertNotNull expectedCount
        def actualCount= AcademicCredential.countByType(AcademicCredentialType.DIPLOMA.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService count method with certificate type
     */
    @Test
    void testCountByCertificateType(){
        def expectedCount= academicCredentialCompositeService.count([type:AcademicCredentialType.CERTIFICATE.value])
        assertNotNull expectedCount
        def actualCount= AcademicCredential.countByType(AcademicCredentialType.CERTIFICATE.value)
        assertNotNull actualCount
        assertEquals expectedCount,actualCount
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method passing with invalid sort order
     */
    @Test
    void testListWithInvalidSortOrder(){
        shouldFail(RestfulApiValidationException) {
            def map = [order:'test']
            academicCredentialCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method passing with invalid sort field
     */
    @Test
    void testListWithInvalidSortField(){
        shouldFail(RestfulApiValidationException) {
            def map = [sort:'code']
            academicCredentialCompositeService.list(map)
        }
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method without pagination
     */
    @Test
    void testListWithoutPaginationParams() {
        List academicCredentials = academicCredentialCompositeService.list([:])
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertEquals academicCredentials.size() , Degree.list([max:'500']).size()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with pagination (max 4 and offset 0)
     */
    @Test
    void testListWithPagination() {
        def paginationParams = [max: '4', offset: '0']
        List academicCredentials = academicCredentialCompositeService.list(paginationParams)
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertTrue academicCredentials.size() == 4
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with type of degree
     */
    @Test
    void testListWithoutPaginationParamsByDegreeType() {
        List academicCredentials = academicCredentialCompositeService.list([type: AcademicCredentialType.DEGREE.value])
        assertNotNull academicCredentials
        assertFalse academicCredentials.isEmpty()
        assertTrue academicCredentials.type.containsAll(AcademicCredentialType.DEGREE.value)
        assertEquals academicCredentials.size() , AcademicCredential.findAllByType(AcademicCredentialType.DEGREE.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with type of honorary
     */
    @Test
    void testListWithoutPaginationParamsByHonoraryType() {
        List academicCredentials = academicCredentialCompositeService.list([type: AcademicCredentialType.HONORARY.value])
        assertNotNull academicCredentials
      //  assertTrue academicCredentials.type.containsAll(AcademicCredentialType.HONORARY.value)
        assertEquals academicCredentials.size() , AcademicCredential.findAllByType(AcademicCredentialType.HONORARY.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with type of diploma
     */
    @Test
    void testListWithoutPaginationParamsByDiplomaType() {
        List academicCredentials = academicCredentialCompositeService.list([type: AcademicCredentialType.DIPLOMA.value])
        assertNotNull academicCredentials
      //  assertTrue academicCredentials.type.containsAll(AcademicCredentialType.DIPLOMA.value)
        assertEquals academicCredentials.size() , AcademicCredential.findAllByType(AcademicCredentialType.DIPLOMA.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with type of certificate
     */
    @Test
    void testListWithoutPaginationParamsByCertificateType() {
        List academicCredentials = academicCredentialCompositeService.list([type: AcademicCredentialType.CERTIFICATE.value])
        assertNotNull academicCredentials
      //  assertTrue academicCredentials.type.containsAll(AcademicCredentialType.CERTIFICATE.value)
        assertEquals academicCredentials.size() , AcademicCredential.findAllByType(AcademicCredentialType.CERTIFICATE.value,[max: '500'])?.size()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService list method with Invalid type
     */
    @Test
    void testListWithInvalidType() {
        List academicCredentials = academicCredentialCompositeService.list([type: "INVALID_TYPE"])
        assertNotNull academicCredentials
        assertTrue academicCredentials.isEmpty()
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService get method with guid as a null
     */
    @Test
    void testGetWithNullGuid() {
        try {
            academicCredentialCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService get method with guid as an empty
     */
    @Test
    void testGetWithEmptyGuid() {
        try {
            academicCredentialCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService get method with other than academic credentials valid guid
     */
    @Test
    void testGetWithValidGuidAndNonExitsInAcademicCredentials(){
        try {
            academicCredentialCompositeService.get(invalid_resource_guid?.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService get method with valid guid
     */
    @Test
    void testGetWithValidGuid(){
       assertNotNull success_guid
       def guid=success_guid?.guid //success_guid variable is defined at the top of the class
       assertNotNull guid
       def  academicCredential= academicCredentialCompositeService.get(guid)
       assertNotNull academicCredential
        assertNotNull academicCredential.code
       assertNotNull academicCredential.guid
    }

    /**
     * This test case is checking for AcademicCredentialCompositeService get method with an invalid guid
     */
    @Test
    void testGetWithInValidGuid(){
        assertNotNull success_guid
        def guid=success_guid?.guid//success_guid variable is defined at the top of the class
        assertNotNull guid
        try {
            academicCredentialCompositeService.get(guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * <p> Test to check the sort order  and sorting field  by type on AcademicCredentialCompositeService</p>
     * */
    @Test
    public void testSortOrderByType(){
        params.order='DESC'
        params.sort='type'
        List list = academicCredentialCompositeService.list(params)
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
        list = academicCredentialCompositeService.list(params)
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

    @Test
    void testCreate() {
        i_success_content.supplementalDesc = 'test supplement description'
        def academicCredential = academicCredentialCompositeService.create(i_success_content)
        assertNotNull academicCredential
        assertNotNull academicCredential.guid
        assertEquals i_success_content.code, academicCredential.code
        assertEquals i_success_content.description, academicCredential.description
        assertEquals i_success_content.supplementalDesc , academicCredential.supplementalDesc
        assertTrue AcademicCredentialType.values().value.contains(academicCredential.type)
    }

    @Test
    void testCreateExistsCode(){
        i_success_content.code='PHD'
        try{
            academicCredentialCompositeService.create(i_success_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "code.exists.message"
        }
    }

    @Test
    void testCreateNullCode(){
        i_success_content.code=null
        try{
            academicCredentialCompositeService.create(i_success_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "code.required.message"
        }
    }

    @Test
    void testCreateNullDescription(){
        i_success_content.description=null
        try{
            academicCredentialCompositeService.create(i_success_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "description.required.message"
        }
    }

    @Test
    void testCreateInvalidType(){
        i_success_content.type='test'
        try{
            academicCredentialCompositeService.create(i_success_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "invalid.type.message"
        }
    }

    @Test
    void testUpdate() {
        assertNotNull success_guid
        i_success_content.id = success_guid.guid
        i_success_content.supplementalDesc = 'test supplement description'
        i_success_content.type = AcademicCredentialType.CERTIFICATE.value
        def academicCredential = academicCredentialCompositeService.update(i_success_content)
        assertNotNull academicCredential
        assertNotNull academicCredential.guid
        assertEquals i_success_content.description, academicCredential.description
        assertEquals  i_success_content.type, academicCredential.type
        assertTrue AcademicCredentialType.values().value.contains(academicCredential.type)
        assertEquals i_success_content.supplementalDesc , academicCredential.supplementalDesc
    }

    @Test
    void testUpdateNullGuid() {
        i_success_content.put('id', null)
        try{
            academicCredentialCompositeService.update(i_success_content)
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testUpdateNonExistsGuid() {
        i_success_content.put('id', 'TEST_GUID')
        def academicCredential = academicCredentialCompositeService.update(i_success_content)
        assertNotNull academicCredential
        assertNotNull academicCredential.guid
        assertEquals i_success_content.id?.trim()?.toLowerCase(), academicCredential.guid
        assertEquals i_success_content.code, academicCredential.code
        assertEquals i_success_content.description, academicCredential.description
        assertTrue AcademicCredentialType.values().value.contains(academicCredential.type)
    }

    @Test
    void testMandatoryTypeField(){
        List<AcademicCredential> academicCredentialList = academicCredentialCompositeService.list(params)
        academicCredentialList.each{
            academicCredential->
                Degree degree = Degree.findByCode(academicCredential.code)
                if(supplementalDataService.hasSdeData(degree)){
                    def sdeModel = supplementalDataService.loadSupplementalDataForModel(degree)
                    assertEquals academicCredential.type,sdeModel.HEDM_CREDENTIAL_CATEGORY."1".value
                }else{
                    assertNotNull academicCredential
                    assertEquals 'degree',academicCredential.type
                }
        }
    }

   }
