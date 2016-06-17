/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class GeographicRegionServiceIntegrationTests extends BaseIntegrationTestCase{

    def geographicRegionService
    //Seed data initialization
    def i_success_code = "REGION"
    def i_success_description = "Northern Hemisphere"


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
    void testCreateGeographicRegion() {
       def geographicRegion = newGeographicRegion()
        geographicRegion = geographicRegionService.create([domainModel:geographicRegion])
       assertNotNull geographicRegion
       assertNotNull geographicRegion.id
    }

    @Test
    void testUpdateGeographicRegion() {
       def geographicRegion = newGeographicRegion()
        geographicRegion = geographicRegionService.create([domainModel:geographicRegion])

       GeographicRegion geographicRegionUpdate = GeographicRegion.findWhere(code: i_success_code)
       assertNotNull geographicRegionUpdate

       geographicRegionUpdate.description = "ZZ"
       geographicRegionUpdate = geographicRegionService.update([domainModel:geographicRegionUpdate])
       assertEquals "ZZ", geographicRegionUpdate.description
    }

    @Test
    void testDeleteGeographicRegion() {
       def geographicRegion = newGeographicRegion()
       geographicRegion = geographicRegionService.create([domainModel:geographicRegion])
       assertNotNull geographicRegion

       def id = geographicRegion.id
       def deleteMe = GeographicRegion.get(id)
       geographicRegionService.delete([domainModel:geographicRegion])
       assertNull GeographicRegion.get(id)

    }

    @Test
    void testList(){
       def geographicRegion = geographicRegionService.list()
       assertTrue geographicRegion.size() > 0
    }


    //Private Methods
    private def newGeographicRegion() {
        def geographicRegion = new GeographicRegion(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicRegion
    }
}
