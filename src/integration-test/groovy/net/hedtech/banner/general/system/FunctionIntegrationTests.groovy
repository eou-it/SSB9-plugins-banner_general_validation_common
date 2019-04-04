/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class FunctionIntegrationTests extends BaseIntegrationTestCase {

    def functionService


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
    void testCreateFunction() {
        def function = newFunction()
        save function
        //Test if the generated entity now has an id assigned
        assertNotNull function.id
    }


    @Test
    void testUpdateFunction() {
        def function = newFunction()
        save function

        assertNotNull function.id
        assertEquals 0L, function.version
        assertEquals "TTTTT", function.code
        assertEquals "TTTTT", function.description
        assertEquals "EBRK", function.etypCode

        //Update the entity
        def testDate = new Date()
        function.code = "UUUUU"
        function.description = "UUUUU"
        function.etypCode = "EKEY"
        function.lastModified = testDate
        function.lastModifiedBy = "test"
        function.dataOrigin = "Banner"
        save function

        function = Function.get(function.id)
        assertEquals 1L, function?.version
        assertEquals "UUUUU", function.code
        assertEquals "UUUUU", function.description
        assertEquals "EKEY", function.etypCode
    }


    @Test
    void testOptimisticLock() {
        def function = newFunction()
        save function

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVFUNC set GTVFUNC_VERSION = 999 where GTVFUNC_SURROGATE_ID = ?", [function.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        function.code = "UUUUU"
        function.description = "UUUUU"
        function.etypCode = "UUUU"
        function.lastModified = new Date()
        function.lastModifiedBy = "test"
        function.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            function.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteFunction() {
        def function = newFunction()
        save function
        def id = function.id
        assertNotNull id
        function.delete()
        assertNull Function.get(id)
    }


    @Test
    void testValidation() {
        def function = newFunction()
        assertTrue "Function could not be validated as expected due to ${function.errors}", function.validate()
    }


    @Test
    void testNullValidationFailure() {
        def function = new Function()
        assertFalse "Function should have failed validation", function.validate()
        assertErrorsFor function, 'nullable', ['code', 'description', 'etypCode']
        assertNoErrorsFor function, []
    }


    @Test
    void testMaxSizeValidationFailures() {
        def function = new Function()
        assertFalse "Function should have failed validation", function.validate()
        assertErrorsFor function, 'maxSize', []
    }



    private def newFunction() {
        new Function(code: "TTTTT", description: "TTTTT", etypCode: "EBRK", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
