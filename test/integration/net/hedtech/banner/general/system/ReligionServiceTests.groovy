package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class ReligionServiceTests extends BaseIntegrationTestCase {

    def religionService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])
        assertNotNull religion
    }


    void testUpdateReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])

        Religion religionUpdate = Religion.findWhere(code: "T")
        assertNotNull religionUpdate

        religionUpdate.description = "ZZ"
        religionUpdate = religionService.update([domainModel: religionUpdate])
        assertEquals "ZZ", religionUpdate.description
    }


    void testDeleteReligion() {
        def religion = new Religion(code: "T", description: "TT")
        religion = religionService.create([domainModel: religion])
        assertNotNull religion
        def id = religion.id
        def deleteMe = Religion.get(id)
        religionService.delete([domainModel: deleteMe])
        assertNull Religion.get(id)

    }


    void testList() {
        def religion = religionService.list()
        assertTrue religion.size() > 0
    }
}
