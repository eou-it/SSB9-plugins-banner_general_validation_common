/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.GeographicDivision
import net.hedtech.banner.general.system.GeographicRegion
import net.hedtech.banner.general.system.GeographicRegionRule
import net.hedtech.banner.general.system.ldm.v4.GeographicArea
import net.hedtech.banner.general.system.ldm.v4.GeographicAreaTypeCategory
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test cases for Geographic Area Composite Service
 */
class GeographicAreaCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

   def geographicAreaCompositeService

    def insertContent

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }


    void initializeTestDataForReferences() {
        insertContent = [regionCode: 'BALT/WASH', divisionCode: 'ALUM', regionType: 'i_test', startTypeRange: '12', endTypeRange: '34']
    }

    @Test
    void testCount() {
        save newGeographicRegionRule(insertContent)
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(DISTINCT SORGEOR_GEOR_CODE||SORGEOR_GEOD_CODE) as gaCount from SORGEOR").gaCount.toInteger()
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = geographicAreaCompositeService.count().toInteger()
        assertEquals expectedCount, actualCount
    }



    @Test
    void testGetValidGuid() {
        GeographicRegionRule geographicRegionRule = save newGeographicRegionRule(insertContent)
        assertNotNull geographicRegionRule
        def guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegionRule.regionCode + '-^' + geographicRegionRule.divisionCode, GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME).guid
        assertNotNull guid
        GeographicArea geographicArea = geographicAreaCompositeService.get(guid)
        assertEquals geographicArea.code, geographicRegionRule.regionCode + '-' + geographicRegionRule.divisionCode
        GeographicRegion geographicRegion = GeographicRegion.findByCode(geographicRegionRule.regionCode)
        assertNotNull geographicRegion
        assertEquals geographicArea.getType().category,GeographicAreaTypeCategory.INSTITUTIONAL.toString()
        assertEquals geographicArea.getType().getDetail().id , GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
        GeographicDivision geographicDivision = GeographicDivision.findByCode(geographicRegionRule.divisionCode)
        assertNotNull geographicDivision
        assertEquals geographicArea.getIncludedAreas().id.getAt(0), GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        assertEquals geographicArea.title, geographicRegion.description + '-' + geographicDivision.description
    }


    @Test
    void testGetInValidGuid() {
        GeographicRegionRule geographicRegionRule = save newGeographicRegionRule(insertContent)
        assertNotNull geographicRegionRule
        def guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegionRule.regionCode + '-^' + geographicRegionRule.divisionCode, GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME).guid
        assertNotNull guid
        geographicRegionRule.delete()
        assertNull GeographicRegionRule.get(geographicRegionRule.id)
         try{
             geographicAreaCompositeService.get(guid)
         } catch (ApplicationException ae){
             assertApplicationException ae, "NotFoundException"
         }
    }

    @Test
    void testListWithOutPagination(){
       List geographicAreas = geographicAreaCompositeService.list([:])
       assertFalse geographicAreas.isEmpty()
        geographicAreas.each{ geographicArea ->
            def areaCodes = geographicArea.code.split("-")
            GeographicRegion geographicRegion = GeographicRegion.findByCode(areaCodes.getAt(0))
            assertNotNull geographicRegion
            assertEquals geographicArea.getType().category,GeographicAreaTypeCategory.INSTITUTIONAL.toString()
            assertEquals geographicArea.getType().getDetail().id , GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
            GeographicDivision geographicDivision = GeographicDivision.findByCode(areaCodes.getAt(1))
            assertNotNull geographicDivision
            assertEquals geographicArea.getIncludedAreas().id.getAt(0), GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
            assertEquals geographicArea.title, geographicRegion.description + '-' + geographicDivision.description
            assertEquals geographicArea.code, geographicRegion.code + '-' + geographicDivision.code
        }
        assertEquals geographicAreas.size().toInteger(),geographicAreaCompositeService.count().toInteger()
    }

    @Test
    void testListWithPagination(){
        List geographicAreas = geographicAreaCompositeService.list([max:'4',offset: '1'])
        assertFalse geographicAreas.isEmpty()
        geographicAreas.each{ geographicArea ->
            def areaCodes = geographicArea.code.split("-")
            GeographicRegion geographicRegion = GeographicRegion.findByCode(areaCodes.getAt(0))
            assertNotNull geographicRegion
            assertEquals geographicArea.getType().category,GeographicAreaTypeCategory.INSTITUTIONAL.toString()
            assertEquals geographicArea.getType().getDetail().id , GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
            GeographicDivision geographicDivision = GeographicDivision.findByCode(areaCodes.getAt(1))
            assertNotNull geographicDivision
            assertEquals geographicArea.getIncludedAreas().id.getAt(0), GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
            assertEquals geographicArea.title, geographicRegion.description + '-' + geographicDivision.description
            assertEquals geographicArea.code, geographicRegion.code + '-' + geographicDivision.code
        }
        assertEquals geographicAreas.size().toInteger(),4
    }

    private GeographicRegionRule newGeographicRegionRule(Map content) {
        new GeographicRegionRule(content)
    }
}
