/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.lettergeneration.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.lettergeneration.PopulationSelectionExtract
import net.hedtech.banner.general.lettergeneration.ldm.v2.PersonFilter
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class PersonFilterCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personFilterCompositeService
    public static final String LDM_NAME = 'person-filters'
    String application
    String user
    String selection
    String selection1
    String guid
    String key


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    private void initiializeDataReferences() {
        application = "GENERAL"
        user = "GRAILS_USER"
        selection = "SAT_Test"
        selection1 = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        key = "1001"
    }


    @Test
    void testBasicList() {
        List personFiltersList = personFilterCompositeService.list([:])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertTrue personFiltersList[0] instanceof PersonFilter
    }


    @Test
    void testListWithPagination() {
        List personFiltersList = personFilterCompositeService.list([max: '4', offset: '0'])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertEquals 4, personFiltersList.size()
        assertTrue personFiltersList[0] instanceof PersonFilter
    }


    @Test
    void testListWithSort() {
        createPopulationSelectionExtract(application, selection1, user, user, key)
        List personFiltersList = personFilterCompositeService.list([sort: 'abbreviation', order: 'desc'])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertTrue personFiltersList[0] instanceof PersonFilter
        assertEquals selection1, personFiltersList[0].abbreviation
    }


    @Test
    void testListWithInvalidSortFiled() {
        shouldFail(RestfulApiValidationException) {
            personFilterCompositeService.list([sort: 'title', order: 'desc'])
        }
    }


    @Test
    void testListWithInvalidSortOrder() {
        shouldFail(RestfulApiValidationException) {
            personFilterCompositeService.list([sort: 'abbreviation', order: 'des'])
        }
    }


    @Test
    void testSchemaPropertiesForList() {
        PopulationSelectionExtract populationSelectionExtract = createPopulationSelectionExtract(application, selection, user, user, key)
        String domain_key = "${populationSelectionExtract.application}-^${populationSelectionExtract.selection}-^${populationSelectionExtract.creatorId}-^${populationSelectionExtract.lastModifiedBy}"
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(LDM_NAME, domain_key)
        assertNotNull globalUniqueIdentifier
        assertNotNull globalUniqueIdentifier.guid
        List<PersonFilter> personFiltersList = personFilterCompositeService.list([:])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertTrue personFiltersList[0] instanceof PersonFilter

        PersonFilter personFilter = personFiltersList.find {
            it.abbreviation == selection
        }

        assertEquals selection, personFilter.abbreviation
        assertEquals "Banner", personFilter.metadata.dataOrigin
        assertEquals globalUniqueIdentifier.guid, personFilter.guid
        assertEquals globalUniqueIdentifier.domainKey, personFilter.title
    }


    @Test
    void testGetInvalidGuid() {
        try {
            personFilterCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetNullGuid() {
        try {
            personFilterCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGet() {
        PopulationSelectionExtract populationSelectionExtract = createPopulationSelectionExtract(application, selection, user, user, key)
        String domain_key = "${populationSelectionExtract.application}-^${populationSelectionExtract.selection}-^${populationSelectionExtract.creatorId}-^${populationSelectionExtract.lastModifiedBy}"
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(LDM_NAME, domain_key)
        assertNotNull globalUniqueIdentifier
        assertNotNull globalUniqueIdentifier.guid

        def personFilter = personFilterCompositeService.get(globalUniqueIdentifier.guid)
        assertNotNull personFilter
        assertNotNull personFilter.title
        assertEquals selection, personFilter.abbreviation
        assertEquals "Banner", personFilter.metadata.dataOrigin
        assertEquals globalUniqueIdentifier.guid, personFilter.guid
        assertEquals globalUniqueIdentifier.domainKey, personFilter.title
    }


    private
    def createPopulationSelectionExtract(String application, String selection, String creator, String user, String key) {
        def populationSelectionExtract = new PopulationSelectionExtract(
                application: application,
                selection: selection,
                creatorId: creator,
                key: key,
                description: "Test",
                systemIndicator: "S",
                lastModified: new Date(),
                lastModifiedBy: user,
                dataOrigin: "Banner"
        )
        populationSelectionExtract.save(failOnError: true, flush: true)
        assertNotNull populationSelectionExtract.id
        return populationSelectionExtract
    }


}
