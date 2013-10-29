package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class DegreeServiceIntegrationTests extends BaseIntegrationTestCase {

    def degreeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateDegree() {
        def degree = newDegree("TT")
        degree = degreeService.create([domainModel: degree])

        assertNotNull degree
    }


    void testList() {
        def degrees = degreeService.list()
        assertTrue degrees.size() > 0
    }


    void testUpdateDegree() {
        def degree = newDegree("TT")
        degree = degreeService.create([domainModel: degree])

        Degree degreeUpdate = Degree.findWhere(code: "TT")
        assertNotNull degreeUpdate

        degreeUpdate.description = "ZZ"
        degreeUpdate = degreeService.update([domainModel: degreeUpdate])
        assertEquals "ZZ", degreeUpdate.description
    }


    void testDeleteDegree() {
        def degree = newDegree("TT")
        degree = degreeService.create([domainModel: degree])
        assertNotNull degree

        def id = degree.id
        def deleteMe = Degree.get(id)

        degreeService.delete([domainModel: deleteMe])
        assertNull Degree.get(id)
    }


    private Degree newDegree(String code) {
        new Degree(code: code, description: "$code description", displayWebIndicator:false)
    }

}
