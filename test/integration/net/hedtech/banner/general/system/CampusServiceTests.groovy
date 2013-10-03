package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase


class CampusServiceTests extends BaseIntegrationTestCase {

    def campusService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateCampus() {
        def campus = new Campus(code: "TT", description: "TT")
        campus = campusService.create([domainModel: campus])
        assertNotNull campus
    }


    void testUpdateCampus() {
        def campus = new Campus(code: "TT", description: "TT")
        campus = campusService.create([domainModel: campus])

        Campus campusUpdate = Campus.findWhere(code: "TT")
        assertNotNull campusUpdate

        campusUpdate.description = "ZZ"
        campusUpdate = campusService.update([domainModel: campus])
        assertEquals "ZZ", campusUpdate.description
    }


    void testDeleteCampus() {
        def campus = new Campus(code: "TT", description: "TT")
        campus = campusService.create([domainModel: campus])
        assertNotNull campus
        def id = campus.id
        def deleteMe = Campus.get(id)
        campusService.delete([domainModel: deleteMe])
        assertNull Campus.get(id)
    }


    void testList() {
        def campus = campusService.list()
        assertTrue campus.size() > 0
    }
}
