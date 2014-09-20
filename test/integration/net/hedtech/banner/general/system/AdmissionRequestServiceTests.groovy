package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

class AdmissionRequestServiceTests extends BaseIntegrationTestCase {

    def admissionRequestService


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
    void testCreateAdmissionRequest() {
        def admissionRequest = new AdmissionRequest(code: "TT", description: "TT", displayWebIndicator:"False", voiceResponseEligIndicator:"T", voiceResponseMsgNumber:"1", tableName:"T")
        admissionRequest = admissionRequestService.create([domainModel: admissionRequest])
        assertNotNull admissionRequest
    }


	@Test
    void testUpdateAdmissionRequest() {
        def admissionRequest = new AdmissionRequest(code: "TT", description: "TT", displayWebIndicator:"False", voiceResponseEligIndicator:"T", voiceResponseMsgNumber:"1", tableName:"T")
        admissionRequest = admissionRequestService.create([domainModel: admissionRequest])

        AdmissionRequest admissionRequestUpdate = AdmissionRequest.findWhere(code: "TT")
        assertNotNull admissionRequestUpdate

        admissionRequestUpdate.description = "ZZ"
        admissionRequestUpdate = admissionRequestService.update([domainModel: admissionRequestUpdate])
        assertEquals "ZZ", admissionRequestUpdate.description
    }


	@Test
    void testDeleteAdmissionRequest() {
        def admissionRequest = new AdmissionRequest(code: "TT", description: "TT", displayWebIndicator:"False", voiceResponseEligIndicator:"T", voiceResponseMsgNumber:"1", tableName:"T")
        admissionRequest = admissionRequestService.create([domainModel: admissionRequest])
        assertNotNull admissionRequest
        def id = admissionRequest.id
        def deleteMe = AdmissionRequest.get(id)
        admissionRequestService.delete([domainModel: deleteMe])
        assertNull AdmissionRequest.get(id)
    }


	@Test
    void testList() {
        def admissionRequest = admissionRequestService.list()
        assertTrue admissionRequest.size() > 0
    }
}
