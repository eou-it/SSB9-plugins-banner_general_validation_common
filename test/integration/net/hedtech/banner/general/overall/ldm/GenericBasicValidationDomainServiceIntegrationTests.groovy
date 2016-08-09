package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Level
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException

class GenericBasicValidationDomainServiceIntegrationTests extends BaseIntegrationTestCase {

    def genericBasicValidationDomainService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        genericBasicValidationDomainService.baseDomain = null
        genericBasicValidationDomainService.guidDomain = null
        genericBasicValidationDomainService.guidIdField = null
        genericBasicValidationDomainService.decorator = null
        genericBasicValidationDomainService.supportedSearchFields = null
        genericBasicValidationDomainService.supportedSortFields = null
        genericBasicValidationDomainService.ethosToDomainFieldNameMap = null
        genericBasicValidationDomainService.defaultSortField = null
        genericBasicValidationDomainService.joinFieldMap = null
        genericBasicValidationDomainService.joinFieldSeperator = null
        genericBasicValidationDomainService.baseDomainFilter = null
        genericBasicValidationDomainService.guidDomainFilter = null
    }

    @Test
    void testGetGuidInSameDomain() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('section-grade-types')[0]
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidIdField = 'guid'
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'section-grade-types']
        GlobalUniqueIdentifier response = genericBasicValidationDomainService.get(globalUniqueIdentifier.guid)
        assertNotNull(response)
        assertEquals(globalUniqueIdentifier.guid, response.guid)
    }

    @Test
    void testGetGuidInSameDomainDefaultDecorator() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('section-grade-types')[0]
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GenericBasicValidationDecorator.class
        genericBasicValidationDomainService.guidIdField = 'guid'
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'section-grade-types']
        GenericBasicValidationDecorator response = genericBasicValidationDomainService.get(globalUniqueIdentifier.guid)
        assertNotNull(response)
        assertEquals(globalUniqueIdentifier.guid, response.guid)
    }

    @Test
    void testGetGuidInDifferentDomain() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('academic-levels')[0]
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = AcademicLevel.class
        genericBasicValidationDomainService.guidIdField = 'guid'
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        AcademicLevel response = genericBasicValidationDomainService.get(globalUniqueIdentifier.guid)
        assertNotNull(response)
        assertEquals(globalUniqueIdentifier.domainKey, response.code)
        assertEquals(globalUniqueIdentifier.guid, response.guid)
    }

    @Test
    void testGetGuidInDifferentDomainDefaultDecorator() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('academic-levels')[0]
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GenericBasicValidationDecorator.class
        genericBasicValidationDomainService.guidIdField = 'guid'
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        GenericBasicValidationDecorator response = genericBasicValidationDomainService.get(globalUniqueIdentifier.guid)
        assertNotNull(response)
        assertEquals(globalUniqueIdentifier.domainKey, response.code)
        assertEquals(globalUniqueIdentifier.guid, response.guid)
    }

    @Test
    void testValidateSettingsNullBaseDomain() {
        genericBasicValidationDomainService.baseDomain = null
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testValidateSettingsInvalidBaseDomain() {
        genericBasicValidationDomainService.baseDomain = ArrayList.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testValidateSettingsNullGuidDomain() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = null
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testValidateSettingsInvalidGuidDomain() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = ArrayList.class
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testValidateSettingsNullDecorator() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = null
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testValidateSettingsNullGuidIdFieldDomain() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = Level.class
        genericBasicValidationDomainService.guidIdField = null
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get(1)
        }
    }

    @Test
    void testListSameDomainNoFilters() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list([:])
        assertNotNull(globalUniqueIdentifierList)
        assertEquals(genericBasicValidationDomainService.DEFAULT_PAGE_SIZE, globalUniqueIdentifierList.size())
    }

    @Test
    void testListSameDomainNoFiltersWithMax() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list([max: "100"])
        assertNotNull(globalUniqueIdentifierList)
        assertEquals(100, globalUniqueIdentifierList.size())
    }

    @Test
    void testListSameDomainNoFiltersWithMaxMoreThanMaxAllowed() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list([max: "2000"])
        assertNotNull(globalUniqueIdentifierList)
        assertEquals(genericBasicValidationDomainService.MAX_PAGE_SIZE, globalUniqueIdentifierList.size())
    }

    @Test
    void testListSameDomainFilterNotListedAsAllowed() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.defaultSortField = 'id'
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list([ldmName: 'persons'])
        assertNotNull(globalUniqueIdentifierList)
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList1 = genericBasicValidationDomainService.list([:])
        assertEquals(globalUniqueIdentifierList.id.sort(), globalUniqueIdentifierList1.id.sort())
    }

    @Test
    void testListSameDomainFiltered() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.supportedSearchFields = ['ldmName']
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list([ldmName: 'persons'])
        assertNotNull(globalUniqueIdentifierList)
        globalUniqueIdentifierList.each {
            assertEquals('persons', it.ldmName)
        }
    }

    @Test
    void testListSameDomainFilteredWhenEthosNameAndDomainFieldNameIsDifferent() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.supportedSearchFields = ['resourceType']
        genericBasicValidationDomainService.ethosToDomainFieldNameMap = ['resourceType': 'ldmName']
        List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list(['resourceType': 'persons'])
        assertNotNull(globalUniqueIdentifierList)
        globalUniqueIdentifierList.each {
            assertEquals('persons', it.ldmName)
        }
    }

    @Test
    void testListSameDomainFilteredWhenEthosNameAndDomainFieldNameIsDifferentInvalidMapping() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.supportedSearchFields = ['resourceType']
        genericBasicValidationDomainService.ethosToDomainFieldNameMap = ['resourceType1': 'ldmName']
        shouldFail(InvalidDataAccessResourceUsageException) {
            List<GlobalUniqueIdentifier> globalUniqueIdentifierList = genericBasicValidationDomainService.list(['resourceType': 'persons'])
        }
    }

    @Test
    void testListDifferentDomainNoFilters() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = AcademicLevel.class
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericBasicValidationDomainService.defaultSortField = 'code'
        List<AcademicLevel> levels = genericBasicValidationDomainService.list([:])
        assertNotNull(levels)
        levels.each {
            assertTrue(it instanceof AcademicLevel)
        }
    }

    @Test
    void testListDifferentDomainNoFiltersDefaultDecorator() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GenericBasicValidationDecorator.class
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericBasicValidationDomainService.defaultSortField = 'code'
        List<GenericBasicValidationDecorator> response = genericBasicValidationDomainService.list([:])
        assertNotNull(response)
        response.each {
            assertTrue(it instanceof GenericBasicValidationDecorator)
        }
    }

    @Test
    void testListDifferentDomainFiltered() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = AcademicLevel.class
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericBasicValidationDomainService.defaultSortField = 'code'
        genericBasicValidationDomainService.supportedSearchFields = ['ceuInd']
        List<AcademicLevel> levels = genericBasicValidationDomainService.list(['ceuInd': true])
        assertNotNull(levels)
        levels.each {
            assertTrue(it instanceof AcademicLevel)
            assertEquals(true, it.ceuInd)
        }
    }

    @Test
    void testListDifferentDomainFilteredDefaultDecorator() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GenericBasicValidationDecorator.class
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericBasicValidationDomainService.defaultSortField = 'code'
        genericBasicValidationDomainService.supportedSearchFields = ['ceuInd']
        List<GenericBasicValidationDecorator> response = genericBasicValidationDomainService.list(['ceuInd': true])
        assertNotNull(response)
        response.each {
            assertTrue(it instanceof GenericBasicValidationDecorator)
            assertEquals(true, Level.findByCode(it.code).ceuInd)
        }
    }

    @Test
    void testFetchByFieldValueInListSameDomain() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        List<GlobalUniqueIdentifier> response = genericBasicValidationDomainService.fetchByFieldValueInList('ldmName', ['acdemic-levels', 'section-grade-types'])
        assertNotNull(response)
        response.each {
            assertTrue(it.ldmName == 'acdemic-levels' || it.ldmName == 'section-grade-types')
        }
    }

    @Test
    void testFetchByFieldValueInListDifferentDomain() {
        genericBasicValidationDomainService.baseDomain = Level.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = AcademicLevel.class
        genericBasicValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericBasicValidationDomainService.defaultSortField = 'code'
        List<AcademicLevel> response = genericBasicValidationDomainService.fetchByFieldValueInList('code', ['UG', 'CE'])
        assertNotNull(response)
        response.each {
            assertTrue(it.code == 'UG' || it.code == 'CE')
        }
    }

    @Test
    void testInvalidGuidInGet() {
        genericBasicValidationDomainService.baseDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.decorator = GlobalUniqueIdentifier.class
        genericBasicValidationDomainService.guidIdField = 'guid'
        genericBasicValidationDomainService.guidDomainFilter = ['ldmName': 'section-grade-types']
        shouldFail(ApplicationException) {
            genericBasicValidationDomainService.get('invalid-guid')
        }
    }
}
