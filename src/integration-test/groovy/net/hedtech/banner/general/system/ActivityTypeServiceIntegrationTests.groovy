/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test cases for Activity Type service.
 */
class ActivityTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def activityTypeService
    final String I_SUCCESS_CODE = "SPRTS"


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testFetchAllWithGuidByCodeInList() {
        ActivityType activityType = ActivityType.findByCode(I_SUCCESS_CODE)
        assertNotNull activityType
        List activityTypeWithGuidList = activityTypeService.fetchAllWithGuidByCodeInList([activityType.code], 1, 0)
        assertFalse activityTypeWithGuidList.isEmpty()
        assertEquals 1, activityTypeWithGuidList.size()
        activityTypeWithGuidList.each {
            ActivityType actualActivityType = it.getAt('activityType')
            assertNotNull actualActivityType
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACTIVITY_TYPE_LDM_NAME, actualActivityType.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt('globalUniqueIdentifier')
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
            assertEquals actualActivityType, activityType
        }
    }


    @Test
    void testFetchAllByCodeInList() {
        ActivityType activityType = ActivityType.findByCode(I_SUCCESS_CODE)
        assertNotNull activityType
        List<ActivityType> activityTypeList = activityTypeService.fetchAllByCodeInList([activityType.code])
        assertFalse activityTypeList.isEmpty()
        assertEquals 1, activityTypeList.size()
        activityTypeList.each {
            assertEquals it, activityType
        }
    }

    @Test
    void testFetchAllWithGuid() {
        List activityTypeWithGuidList = activityTypeService.fetchAllWithGuid(1, 0)
        assertFalse activityTypeWithGuidList.isEmpty()
        assertEquals 1, activityTypeWithGuidList.size()
        activityTypeWithGuidList.each {
            ActivityType actualActivityType = it.getAt('activityType')
            assertNotNull actualActivityType
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACTIVITY_TYPE_LDM_NAME, actualActivityType.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt('globalUniqueIdentifier')
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
        }
    }


    private def newActivityType() {
        def activityType = new ActivityType(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return activityType
    }

}
