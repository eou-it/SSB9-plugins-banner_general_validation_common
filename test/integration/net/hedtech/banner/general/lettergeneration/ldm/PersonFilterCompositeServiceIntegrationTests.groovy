/** *****************************************************************************
 © 2015 Ellucian.  All Rights Reserved.
 ****************************************************************************** */
package net.hedtech.banner.general.lettergeneration.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.lettergeneration.PopulationSelectionBase
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
    private static final String SEPARATOR = '-^'
    String application
    String user
    String selection
    String selection1
    String guid


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
        createPopulationSelectionBase(application, selection1, user, user)
        List personFiltersList = personFilterCompositeService.list([sort: 'title', order: 'desc'])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertTrue personFiltersList[0] instanceof PersonFilter
        assertEquals selection1, personFiltersList[0].selection
    }


    @Test
    void testListWithInvalidSortFiled() {
        shouldFail(RestfulApiValidationException) {
            personFilterCompositeService.list([sort: 'abbreviation', order: 'desc'])
        }
    }


    @Test
    void testListWithInvalidSortOrder() {
        shouldFail(RestfulApiValidationException) {
            personFilterCompositeService.list([sort: 'title', order: 'des'])
        }
    }


    @Test
    void testSchemaPropertiesForList() {
        def populationSelectionBase = createPopulationSelectionBase(application, selection, user, user)
        guid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, populationSelectionBase.id).guid

        List<PersonFilter> personFiltersList = personFilterCompositeService.list([:])
        assertNotNull personFiltersList
        assertTrue personFiltersList.size() > 0
        assertTrue personFiltersList[0] instanceof PersonFilter

        PersonFilter personFilter = personFiltersList.find {
            it.selection == selection
        }

        assertEquals selection, personFilter.selection
        assertEquals "Banner", personFilter.metadata.dataOrigin
        assertEquals guid, personFilter.guid
        assertEquals formAbbreviation(application, selection, user, personFilter.lastModifiedBy), personFilter.abbreviation
    }


    @Test
    void testCount() {
        assertEquals PopulationSelectionBase.count(), personFilterCompositeService.count()
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
        def populationSelectionBase = createPopulationSelectionBase(application, selection, user, user)
        guid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, populationSelectionBase.id).guid
        assertNotNull guid


        def personFilter = personFilterCompositeService.get(guid)
        assertNotNull personFilter
        assertNotNull personFilter.abbreviation
        assertEquals selection, personFilter.selection
        assertEquals "Banner", personFilter.metadata.dataOrigin
        assertEquals guid, personFilter.guid
        assertEquals formAbbreviation(application, selection, user, personFilter.lastModifiedBy), personFilter.abbreviation
    }


    private
    def createPopulationSelectionBase(String application, String selection, String creator, String user = null) {
        def populationSelectionBase = new PopulationSelectionBase(
                application: application,
                selection: selection,
                creatorId: creator,
                description: "Test",
                lockIndicator: true,
                typeIndicator: "M",
                lastModified: new Date(),
                lastModifiedBy: user,
                dataOrigin: "Banner"
        )
        populationSelectionBase.save(failOnError: true, flush: true)
        assertNotNull populationSelectionBase.id
        return populationSelectionBase
    }


    private String formAbbreviation(String application, String selection, String creator, String user = null) {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(application)
        stringBuilder.append(SEPARATOR)
        stringBuilder.append(selection)
        stringBuilder.append(SEPARATOR)
        stringBuilder.append(creator)
        if (user != null) {
            stringBuilder.append(SEPARATOR)
            stringBuilder.append(user)
        }

        return stringBuilder.toString()
    }
}
