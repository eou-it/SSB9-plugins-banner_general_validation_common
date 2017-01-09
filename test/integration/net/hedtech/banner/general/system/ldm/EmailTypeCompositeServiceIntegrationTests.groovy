/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.EmailTypeService
import net.hedtech.banner.general.system.ldm.v4.EmailTypeDetails
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
/**
 * <p>Integration Test cases for EmailTypeCompositeService</p>
 */
class EmailTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

    def emailTypeCompositeService
    EmailTypeService emailTypeService

    Map i_success_input_content
    private String i_success_code = 'AOL'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        i_success_input_content = [code: 'AB', description: 'Test Description',emailType:"Home"]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to lists the EmailTypes Records from EmailTypeCompositeService</p>
     * */
    @Test
    public void testListEmailTypes() {
        List emailTypes = emailTypeCompositeService.list(params)
        assertFalse emailTypes.isEmpty()

        Map<String, String> bannerEmailypeToHedmEmailTypeMap = emailTypeCompositeService.getBannerEmailTypeToHedmV6EmailTypeMap()
        assertFalse bannerEmailypeToHedmEmailTypeMap.isEmpty()

        List entities = emailTypeService.fetchAllWithGuidByCodeInList(bannerEmailypeToHedmEmailTypeMap.keySet(), 500, 0)
        assertFalse entities.isEmpty()

        assertEquals emailTypes.size(), entities.size()
        Iterator it1 = emailTypes.iterator()
        Iterator it2 = entities.iterator()

        while (it1.hasNext() && it2.hasNext()) {
            EmailTypeDetails emailTypeDetail = it1.next()
            List actualEntities = it2.next()
            EmailType emailType = actualEntities.getAt(0)
            assertEquals emailType.code, emailTypeDetail.code
            assertEquals emailType.description, emailTypeDetail.description
            GlobalUniqueIdentifier globalUniqueIdentifier = actualEntities.getAt(1)
            assertEquals globalUniqueIdentifier.guid, emailTypeDetail.id
            assertEquals emailTypeDetail.emailType, bannerEmailypeToHedmEmailTypeMap.get(emailType.code)
        }
    }

    /**
     * <p>Test to check the count on EmailTypeCompositeService</p>
     * */
    @Test
    public void testCount(){
        def actualCount = emailTypeCompositeService.count().toInteger()
        assertNotNull actualCount

        Map<String, String> bannerEmailTypeToHedmEmailTypeMap = emailTypeCompositeService.getBannerEmailTypeToHedmV6EmailTypeMap()
        assertFalse bannerEmailTypeToHedmEmailTypeMap.isEmpty()

        List<EmailType> emailTypeList = emailTypeService.fetchAllByCodeInList(bannerEmailTypeToHedmEmailTypeMap.keySet())
        assertFalse emailTypeList.isEmpty()
        assertEquals emailTypeList.size(), actualCount
    }


    /**
     * <p> Tests to check the offset criteria on EmailTypeCompositeService</p>
     * */
    @Test
    public void testOffsetCriteria(){
        List<EmailTypeDetails> emailTypeDetailses = emailTypeCompositeService.list(params)
        assertNotNull emailTypeDetailses
        params.offset='2'
        List emailTypeCompositeServiceNew = emailTypeCompositeService.list(params)
        assertNotNull emailTypeCompositeServiceNew

        EmailTypeDetails emailTypeDetails = emailTypeDetailses.get(2)
        assertNotNull emailTypeDetails
        EmailTypeDetails emailTypeDetailsWithOffset = emailTypeCompositeServiceNew.get(0)
        assertNotNull emailTypeDetails

        assertEquals emailTypeDetails.id,emailTypeDetailsWithOffset.id
        assertEquals emailTypeDetails.code,emailTypeDetailsWithOffset.code
        assertEquals emailTypeDetails.description,emailTypeDetailsWithOffset.description

    }


    /**
     * <p> Test to get a single record from EmailTypeCompositeService</p>
     * */
    @Test
    public void testShow(){
        List list = emailTypeCompositeService.list([max:'1'])
        assertFalse list.isEmpty()
        EmailTypeDetails emailTypeDetails = list.get(0)
        assertNotNull emailTypeDetails

        EmailTypeDetails newEmailTypeDetails = emailTypeCompositeService.get(emailTypeDetails.id)
        assertNotNull newEmailTypeDetails
        assertEquals emailTypeDetails.code,newEmailTypeDetails.code
        assertEquals emailTypeDetails.description,newEmailTypeDetails.description
        assertEquals emailTypeDetails.id,newEmailTypeDetails.id
        assertEquals newEmailTypeDetails.emailType, emailTypeDetails.emailType
    }

    /**
     * <p> Test to pass an invalid guid to EmailTypeCompositeService which will return an exception saying record not found</p>
     * */
    @Test
    public void testInvalidShow(){
        List list = emailTypeCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        EmailTypeDetails emailTypeDetails = list.get(0)
        assertNotNull emailTypeDetails

        try {
            emailTypeCompositeService.get(emailTypeDetails.id.substring(0,emailTypeDetails.id.length()-2))
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * <p> Test to pass another LDM guid to EmailTypeCompositeService which will throw exception saying Invalid Guid for EmailTypeCompositeService</p>
     * */
    @Test
    public void testDifferentLDMGuid(){
        String campusGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('campuses', 'M')?.guid
        assertNotNull campusGuid
        try {
            emailTypeCompositeService.get(campusGuid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    /**
     * <p> Test show for EmailTypeCompositeService with null guid </p>
     * */
    @Test
    void testGetNullGuid() {
        try {
            emailTypeCompositeService.get( null )
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetInvalidMappedGuid(){
        EmailTypeDetails emailType = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailType
        try {
            emailTypeCompositeService.get( emailType.id )
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testCreateEmailType() {
        EmailTypeDetails emailType = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailType
        assertNotNull emailType.toString()
        assertEquals i_success_input_content.code, emailType.code
        assertEquals i_success_input_content.description, emailType.description
    }

    @Test
    void testCreateEmailTypeWithoutMandatoryCode() {
        i_success_input_content.remove('code')
        try {
            emailTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required"
        }
    }

    @Test
    void testCreateEmailTypeExistingCode() {
        i_success_input_content.code=i_success_code
        try {
            emailTypeCompositeService.create(i_success_input_content)
        } catch (Exception ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    /**
     * Test to update the Email-Type with a valid request payload
     * */
    @Test
    void testUpdateEmailType() {
        EmailTypeDetails emailTypeDetails = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailTypeDetails
        assertNotNull emailTypeDetails.toString()
        assertNotNull emailTypeDetails.id
        assertEquals i_success_input_content.code, emailTypeDetails.code
        assertEquals i_success_input_content.description, emailTypeDetails.description
        Map update_content = updateEmailTypeMap(emailTypeDetails.id, 'UEMAIL')
        def o_success_EmailType_update = emailTypeCompositeService.update(update_content)
        assertNotNull o_success_EmailType_update
        assertEquals o_success_EmailType_update.id, update_content.id
        assertEquals o_success_EmailType_update.code, emailTypeDetails.code
        assertEquals o_success_EmailType_update.description, update_content.description
    }

    /**
     * Test to update the Email-Type with Invalid Guid
     * */
    @Test
    void testUpdateEmailTypeWithInvalidGuid() {
        EmailTypeDetails emailType = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailType
        assertNotNull emailType.id
        assertEquals i_success_input_content.code, emailType.code
        assertEquals i_success_input_content.description, emailType.description
        Map update_content = updateEmailTypeMap(null,emailType.code)
        shouldFail(ApplicationException) {
            emailTypeCompositeService.update(update_content)
        }
    }

    /**
     * Test to update the Email-Type with non existing Guid and Code for EmailTypeCompositeService create method invocation
     * */
    @Test
    void testUpdateEmailTypeWithCreateForNewCodeAndGuid() {
        i_success_input_content.put("id","test-guid")
        EmailTypeDetails emailType = emailTypeCompositeService.update(i_success_input_content)
        assertNotNull emailType
        assertNotNull emailType.id
        assertEquals i_success_input_content.id, emailType.id
        assertEquals i_success_input_content.code, emailType.code
        assertEquals i_success_input_content.description, emailType.description
    }

    /**
     * Test to check the EmailTypeCompositeService create method with existing code in the request payload
     */
    @Test
    void testUpdateEmailTypeWithExistingCode() {
        i_success_input_content.put("id","test-guid")
        i_success_input_content.code=i_success_code
        try {
            emailTypeCompositeService.update(i_success_input_content)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "exists.message"
        }
    }

    @Test
    void testGetEmailTypeCodeToGuidMap() {
        EmailTypeDetails emailType = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailType
        assertNotNull emailType.id
        Map<String,String> emailTypeCodeToGuidMap = emailTypeCompositeService.getEmailTypeCodeToGuidMap([emailType.code])
        assertFalse emailTypeCodeToGuidMap.isEmpty()
        assertTrue emailTypeCodeToGuidMap.containsKey(emailType.code)
        assertEquals emailType.id, emailTypeCodeToGuidMap.get(emailType.code)
    }

    @Test
    void testGetBannerEmailTypeToHedmV6EmailTypeMap() {
        EmailType emailType = EmailType.findByCode("BI")
        assertNotNull emailType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME_V6, emailType.code)
        assertNotNull intConf
        def map = emailTypeCompositeService.getBannerEmailTypeToHedmV6EmailTypeMap()
        assertNotNull map
        assertTrue map.containsKey(emailType.code)
        assertEquals map.get(emailType.code), HedmEmailType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V6]
    }

    @Test
    void testGetBannerEmailTypeToHedmV3EmailTypeMap() {
        EmailType emailType = EmailType.findByCode("HOME")
        assertNotNull emailType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME_V3, emailType.code)
        assertNotNull intConf
        def map = emailTypeCompositeService.getBannerEmailTypeToHedmV3EmailTypeMap()
        assertNotNull map
        assertTrue map.containsKey(emailType.code)
        assertEquals map.get(emailType.code), HedmEmailType.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V3).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V3]

    }


    private Map updateEmailTypeMap(id, code) {
        Map params = [id: id,code: code, description: 'Description Test',emailType:"Home"]
        return params
    }

}
