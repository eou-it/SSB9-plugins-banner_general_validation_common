/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.general.system.NameTypeService
import net.hedtech.banner.general.system.ldm.v6.NameTypeDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for person name type composite service.</p>
 */
class PersonNameTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personNameTypeCompositeService
    NameTypeService nameTypeService


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
    void testListWithOutPagination() {
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()

        Map<String, String> bannerNameTypeToHedmNameTypeMap = personNameTypeCompositeService.getBannerNameTypeToHedmV6NameTypeMap()
        assertFalse bannerNameTypeToHedmNameTypeMap.isEmpty()

        List entities = nameTypeService.fetchAllWithGuidByCodeInList(bannerNameTypeToHedmNameTypeMap.keySet(), 500, 0)
        assertFalse entities.isEmpty()

        assertEquals personNameTypeList.size(), entities.size()
        Iterator it1 = personNameTypeList.iterator()
        Iterator it2 = entities.iterator()

        while (it1.hasNext() && it2.hasNext()) {
            NameTypeDecorator nameTypeDecorator = it1.next()
            List actualEntities = it2.next()
            NameType nameType = actualEntities.getAt(0)
            assertEquals nameType.code, nameTypeDecorator.code
            assertEquals nameType.description, nameTypeDecorator.title
            GlobalUniqueIdentifier globalUniqueIdentifier = actualEntities.getAt(1)
            assertEquals globalUniqueIdentifier.guid, nameTypeDecorator.id
            assertEquals nameTypeDecorator.category, bannerNameTypeToHedmNameTypeMap.get(nameType.code)
        }
    }


    @Test
    void testListWithPagination() {
        params.max = '2'
        params.offset = '1'
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()

        Map<String, String> bannerNameTypeToHedmNameTypeMap = personNameTypeCompositeService.getBannerNameTypeToHedmV6NameTypeMap()
        assertFalse bannerNameTypeToHedmNameTypeMap.isEmpty()

        List entities = nameTypeService.fetchAllWithGuidByCodeInList(bannerNameTypeToHedmNameTypeMap.keySet(), 2, 1)
        assertFalse entities.isEmpty()

        assertEquals personNameTypeList.size(), entities.size()
        Iterator it1 = personNameTypeList.iterator()
        Iterator it2 = entities.iterator()

        while (it1.hasNext() && it2.hasNext()) {
            NameTypeDecorator nameTypeDecorator = it1.next()
            List actualEntities = it2.next()
            NameType nameType = actualEntities.getAt(0)
            assertEquals nameType.code, nameTypeDecorator.code
            assertEquals nameType.description, nameTypeDecorator.title
            GlobalUniqueIdentifier globalUniqueIdentifier = actualEntities.getAt(1)
            assertEquals globalUniqueIdentifier.guid, nameTypeDecorator.id
            assertEquals nameTypeDecorator.category, bannerNameTypeToHedmNameTypeMap.get(nameType.code)
        }
    }


    @Test
    void testCount() {
        def actualCount = personNameTypeCompositeService.count().toInteger()
        assertNotNull actualCount

        Map<String, String> bannerNameTypeToHedmNameTypeMap = personNameTypeCompositeService.getBannerNameTypeToHedmV6NameTypeMap()
        assertFalse bannerNameTypeToHedmNameTypeMap.isEmpty()

        List<NameType> nameTypeList = nameTypeService.fetchAllByCodeInList(bannerNameTypeToHedmNameTypeMap.keySet())
        assertFalse nameTypeList.isEmpty()
        assertEquals nameTypeList.size(), actualCount
    }


    @Test
    void testGetValidGuid() {
        params.max = '1'
        params.offset = '0'
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()
        NameTypeDecorator personNameType = personNameTypeList.getAt(0)
        assertNotNull personNameType
        NameTypeDecorator nameTypeDecorator = personNameTypeCompositeService.get(personNameType.id)
        assertNotNull nameTypeDecorator
        assertEquals personNameType.id, nameTypeDecorator.id
        assertEquals personNameType.code, nameTypeDecorator.code
        assertEquals personNameType.title, nameTypeDecorator.title
        assertEquals personNameType.category, nameTypeDecorator.category

    }


    @Test
    void testGetInValidGuid() {
        try {
            personNameTypeCompositeService.get('invalid_guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetWithNullGuid() {
        try {
            personNameTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetInValidMappedGuid() {
        NameType nameType = save newNameType()
        assertNotNull nameType
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, nameType.id)
        assertNotNull globalUniqueIdentifier
        try {
            personNameTypeCompositeService.get(globalUniqueIdentifier.guid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetBannerNameTypeToHedmV6NameTypeMap() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING, nameType.code)
        assertNotNull intConf
        def map = personNameTypeCompositeService.getBannerNameTypeToHedmV6NameTypeMap()
        assertNotNull map
        assertTrue map.containsKey(nameType.code)
        assertEquals map.get(nameType.code), NameTypeCategory.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V6).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V6]

    }


    @Test
    void testGetBannerNameTypeToHedmV3NameTypeMap() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        IntegrationConfiguration intConf = IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(GeneralValidationCommonConstants.PROCESS_CODE, GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING, nameType.code)
        assertNotNull intConf
        def map = personNameTypeCompositeService.getBannerNameTypeToHedmV3NameTypeMap()
        assertNotNull map
        assertTrue map.containsKey(nameType.code)
        assertEquals map.get(nameType.code), NameTypeCategory.getByDataModelValue(intConf.translationValue, GeneralValidationCommonConstants.VERSION_V3).versionToEnumMap[GeneralValidationCommonConstants.VERSION_V3]

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
