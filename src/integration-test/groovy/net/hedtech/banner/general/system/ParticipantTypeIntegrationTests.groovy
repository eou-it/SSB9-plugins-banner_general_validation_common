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
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ParticipantTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "FFFFF"
    def i_failure_description = "FFFFF"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "USSSS"
    def u_success_description = "USSSS"
    //Valid test data (For failure tests)

    def u_failure_code = "UFFFF"
    def u_failure_description = "UFFFF"


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
    void testCreateValidParticipantType() {
        def participantType = newValidForCreateParticipantType()
        save participantType
        //Test if the generated entity now has an id assigned
        assertNotNull participantType.id
    }


    @Test
    void testCreateInvalidParticipantType() {
        def participantType = newInvalidForCreateParticipantType()
        participantType.code = null
        shouldFail(ValidationException) {
            participantType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidParticipantType() {
        def participantType = newValidForCreateParticipantType()
        save participantType
        assertNotNull participantType.id
        assertEquals 0L, participantType.version
        assertEquals i_success_code, participantType.code
        assertEquals i_success_description, participantType.description

        //Update the entity
        participantType.description = u_success_description
        participantType.save(failOnError: true, flush: true)

        //Asset for successful update
        participantType = ParticipantType.get(participantType.id)
        assertEquals 1L, participantType?.version
        assertEquals u_success_description, participantType.description
    }


    @Test
    void testUpdateInvalidParticipantType() {
        def participantType = newValidForCreateParticipantType()
        save participantType
        assertNotNull participantType.id
        assertEquals 0L, participantType.version
        assertEquals i_success_code, participantType.code
        assertEquals i_success_description, participantType.description

        //Update the entity with invalid values
        participantType.description = u_failure_description
        participantType.code = null
        shouldFail(ValidationException) {
            participantType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def participantType = newValidForCreateParticipantType()
        participantType.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVPTYP set GTVPTYP_VERSION = 999 where GTVPTYP_SURROGATE_ID = ?", [participantType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        participantType.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            participantType.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteParticipantType() {
        def participantType = newValidForCreateParticipantType()
        save participantType
        def id = participantType.id
        assertNotNull id
        participantType.delete()
        assertNull ParticipantType.get(id)
    }


    @Test
    void testValidation() {
        def participantType = newInvalidForCreateParticipantType()
        assertTrue "ParticipantType could not be validated as expected due to ${participantType.errors}", participantType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def participantType = new ParticipantType()
        assertFalse "ParticipantType should have failed validation", participantType.validate()
        assertErrorsFor participantType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateParticipantType() {
        def participantType = new ParticipantType(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return participantType
    }


    private def newInvalidForCreateParticipantType() {
        def participantType = new ParticipantType(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return participantType
    }

}
