/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

/**
 * <p> Integration Tests for DepartmentalHonor </p>
 */
class DepartmentalHonorIntegrationTests extends BaseIntegrationTestCase{

    @Before
    public void setUp(){
        formContext=['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
       super.tearDown()
    }

    /**
     * <p> Test for creating a valid DepartmentalHonor</p>
     * */
    @Test
    public void testCreateValidDepartmentalHonor(){
        def departmentalHonor = createValidDepartnmentalHonor()
        assert departmentalHonor instanceof DepartmentalHonor
        departmentalHonor.save(flush: true, failOnError: true)
        assertNotNull departmentalHonor.id
    }

    /**
     * <p> Test for creating a DepartmentalHonor with null code and validating the constraints </p>
     * */
    @Test
    public void testNullableForDepartmentHonor(){
        def departmentalHonor = new DepartmentalHonor()
        assertTrue departmentalHonor instanceof DepartmentalHonor
        assertFalse "Departmental Honor should have failed validation", departmentalHonor.validate()
        assertErrorsFor departmentalHonor, 'nullable',['code']
    }

    /**
     * <p> Test for creating a valid DepartmentalHonor and asserting for the values</p>
     * */
    @Test
    public void testValueOfDepartmentalHonor(){
        def departmentalHonor = createValidDepartnmentalHonor()
        assert departmentalHonor instanceof DepartmentalHonor
        departmentalHonor.save(flush: true, failOnError: true)

        def departmentalHonorResponse = departmentalHonor.get(departmentalHonor.id)
        assertEquals departmentalHonor.description,departmentalHonorResponse.description
        assertEquals departmentalHonor.code,departmentalHonorResponse.code
        assertEquals departmentalHonor.commencePrintIndicator,departmentalHonorResponse.commencePrintIndicator
        assertEquals departmentalHonor.transcPrintIndicator,departmentalHonorResponse.transcPrintIndicator

    }


    /**
     * <p>Test for creating an invalid DepartmentalHonor and expecting some constraint Validation Exception</p>
     * */
    @Test
    public void testInvalidDepartmentalHonor(){
        def departmentalHonor=createInvalidDepartnmentalHonor()
        shouldFail(ValidationException) {
            departmentalHonor.save(failOnError: true, flush: true)
        }
    }

    /**
     * <p> Test for updating a Departmental Honor Record</p>
     * */
    @Test
    public void testValidUpdateDepartmentalHonor(){
        def departmentHonor = createValidDepartnmentalHonor()
        departmentHonor.save(failOnError: true, flush: true)
        assertNotNull departmentHonor.id
        def departmentHonorUpdate = new DepartmentalHonor().get(departmentHonor.id)
        departmentHonorUpdate.description="Dummy Description"
        departmentHonorUpdate.save(failOnError: true, flush: true)
        assertEquals departmentHonorUpdate.description,"Dummy Description"
        assertEquals departmentHonor.id,departmentHonorUpdate.id
    }

    /**
     * <p> Test for invalid record update in DepartmentalHonor and expecting Exception</p>
     * */
    @Test
    void testUpdateInvalidInstitutionalHonor() {
        def departmentalHonor = createValidDepartnmentalHonor()
        departmentalHonor.save(failOnError: true, flush: true)
        assertNotNull departmentalHonor.id
        assertEquals 0L, departmentalHonor.version


        //Update the entity with invalid values
        departmentalHonor.description = "123456789012345678901234567890FAIL"
        departmentalHonor.transcPrintIndicator = "ZZZ"
        departmentalHonor.commencePrintIndicator = "Z"
        shouldFail(ValidationException) {
            departmentalHonor.save(failOnError: true, flush: true)
        }
    }

    /**
     * <p> Test the lastModified date format in DepartmentalHonor</p>
     * */
    @Test
    void testDatesOnDepartmentalHonor() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def currentDate = new Date()

        def departmentalHonor = createValidDepartnmentalHonor()
        departmentalHonor.save(flush: true, failOnError: true)
        departmentalHonor.refresh()
        assertNotNull departmentalHonor.id
        assertEquals date.format(currentDate), date.format(departmentalHonor.lastModified)
        assertEquals hour.format(currentDate), hour.format(departmentalHonor.lastModified)
    }
    /**
     * <p> Test for Optimistic Locking where manually we'll insert the record to increase the version and expecting an optimistic lock</p>
     * */
    @Test
    public void testOptimisticLock(){
        def departmentHonor = createValidDepartnmentalHonor()
        departmentHonor.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVHOND set STVHOND_VERSION = 999 where STVHOND_SURROGATE_ID = ?", [departmentHonor.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        departmentHonor.description = "UPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            departmentHonor.save(failOnError: true, flush: true)
        }

    }

    /**
     * <p> Test for Deleting a Departmental Honor </p>
     * */
    @Test
    public void testDeleteDepartnmentHonor(){
        def departnmentHonor = createValidDepartnmentalHonor()
        departnmentHonor.save(failOnError: true, flush: true)

        departnmentHonor.delete()

        def departnmentResponse = departnmentHonor.get(departnmentHonor.id)
        assertNull departnmentResponse
    }

    private def createValidDepartnmentalHonor() {
        def departmentalHonor = new DepartmentalHonor(
                code: "AAAAZ",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "Y",
                commencePrintIndicator: "Y",
        )
        return departmentalHonor
    }

    private def createInvalidDepartnmentalHonor(){
        def departmentalHonor = new DepartmentalHonor(
                code: "AAAAAAA",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "YAAA",
                commencePrintIndicator: "YAAA",
        )
        return departmentalHonor
    }
}
