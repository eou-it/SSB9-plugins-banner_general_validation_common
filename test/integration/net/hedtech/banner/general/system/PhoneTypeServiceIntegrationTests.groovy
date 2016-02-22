/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Service Integration Test cases for PhoneType, which is Read Only view.
 */
class PhoneTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def phoneTypeService
    private String i_success_phoneType = 'pager'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
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
    void testCreate() {
        PhoneType phoneType = newPhoneType()
        try{
            phoneTypeService.create(phoneType)
        }catch (ApplicationException ae){
            assertApplicationException ae, GeneralValidationCommonConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED
        }
    }


    /**
     * This test case is checking for updating one of record on read only view
     */
    @Test
    void testUpdate() {
        PhoneType phoneType = PhoneType.findByPhoneType(i_success_phoneType)
        phoneType.description = 'test'
        shouldFail(ApplicationException){
            phoneTypeService.update(phoneType)
        }
    }

    /**
     * This test case is checking for deletion one of record on read only view
     */
    @Test
    void testDelete() {
        PhoneType phoneType = PhoneType.findByPhoneType(i_success_phoneType)
        try{
            phoneTypeService.delete(phoneType)
        }catch (ApplicationException ae){
            assertApplicationException ae, GeneralValidationCommonConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED
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

