/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class RelationshipIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull relationship.id
    }


    @Test
    void testCreateInvalidRelationship() {
        def relationship = newInvalidForCreateRelationship()
        relationship.code = null
        shouldFail(ValidationException) {
            relationship.save(failOnError: true, flush: true)
        }
    }


    @Test
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

        //Assert for successful update
        relationship = Relationship.get(relationship.id)
        assertEquals 1L, relationship?.version
        assertEquals u_success_description, relationship.description
        assertEquals u_success_studentExchangeVisitorInformationSystemEquivalent, relationship.studentExchangeVisitorInformationSystemEquivalent
    }


    @Test
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


    @Test
    void testOptimisticLock() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRELT set STVRELT_VERSION = 999 where STVRELT_SURROGATE_ID = ?", [relationship.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        relationship.description = u_success_description
        relationship.studentExchangeVisitorInformationSystemEquivalent = u_success_studentExchangeVisitorInformationSystemEquivalent
        shouldFail(HibernateOptimisticLockingFailureException) {
            relationship.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteRelationship() {
        def relationship = newValidForCreateRelationship()
        relationship.save(failOnError: true, flush: true)
        def id = relationship.id
        assertNotNull id
        relationship.delete()
        assertNull Relationship.get(id)
    }


    @Test
    void testValidation() {
        def relationship = newInvalidForCreateRelationship()
        relationship.code = null
        assertFalse "Relationship could not be validated as expected due to ${relationship.errors}", relationship.validate()
    }


    @Test
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


    @Test
    void testMaxSizeValidationFailures() {
        def relationship = new Relationship(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                studentExchangeVisitorInformationSystemEquivalent: 'XXXXX')
        assertFalse "Relationship should have failed validation", relationship.validate()
        assertErrorsFor relationship, 'maxSize', ['description', 'studentExchangeVisitorInformationSystemEquivalent']
    }

    @Test
    void testFetchByCode() {
        Relationship rr = newValidForCreateRelationship().save()
        Relationship relationship = Relationship.fetchByCode(i_success_code)
        assertNotNull(rr.id)
        assertEquals(i_success_code, rr.code)
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


}
