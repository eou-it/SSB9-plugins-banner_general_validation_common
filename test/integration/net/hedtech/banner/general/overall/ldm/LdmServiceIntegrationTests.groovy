/*******************************************************************************
 Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.general.system.AcademicYear
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test

class LdmServiceIntegrationTests extends BaseIntegrationTestCase {

    def ldmService
    def academicYearService

    static List includeList = ['code', 'description']
    static List excludeList = ['description', 'lastModified']
    static def descriptionString = "The 2020 Acadmic Year"
    static def codeString = "2399"
    static def dateString = "This is not a date"
    static def i_creation_guid = '11599c85-afe8-4624-9832-106a716624a7'
    static Map propertiesMap = [code        : codeString,
                                description : descriptionString,
                                lastModified: dateString]


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @Test
    void testBindDataIncludeMap() {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include: includeList])
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, descriptionString
        assertNull academicYear.lastModified
    }


    @Test
    void testBindDataExcludeMap() {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [exclude: excludeList])
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }


    @Test
    void testBindDataIncludeAndExcludeMap() {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include: includeList, exclude: excludeList])
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }


    @Test
    void testBindDataExtraMapProperties() {
        def academicYear = new AcademicYear()
        propertiesMap.put('testing', 'Extra Values')
        ldmService.bindData(academicYear, propertiesMap, [exclude: excludeList])
        assertEquals academicYear.code, codeString
        assertNull academicYear.description
        assertNull academicYear.lastModified
    }


    @Test
    void testBindDataBindingErrors() {
        def academicYear = new AcademicYear()
        try {
            ldmService.bindData(academicYear, propertiesMap, [:])
        }
        catch (Exception e) {
            assertApplicationException(e, "AcademicYear")
        }
        assertNotNull academicYear?.errors
    }


    @Test
    void testBindDataOptionalRequired() {
        def academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include: includeList])
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, descriptionString
        assertNull academicYear.lastModified
        ldmService.bindData(academicYear, [description: "This is another test"], [include: includeList])
        assertEquals academicYear.code, codeString
        assertEquals academicYear.description, "This is another test"
        assertNull academicYear.lastModified
    }


    @Test
    void testUpdateGuidValue() {
        AcademicYear academicYear = new AcademicYear()
        ldmService.bindData(academicYear, propertiesMap, [include: includeList])
        academicYear = academicYearService.create(academicYear)
        def result = ldmService.updateGuidValue(academicYear.id, i_creation_guid, 'year')
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
        ldmService.bindData(academicYear, propertiesMap, [include: includeList])
        academicYear = academicYearService.create(academicYear)
        def result = ldmService.updateGuidValueByDomainKey(academicYear.code, i_creation_guid, 'year')
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


    @Test
    void testGetAcceptVersion_scenario1() {
        // Generic Accept header and no API versions - return null
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/json")
        def apiVersions = null
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertNull acceptVersion
    }


    @Test
    void testGetAcceptVersion_scenario2() {
        // v1 Accept header and no API versions - return v1
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        def apiVersions = null
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v1", acceptVersion
    }


    @Test
    void testGetAcceptVersion_scenario3() {
        // Generic Accept header - return latest version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/json")
        List<String> apiVersions = ["v3", "v1"]
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v3", acceptVersion
    }


    @Test
    void testGetAcceptVersion_scenario4() {
        // v3 Accept header for API which is up to v2 only - return v2 version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v3+json")
        List<String> apiVersions = ["v1", "v2"]
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v2", acceptVersion
    }


    @Test
    void testGetAcceptVersion_scenario5() {
        // v2 Accept header which API never handling - return previous version to v2 i.e. v1 version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v2+json")
        List<String> apiVersions = ["v1", "v3"]
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v1", acceptVersion
    }


    @Test
    void testGetAcceptVersion_DoubleDigitVersion() {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/json")
        List<String> apiVersions = ["v10", "v1"]
        String acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v10", acceptVersion

        request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v1+json")
        apiVersions = ["v10", "v1"]
        acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v1", acceptVersion

        request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", "application/vnd.hedtech.integration.v10+json")
        apiVersions = ["v10", "v1"]
        acceptVersion = LdmService.getAcceptVersion(apiVersions)
        assertEquals "v10", acceptVersion
    }


    @Test
    void testGetContentTypeVersion_scenario1() {
        // Generic Content-Type header and no API versions - return null
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/json")
        def apiVersions = null
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertNull ctVersion
    }


    @Test
    void testGetContentTypeVersion_scenario2() {
        // v1 Content-Type header and no API versions - return v1
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v1+json")
        def apiVersions = null
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v1", ctVersion
    }


    @Test
    void testGetContentTypeVersion_scenario3() {
        // Generic Content-Type header - return latest version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/json")
        List<String> apiVersions = ["v3", "v1"]
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v3", ctVersion
    }


    @Test
    void testGetContentTypeVersion_scenario4() {
        // v3 Content-Type header for API which is up to v2 only - return v2 version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v3+json")
        List<String> apiVersions = ["v1", "v2"]
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v2", ctVersion
    }


    @Test
    void testGetContentTypeVersion_scenario5() {
        // v2 Content-Type header which API never handling - return previous version to v2 i.e. v1 version
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v2+json")
        List<String> apiVersions = ["v1", "v3"]
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v1", ctVersion
    }


    @Test
    void testGetContentTypeVersion_DoubleDigitVersion() {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/json")
        List<String> apiVersions = ["v10", "v1"]
        String ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v10", ctVersion

        request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v1+json")
        apiVersions = ["v10", "v1"]
        ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v1", ctVersion

        request = LdmService.getHttpServletRequest()
        request.addHeader("Content-Type", "application/vnd.hedtech.integration.v10+json")
        apiVersions = ["v10", "v1"]
        ctVersion = LdmService.getContentTypeVersion(apiVersions)
        assertEquals "v10", ctVersion
    }

}
