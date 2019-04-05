package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class VisaTypeServiceTests extends BaseIntegrationTestCase {

    def visaTypeService


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
    void testCreateVisaType() {
        def visaType = new VisaType(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        visaType = visaTypeService.create([domainModel: visaType])
        assertNotNull visaType
    }


    @Test
    void testUpdateVisaType() {
        def visaType = new VisaType(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        visaType = visaTypeService.create([domainModel: visaType])

        VisaType visaTypeUpdate = VisaType.findWhere(code: "T")
        assertNotNull visaTypeUpdate

        visaTypeUpdate.description = "ZZ"
        visaTypeUpdate = visaTypeService.update([domainModel: visaTypeUpdate])
        assertEquals "ZZ", visaTypeUpdate.description
    }


    @Test
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


    @Test
    void testList() {
        def visaType = visaTypeService.list()
        assertTrue visaType.size() > 0
    }

    @Test
    void testFetchAllWithGuidByCodeInList(){
        List<VisaType> visaTypes = visaTypeService.list()
        def visaTypeGuids = visaTypeService.fetchAllWithGuidByCodeInList(visaTypes.code)
        assertTrue visaTypeGuids.size()>0
    }

    @Test
    void testFetchAllWithGuid(){
        def visaTypes = visaTypeService.fetchAllWithGuid()
        assertTrue visaTypes.size()>0
    }

}
