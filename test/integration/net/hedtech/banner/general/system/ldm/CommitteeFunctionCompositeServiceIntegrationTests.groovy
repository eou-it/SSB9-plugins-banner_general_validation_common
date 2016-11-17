/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.CommitteeFunction
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for Committee Function composite service.</p>
 */
class CommitteeFunctionCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def committeeFunctionCompositeService


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
    void testGetCommitteeFunctionCodeToGuidMap() {
        CommitteeFunction committeeFunction = save newCommitteeFunction()
        assertNotNull committeeFunction
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.COMMITTEE_FUNCTION_LDM_NAME, committeeFunction.id)
        assertNotNull globalUniqueIdentifier
        Map<String,String> committeeFunctionCodeToGuidMap = committeeFunctionCompositeService.getCommitteeFunctionCodeToGuidMap([committeeFunction.code])
        assertFalse committeeFunctionCodeToGuidMap.isEmpty()
        assertTrue committeeFunctionCodeToGuidMap.containsKey(committeeFunction.code)
        assertEquals globalUniqueIdentifier.guid, committeeFunctionCodeToGuidMap.get(committeeFunction.code)
    }


    private def newCommitteeFunction() {
        def committeeFunction = new CommitteeFunction(
                code: "TT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return committeeFunction
    }

}
