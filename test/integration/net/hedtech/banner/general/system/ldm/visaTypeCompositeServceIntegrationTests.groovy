/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AdmissionRequest
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.VisaType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test

class visaTypeCompositeServceIntegrationTests  extends BaseIntegrationTestCase{

    def visaTypeCompositeService
    VisaType i_success_visaType
    private static final String VISATYPE_LDM_NAME = 'visa-types'

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        //initiializeDataReferences()
    }

    @Test
    void testListWithoutUserPagination() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")
        List visaType = visaTypeCompositeService.list([:])
        assertNotNull visaType
        assertFalse visaType.isEmpty()
        assertTrue visaType.size() > 0
    }

    @Test
    void testListWithPagination() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")
        def paginationParams = [max: '2', offset: '0']
        List visaType = visaTypeCompositeService.list(paginationParams)
        assertNotNull visaType
        assertFalse visaType.isEmpty()
        assertTrue visaType.size() == 2
    }

    @Test
    void testCount() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")
        //assertNotNull i_success_gradeChangeReason
        assertEquals VisaType.count(), visaTypeCompositeService.count()
    }

    @Test
    void testGetInvalidGuid() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")
        try {
            visaTypeCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetNullGuid() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")
        try {
            visaTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testCheckImmigrantForNullNonResIndicatorGet() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")


        newVisaType("")
        List <GlobalUniqueIdentifier> globUnIds = fetchByLdmNameAndDomainKeys(VISATYPE_LDM_NAME, "TT")
        def globUniqId=globUnIds[0].guid

        def visaTypeDetail = visaTypeCompositeService.get(globUniqId)
        assertNotNull visaTypeDetail
        assertEquals visaTypeDetail.category, "immigrant"
    }

    @Test
    void testCheckImmigrantForGet() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")


        newVisaType("N")
        List <GlobalUniqueIdentifier> globUnIds = fetchByLdmNameAndDomainKeys(VISATYPE_LDM_NAME, "TT")
        def globUniqId=globUnIds[0].guid

        def visaTypeDetail = visaTypeCompositeService.get(globUniqId)
        assertNotNull visaTypeDetail
        assertEquals visaTypeDetail.category, "immigrant"
    }

    @Test
    void testCheckNonImmigrantForGet() {
        setAcceptHeader("application/vnd.hedtech.integration.v4+json")


        newVisaType("Y")
        List <GlobalUniqueIdentifier> globUnIds = fetchByLdmNameAndDomainKeys(VISATYPE_LDM_NAME, "TT")
        def globUniqId=globUnIds[0].guid

        def visaTypeDetail = visaTypeCompositeService.get(globUniqId)
        assertNotNull visaTypeDetail
        assertEquals visaTypeDetail.category, "nonImmigrant"
    }

    private void setAcceptHeader(String acceptHeader) {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", acceptHeader)
    }

    private def newVisaType(String i_nonResIndicator) {
        def admr = new AdmissionRequest(code: "TTTT", description: "TTTTT", tableName: "TTTTT", voiceResponseMsgNumber: 1,
                voiceResponseEligIndicator: "T", displayWebIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
        admr.save(flush: true, failOnError: true)

        def visa = new VisaType(code: "TT", description: "TTTTT", nonResIndicator: i_nonResIndicator,
                voiceResponseMsgNumber: new Integer(1),
                statscanCde2: new Integer(2), sevisEquiv: "TT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner", admrCode: admr)

        visa.save(failOnError: true, flush: true)
    }

}
