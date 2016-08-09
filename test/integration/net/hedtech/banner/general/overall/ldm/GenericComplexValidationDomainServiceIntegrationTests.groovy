package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.general.system.ldm.v1.AcademicLevel
import net.hedtech.banner.general.system.ldm.v4.EmailTypeDetails
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class GenericComplexValidationDomainServiceIntegrationTests extends BaseIntegrationTestCase {

    def genericComplexValidationDomainService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        genericComplexValidationDomainService.baseDomain = null
        genericComplexValidationDomainService.guidDomain = null
        genericComplexValidationDomainService.guidIdField = null
        genericComplexValidationDomainService.decorator = null
        genericComplexValidationDomainService.supportedSearchFields = null
        genericComplexValidationDomainService.supportedSortFields = null
        genericComplexValidationDomainService.ethosToDomainFieldNameMap = null
        genericComplexValidationDomainService.defaultSortField = null
        genericComplexValidationDomainService.additionDataDomain = null
        genericComplexValidationDomainService.additionDataFieldMap = null
        genericComplexValidationDomainService.additionalDataJoinFieldMap = null
        genericComplexValidationDomainService.defaultFields = null
        genericComplexValidationDomainService.joinFieldMap = null
        genericComplexValidationDomainService.joinFieldSeperator = null
        genericComplexValidationDomainService.baseDomainFilter = null
        genericComplexValidationDomainService.guidDomainFilter = null
    }

    @Test
    void testValidateSettingsAdditionDataJoinFieldMap() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('academic-levels')[0]
        genericComplexValidationDomainService.baseDomain = Relationship.class
        genericComplexValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.decorator = AcademicLevel.class
        genericComplexValidationDomainService.guidIdField = 'guid'
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericComplexValidationDomainService.additionDataDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.additionDataFieldMap = ['field1': 'field1']
        shouldFail(ApplicationException) {
            genericComplexValidationDomainService.get(globalUniqueIdentifier.guid)
        }
        genericComplexValidationDomainService.additionalDataJoinFieldMap = [:]
        shouldFail(ApplicationException) {
            genericComplexValidationDomainService.get(globalUniqueIdentifier.guid)
        }
    }

    @Test
    void testValidateSettingsAdditionDataFieldMap() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmName('academic-levels')[0]
        genericComplexValidationDomainService.baseDomain = Relationship.class
        genericComplexValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.decorator = AcademicLevel.class
        genericComplexValidationDomainService.guidIdField = 'guid'
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.guidDomainFilter = ['ldmName': 'academic-levels']
        genericComplexValidationDomainService.additionDataDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.additionalDataJoinFieldMap = ['field1': 'field1']
        shouldFail(ApplicationException) {
            genericComplexValidationDomainService.get(globalUniqueIdentifier.guid)
        }
        genericComplexValidationDomainService.additionDataFieldMap = [:]
        shouldFail(ApplicationException) {
            genericComplexValidationDomainService.get(globalUniqueIdentifier.guid)
        }
    }

    @Test
    void testGetAdditionalData() {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findAllByLdmNameAndDomainKey('email-types', 'BUSI')[0]
        genericComplexValidationDomainService.baseDomain = EmailType.class
        genericComplexValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.decorator = EmailTypeDetails.class
        genericComplexValidationDomainService.guidIdField = 'guid'
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.guidDomainFilter = ['ldmName': 'email-types']
        genericComplexValidationDomainService.additionDataDomain = IntegrationConfiguration.class
        genericComplexValidationDomainService.additionalDataJoinFieldMap = ['code': 'value']
        genericComplexValidationDomainService.additionDataFieldMap = ['translationValue': 'emailType']
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.joinFieldSeperator = '^'
        genericComplexValidationDomainService.additionDataDomainFilter = ['processCode': 'HEDM', 'settingName': 'EMAILS.EMAILTYPE']
        EmailTypeDetails emailTypeDetails = genericComplexValidationDomainService.get(globalUniqueIdentifier.guid)
        assertNotNull(emailTypeDetails)
        assertNotNull(emailTypeDetails.emailType)
        IntegrationConfiguration integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue('HEDM', 'EMAILS.EMAILTYPE', 'BUSI')
        assertEquals(integrationConfiguration.translationValue, emailTypeDetails.emailType)
    }

    @Test
    void testListAdditionalData() {
        genericComplexValidationDomainService.baseDomain = EmailType.class
        genericComplexValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.decorator = EmailTypeDetails.class
        genericComplexValidationDomainService.guidIdField = 'guid'
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.guidDomainFilter = ['ldmName': 'email-types']
        genericComplexValidationDomainService.additionDataDomain = IntegrationConfiguration.class
        genericComplexValidationDomainService.additionalDataJoinFieldMap = ['code': 'value']
        genericComplexValidationDomainService.additionDataFieldMap = ['translationValue': 'emailType']
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.joinFieldSeperator = '^'
        genericComplexValidationDomainService.additionDataDomainFilter = ['processCode': 'HEDM', 'settingName': 'EMAILS.EMAILTYPE']
        List<EmailTypeDetails> emailTypeDetailsList = genericComplexValidationDomainService.list([:])
        assertNotNull(emailTypeDetailsList)
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.findAllByProcessCodeAndSettingName('HEDM', 'EMAILS.EMAILTYPE')
        assertNotNull(integrationConfigurationList)
        emailTypeDetailsList.each {
            if (it.emailType) {
                assertTrue(integrationConfigurationList.translationValue.contains(it.emailType))
            }
        }
    }

    @Test
    void testListAdditionalDataWithSkipRecordsTrue() {
        genericComplexValidationDomainService.baseDomain = EmailType.class
        genericComplexValidationDomainService.guidDomain = GlobalUniqueIdentifier.class
        genericComplexValidationDomainService.decorator = EmailTypeDetails.class
        genericComplexValidationDomainService.guidIdField = 'guid'
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.guidDomainFilter = ['ldmName': 'email-types']
        genericComplexValidationDomainService.additionDataDomain = IntegrationConfiguration.class
        genericComplexValidationDomainService.additionalDataJoinFieldMap = ['code': 'value']
        genericComplexValidationDomainService.additionDataFieldMap = ['translationValue': 'emailType']
        genericComplexValidationDomainService.joinFieldMap = ['code': 'domainKey']
        genericComplexValidationDomainService.joinFieldSeperator = '^'
        genericComplexValidationDomainService.additionDataDomainFilter = ['processCode': 'HEDM', 'settingName': 'EMAILS.EMAILTYPE']
        genericComplexValidationDomainService.skipRecordsWithNoAdditionalData = true
        List<EmailTypeDetails> emailTypeDetailsList = genericComplexValidationDomainService.list([:])
        assertNotNull(emailTypeDetailsList)
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.findAllByProcessCodeAndSettingName('HEDM', 'EMAILS.EMAILTYPE')
        assertNotNull(integrationConfigurationList)
        emailTypeDetailsList.each {
            assertNotNull(it.emailType)
            assertTrue(integrationConfigurationList.translationValue.contains(it.emailType))
        }
    }
}
