package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class VisaTypeServiceTests extends BaseIntegrationTestCase {

    def visaTypeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateVisaType() {
        def visaType = new VisaType(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        visaType = visaTypeService.create([domainModel: visaType])
        assertNotNull visaType
    }


    void testUpdateVisaType() {
        def visaType = new VisaType(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        visaType = visaTypeService.create([domainModel: visaType])

        VisaType visaTypeUpdate = VisaType.findWhere(code: "T")
        assertNotNull visaTypeUpdate

        visaTypeUpdate.description = "ZZ"
        visaTypeUpdate = visaTypeService.update([domainModel: visaType])
        assertEquals "ZZ", visaTypeUpdate.description
    }


    void testDeleteVisaType() {
        def visaType = new VisaType(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        visaType = visaTypeService.create([domainModel: visaType])
        assertNotNull visaType
        def id = visaType.id
        def deleteMe = VisaType.get(id)
        visaTypeService.delete([domainModel: deleteMe])
        assertNull VisaType.get(id)

    }


    void testList() {
        def visaType = visaTypeService.list()
        assertTrue visaType.size() > 0
    }

}