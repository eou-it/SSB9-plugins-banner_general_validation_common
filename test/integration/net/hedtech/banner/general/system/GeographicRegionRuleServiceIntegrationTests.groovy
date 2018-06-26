/*********************************************************************************
 Copyright 2017-2018 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test cases for GeographicRegionRuleService
 */
class GeographicRegionRuleServiceIntegrationTests extends BaseIntegrationTestCase {

    def geographicRegionRuleService

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
        insertContent = [region:  GeographicRegion.findByCode('BALT/WASH'), division: GeographicDivision.findByCode('ALUM'), regionType: 'i_test', startTypeRange: '12', endTypeRange: '34']
    }

    @Test
    void testCount() {
        geographicRegionRuleService.createOrUpdate(newGeographicRegionRule(insertContent))
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(DISTINCT SORGEOR_GEOR_CODE||SORGEOR_GEOD_CODE) as gaCount from SORGEOR").gaCount.toInteger()
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = geographicRegionRuleService.countAll().toInteger()
        assertEquals expectedCount, actualCount
    }

    @Test
    void testFindAll() {
        def geographicRegionRule = geographicRegionRuleService.createOrUpdate(newGeographicRegionRule(insertContent))
        assertNotNull geographicRegionRule
        def geographicaAreas = geographicRegionRuleService.fetchAll([max: 500, offset: 0])
        assertFalse geographicaAreas.isEmpty()
        assertEquals geographicaAreas.size(), geographicRegionRuleService.countAll()
    }

    @Test
    void testFindByGuid() {
        GeographicRegionRule geographicRegionRule = geographicRegionRuleService.createOrUpdate(newGeographicRegionRule(insertContent))
        assertNotNull geographicRegionRule
        def guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegionRule.region.code + '-^' + geographicRegionRule.division.code, GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME).guid
        assertNotNull guid
        def geographicArea = geographicRegionRuleService.fetchByGuid(guid)
        assertEquals geographicArea.getAt(0), geographicRegionRule.region.code + '-' + geographicRegionRule.division.code
        GeographicRegion geographicRegion = GeographicRegion.findByCode(geographicRegionRule.region.code)
        assertNotNull geographicRegion
        assertEquals geographicArea.getAt(4), GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
        GeographicDivision geographicDivision = GeographicDivision.findByCode(geographicRegionRule.division.code)
        assertNotNull geographicDivision
        assertEquals geographicArea.getAt(3), GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        assertEquals geographicArea.getAt(1), geographicRegion.description + '-' + geographicDivision.description
    }

    @Test
    void testFetchAllByGuidInList(){
        geographicRegionRuleService.createOrUpdate(newGeographicRegionRule(insertContent))
        List guid = GlobalUniqueIdentifier.fetchByLdmName(GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME)?.guid
        List data = geographicRegionRuleService.fetchAllByGuidInList(guid)

        assertNotNull data
        def rule = data[0].get("geographicRegionRule")
        assertNotNull(rule)

    }

    private GeographicRegionRule newGeographicRegionRule(Map content) {
        new GeographicRegionRule(content)
    }
}
