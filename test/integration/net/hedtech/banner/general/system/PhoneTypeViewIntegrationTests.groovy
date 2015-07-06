/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

/**
 * Integration Test cases for PhoneTypeView, which is Read Only view.
 */
class PhoneTypeViewIntegrationTests extends BaseIntegrationTestCase {


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

