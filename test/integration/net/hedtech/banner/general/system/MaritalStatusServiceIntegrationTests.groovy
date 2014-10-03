package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

class MaritalStatusServiceTests extends BaseIntegrationTestCase {

    def maritalStatusService


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
    void testCreateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                                              code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel:maritalStatus])
        assertNotNull maritalStatus
    }


	@Test
    void testUpdateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                                              code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel:maritalStatus])

        MaritalStatus maritalStatusUpdate = MaritalStatus.findWhere(code: "T")
        assertNotNull maritalStatusUpdate

        maritalStatusUpdate.description = "ZZ"
        maritalStatusUpdate = maritalStatusService.update([domainModel:maritalStatusUpdate])
        assertEquals "ZZ", maritalStatusUpdate.description
    }


	@Test
    void testDeleteMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                                              code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel:maritalStatus])
        assertNotNull maritalStatus
        def id = maritalStatus.id
        def deleteMe = MaritalStatus.get(id)
        maritalStatusService.delete([domainModel:maritalStatus])
        assertNull MaritalStatus.get(id)

    }

	@Test
    void testList(){
        def maritalStatus = maritalStatusService.list()
        assertTrue maritalStatus.size() > 0
    }
}