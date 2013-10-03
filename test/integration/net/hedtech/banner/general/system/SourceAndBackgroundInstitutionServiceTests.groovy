package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class SourceAndBackgroundInstitutionServiceTests extends BaseIntegrationTestCase {

    def sourceAndBackgroundInstitutionService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution(code: "T", typeIndicator: "T", srceIndicator: "T", description: "TT", ediCapable: "T", fice: "T", voiceResponseMsgNumber: 1)
        sourceAndBackgroundInstitution = sourceAndBackgroundInstitutionService.create([domainModel: sourceAndBackgroundInstitution])
        assertNotNull sourceAndBackgroundInstitution
    }


    void testUpdateSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution(code: "T", typeIndicator: "T", srceIndicator: "T", description: "TT", ediCapable: "T", fice: "T", voiceResponseMsgNumber: 1)
        sourceAndBackgroundInstitution = sourceAndBackgroundInstitutionService.create([domainModel: sourceAndBackgroundInstitution])

        SourceAndBackgroundInstitution sourceAndBackgroundInstitutionUpdate = SourceAndBackgroundInstitution.findWhere(code: "T")
        assertNotNull sourceAndBackgroundInstitutionUpdate

        sourceAndBackgroundInstitutionUpdate.description = "ZZ"
        sourceAndBackgroundInstitutionUpdate = sourceAndBackgroundInstitutionService.update([domainModel: sourceAndBackgroundInstitutionUpdate])
        assertEquals "ZZ", sourceAndBackgroundInstitutionUpdate.description
    }


    void testDeleteSourceAndBackgroundInstitution() {
        def sourceAndBackgroundInstitution = new SourceAndBackgroundInstitution(code: "T", typeIndicator: "T", srceIndicator: "T", description: "TT", ediCapable: "T", fice: "T", voiceResponseMsgNumber: 1)
        sourceAndBackgroundInstitution = sourceAndBackgroundInstitutionService.create([domainModel: sourceAndBackgroundInstitution])
        assertNotNull sourceAndBackgroundInstitution

        def id = sourceAndBackgroundInstitution.id
        def deleteMe = SourceAndBackgroundInstitution.get(id)
        sourceAndBackgroundInstitutionService.delete([domainModel: deleteMe])
        assertNull SourceAndBackgroundInstitution.get(id)
    }


    void testList() {
        def sourceAndBackgroundInstitution = sourceAndBackgroundInstitutionService.list()
        assertTrue sourceAndBackgroundInstitution.size() > 0
    }

}
