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
/**
 Banner Automator Version: 0.1.1
 Generated: Tue May 10 23:42:29 IST 2011
 */
package net.hedtech.banner.general.system

import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException as OptimisticLock

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

class FunctionEmphasisIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(functionemphasis_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_college
    def i_success_department

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)
    def i_failure_college
    def i_failure_department

    def i_failure_code = null
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_college
    def u_success_department

    def u_success_code = "TTTTTT"
    def u_success_description = "TTTTTT"
    //Valid test data (For failure tests)


    def u_failure_code = "123456789012345"
    def u_failure_description = "1234567890123456789012345678901234"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['GTVEMPH'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction


    void initializeTestDataForReferences() {
        /*PROTECTED REGION ID(functionemphasis_domain_integration_test_data_initialization) ENABLED START*/
        //Valid test data (For success tests)
        i_success_college = College.findWhere(code: "FD")
        i_success_department = Department.findWhere(code: "GENE")

        //Valid test data (For success tests)
        u_success_college = College.findWhere(code: "LS")
        u_success_department = Department.findWhere(code: "GEOL")

        //Test data for references for custom tests
        /*PROTECTED REGION END*/
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidFunctionEmphasis() {
        def functionEmphasis = newValidForCreateFunctionEmphasis()
        save functionEmphasis
        //Test if the generated entity now has an id assigned
        assertNotNull functionEmphasis.id
    }


    void testUpdateValidFunctionEmphasis() {
        def functionEmphasis = newValidForCreateFunctionEmphasis()
        save functionEmphasis
        assertNotNull functionEmphasis.id
        assertEquals 0L, functionEmphasis.version
        functionEmphasis = FunctionEmphasis.get(functionEmphasis.id)
        assertEquals i_success_code, functionEmphasis.code
        assertEquals i_success_description, functionEmphasis.description

        //Update the entity
        functionEmphasis.description = u_success_description
        functionEmphasis.college = u_success_college
        functionEmphasis.department = u_success_department
        functionEmphasis.college = u_success_college

        functionEmphasis.department = u_success_department
        save functionEmphasis
        //Asset for sucessful update
        functionEmphasis = FunctionEmphasis.get(functionEmphasis.id)
        assertEquals 1L, functionEmphasis?.version
        assertEquals u_success_description, functionEmphasis.description
        assertEquals u_success_college, functionEmphasis.college
        assertEquals u_success_department, functionEmphasis.department

    }



    void testOptimisticLock() {
        def functionEmphasis = newValidForCreateFunctionEmphasis()
        save functionEmphasis
        functionEmphasis = FunctionEmphasis.get(functionEmphasis.id)
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVEMPH set GTVEMPH_VERSION = 999 where GTVEMPH_SURROGATE_ID = ?", [functionEmphasis.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        functionEmphasis.description = u_success_description
        shouldFail(OptimisticLock) {
            functionEmphasis.save(failOnError: true, flush: true)
        }
    }


    void testDeleteFunctionEmphasis() {
        def functionEmphasis = newValidForCreateFunctionEmphasis()
        save functionEmphasis
        def id = functionEmphasis.id
        assertNotNull id
        functionEmphasis.delete()
        assertNull FunctionEmphasis.get(id)
    }


    void testValidation() {
        def functionEmphasis = newInvalidForCreateFunctionEmphasis()
        functionEmphasis.code = u_failure_code
        functionEmphasis.description = u_failure_description
        assertFalse "FunctionEmphasis could not be validated as expected due to ${functionEmphasis.errors}", functionEmphasis.validate()
        assertErrorsFor functionEmphasis, 'maxSize', ['code', 'description']
    }


    void testNullValidationFailure() {
        def functionEmphasis = new FunctionEmphasis()
        assertFalse "FunctionEmphasis should have failed validation", functionEmphasis.validate()
        assertErrorsFor functionEmphasis, 'nullable',
                [
                        'code',
                        'description'
                ]
        assertNoErrorsFor functionEmphasis,
                [
                        'college',
                        'department'
                ]
    }



    private def newValidForCreateFunctionEmphasis() {
        def functionEmphasis = new FunctionEmphasis(
                code: i_success_code,
                description: i_success_description,
                college: i_success_college,
                department: i_success_department,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return functionEmphasis
    }


    private def newInvalidForCreateFunctionEmphasis() {
        def functionEmphasis = new FunctionEmphasis(
                code: i_failure_code,
                description: i_failure_description,
                college: i_failure_college,
                department: i_failure_department,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return functionEmphasis
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionemphasis_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
