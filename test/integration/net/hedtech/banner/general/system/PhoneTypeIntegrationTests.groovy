/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * Integration Test cases for PhoneType, which is Read Only view.
 */
class PhoneTypeIntegrationTests extends BaseIntegrationTestCase {


    private String i_success_phoneType = 'pager'
    private String i_failure_phoneType = 'Pager'
    private String i_success_description = 'Billing'
    private String i_success_code = 'HOME'

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
     * This test case is checking for creating one of record on read only view
     */
    @Test
    void testReadOnlyForCreatePhoneType(){
        def phoneType = newPhoneType()
        assertNotNull phoneType
        phoneType.id = 'test'
        shouldFail(InvalidDataAccessResourceUsageException) {
            phoneType.save(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for updating one of record on read only view
     */
    @Test
    void testReadOnlyForUpdatePhoneType(){
        PhoneType phoneType = PhoneType.findByPhoneType('business')
        assertNotNull phoneType
        phoneType.description='Test for Update'
        shouldFail(InvalidDataAccessResourceUsageException) {
            phoneType.save(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for deletion one of record on read only view
     */
    @Test
    void testReadOnlyForDeletePhoneType(){
        PhoneType phoneType = PhoneType.findByPhoneType('business')
        assertNotNull phoneType
        shouldFail(InvalidDataAccessResourceUsageException) {
            phoneType.delete(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for phone type view list
     */
    @Test
    void testList() {
        def params = [max: '500', offset: '0',order: 'ASC']
        List phoneTypeList= PhoneType.list(params)
        assertNotNull phoneTypeList
        assertFalse phoneTypeList.isEmpty()
        assertTrue phoneTypeList.phoneType.contains(i_success_phoneType)
        assertTrue phoneTypeList.description.contains(i_success_description)
        assertTrue phoneTypeList.code.contains(i_success_code)
        assertFalse phoneTypeList.phoneType.contains(i_failure_phoneType)
    }

    /**
     * This test case is checking for Phone Type View get
     */
    @Test
    void testGet() {
        assertNull PhoneType.get("")
        assertNull  PhoneType.get(null)

        PhoneType phoneTypeView=PhoneType.findByPhoneType(i_success_phoneType)
        assertNotNull phoneTypeView

        TelephoneType telephoneType= TelephoneType.findByCode(phoneTypeView.value)
        assertNotNull telephoneType

        assertEquals phoneTypeView.code,telephoneType.code
        assertEquals phoneTypeView.description,telephoneType.description
        assertEquals phoneTypeView.dataOrigin,telephoneType.dataOrigin

        GlobalUniqueIdentifier  globalUniqueIdentifier= GlobalUniqueIdentifier.findByLdmNameAndDomainKey('phone-types',telephoneType.code)
        assertEquals  globalUniqueIdentifier.guid,phoneTypeView.id
        assertEquals  globalUniqueIdentifier.domainKey,phoneTypeView.value
        assertEquals  globalUniqueIdentifier.domainKey,phoneTypeView.code

        IntegrationConfiguration integrationConfiguration=   IntegrationConfiguration.findBySettingNameAndTranslationValue('PHONES.PHONETYPE',i_success_phoneType)
        assertNotNull integrationConfiguration
        assertEquals integrationConfiguration.value, phoneTypeView.value
        assertEquals integrationConfiguration.value, telephoneType.code
    }


    @Test
    void testFetchByGuid(){
        def params = [max: '1', offset: '0']
        List<PhoneType> phoneTypeList= PhoneType.list(params)
        assertFalse phoneTypeList.isEmpty()

        phoneTypeList.each{
            PhoneType phoneType =  PhoneType.fetchByGuid(it.id)
            assertNotNull phoneType
            assertEquals it.phoneType, phoneType.phoneType
            assertEquals it.code, phoneType.code
            assertEquals it.id, phoneType.id
            assertEquals it.value,phoneType.value
            assertEquals it.description,phoneType.description
        }

    }

    private def newPhoneType(){
        new PhoneType(
                code:'test',
                description:'test data',
                dataOrigin:'test',
                id:'test_guid',
                phoneType:'test',
                entityType:'person'
        )

    }
}

