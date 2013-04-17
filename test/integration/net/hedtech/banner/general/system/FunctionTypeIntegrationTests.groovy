/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import grails.validation.ValidationException


class FunctionTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "i_success_description"
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTT"
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "u_success_description"
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = null

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        //	initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)

        //Invalid test data (For failure tests)

        //Valid test data (For success tests)

        //Valid test data (For failure tests)

        //Test data for references for custom tests
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateValidFunctionType() {
        def functionType = newValidForCreateFunctionType()
        functionType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull functionType.id
    }

    void testCreateInvalidFunctionType() {
        def functionType = newInvalidForCreateFunctionType()
        shouldFail(ValidationException) {
            functionType.save(failOnError: true, flush: true)
        }
    }

    void testUpdateValidFunctionType() {
        def functionType = newValidForCreateFunctionType()
        functionType.save(failOnError: true, flush: true)
        assertNotNull functionType.id
        assertEquals 0L, functionType.version
        assertEquals i_success_code, functionType.code
        assertEquals i_success_description, functionType.description

        //Update the entity
        functionType.description = u_success_description
        functionType.save(failOnError: true, flush: true)

        //Assert for successful update
        functionType = FunctionType.get(functionType.id)
        assertEquals 1L, functionType?.version
        assertEquals u_success_description, functionType.description
    }

    void testUpdateInvalidFunctionType() {
        def functionType = newValidForCreateFunctionType()
        functionType.save(failOnError: true, flush: true)
        assertNotNull functionType.id
        assertEquals 0L, functionType.version
        assertEquals i_success_code, functionType.code
        assertEquals i_success_description, functionType.description

        //Update the entity with invalid values
        functionType.description = u_failure_description
        shouldFail(ValidationException) {
            functionType.save(failOnError: true, flush: true)
        }
    }

    void testOptimisticLock() {
        def functionType = newValidForCreateFunctionType()
        functionType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVFTYP set GTVFTYP_VERSION = 999 where GTVFTYP_SURROGATE_ID = ?", [functionType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        functionType.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            functionType.save(failOnError: true, flush: true)
        }
    }

    void testDeleteFunctionType() {
        def functionType = newValidForCreateFunctionType()
        functionType.save(failOnError: true, flush: true)
        def id = functionType.id
        assertNotNull id
        functionType.delete()
        assertNull FunctionType.get(id)
    }

    void testValidation() {
        def functionType = newInvalidForCreateFunctionType()
        assertFalse "FunctionType could not be validated as expected due to ${functionType.errors}", functionType.validate()
    }

    void testNullValidationFailure() {
        def functionType = new FunctionType()
        assertFalse "FunctionType should have failed validation", functionType.validate()
        assertErrorsFor functionType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateFunctionType() {
        def functionType = new FunctionType(
                code: i_success_code,
                description: i_success_description,
        )
        return functionType
    }

    private def newInvalidForCreateFunctionType() {
        def functionType = new FunctionType(
                code: i_failure_code,
                description: i_failure_description,
        )
        return functionType
    }


}
