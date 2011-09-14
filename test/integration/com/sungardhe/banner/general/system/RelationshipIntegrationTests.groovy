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
 Banner Automator Version: 1.24
 Generated: Thu Jul 28 14:22:54 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import grails.validation.ValidationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class RelationshipIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(relationship_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "#"
    def i_success_description = "TTTTT"
    def i_success_studentExchangeVisitorInformationSystemEquivalent = "TTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "#"
    def i_failure_description = "TTTTT"
    def i_failure_studentExchangeVisitorInformationSystemEquivalent = "TTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "#"
    def u_success_description = "WWWWW"
    def u_success_studentExchangeVisitorInformationSystemEquivalent = "TTT"
    //Valid test data (For failure tests)

    def u_failure_code = "#"
    def u_failure_description = "TTTTT"
    def u_failure_studentExchangeVisitorInformationSystemEquivalent = "TTT"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['STVRELT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        /*PROTECTED REGION ID(relationship_domain_integration_test_data_initialization) ENABLED START*/
        //Valid test data (For success tests)

        //Invalid test data (For failure tests)

        //Valid test data (For success tests)

        //Valid test data (For failure tests)

        //Test data for references for custom tests
        /*PROTECTED REGION END*/
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull relationship.id
    }


    void testCreateInvalidRelationship() {
        def relationship = newInvalidForCreateRelationship()
        relationship.code = null
        shouldFail(ValidationException) {
            relationship.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        assertNotNull relationship.id
        assertEquals 0L, relationship.version
        assertEquals i_success_code, relationship.code
        assertEquals i_success_description, relationship.description
        assertEquals i_success_studentExchangeVisitorInformationSystemEquivalent, relationship.studentExchangeVisitorInformationSystemEquivalent

        //Update the entity
        relationship.description = u_success_description
        relationship.studentExchangeVisitorInformationSystemEquivalent = u_success_studentExchangeVisitorInformationSystemEquivalent
        relationship.save(failOnError: true, flush: true)
        //Assert for sucessful update
        relationship = Relationship.get(relationship.id)
        assertEquals 1L, relationship?.version
        assertEquals u_success_description, relationship.description
        assertEquals u_success_studentExchangeVisitorInformationSystemEquivalent, relationship.studentExchangeVisitorInformationSystemEquivalent
    }


    void testUpdateInvalidRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        assertNotNull relationship.id
        assertEquals 0L, relationship.version
        assertEquals i_success_code, relationship.code
        assertEquals i_success_description, relationship.description
        assertEquals i_success_studentExchangeVisitorInformationSystemEquivalent, relationship.studentExchangeVisitorInformationSystemEquivalent

        //Update the entity with invalid values
        relationship.description = u_failure_description
        relationship.studentExchangeVisitorInformationSystemEquivalent = u_failure_studentExchangeVisitorInformationSystemEquivalent
        relationship.code = null
        shouldFail(ValidationException) {
            relationship.save(failOnError: true, flush: true)
        }
    }


    void testOptimisticLock() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRELT set STVRELT_VERSION = 999 where STVRELT_SURROGATE_ID = ?", [relationship.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        relationship.description = u_success_description
        relationship.studentExchangeVisitorInformationSystemEquivalent = u_success_studentExchangeVisitorInformationSystemEquivalent
        shouldFail(HibernateOptimisticLockingFailureException) {
            relationship.save(failOnError: true, flush: true)
        }
    }


    void testDeleteRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        def id = relationship.id
        assertNotNull id
        relationship.delete()
        assertNull Relationship.get(id)
    }


    void testValidation() {
        def relationship = newInvalidForCreateRelationship()
        relationship.code = null
        assertFalse "Relationship could not be validated as expected due to ${relationship.errors}", relationship.validate()
    }


    void testNullValidationFailure() {
        def relationship = new Relationship()
        assertFalse "Relationship should have failed validation", relationship.validate()
        assertErrorsFor relationship, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor relationship,
                [
                        'description',
                        'studentExchangeVisitorInformationSystemEquivalent'
                ]
    }


    void testMaxSizeValidationFailures() {
        def relationship = new Relationship(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                studentExchangeVisitorInformationSystemEquivalent: 'XXXXX')
        assertFalse "Relationship should have failed validation", relationship.validate()
        assertErrorsFor relationship, 'maxSize', ['description', 'studentExchangeVisitorInformationSystemEquivalent']
    }




    private def newValidForCreateRelationship() {
        def relationship = new Relationship(
                code: i_success_code,
                description: i_success_description,
                studentExchangeVisitorInformationSystemEquivalent: i_success_studentExchangeVisitorInformationSystemEquivalent,
        )
        return relationship
    }


    private def newInvalidForCreateRelationship() {
        def relationship = new Relationship(
                code: i_failure_code,
                description: i_failure_description,
                studentExchangeVisitorInformationSystemEquivalent: i_failure_studentExchangeVisitorInformationSystemEquivalent,
        )
        return relationship
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(relationship_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
