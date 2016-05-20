/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
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
class EmailTypeReadOnlyIntegrationTests extends BaseIntegrationTestCase{

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
     * <p> Test to get the record from EmailType based on Guid</p>
     * */
    @Test
    void testFetchByguid() {
        String emailGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(EMAIL_TYPE_HEDM_NAME, MAIL_CODE)?.guid
        assertNotNull emailGuid
        EmailTypeReadOnly emailTypesView = EmailTypeReadOnly.get(emailGuid)
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
        def emailTypesView = EmailTypeReadOnly.get(emailGuid.substring(0,10));
        assertNull emailTypesView
    }

    /**
     * <p> Test to create a record on EmailType which will return exception as this is a read-only view</p>
     */
    @Test
    void testReadOnlyForCreateEmailType(){
        def emailType = newEmailType()
        emailType.id = 'test_guid'
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
        def emailType = EmailTypeReadOnly.get(emailTypeGuid)
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
        def emailType = EmailTypeReadOnly.get(emailTypeGuid)
        assertNotNull emailType
        shouldFail(InvalidDataAccessResourceUsageException) {
            emailType.delete(flush: true, onError: true)
        }
    }

    private def newEmailType(){
     return   new EmailTypeReadOnly(
                id: 'test_guid',code: 'SS',description: 'Dummy Description',entityType: EMAIL_TYPE_HEDM_NAME
        )
    }

}
