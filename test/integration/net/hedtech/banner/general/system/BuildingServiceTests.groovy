package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class BuildingServiceTests extends BaseIntegrationTestCase {

    def buildingService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateBuilding() {
        def building = new Building(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        building = buildingService.create([domainModel: building])
        assertNotNull building
    }


    void testUpdateBuilding() {
        def building = new Building(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        building = buildingService.create([domainModel: building])

        Building buildingUpdate = Building.findWhere(code: "T")
        assertNotNull buildingUpdate

        buildingUpdate.description = "ZZ"
        buildingUpdate = buildingService.update([domainModel: building])
        assertEquals "ZZ", buildingUpdate.description
    }


    void testDeleteBuilding() {
        def building = new Building(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        building = buildingService.create([domainModel: building])
        assertNotNull building
        def id = building.id
        def deleteMe = Building.get(id)
        buildingService.delete([domainModel: deleteMe])
        assertNull Building.get(id)

    }


    void testList() {
        def building = buildingService.list()
        assertTrue building.size() > 0
    }

}
