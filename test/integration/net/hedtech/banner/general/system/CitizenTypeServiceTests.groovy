package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class CitizenTypeServiceTests extends BaseIntegrationTestCase {

    def citizenTypeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])
        assertNotNull citizenType
    }


    void testUpdateCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])

        CitizenType citizenTypeUpdate = CitizenType.findWhere(code: "TT")
        assertNotNull citizenTypeUpdate

        citizenTypeUpdate.description = "ZZ"
        citizenTypeUpdate = citizenTypeService.update([domainModel: citizenType])
        assertEquals "ZZ", citizenTypeUpdate.description
    }


    void testDeleteCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])
        assertNotNull citizenType
        def id = citizenType.id
        def deleteMe = CitizenType.get(id)
        citizenTypeService.delete([domainModel: deleteMe])
        assertNull CitizenType.get(id)

    }


    void testList() {
        def citizenType = citizenTypeService.list()
        assertTrue citizenType.size() > 0
    }
}
