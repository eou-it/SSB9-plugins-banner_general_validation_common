package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class MaritalStatusServiceIntegrationTests extends BaseIntegrationTestCase {

    def maritalStatusService


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
    void testCreateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel: maritalStatus])
        assertNotNull maritalStatus
    }


    @Test
    void testUpdateMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel: maritalStatus])

        MaritalStatus maritalStatusUpdate = MaritalStatus.findWhere(code: "T")
        assertNotNull maritalStatusUpdate

        maritalStatusUpdate.description = "ZZ"
        maritalStatusUpdate = maritalStatusService.update([domainModel: maritalStatusUpdate])
        assertEquals "ZZ", maritalStatusUpdate.description
    }


    @Test
    void testDeleteMaritalStatus() {
        def maritalStatus = new MaritalStatus(electronicDataInterchangeEquivalent: "T", financeConversion: "T",
                code: "T", description: "TT")
        maritalStatus = maritalStatusService.create([domainModel: maritalStatus])
        assertNotNull maritalStatus
        def id = maritalStatus.id
        def deleteMe = MaritalStatus.get(id)
        maritalStatusService.delete([domainModel: maritalStatus])
        assertNull MaritalStatus.get(id)

    }


    @Test
    void testList() {
        def maritalStatus = maritalStatusService.list()
        assertTrue maritalStatus.size() > 0
    }


    @Test
    void testFetchAllWithGuid() {
        // sortColumn=Default (id), sortOrder=Database default
        List<MaritalStatus> entities = maritalStatusService.fetchAllWithGuid()
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "id", "ASC")

        // sortColumn=description, sortOrder=Database default
        entities = maritalStatusService.fetchAllWithGuid("description")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "description", "ASC")

        // sortColumn=Default (id), sortOrder=desc
        entities = maritalStatusService.fetchAllWithGuid(null, "desc")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "id", "DESC")

        // sortColumn=description, sortOrder=desc
        entities = maritalStatusService.fetchAllWithGuid("description", "desc")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "description", "DESC")
    }


    @Test
    void testFetchAllWithGuidByCodeInList() {
        def maritalStatusCodes = ['S', 'M']

        // sortColumn=Default (id), sortOrder=Database default
        List<MaritalStatus> entities = maritalStatusService.fetchAllWithGuidByCodeInList(maritalStatusCodes)
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "id", "ASC")

        // sortColumn=description, sortOrder=Database default
        entities = maritalStatusService.fetchAllWithGuidByCodeInList(maritalStatusCodes, "description")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "description", "ASC")

        // sortColumn=Default (id), sortOrder=desc
        entities = maritalStatusService.fetchAllWithGuidByCodeInList(maritalStatusCodes, null, "desc")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "id", "DESC")

        // sortColumn=description, sortOrder=desc
        entities = maritalStatusService.fetchAllWithGuidByCodeInList(maritalStatusCodes, "description", "desc")
        assertFalse entities.isEmpty()
        assertListIsSortedOnField(entities.maritalStatus, "description", "DESC")
    }


    private void assertListIsSortedOnField(def list, String field, String sortOrder = "ASC") {
        def prevListItemVal
        list.each {
            String curListItemVal = it[field]
            if (!prevListItemVal) {
                prevListItemVal = curListItemVal
            }
            if (sortOrder == "ASC") {
                assertTrue prevListItemVal.compareTo(curListItemVal) < 0 || prevListItemVal.compareTo(curListItemVal) == 0
            } else {
                assertTrue prevListItemVal.compareTo(curListItemVal) > 0 || prevListItemVal.compareTo(curListItemVal) == 0
            }
            prevListItemVal = curListItemVal
        }
    }

}
