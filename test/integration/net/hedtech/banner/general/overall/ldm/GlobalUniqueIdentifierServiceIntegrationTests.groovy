package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class GlobalUniqueIdentifierServiceIntegrationTests extends BaseIntegrationTestCase {

    String i_success_guid = 'ABC12-DEF34-GHI56-JKL78'
    String i_success_ldmName = 'ldmName'
    Long i_success_domainId = 1L
    String i_success_domainKey = 'XX'

    Long u_success_domainId = 2L

    def globalUniqueIdentifierService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testCreateGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        globalUniqueIdentifier = globalUniqueIdentifierService.create([domainModel: globalUniqueIdentifier])
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
        globalUniqueIdentifier = globalUniqueIdentifierService.create([domainModel: globalUniqueIdentifier])
        assertNotNull globalUniqueIdentifier.id
        assertEquals 0L, globalUniqueIdentifier.version

        globalUniqueIdentifier.domainId = u_success_domainId
        globalUniqueIdentifier = globalUniqueIdentifierService.update([domainModel: globalUniqueIdentifier])

        globalUniqueIdentifier = GlobalUniqueIdentifier.get(globalUniqueIdentifier.id)
        assertEquals 1L, globalUniqueIdentifier.version
        assertEquals u_success_domainId, globalUniqueIdentifier.domainId
    }

    @Test
    void testDeleteGlobalUniqueIdentifier() {
        GlobalUniqueIdentifier globalUniqueIdentifier = createNewGlobalUniqueIdentifier()
        globalUniqueIdentifier = globalUniqueIdentifierService.create([domainModel: globalUniqueIdentifier])
        def id = globalUniqueIdentifier.id
        assertNotNull id

        globalUniqueIdentifierService.delete([domainModel: globalUniqueIdentifier])
        assertNull GlobalUniqueIdentifier.get(id)
    }

    @Test
    void testFetchByLdmNameAndDomainKeyLike() {
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = globalUniqueIdentifierService.fetchAllByLdmNameAndDomainKeyLike('instructional-events', '201410-^20434')
        globalUniqueIdentifierList.each {
            assertTrue "expected to contain 201410-^20434 but had ${it.domainKey}", it.domainKey.contains('201410-^20434')
        }
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
