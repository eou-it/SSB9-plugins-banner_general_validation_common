/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class GlobalUniqueIdentifierIntegrationTests extends BaseIntegrationTestCase {

    String i_success_guid = 'ABC12-DEF34-GHI56-JKL78'
    String i_success_ldmName = 'ldmName'
    Long i_success_domainId = 1L
    String i_success_domainKey = 'XX'

    Long u_success_domainId = 2L

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testCreateGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        save globalUniqueIdentifier
        assertNotNull globalUniqueIdentifier.id
        assertEquals 0L, globalUniqueIdentifier.version
        assertEquals i_success_guid, globalUniqueIdentifier.guid
        assertEquals i_success_ldmName, globalUniqueIdentifier.ldmName
        assertEquals i_success_domainId, globalUniqueIdentifier.domainId
        assertEquals i_success_domainKey, globalUniqueIdentifier.domainKey
    }

    @Test
    void testUpdateGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        save globalUniqueIdentifier
        assertNotNull globalUniqueIdentifier.id
        assertEquals 0L, globalUniqueIdentifier.version

        globalUniqueIdentifier.domainId = u_success_domainId
        save globalUniqueIdentifier

        globalUniqueIdentifier = GlobalUniqueIdentifier.get(globalUniqueIdentifier.id)
        assertEquals 1L, globalUniqueIdentifier.version
        assertEquals u_success_domainId, globalUniqueIdentifier.domainId
    }

    @Test
    void testDeleteGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        save globalUniqueIdentifier
        def id = globalUniqueIdentifier.id
        assertNotNull id

        globalUniqueIdentifier.delete(flush: true)
        assertNull GlobalUniqueIdentifier.get(id)
    }

    @Test
    void testOptimisticLock() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        save globalUniqueIdentifier
        executeUpdateSQL("update GORGUID set GORGUID_VERSION = 999 where GORGUID_SURROGATE_ID = ?", globalUniqueIdentifier.id)
        globalUniqueIdentifier.domainId = u_success_domainId
        shouldFail(HibernateOptimisticLockingFailureException) {
            globalUniqueIdentifier.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testNullableConstraints() {
        GlobalUniqueIdentifier globalUniqueIdentifier = new GlobalUniqueIdentifier()
        assertFalse globalUniqueIdentifier.validate()
        assertErrorsFor globalUniqueIdentifier, 'nullable', ['guid', 'ldmName', 'domainId']
        assertNoErrorsFor globalUniqueIdentifier, ['domainKey']
    }

    @Test
    void testMaxSizeConstraints() {
        GlobalUniqueIdentifier globalUniqueIdentifier = new GlobalUniqueIdentifier(
                guid: 'A' * 37,
                ldmName: 'A' * 101,
                domainKey: 'A' * 101
        )
        assertFalse globalUniqueIdentifier.validate()
        assertErrorsFor globalUniqueIdentifier, 'maxSize', ['guid', 'ldmName', 'domainKey']
    }

    @Test
    void testFetchByLdmNameAndGuidForInvalidLdmName() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        globalUniqueIdentifier = globalUniqueIdentifier.save(failOnError: true, flush: true)
        assertNotNull globalUniqueIdentifier.id
        assertNotNull globalUniqueIdentifier.guid
        assertNull GlobalUniqueIdentifier.fetchByLdmNameAndGuid('invalid-ldm-name', globalUniqueIdentifier.guid)
    }

    @Test
    void testFetchByLdmNameAndGuid() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        globalUniqueIdentifier = globalUniqueIdentifier.save(failOnError: true, flush: true)
        assertNotNull globalUniqueIdentifier.id
        assertNotNull globalUniqueIdentifier.guid
        def globalUniqueIdentifier1 = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(i_success_ldmName, globalUniqueIdentifier.guid)
        assertNotNull globalUniqueIdentifier1
        assertEquals globalUniqueIdentifier, globalUniqueIdentifier1
    }


    private GlobalUniqueIdentifier createNewGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = new GlobalUniqueIdentifier(
                guid: i_success_guid,
                ldmName: i_success_ldmName,
                domainId: i_success_domainId,
                domainKey: i_success_domainKey
        )
        return globalUniqueIdentifier
    }
}
