/*******************************************************************************
 Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class IntegrationConfigurationIntegrationTest extends BaseIntegrationTestCase {

    private final static PROCESS_CODE_HEDM = "HEDM"
    private final static SETTING_NAME_SECTION_GRADABLE = "SECTIONDETAIL.CE_GRADABLE"
    private final static SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY = "MARITALSTATUS.MARITALCATEGORY"
    private final static VALUE_D = "D"
    private final static TRANSLATION_VALUE_DIVORCED = "divorced"
    private final static VALUE_M = "M"
    private final static TRANSLATION_VALUE_MARRIED = "married"

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
    public void testFetchAllByProcessCode(){
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.fetchAllByProcessCode(null)
        assertNull integrationConfigurationList
        integrationConfigurationList = IntegrationConfiguration.fetchAllByProcessCode(PROCESS_CODE_HEDM)
        assertNotNull integrationConfigurationList
        assertEquals IntegrationConfiguration.countByProcessCode(PROCESS_CODE_HEDM), integrationConfigurationList.size()
    }

    @Test
    public void testFetchAllByProcessCodeAndSettingNameAndTranslationValue(){
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.
                fetchAllByProcessCodeAndSettingNameAndTranslationValue(null,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY, TRANSLATION_VALUE_DIVORCED)
        assertNull integrationConfigurationList
        integrationConfigurationList = IntegrationConfiguration.
                fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE_HEDM, SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY, TRANSLATION_VALUE_DIVORCED)
        assertNotNull integrationConfigurationList
        assertEquals 1, integrationConfigurationList.size()
        assertEquals VALUE_D, integrationConfigurationList[0].value
    }

    @Test
    public void testFetchAllByProcessCodeAndSettingNameAndValue(){
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.
                fetchAllByProcessCodeAndSettingNameAndValue(null,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,VALUE_D)
        assertNull integrationConfigurationList
        integrationConfigurationList = IntegrationConfiguration.
                fetchAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE_HEDM,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,VALUE_D)
        assertNotNull integrationConfigurationList
        assertEquals 1, integrationConfigurationList.size()
        assertEquals TRANSLATION_VALUE_DIVORCED, integrationConfigurationList[0].translationValue
    }

    @Test
    public void testFetchByProcessCodeAndSettingNameAndValue(){
        IntegrationConfiguration integrationConfiguration = IntegrationConfiguration.
                fetchByProcessCodeAndSettingNameAndValue(null,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,VALUE_D)
        assertNull integrationConfiguration
        integrationConfiguration = IntegrationConfiguration.
                fetchByProcessCodeAndSettingNameAndValue(PROCESS_CODE_HEDM,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,VALUE_D)
        assertNotNull integrationConfiguration
        assertEquals TRANSLATION_VALUE_DIVORCED, integrationConfiguration.translationValue
    }

    @Test
    public void testFetchByProcessCodeAndSettingNameAndValues(){
        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.
                fetchByProcessCodeAndSettingNameAndValues(null,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,[VALUE_D,VALUE_M])
        assertNull integrationConfigurationList
        integrationConfigurationList = IntegrationConfiguration.
                fetchByProcessCodeAndSettingNameAndValues(PROCESS_CODE_HEDM,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,[VALUE_D,VALUE_M])
        assertNotNull integrationConfigurationList
        assertEquals 2, integrationConfigurationList.size()
        assertEquals integrationConfigurationList[0].value==VALUE_D?TRANSLATION_VALUE_DIVORCED:TRANSLATION_VALUE_MARRIED, integrationConfigurationList[0].translationValue
        assertEquals integrationConfigurationList[1].value==VALUE_M?TRANSLATION_VALUE_MARRIED:TRANSLATION_VALUE_DIVORCED, integrationConfigurationList[1].translationValue
    }

    @Test
    public void testFetchByProcessCodeAndSettingName(){
        IntegrationConfiguration integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingName(null,null)
        assertNull integrationConfiguration
        integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingName("invalid",null)
        assertNull integrationConfiguration
        integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingName("invalid","invalid")
        assertNull integrationConfiguration
        integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingName(PROCESS_CODE_HEDM,"invalid")
        assertNull integrationConfiguration
        integrationConfiguration = IntegrationConfiguration.fetchByProcessCodeAndSettingName(PROCESS_CODE_HEDM,SETTING_NAME_SECTION_GRADABLE)
        assertNotNull integrationConfiguration
        assertEquals "Y", integrationConfiguration.value
    }

    @Test
    public void testFetchByProcessCodeAndSettingNameAndValues_1000PlusValues(){

        List<String> values = generateRecords(10000)
        values.addAll([VALUE_D,VALUE_M])

        List<IntegrationConfiguration> integrationConfigurationList = IntegrationConfiguration.
                fetchByProcessCodeAndSettingNameAndValues(PROCESS_CODE_HEDM,SETTING_NAME_MARITAL_STATUS_PARENT_CATEGORY,values)
        assertNotNull integrationConfigurationList
        assertEquals 2, integrationConfigurationList.size()

        assertEquals integrationConfigurationList[0].value==VALUE_D?TRANSLATION_VALUE_DIVORCED:TRANSLATION_VALUE_MARRIED, integrationConfigurationList[0].translationValue
        assertEquals integrationConfigurationList[1].value==VALUE_M?TRANSLATION_VALUE_MARRIED:TRANSLATION_VALUE_DIVORCED, integrationConfigurationList[1].translationValue
    }

    public List<String> generateRecords(Integer records){
        List<String> strRecords = new ArrayList<String>()
        for(int i=0; i< records;i++){
            strRecords.add(i.toString())
        }
        return strRecords
    }



}
