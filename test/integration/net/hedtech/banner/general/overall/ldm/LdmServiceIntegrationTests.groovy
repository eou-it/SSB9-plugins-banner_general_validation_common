package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.general.system.AcademicYear
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class LdmServiceIntegrationTests extends BaseIntegrationTestCase {

    def ldmService
    def academicYearService

    static List includeList = ['code','description']
    static List excludeList = ['description','lastModified']
    static def descriptionString = "The 2020 Acadmic Year"
    static def codeString = "2399"
    static def dateString = "This is not a date"
    static def i_creation_guid = '11599c85-afe8-4624-9832-106a716624a7'
    static Map propertiesMap = [code:codeString,
                                description:descriptionString,
                                lastModified:dateString]


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testBindDataIncludeMap () {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include:includeList] )
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, descriptionString
        assertNull academicYear.lastModified
    }

    @Test
    void testBindDataExcludeMap () {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [exclude:excludeList] )
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }

    @Test
    void testBindDataIncludeAndExcludeMap () {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include:includeList,exclude:excludeList] )
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }

    @Test
    void testBindDataExtraMapProperties () {
        def academicYear = new AcademicYear()
        propertiesMap.put('testing','Extra Values')
        ldmService.bindData(academicYear, propertiesMap, [exclude:excludeList] )
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }

    @Test
    void testBindDataBindingErrors () {
        def academicYear = new AcademicYear()
        try {
            ldmService.bindData(academicYear, propertiesMap, [:])
        }
        catch (Exception e) {
            assertApplicationException( e, "AcademicYear")
        }
        assertNotNull academicYear?.errors
    }

    @Test
    void testBindDataOptionalRequired () {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include:includeList] )
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, descriptionString
        assertNull academicYear.lastModified
        ldmService.bindData(academicYear, [description:"This is another test"], [include:includeList] )
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, "This is another test"
        assertNull academicYear.lastModified
    }

    @Test
    void testUpdateGuidValue() {
        AcademicYear academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include:includeList] )
        academicYear = academicYearService.create(academicYear)
        def result = ldmService.updateGuidValue(academicYear.id,i_creation_guid,'year')
        assertEquals result.guid, i_creation_guid
    }

    @Test
    void testUpdateGuidValueInvalidId() {
        try {
            ldmService.updateGuidValue(null, i_creation_guid, 'year')
        } catch (Exception e) {
            assertApplicationException e, "NotFoundException"
        }
    }

    @Test
    void testUpdateGuidValueByDomainKey() {
        AcademicYear academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include:includeList] )
        academicYear = academicYearService.create(academicYear)
        def result = ldmService.updateGuidValueByDomainKey(academicYear.code,i_creation_guid,'year')
        assertEquals result.guid, i_creation_guid
    }

    @Test
    void testUpdateGuidValueByDomainKeyInvalidId() {
        try {
            ldmService.updateGuidValueByDomainKey(null, i_creation_guid, 'year')
        } catch (Exception e) {
            assertApplicationException e, "NotFoundException"
        }
    }
}