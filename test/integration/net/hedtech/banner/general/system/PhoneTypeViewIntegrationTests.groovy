/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
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
 * Integration Test cases for PhoneTypeView, which is Read Only view.
 */
class PhoneTypeViewIntegrationTests extends BaseIntegrationTestCase {


    private String i_success_translationValue = 'pager'
    private String i_failure_translationValue = 'Pager'
    private String i_success_description = 'Billing'
    private String i_success_code = 'MOBL'

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
        shouldFail(InvalidDataAccessResourceUsageException) {
            phoneType.save(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for updating one of record on read only view
     */
    @Test
    void testReadOnlyForUpdatePhoneType(){
        PhoneTypeView phoneType = PhoneTypeView.findByTranslationValue('business')
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
        PhoneTypeView phoneType = PhoneTypeView.findByTranslationValue('business')
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
        List phoneTypeList= PhoneTypeView.list(params)
        assertNotNull phoneTypeList
        assertFalse phoneTypeList.isEmpty()
        assertTrue phoneTypeList.translationValue.contains(i_success_translationValue)
        assertTrue phoneTypeList.description.contains(i_success_description)
        assertTrue phoneTypeList.code.contains(i_success_code)
        assertFalse phoneTypeList.translationValue.contains(i_failure_translationValue)
    }

    /**
     * This test case is checking for Phone Type View get
     */
    @Test
    void testGet() {
       assertNull PhoneTypeView.get("")
       assertNull  PhoneTypeView.get(null)
        
       PhoneTypeView phoneTypeView=PhoneTypeView.findByTranslationValue(i_success_translationValue)
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
        
       IntegrationConfiguration integrationConfiguration=   IntegrationConfiguration.findBySettingNameAndTranslationValue('PERSON.PHONETYPES',i_success_translationValue)
       assertNotNull integrationConfiguration
       assertEquals integrationConfiguration.value, phoneTypeView.value
       assertEquals integrationConfiguration.value, telephoneType.code
    }


    private def newPhoneType(){
        new PhoneTypeView(
                code:'test',
                description:'test data',
                dataOrigin:'test',
                id:'test_guid',
                translationValue:'test',
                type:'person'
        )
        
    }
}

