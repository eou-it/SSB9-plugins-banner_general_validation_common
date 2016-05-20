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
 * Integration Test cases for LocationTypeReadOnly, which is Read Only view.
 */
class LocationTypeReadOnlyIntegrationTests extends BaseIntegrationTestCase {
    private String i_success_locationType = 'billing'
    private String i_failure_locationType = 'testFailure'
    private String i_success_description = 'Business'
    private String i_success_code = 'BU'
    private String i_failure_description = 'Test data Update'

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
     * TestCase to check fetch list  on view
     */
    @Test
    void testFetchListOnView() {
        def params = [max: '500', offset: '0',order: 'ASC']
        def locationTypeList= LocationTypeReadOnly.list(params)
        assertNotNull locationTypeList
        assertTrue locationTypeList.locationType.contains(i_success_locationType)
        assertTrue locationTypeList.description.contains(i_success_description)
        assertTrue locationTypeList.code.contains(i_success_code)
        assertFalse locationTypeList.locationType.contains(i_failure_locationType)
    }

    /**
     * Test Case to  check creation of a new record on the read only view
     */
    @Test
    void testReadOnlyForCreateLocationType(){
        def locationType = newLocationType()
        locationType.id='test'
        locationType.version= 0
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
        LocationTypeReadOnly locationType = LocationTypeReadOnly.findByLocationType(i_success_locationType)
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
        LocationTypeReadOnly locationType = LocationTypeReadOnly.findByLocationType(i_success_locationType)
        assertNotNull locationType
        shouldFail(InvalidDataAccessResourceUsageException) {
            locationType.delete(flush: true, onError: true)
        }
    }


    private def newLocationType(){
        new LocationTypeReadOnly(
                code:'tt',
                description:'test data',
                dataOrigin:'test',
                id:'test',
                locationType:'test',
        )

    }
}

