/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException
import org.springframework.jdbc.UncategorizedSQLException

/**
 * Integration Test cases for AcademicCredentialView which is Read Only view
 */
class AcademicCredentialViewIntegrationTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * This test case is checking for creating one of record on read only view
     */
    @Test
    void testReadOnlyForCreateAcademicCredential() {
        def academicCredential = newAcademicCredentialView()
        academicCredential.id = 'test'
        academicCredential.version= 0
        assertNotNull academicCredential
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicCredential.save(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for updating one of record on read only view
     */

    @Test
    void testReadOnlyForUpdateAcademicCredential() {
        AcademicCredentialView academicCredential = AcademicCredentialView.findByCode('MA')
        assertNotNull academicCredential
        academicCredential.description = 'Test for Update'
        shouldFail(InvalidDataAccessResourceUsageException) {
            academicCredential.save(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for deletion one of record on read only view
     */

    @Test
    void testReadOnlyForDeleteAcademicCredential() {
        AcademicCredentialView academicCredential = AcademicCredentialView.findByCode('MA')
        assertNotNull academicCredential
        shouldFail(UncategorizedSQLException) {
            academicCredential.delete(flush: true, onError: true)
        }
    }

    /**
     * This test case is checking for academic credential view list
     */

    @Test
    void testList() {
        def params = [max: '500', offset: '0', order: 'ASC']
        def degree = Degree.findByCode('MA')
        assertNotNull degree
        List academicCredentialList = AcademicCredentialView.list(params)
        assertNotNull academicCredentialList
        assertFalse academicCredentialList.isEmpty()
        def typeList = ['degree', 'honorary', 'diploma', 'certificate']
        assertTrue typeList.containsAll(academicCredentialList.type)
        assertTrue academicCredentialList.description.contains(degree.description)
        assertTrue academicCredentialList.code.contains(degree.code)
    }

    /**
     * This test case is checking for academic credential View get
     */

    @Test
    void testGet() {
        assertNull AcademicCredentialView.get("")
        assertNull AcademicCredentialView.get(null)
        def degree = Degree.findByCode('MA')
        assertNotNull degree
        def academicCredential = AcademicCredentialView.get(degree.id)
        assertNotNull academicCredential
        def typeList = ['degree', 'honorary', 'diploma', 'certificate']
        assertTrue typeList.contains(academicCredential.type)
        assertEquals academicCredential.description, degree.description
        assertEquals academicCredential.code, degree.code
    }

    private def newAcademicCredentialView() {
        new AcademicCredentialView(
                id: 'test',
                code: 'test',
                description: 'test data',
                dataOrigin: 'test',
                guid: 'test_guid',
                type: 'test'
        )

    }

}
