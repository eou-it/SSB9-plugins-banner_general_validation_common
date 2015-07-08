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
 * Integration Test cases for LocationTypeView, which is Read Only view.
 */
class LocationTypeViewIntegrationTests extends BaseIntegrationTestCase {
    private String i_success_translationValue = 'billing'
    private String i_failure_translationValue = 'testFailure'
    private String i_success_description = 'Business'
    private String i_success_code = 'BU'
    private String i_failure_description = 'Test data Update'
    private String typePerson = 'person'
    private String typeOrganization = 'organization'

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
     * Test Case to match the total view count with the personLocation type count and the organizationLocation type count
     */
    @Test
    void testViewCount() {
        def locationTypeCount= LocationTypeView.count()
        assertNotNull locationTypeCount
        def personLocationTypeCount = LocationTypeView.findAllByLocationType(typePerson).size()
        assertNotNull personLocationTypeCount
        def orgLocationTypeCount = LocationTypeView.findAllByLocationType(typeOrganization).size()
        assertNotNull orgLocationTypeCount
        assertEquals locationTypeCount,personLocationTypeCount+orgLocationTypeCount
    }

    /**
     * TestCase to check fetch list  on view
     */
    @Test
    void testFetchListOnView() {
        def params = [max: '500', offset: '0',order: 'ASC']
        def locationTypeList= LocationTypeView.list(params)
        assertNotNull locationTypeList
        assertTrue locationTypeList.translationValue.contains(i_success_translationValue)
        assertTrue locationTypeList.description.contains(i_success_description)
        assertTrue locationTypeList.code.contains(i_success_code)
        assertFalse locationTypeList.translationValue.contains(i_failure_translationValue)
    }

    /**
     * Test Case to  check creation of a new record on the read only view
     */
    @Test
    void testReadOnlyForCreateLocationType(){
        def locationType = newLocationType()
        assertNotNull locationType
        shouldFail(InvalidDataAccessResourceUsageException) {
            locationType.save(flush: true, onError: true)
        }
    }

    /**
     * Test Case to  check update on one of the records on the read only view
     */
    @Test
    void testReadOnlyForUpdateLocationType(){
        LocationTypeView locationType = LocationTypeView.findByTranslationValue(i_success_translationValue)
        assertNotNull locationType
        locationType.description=i_failure_description
        shouldFail(InvalidDataAccessResourceUsageException) {
            locationType.save(flush: true, onError: true)
        }
    }

    /**
     * Test Case to  check the deleting a record from the readonly view
     */
    @Test
    void testReadOnlyForDeleteLocationType(){
        LocationTypeView locationType = LocationTypeView.findByTranslationValue(i_success_translationValue)
        assertNotNull locationType
        shouldFail(InvalidDataAccessResourceUsageException) {
            locationType.delete(flush: true, onError: true)
        }
    }


    private def newLocationType(){
        new LocationTypeView(
                code:'tt',
                description:'test data',
                dataOrigin:'test',
                id:'test_guid',
                translationValue:'test',
                type:'person'
        )
        
    }
}

