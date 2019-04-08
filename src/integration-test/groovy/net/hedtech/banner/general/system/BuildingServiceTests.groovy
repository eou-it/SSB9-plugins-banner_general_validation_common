package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class BuildingServiceTests extends BaseIntegrationTestCase {

    def buildingService


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
    void testCreateBuilding() {
        def building = new Building(code: "T", description: "TT",
                                    voiceResponseMsgNumber: 1)
        building = buildingService.create([domainModel: building])
        assertNotNull building
    }


    @Test
    void testUpdateBuilding() {
        def building = new Building(code: "T", description: "TT",
                                    voiceResponseMsgNumber: 1)
        building = buildingService.create([domainModel: building])

        Building buildingUpdate = Building.findWhere(code: "T")
        assertNotNull buildingUpdate

        buildingUpdate.description = "ZZ"
        buildingUpdate = buildingService.update([domainModel: buildingUpdate])
        assertEquals "ZZ", buildingUpdate.description
    }


    @Test
    void testDeleteBuilding() {
        def building = new Building(code: "T", description: "TT",
                                    voiceResponseMsgNumber: 1)
        building = buildingService.create([domainModel: building])
        assertNotNull building
        def id = building.id
        def deleteMe = Building.get(id)
        buildingService.delete([domainModel: deleteMe])
        assertNull Building.get(id)

    }


    @Test
    void testList() {
        def building = buildingService.list()
        assertTrue building.size() > 0
    }

}
