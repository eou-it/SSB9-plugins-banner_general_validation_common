/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.IpedsEthnicity
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.EthnicityDetail
import net.hedtech.banner.general.system.ldm.v1.EthnicityParentCategory
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class EthnicityCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

    Ethnicity i_success_ethnicity
    def ethnicityCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        newEthnicity()
        i_success_ethnicity = Ethnicity.findByCode('TT')
    }

    @Test
    void testListWithoutPaginationParams() {
        List ethnicities = ethnicityCompositeService.list([:])
        assertNotNull ethnicities
        assertFalse ethnicities.isEmpty()
        assertTrue ethnicities.size() > 0
    }

    @Test
    void testListWithPagination() {
        def paginationParams = [max: '2', offset: '0']
        List ethnicities = ethnicityCompositeService.list(paginationParams)
        assertNotNull ethnicities
        assertFalse ethnicities.isEmpty()
        assertTrue ethnicities.size() == 2
    }

    @Test
    void testCount() {
        assertNotNull i_success_ethnicity
        assertEquals Ethnicity.count(), ethnicityCompositeService.count()
    }

    @Test
    void testGetInvalidGuid() {
        try {
            ethnicityCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetNullGuid() {
        try {
            ethnicityCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def ethnicityDetails = ethnicityCompositeService.list(paginationParams)
        assertNotNull ethnicityDetails
        assertFalse ethnicityDetails.isEmpty()

        assertNotNull ethnicityDetails[0].guid
        def ethnicityDetail = ethnicityCompositeService.get(ethnicityDetails[0].guid)
        assertNotNull ethnicityDetail
        assertNotNull ethnicityDetail.code
        assertEquals ethnicityDetails[0].code, ethnicityDetail.code
        assertNotNull ethnicityDetail.guid
        assertEquals ethnicityDetails[0].guid, ethnicityDetail.guid
        assertNotNull ethnicityDetail.metadata
        assertEquals ethnicityDetails[0].metadata.dataOrigin, ethnicityDetail.metadata.dataOrigin
        assertEquals ethnicityDetails[0], ethnicityDetail
    }

    @Test
    void testFetchByEthnicityIdInvalid() {
        try {
            ethnicityCompositeService.fetchByEthnicityId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testFetchByEthnicityId() {
        EthnicityDetail ethnicityDetail = ethnicityCompositeService.fetchByEthnicityId(i_success_ethnicity.id)
        assertNotNull ethnicityDetail
        assertEquals i_success_ethnicity.id, ethnicityDetail.id
        assertEquals i_success_ethnicity.code, ethnicityDetail.code
        assertEquals i_success_ethnicity.description, ethnicityDetail.description
        assertEquals i_success_ethnicity.dataOrigin, ethnicityDetail.metadata.dataOrigin
        assertEquals getLdmEthnicity(i_success_ethnicity), ethnicityDetail.parentCategory
    }

    @Test
    void testFetchByEthnicityInvalid() {
        assertNull ethnicityCompositeService.fetchByEthnicityCode(null)
        assertNull ethnicityCompositeService.fetchByEthnicityCode('Q')
    }

    @Test
    void testFetchByEthnicityCode() {
        EthnicityDetail ethnicityDetail = ethnicityCompositeService.fetchByEthnicityCode(i_success_ethnicity.code)
        assertNotNull ethnicityDetail
        assertEquals i_success_ethnicity.id, ethnicityDetail.id
        assertEquals i_success_ethnicity.code, ethnicityDetail.code
        assertEquals i_success_ethnicity.description, ethnicityDetail.description
        assertEquals i_success_ethnicity.dataOrigin, ethnicityDetail.metadata.dataOrigin
        assertEquals getLdmEthnicity(i_success_ethnicity), ethnicityDetail.parentCategory
    }


    def getLdmEthnicity(def ethnicity) {
        if (ethnicity != null) {
            return ethnicity.ethnic == "1" ? EthnicityParentCategory.NON_HISPANIC.value :
                    (ethnicity.ethnic == "2" ? EthnicityParentCategory.HISPANIC.value : null)
        }
        return null
    }


    private def newEthnicity() {
        def race = Race.findByRace("IND")
        def ipedsEthnicity = IpedsEthnicity.findByCode("2")

        def ethnicity = new Ethnicity(
                code: "TT",
                description: "TTTTTTTTTT",
                ethnicCode: "T",
                electronicDataInterchangeEquivalent: "T",
                lmsEquivalent: "T",
                ethnic: "1",
                race: race,
                ipedsEthnicity: ipedsEthnicity,
        )
        ethnicity.save(failOnError: true, flush: true)
    }
}
