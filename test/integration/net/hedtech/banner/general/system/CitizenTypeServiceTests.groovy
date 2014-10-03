package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

class CitizenTypeServiceTests extends BaseIntegrationTestCase {

    def citizenTypeService


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
    void testCreateCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])
        assertNotNull citizenType
    }


	@Test
    void testUpdateCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])

        CitizenType citizenTypeUpdate = CitizenType.findWhere(code: "TT")
        assertNotNull citizenTypeUpdate

        citizenTypeUpdate.description = "ZZ"
        citizenTypeUpdate = citizenTypeService.update([domainModel: citizenTypeUpdate])
        assertEquals "ZZ", citizenTypeUpdate.description
    }


	@Test
    void testDeleteCitizenType() {
        def citizenType = new CitizenType(code: "TT", description: "TT", citizenIndicator: true, electronicDataInterchnageEquivalent: "T")
        citizenType = citizenTypeService.create([domainModel: citizenType])
        assertNotNull citizenType
        def id = citizenType.id
        def deleteMe = CitizenType.get(id)
        citizenTypeService.delete([domainModel: deleteMe])
        assertNull CitizenType.get(id)

    }


	@Test
    void testList() {
        def citizenType = citizenTypeService.list()
        assertTrue citizenType.size() > 0
    }
}
