/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.EmailTypesView
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
    Map i_success_input_content
    private String i_success_code = 'AOL'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        i_success_input_content = [code: 'AB', description: 'Test Description',type:[person:[emailType:"Home"]]]
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to lists the EmailTypes Records from EmailTypeCompositeService</p>
     * */
    @Test
    public void testListEmailTypes(){
        List emailTypes = emailTypeCompositeService.list(params)
        assertNotNull emailTypes
        assertFalse emailTypes.isEmpty()
        def totalCount = EmailTypesView.count()
        assertNotNull totalCount
        assertEquals totalCount, emailTypes.size()
    }

    /**
     * <p>Test to check the count on EmailTypeCompositeService</p>
     * */
    @Test
    public void testCount(){
        assertNotNull emailTypeCompositeService.count(params)
        assertNotNull EmailTypesView.count()
        assertEquals EmailTypesView.count(),emailTypeCompositeService.count(params)
    }

    /**
     * <p> Test to check the sort order and sorting field on EmailTypeCompositeService</p>
     * */
    @Test
    public void testSortOrder(){
        params.order='DESC'
        params.sort='code'
        List<EmailTypeDetails> list = emailTypeCompositeService.list(params)
        String tempParam
        list.each{
            emailType->
                String code=emailType?.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='ASC'
        params.sort='code'
        list = emailTypeCompositeService.list(params)
        tempParam=null
        list.each{
            emailType->
                String code=emailType?.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }
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

        assertEquals emailTypeDetails?.guid,emailTypeDetailsWithOffset?.guid
        assertEquals emailTypeDetails?.code,emailTypeDetailsWithOffset?.code
        assertEquals emailTypeDetails?.description,emailTypeDetailsWithOffset?.description

    }


    /**
     * <p> Test to get a single record from EmailTypeCompositeService</p>
     * */
    @Test
    public void testShow(){
        List list = emailTypeCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        EmailTypeDetails emailTypeDetails = list.get(0)
        assertNotNull emailTypeDetails

        EmailTypeDetails newEmailTypeDetails = emailTypeCompositeService.get(emailTypeDetails.guid)
        assertNotNull newEmailTypeDetails
        assertEquals emailTypeDetails.code,newEmailTypeDetails.code
        assertEquals emailTypeDetails.description,newEmailTypeDetails.description
        assertEquals emailTypeDetails.guid,newEmailTypeDetails.guid
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
            emailTypeCompositeService.get(emailTypeDetails.guid.substring(0,emailTypeDetails.guid.length()-2))
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
            assertApplicationException ae, "invalid.guid"
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
    void testCreateEmailType() {
        EmailTypeDetails emailType = emailTypeCompositeService.create(i_success_input_content)
        assertNotNull emailType
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


}
