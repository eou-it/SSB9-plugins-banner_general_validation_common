/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class GeographicDivisionServiceIntegrationTests extends BaseIntegrationTestCase{

    def geographicDivisionService
    //Seed data initialization
    //Success Data for Insert
    def i_success_code = "DIVISION"
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
    void testCreateGeographicDivision() {
       def geographicDivision = newGeographicDivision()
        geographicDivision = geographicDivisionService.create([domainModel:geographicDivision])
       assertNotNull geographicDivision
       assertNotNull geographicDivision.id
    }

    @Test
    void testUpdateGeographicDivision() {
       def geographicDivision = newGeographicDivision()
        geographicDivision = geographicDivisionService.create([domainModel:geographicDivision])

       GeographicDivision geographicDivisionUpdate = GeographicDivision.findWhere(code: i_success_code)
       assertNotNull geographicDivisionUpdate

       geographicDivisionUpdate.description = "ZZ"
       geographicDivisionUpdate = geographicDivisionService.update([domainModel:geographicDivisionUpdate])
       assertEquals "ZZ", geographicDivisionUpdate.description
    }

    @Test
    void testDeleteGeographicDivision() {
       def geographicDivision = newGeographicDivision()
       geographicDivision = geographicDivisionService.create([domainModel:geographicDivision])
       assertNotNull geographicDivision

       def id = geographicDivision.id
       def deleteMe = GeographicDivision.get(id)
       geographicDivisionService.delete([domainModel:geographicDivision])
       assertNull GeographicDivision.get(id)

    }

    @Test
    void testList(){
       def geographicDivision = geographicDivisionService.list()
       assertTrue geographicDivision.size() > 0
    }


    //Private Methods
    private def newGeographicDivision() {
        def geographicDivision = new GeographicDivision(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "Integration tests",
                dataOrigin: "Banner Integration Tests"
        )
        return geographicDivision
    }
}
