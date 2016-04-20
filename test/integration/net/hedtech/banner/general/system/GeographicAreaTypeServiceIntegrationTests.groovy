/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Geographic area view domain service integration test cases.
 */
class GeographicAreaTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def geographicAreaTypeService

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
    void testCountAll(){
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(*) as gatCount from (select * from STVGEOR  union select * from STVGEOD)").gatCount.toInteger()
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = geographicAreaTypeService.countAll().toInteger()
        assertEquals expectedCount, actualCount
    }

    @Test
    void testFetchByRegionGuid(){
        GeographicRegion geographicRegion = save  newValidCreateGeographicRegionDesc()
        assertNotNull geographicRegion.id
        String guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
        assertNotNull guid
        GeographicAreaType geographicAreaType = geographicAreaTypeService.fetchByGuid(guid)
        validateResponse(geographicAreaType)
    }

    @Test
    void testFetchByDivisionGuid(){
        GeographicDivision geographicDivision = save  newValidForCreateGeographicDivision()
        assertNotNull geographicDivision.id
        String guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        assertNotNull guid
        GeographicAreaType geographicAreaType = geographicAreaTypeService.fetchByGuid(guid)
        validateResponse(geographicAreaType)

    }

    @Test
    void testFetchAll(){
        List geographicTypes = geographicAreaTypeService.fetchAll([max:500])
        assertFalse geographicTypes.isEmpty()
        geographicTypes.each{ geographicAreaType ->
            validateResponse(geographicAreaType)
        }
        assertEquals geographicTypes.size(), geographicAreaTypeService.countAll().toInteger()
    }

    @Test
    void testFetchAllWithPagination(){
        List geographicTypes = geographicAreaTypeService.fetchAll([max:10,offset: 2])
        assertFalse geographicTypes.isEmpty()
        geographicTypes.each{ geographicAreaType ->
            validateResponse(geographicAreaType)
        }
        assertEquals geographicTypes.size(), 10
    }

    private void validateResponse(GeographicAreaType geographicAreaType) {
        assertNotNull geographicAreaType.description
        if (geographicAreaType.description == GeneralValidationCommonConstants.GEOGRAPHIC_REGION_TYPE) {
            assertNotNull geographicAreaType.code
            GeographicRegion geographicRegion = GeographicRegion.findByCode(geographicAreaType.code)
            assertNotNull geographicRegion
            assertEquals geographicAreaType.id, GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
            assertEquals geographicAreaType.title, geographicRegion.description

        } else if (geographicAreaType.description == GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_TYPE) {
            assertNotNull geographicAreaType.code
            GeographicDivision geographicDivision = GeographicDivision.findByCode(geographicAreaType.code)
            assertNotNull geographicDivision
            assertEquals geographicAreaType.id, GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
            assertEquals geographicAreaType.title, geographicDivision.description
        }
    }

    private def newValidCreateGeographicRegionDesc() {
        def geographicRegion = new GeographicRegion(
                code: 'rtest',
                description: 'region_desc',
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }

    private def newValidForCreateGeographicDivision() {
        def geographicDivision = new GeographicDivision(
                code: 'dtest',
                description: 'division_desc',
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }
}
