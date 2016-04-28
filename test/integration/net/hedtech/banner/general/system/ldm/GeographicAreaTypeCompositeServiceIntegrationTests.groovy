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
import net.hedtech.banner.general.system.ldm.v4.GeographicAreaTypeCategory
import net.hedtech.banner.general.system.ldm.v4.GeographicAreaTypeDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test cases for Geographic Area Type Composite Service
 */
class GeographicAreaTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def geographicAreaTypeCompositeService
    private  String  i_success_code ='test_code'
    private  String  i_sucess_description = 'test_desc'


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
        List geographicAreaTypes = geographicAreaTypeCompositeService.list([:])
        assertFalse geographicAreaTypes.isEmpty()
        geographicAreaTypes.each { geographicAreaType ->
            validateResponse(geographicAreaType)
        }
        assertEquals geographicAreaTypes.size().toInteger(), geographicAreaTypeCompositeService.count().toInteger()
    }

    @Test
    void testListWithPagination() {
        List geographicAreaTypes = geographicAreaTypeCompositeService.list([max: '4', offset: '1'])
        assertFalse geographicAreaTypes.isEmpty()
        geographicAreaTypes.each { geographicAreaType ->
            validateResponse(geographicAreaType)
        }
        assertEquals geographicAreaTypes.size().toInteger(), 4
    }

    @Test
    void testCount() {
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(*) as gatCount from (select * from STVGEOR  union select * from STVGEOD)").gatCount.toInteger()
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = geographicAreaTypeCompositeService.count().toInteger()
        assertEquals expectedCount, actualCount
    }

    @Test
    void testGetValidGuidByRegion() {
        GeographicRegion geographicRegion = save newValidCreateGeographicRegionDesc()
        assertNotNull geographicRegion.id
        String guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
        assertNotNull guid
        GeographicAreaTypeDecorator geographicArea = geographicAreaTypeCompositeService.get(guid)
        assertNotNull geographicArea
        validateResponse(geographicArea)
    }

    @Test
    void testGetValidGuidByDivsion() {
        GeographicDivision geographicDivision = save newValidCreateGeographicDivisionDesc()
        assertNotNull geographicDivision.id
        String guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        assertNotNull guid
        GeographicAreaTypeDecorator geographicArea = geographicAreaTypeCompositeService.get(guid)
        assertNotNull geographicArea
        validateResponse(geographicArea)
    }

    @Test
    void testGetInValidGuid() {
        try{
            geographicAreaTypeCompositeService.get('invalid_guid')
        } catch (ApplicationException ae){
            assertApplicationException ae, "NotFoundException"
        }
    }


    private void validateResponse(GeographicAreaTypeDecorator geographicAreaType) {
        assertNotNull geographicAreaType.description
        if (geographicAreaType.description == GeneralValidationCommonConstants.GEOGRAPHIC_REGION_TYPE) {
            assertNotNull geographicAreaType.code
            GeographicRegion geographicRegion = GeographicRegion.findByCode(geographicAreaType.code)
            assertNotNull geographicRegion
            assertEquals geographicAreaType.category, GeographicAreaTypeCategory.INSTITUTIONAL.toString()
            assertEquals geographicAreaType.id, GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
            assertEquals geographicAreaType.title, geographicRegion.description
            assertEquals geographicAreaType.getDetail().id , GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code, GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid

        } else if (geographicAreaType.description == GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_TYPE) {
            assertNotNull geographicAreaType.code
            assertEquals geographicAreaType.category, GeographicAreaTypeCategory.INSTITUTIONAL.toString()
            GeographicDivision geographicDivision = GeographicDivision.findByCode(geographicAreaType.code)
            assertNotNull geographicDivision
            assertEquals geographicAreaType.id, GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
            assertEquals geographicAreaType.title, geographicDivision.description
            assertEquals geographicAreaType.getDetail().id , GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code, GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        }
    }

    private def newValidCreateGeographicRegionDesc() {
        def geographicRegion = new GeographicRegion(
                code: i_success_code,
                description: i_sucess_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }

    private def newValidCreateGeographicDivisionDesc() {
        def geographicDivision = new GeographicDivision(
                code: i_success_code,
                description: i_sucess_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }
}
