/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class FunctionIntegrationTests extends BaseIntegrationTestCase {

    def functionService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateFunction() {
        def function = newFunction()
        save function
        //Test if the generated entity now has an id assigned
        assertNotNull function.id
    }


    void testUpdateFunction() {
        def function = newFunction()
        save function

        assertNotNull function.id
        assertEquals 0L, function.version
        assertEquals "TTTTT", function.code
        assertEquals "TTTTT", function.description
        assertEquals "TTTT", function.etypCode

        //Update the entity
        def testDate = new Date()
        function.code = "UUUUU"
        function.description = "UUUUU"
        function.etypCode = "UUUU"
        function.lastModified = testDate
        function.lastModifiedBy = "test"
        function.dataOrigin = "Banner"
        save function

        function = Function.get(function.id)
        assertEquals 1L, function?.version
        assertEquals "UUUUU", function.code
        assertEquals "UUUUU", function.description
        assertEquals "UUUU", function.etypCode
    }


    void testOptimisticLock() {
        def function = newFunction()
        save function

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVFUNC set GTVFUNC_VERSION = 999 where GTVFUNC_SURROGATE_ID = ?", [function.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        function.code = "UUUUU"
        function.description = "UUUUU"
        function.etypCode = "UUUU"
        function.lastModified = new Date()
        function.lastModifiedBy = "test"
        function.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            function.save(flush:true, failOnError:true)
        }
    }


    void testDeleteFunction() {
        def function = newFunction()
        save function
        def id = function.id
        assertNotNull id
        function.delete()
        assertNull Function.get(id)
    }


    void testValidation() {
        def function = newFunction()
        assertTrue "Function could not be validated as expected due to ${function.errors}", function.validate()
    }


    void testNullValidationFailure() {
        def function = new Function()
        assertFalse "Function should have failed validation", function.validate()
        assertErrorsFor function, 'nullable', ['code', 'description', 'etypCode']
        assertNoErrorsFor function, []
    }


    void testMaxSizeValidationFailures() {
        def function = new Function()
        assertFalse "Function should have failed validation", function.validate()
        assertErrorsFor function, 'maxSize', []
    }



    private def newFunction() {
        new Function(code: "TTTTT", description: "TTTTT", etypCode: "TTTT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(function_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
