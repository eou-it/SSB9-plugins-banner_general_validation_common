/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException
/**
 * Integration Test cases for AcademicCredential which is Read Only view
 */
class AcademicCredentialIntegrationTests extends BaseIntegrationTestCase {


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
        AcademicCredential academicCredential = AcademicCredential.findByCode('MA')
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
        AcademicCredential academicCredential = AcademicCredential.findByCode('MA')
        assertNotNull academicCredential
        shouldFail(InvalidDataAccessResourceUsageException) {
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
        List academicCredentialList = AcademicCredential.list(params)
        assertNotNull academicCredentialList
        assertFalse academicCredentialList.isEmpty()
      //  def typeList = ['degree', 'honorary', 'diploma', 'certificate']
     //   assertTrue typeList.containsAll(academicCredentialList.type)
        assertTrue academicCredentialList.description.contains(degree.description)
        assertTrue academicCredentialList.code.contains(degree.code)
    }

    /**
     * This test case is checking for academic credential View get
     */

    @Test
    void testGet() {
        assertNull AcademicCredential.get("")
        assertNull AcademicCredential.get(null)
        def degree = Degree.findByCode('MA')
        assertNotNull degree
        def academicCredential = AcademicCredential.get(degree.id)
        assertNotNull academicCredential
      //  def typeList = ['degree', 'honorary', 'diploma', 'certificate']
      //  assertTrue typeList.contains(academicCredential.type)
        assertEquals academicCredential.description, degree.description
        assertEquals academicCredential.code, degree.code
    }

    @Test
    void testCount(){
        params.put('type','degree')
        def criteria = []
        criteria.add([key: 'type', binding: 'type', operator: Operators.EQUALS_IGNORE_CASE])
        def filterData = [:]
        filterData.params = params
        filterData.criteria = criteria
        def count = AcademicCredential.countAll(filterData)
        def sqlCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sqlCount = sql.firstRow("select count(*) as cnt from gvq_academic_credentials where TYPE= ?", ['degree'])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        assertEquals count.toInteger(),sqlCount.cnt.toInteger()
    }

    @Test
    void testSearch(){
        params.put('type','degree')
        def criteria = []
        criteria.add([key: 'type', binding: 'type', operator: Operators.EQUALS_IGNORE_CASE])
        def filterData = [:]
        filterData.params = params
        filterData.criteria = criteria
        List<AcademicCredential> academicCredentialList = AcademicCredential.fetchSearch(filterData,[max:20,offset: 0])
        academicCredentialList.each{
            academicCredential->
                assertNotNull academicCredential
                assertNotNull academicCredential.id
                assertNotNull academicCredential.type
                assertEquals 'degree',academicCredential.type
        }
    }
    @Test
    void testSuplementaryDesc(){
        List<AcademicCredential> academicCredentialList = AcademicCredential.list()
        academicCredentialList.each {
            academicCredential->
                assertNotNull academicCredential
                if(academicCredential.suplementaryDesc){
                    AcademicCredential academicCred = AcademicCredential.fetchByGuid(academicCredential.guid)
                    assertEquals academicCredential.guid,academicCred.guid
                    assertEquals academicCredential.suplementaryDesc,academicCred.suplementaryDesc
                    assertEquals academicCredential.type,academicCred.type
                    assertEquals academicCredential.code,academicCred.code
                    assertEquals academicCredential.description,academicCred.description
                    assertEquals academicCredential.id,academicCred.id
                }

        }
    }

    private def newAcademicCredentialView() {
        new AcademicCredential(
                id: 'test',
                code: 'test',
                description: 'test data',
                dataOrigin: 'test',
                guid: 'test_guid',
                type: 'test'
        )

    }

}
