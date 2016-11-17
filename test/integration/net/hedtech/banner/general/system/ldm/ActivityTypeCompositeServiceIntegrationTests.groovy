/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ActivityType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for Activity Type composite service.</p>
 */
class ActivityTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def activityTypeCompositeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testGetActivityTypeCodeToGuidMap() {
        ActivityType activityType = save newActivityType()
        assertNotNull activityType
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACTIVITY_TYPE_LDM_NAME, activityType.id)
        assertNotNull globalUniqueIdentifier
        Map<String,String> activityTypeCodeToGuidMap = activityTypeCompositeService.getActivityTypeCodeToGuidMap([activityType.code])
        assertFalse activityTypeCodeToGuidMap.isEmpty()
        assertTrue activityTypeCodeToGuidMap.containsKey(activityType.code)
        assertEquals globalUniqueIdentifier.guid, activityTypeCodeToGuidMap.get(activityType.code)
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
