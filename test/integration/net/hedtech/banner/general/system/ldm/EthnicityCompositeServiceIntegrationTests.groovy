/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.IpedsEthnicity
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.EthnicityDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test

class EthnicityCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Ethnicity i_success_ethnicity
    def ethnicityCompositeService

    def i_success_guid = 'a' * 36
    def i_success_code = 'XY'
    def i_success_description = 'New Ethnicity'
    def i_success_data_origin = 'Banner'
    def i_success_parent_category = 'Hispanic'

    def u_success_description = 'Update Ethnicity'
    def u_success_data_origin = 'Banner'
    def u_success_parent_category = 'Non-Hispanic'


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
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        List ethnicities = ethnicityCompositeService.list([:])
        assertNotNull ethnicities
        assertFalse ethnicities.isEmpty()
        assertTrue ethnicities.size() > 0
    }


    @Test
    void testListWithPagination() {
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        def paginationParams = [max: '2', offset: '0']
        List ethnicities = ethnicityCompositeService.list(paginationParams)
        assertNotNull ethnicities
        assertFalse ethnicities.isEmpty()
        assertTrue ethnicities.size() == 2
    }


    @Test
    void testCount() {
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        assertNotNull i_success_ethnicity
        assertEquals Ethnicity.count(), ethnicityCompositeService.count()
    }


    @Test
    void testGetInvalidGuid() {
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        try {
            ethnicityCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetNullGuid() {
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        try {
            ethnicityCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGet() {
        setAcceptHeader("application/vnd.hedtech.integration.v1+json")
        def paginationParams = [max: '1', offset: '0']
        def ethnicityDetails = ethnicityCompositeService.list(paginationParams)
        assertNotNull ethnicityDetails
        assertFalse ethnicityDetails.isEmpty()

        assertNotNull ethnicityDetails[0].guid
        def ethnicityDetail = ethnicityCompositeService.get(ethnicityDetails[0].guid)
        assertNotNull ethnicityDetail.toString()
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
        assertEquals ethnicityCompositeService.getHeDMEnumeration(i_success_ethnicity.ethnic), ethnicityDetail.parentCategory
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
        assertEquals ethnicityCompositeService.getHeDMEnumeration(i_success_ethnicity.ethnic), ethnicityDetail.parentCategory
    }


    @Test
    void testCreateEthnicity() {
        Map content = newEthnicityMap()
        def o_success_ethnicity_create = ethnicityCompositeService.create(content)
        assertNotNull o_success_ethnicity_create
        assertNotNull o_success_ethnicity_create.guid
        assertEquals i_success_code, o_success_ethnicity_create.code
        assertEquals i_success_description, o_success_ethnicity_create.description
        assertEquals i_success_data_origin, o_success_ethnicity_create.dataOrigin
        assertEquals i_success_parent_category, o_success_ethnicity_create.parentCategory
    }


    @Test
    void testCreateEthnicityWithUserInputGuid() {
        Map content = newEthnicityMap()
        content.guid = i_success_guid
        def o_success_ethnicity_create = ethnicityCompositeService.create(content)
        assertNotNull o_success_ethnicity_create
        assertEquals i_success_guid, o_success_ethnicity_create.guid
        assertEquals i_success_code, o_success_ethnicity_create.code
        assertEquals i_success_description, o_success_ethnicity_create.description
        assertEquals i_success_data_origin, o_success_ethnicity_create.dataOrigin
        assertEquals i_success_parent_category, o_success_ethnicity_create.parentCategory
    }


    @Test
    void testCreateInvalidEthnicityCode() {
        Map content = newEthnicityMap()
        content.remove('code')
        try {
            ethnicityCompositeService.create(content)
        } catch (Exception ae) {
            assertApplicationException ae, "code.required.message"
        }
    }


    @Test
    void testCreateInvalidEthnicityDescription() {
        Map content = newEthnicityMap()
        content.remove('description')
        try {
            ethnicityCompositeService.create(content)
        } catch (Exception ae) {
            assertApplicationException ae, "description.required.message"
        }
    }


    @Test
    void testUpdateEthnicity() {
        Map create_content = newEthnicityMap()
        def o_success_ethnicity_create = ethnicityCompositeService.create(create_content)
        assertNotNull o_success_ethnicity_create
        assertNotNull o_success_ethnicity_create.guid
        assertEquals i_success_code, o_success_ethnicity_create.code
        assertEquals i_success_description, o_success_ethnicity_create.description
        assertEquals i_success_data_origin, o_success_ethnicity_create.dataOrigin
        assertEquals i_success_parent_category, o_success_ethnicity_create.parentCategory

        Map update_content = updateEthnicityMap(o_success_ethnicity_create.guid)
        def o_success_ethnicity_update = ethnicityCompositeService.update(update_content)
        assertNotNull o_success_ethnicity_update
        assertEquals o_success_ethnicity_create.guid, o_success_ethnicity_update.guid
        assertEquals i_success_code, o_success_ethnicity_update.code
        assertEquals u_success_description, o_success_ethnicity_update.description
        assertEquals u_success_data_origin, o_success_ethnicity_update.dataOrigin
        assertEquals u_success_parent_category, o_success_ethnicity_update.parentCategory
    }


    private Map newEthnicityMap() {
        Map params = [code          : i_success_code,
                      description   : i_success_description,
                      metadata      : [dataOrigin: i_success_data_origin],
                      parentCategory: i_success_parent_category
        ]
        return params
    }


    private Map updateEthnicityMap(guid) {
        Map params = [id            : guid,
                      description   : u_success_description,
                      metadata      : [dataOrigin: u_success_data_origin],
                      parentCategory: u_success_parent_category
        ]

        return params
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


    private void setAcceptHeader(String acceptHeader) {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", acceptHeader)
    }

}
