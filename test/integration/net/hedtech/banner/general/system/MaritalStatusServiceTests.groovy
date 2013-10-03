package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class MaritalStatusServiceTests extends BaseIntegrationTestCase {

    def maritalStatusService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                                              code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel:maritalStatus])
        assertNotNull maritalStatus
    }


    void testUpdateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                                              code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel:maritalStatus])

        MaritalStatus maritalStatusUpdate = MaritalStatus.findWhere(code: "T")
        assertNotNull maritalStatusUpdate

        maritalStatusUpdate.description = "ZZ"
        maritalStatusUpdate = maritalStatusService.update([domainModel:maritalStatus])
        assertEquals "ZZ", maritalStatusUpdate.description
    }


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

    void testList(){
        def maritalStatus = maritalStatusService.list()
        assertTrue maritalStatus.size() > 0
    }
}
