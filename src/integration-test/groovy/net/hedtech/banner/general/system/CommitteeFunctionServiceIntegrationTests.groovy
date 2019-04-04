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
 * Integration test cases for Committee Function service.
 */
class CommitteeFunctionServiceIntegrationTests extends BaseIntegrationTestCase {

    def committeeFunctionService
    final String I_SUCCESS_CODE = "01"


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
        CommitteeFunction committeeFunction = CommitteeFunction.findByCode(I_SUCCESS_CODE)
        assertNotNull committeeFunction
        List CommitteeFunctionWithGuidList = committeeFunctionService.fetchAllWithGuidByCodeInList([committeeFunction.code], 1, 0)
        assertFalse CommitteeFunctionWithGuidList.isEmpty()
        assertEquals 1, CommitteeFunctionWithGuidList.size()
        CommitteeFunctionWithGuidList.each {
            CommitteeFunction actualCommitteeFunction = it.getAt('committeeFunction')
            assertNotNull actualCommitteeFunction
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.COMMITTEE_FUNCTION_LDM_NAME, actualCommitteeFunction.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt('globalUniqueIdentifier')
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
            assertEquals actualCommitteeFunction, committeeFunction
        }
    }


    @Test
    void testFetchAllByCodeInList() {
        CommitteeFunction committeeFunction = CommitteeFunction.findByCode(I_SUCCESS_CODE)
        assertNotNull committeeFunction
        List<CommitteeFunction> committeeFunctionList = committeeFunctionService.fetchAllByCodeInList([committeeFunction.code])
        assertFalse committeeFunctionList.isEmpty()
        assertEquals 1, committeeFunctionList.size()
        committeeFunctionList.each {
            assertEquals it, committeeFunction
        }
    }

    @Test
    void testFetchAllWithGuid() {
        List committeeFunctionWithGuidList = committeeFunctionService.fetchAllWithGuid(1, 0)
        assertFalse committeeFunctionWithGuidList.isEmpty()
        assertEquals 1, committeeFunctionWithGuidList.size()
        committeeFunctionWithGuidList.each {
            CommitteeFunction actualCommitteeFunction = it.getAt('committeeFunction')
            assertNotNull actualCommitteeFunction
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.COMMITTEE_FUNCTION_LDM_NAME, actualCommitteeFunction.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt('globalUniqueIdentifier')
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
        }
    }


    private def newCommitteeFunction() {
        def committeeFunction = new CommitteeFunction(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return committeeFunction
    }

}
