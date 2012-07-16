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

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class FunctionPurposeIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(functionpurpose_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = null
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTTT"
    def u_success_description = "TTTTTT"
    //Valid test data (For failure tests)

    def u_failure_code = "123456789012345"
    def u_failure_description = "1234567890123456789012345678901234567890123456789012345678901234567890"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['GTVPURP'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidFunctionPurpose() {
        def functionPurpose = newValidForCreateFunctionPurpose()
        save functionPurpose
        //Test if the generated entity now has an id assigned
        assertNotNull functionPurpose.id
    }



    void testUpdateValidFunctionPurpose() {
        def functionPurpose = newValidForCreateFunctionPurpose()
        save functionPurpose
        assertNotNull functionPurpose.id
        assertEquals 0L, functionPurpose.version
        assertEquals i_success_code, functionPurpose.code
        assertEquals i_success_description, functionPurpose.description

        //Update the entity
        functionPurpose.description = u_success_description
        save functionPurpose
        //Asset for sucessful update
        functionPurpose = FunctionPurpose.get(functionPurpose.id)
        assertEquals 1L, functionPurpose?.version
        assertEquals u_success_description, functionPurpose.description
    }



    void testOptimisticLock() {
        def functionPurpose = newValidForCreateFunctionPurpose()
        save functionPurpose

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVPURP set GTVPURP_VERSION = 999 where GTVPURP_SURROGATE_ID = ?", [functionPurpose.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        functionPurpose.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            functionPurpose.save(failOnError: true, flush: true)
        }
    }


    void testDeleteFunctionPurpose() {
        def functionPurpose = newValidForCreateFunctionPurpose()
        save functionPurpose
        def id = functionPurpose.id
        assertNotNull id
        functionPurpose.delete()
        assertNull FunctionPurpose.get(id)
    }


    void testValidation() {
        def functionPurpose = newInvalidForCreateFunctionPurpose()
        functionPurpose.code=u_failure_code
        functionPurpose.description=u_failure_description
        assertFalse "FunctionPurpose could not be validated as expected due to ${functionPurpose.errors}", functionPurpose.validate()
        assertErrorsFor functionPurpose, 'maxSize', ['code', 'description']
    }


    void testNullValidationFailure() {
        def functionPurpose = new FunctionPurpose()
        assertFalse "FunctionPurpose should have failed validation", functionPurpose.validate()
        assertErrorsFor functionPurpose, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    private def newValidForCreateFunctionPurpose() {
        def functionPurpose = new FunctionPurpose(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return functionPurpose
    }


    private def newInvalidForCreateFunctionPurpose() {
        def functionPurpose = new FunctionPurpose(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return functionPurpose
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(functionpurpose_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
