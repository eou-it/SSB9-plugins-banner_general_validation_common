/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * <p>Integration Test case for EmailTypeView which is a Read only view</p>
 * */
class EmailTypeViewIntegrationTests extends BaseIntegrationTestCase{

    public static final String EMAIL_TYPE_HEDM_NAME = 'email-types'
    private final String MAIL_CODE='SCHL'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to get the record based on type and validate them</p>
     * */
    @Test
    public void testFetchDataByEntityType(){
        def type = 'PERSON'
        List<EmailTypesView> emailTypesViews= EmailTypesView.findAllByEntityType(type)
        assertFalse emailTypesViews==null

        for(EmailTypesView typesView : emailTypesViews){
            assertEquals typesView.entityType,type
        }

        type = 'ORGANIZATION'
        emailTypesViews= EmailTypesView.findAllByEntityType(type)
        assertFalse emailTypesViews==null

        for(EmailTypesView typesView : emailTypesViews){
            assertEquals typesView.entityType,type
        }

    }

    /**
     * <p> Test to check the count on EmailTypeView for both 'PERSON' as well as 'ORGANIZATION' type</p>
     * */
    @Test
    void testCount(){
        def type = 'PERSON'
        List<EmailTypesView> emailTypesOnPerson= EmailTypesView.findAllByEntityType(type)
        assertFalse emailTypesOnPerson==null

        type = 'ORGANIZATION'
        List<EmailTypesView> emailTypesforOrganization= EmailTypesView.findAllByEntityType(type)
        assertFalse emailTypesforOrganization==null

        assertEquals EmailTypesView.count(),emailTypesOnPerson.size()+emailTypesforOrganization.size()

    }
    /**
     * <p> Test to get the record from EmailType based on Guid</p>
     * */
    @Test
    void testFetchByguid() {
        String emailGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(EMAIL_TYPE_HEDM_NAME, MAIL_CODE)?.guid
        assertNotNull emailGuid
        EmailTypesView emailTypesView = EmailTypesView.findByGuid(emailGuid)
        assertNotNull emailTypesView
        assertEquals MAIL_CODE,emailTypesView.code
    }

    /**
     * <p> Test to get the no record from EmailType by passing an invalid guid</p>
     * */
    @Test
    void testFetchByInvalidGuid() {
        String emailGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(EMAIL_TYPE_HEDM_NAME, 'BUSI')?.guid
        assertNotNull emailGuid
        def emailTypesView = EmailTypesView.findByGuid(emailGuid.substring(0,10));
        assertNull emailTypesView
    }

    /**
     * <p> Test to create a record on EmailType which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForCreateEmailType(){
        def emailType = newEmailType()
        emailType.id = new EmailTypePrimary(settingValue:'AA',processCode:'BB')
        emailType.version=0
        assertNotNull emailType
        shouldFail(InvalidDataAccessResourceUsageException) {
            emailType.save(flush: true, onError: true)
        }
    }

    /**
     * <p>Test to update on EmailTypeView which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForUpdateEmailType(){
        String emailTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(EMAIL_TYPE_HEDM_NAME, MAIL_CODE)?.guid
        assertNotNull emailTypeGuid
        def emailType = EmailTypesView.findByGuid(emailTypeGuid)
        assertNotNull emailType
        emailType.description='Dummy Value'
        shouldFail(InvalidDataAccessResourceUsageException) {
            emailType.save(flush: true, onError: true)
        }
    }


    /**
     * <p>Test to delete on EmailTypeView which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForDeleteEmailType(){
        String emailTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(EMAIL_TYPE_HEDM_NAME, MAIL_CODE)?.guid
        assertNotNull emailTypeGuid
        def emailType = EmailTypesView.findByGuid(emailTypeGuid)
        assertNotNull emailType
        shouldFail(InvalidDataAccessResourceUsageException) {
            emailType.delete(flush: true, onError: true)
        }
    }

    private def newEmailType(){
        def compositeEmailType = new EmailTypePrimary(settingValue:'AA',processCode:'BB')
        new EmailTypesView(
                emailTypePrimary: compositeEmailType,code: 'SS',description: 'Dummy Description',entityType: EMAIL_TYPE_HEDM_NAME
        )
    }

}
