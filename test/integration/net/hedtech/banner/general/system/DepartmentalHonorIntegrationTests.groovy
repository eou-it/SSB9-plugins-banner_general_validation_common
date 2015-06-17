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

    @Test
    public void testCreateValidDepartmentalHonor(){
        def departmentalHonor = createValidForCreateDepartnmentalHonor()
        assert departmentalHonor instanceof DepartmentalHonor
        departmentalHonor.save(flush: true, failOnError: true)
        assertNotNull departmentalHonor.id
    }

    @Test
    public void testNullableForDepartnmentHonor(){
        def departmentalHonor = new DepartmentalHonor()
        assertTrue departmentalHonor instanceof DepartmentalHonor
        assertFalse "Departmental Honor should have failed validation", departmentalHonor.validate()
        assertErrorsFor departmentalHonor, 'nullable',['code']
    }

    @Test
    public void testValueOfDepartmentalHonor(){
        def departmentalHonor = createValidForCreateDepartnmentalHonor()
        assert departmentalHonor instanceof DepartmentalHonor
        departmentalHonor.save(flush: true, failOnError: true)

        def departmentalHonorResponse = departmentalHonor.get(departmentalHonor.id)
        assertEquals departmentalHonor.description,departmentalHonorResponse.description
        assertEquals departmentalHonor.code,departmentalHonorResponse.code
        assertEquals departmentalHonor.commencePrintIndicator,departmentalHonorResponse.commencePrintIndicator
        assertEquals departmentalHonor.transcPrintIndicator,departmentalHonorResponse.transcPrintIndicator

    }


    @Test
    public void testInvalidDepartnmentHonor(){
        def departmentalHonor=createInvalidForCreateDepartnmentalHonor()
        shouldFail(ValidationException) {
            departmentalHonor.save(failOnError: true, flush: true)
        }
        }

    @Test
    public void testUpdateDepartnmentHonor(){
        def departnmentHonor = createValidForCreateDepartnmentalHonor()
        departnmentHonor.save(failOnError: true, flush: true)
        assertEquals 0L, departnmentHonor.version
        assertEquals "AAAAZ", departnmentHonor.code
        assertEquals "123456789012345678901234567890", departnmentHonor.description
        assertEquals "Y", departnmentHonor.transcPrintIndicator
        assertEquals "Y", departnmentHonor.commencePrintIndicator

        departnmentHonor = departnmentHonor.get(departnmentHonor.id)
        departnmentHonor.description='Dummy Value'
        departnmentHonor.commencePrintIndicator='Y'
        shouldFail(HibernateOptimisticLockingFailureException) {
            departnmentHonor.save(failOnError: true, flush: true)
        }
    }

    //Optimastic Locking TestCase
    @Test
    public void testOptimasticLock(){
        def departnmentHonor = createValidForCreateDepartnmentalHonor()
        departnmentHonor.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVHOND set STVHOND_VERSION = 999 where STVHOND_SURROGATE_ID = ?", [departnmentHonor.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        departnmentHonor.description = "UPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            departnmentHonor.save(failOnError: true, flush: true)
        }

    }

    @Test
    public void testDeleteDepartnmentHonor(){
        def departnmentHonor = createValidForCreateDepartnmentalHonor()
        departnmentHonor.save(failOnError: true, flush: true)

        departnmentHonor.delete()

        def departnmentResponse = departnmentHonor.get(departnmentHonor.id)
        assertNull departnmentResponse
    }

    private def createValidForCreateDepartnmentalHonor() {
        def departmentalHonor = new DepartmentalHonor(
                code: "AAAAZ",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "Y",
                commencePrintIndicator: "Y",
        )
        return departmentalHonor
    }

    private def createInvalidForCreateDepartnmentalHonor(){
        def departmentalHonor = new DepartmentalHonor(
                code: "AAAAAAA",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "YAAA",
                commencePrintIndicator: "YAAA",
        )
        return departmentalHonor
    }
}
