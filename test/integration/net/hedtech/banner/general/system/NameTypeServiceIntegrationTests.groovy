/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.NameTypeCategory
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test cases for Name type service.
 */
class NameTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def nameTypeService


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
    void testFetchAll() {
        def nameList = nameTypeService.fetchAll([max: '500', offset: '0'])
        assertFalse nameList.isEmpty()
        assertEquals NameType.list([:]).size(), nameList.size()
    }


    @Test
    void testFetchByGuid() {
        NameType nameType = newNameType()
        save nameType
        assertNotNull nameType.id
        def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, nameType.id)?.guid
        assertNotNull guid
        def list = nameTypeService.fetchByGuid(guid)
        assertEquals guid, list.getAt(0)
        assertEquals nameType.code, list.getAt(1)
        assertEquals nameType.description, list.getAt(2)
    }


    @Test
    void testGetBannerNameTypeToHEDMNameTypeMap() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING, nameType.code)
        assertNotNull intConf
        def map = nameTypeService.getBannerNameTypeToHEDMNameTypeMap()
        assertNotNull map
        assertTrue map.containsKey(nameType.code)
        assertTrue map.get(nameType.code) instanceof NameTypeCategory
    }


    @Test
    void testFetchGUIDs() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, nameType.id)
        assertNotNull globalUniqueIdentifier
        def map = nameTypeService.fetchGUIDs([nameType.code])
        assertNotNull map
        assertTrue map.containsKey(nameType.code)
        assertEquals globalUniqueIdentifier.guid, map.get(nameType.code)
    }


    private def newNameType() {
        def nameType = new NameType(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return nameType
    }

}
