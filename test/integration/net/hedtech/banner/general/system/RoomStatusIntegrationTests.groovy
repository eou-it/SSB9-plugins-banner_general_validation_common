/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class RoomStatusIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "AT"
    def i_success_description = "Active Course"
    def i_success_inactiveIndicator = "Y"
    //Invalid test data (For failure tests)

    def i_failure_code = "FAIL"
    def i_failure_description = "Failure Room Status Description Too Long"
    def i_failure_inactiveIndicator = "Y"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "AT"
    def u_success_description = "Active Course Description"
    def u_success_inactiveIndicator = "Y"
    //Valid test data (For failure tests)

    def u_failure_code = "AT"
    def u_failure_description = "Failure Room Status Description Too Long"
    def u_failure_inactiveIndicator = "Y"


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidRoomStatus() {
        def roomStatus = newValidForCreateRoomStatus()
        roomStatus.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull roomStatus.id
    }


    void testCreateInvalidRoomStatus() {
        def roomStatus = newInvalidForCreateRoomStatus()
        shouldFail {
            roomStatus.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidRoomStatus() {
        def roomStatus = newValidForCreateRoomStatus()
        roomStatus.save(failOnError: true, flush: true)
        assertNotNull roomStatus.id
        assertEquals 0L, roomStatus.version
        assertEquals i_success_code, roomStatus.code
        assertEquals i_success_description, roomStatus.description
        assertEquals i_success_inactiveIndicator, roomStatus.inactiveIndicator

        //Update the entity
        roomStatus.description = u_success_description
        roomStatus.inactiveIndicator = u_success_inactiveIndicator
        roomStatus.save(failOnError: true, flush: true)

        //Assert for successful update
        roomStatus = RoomStatus.get(roomStatus.id)
        assertEquals 1L, roomStatus?.version
        assertEquals u_success_description, roomStatus.description
        assertEquals u_success_inactiveIndicator, roomStatus.inactiveIndicator
    }


    void testUpdateInvalidRoomStatus() {
        def roomStatus = newValidForCreateRoomStatus()
        roomStatus.save(failOnError: true, flush: true)
        assertNotNull roomStatus.id
        assertEquals 0L, roomStatus.version
        assertEquals i_success_code, roomStatus.code
        assertEquals i_success_description, roomStatus.description
        assertEquals i_success_inactiveIndicator, roomStatus.inactiveIndicator

        //Update the entity with invalid values
        roomStatus.description = u_failure_description
        roomStatus.inactiveIndicator = u_failure_inactiveIndicator
        shouldFail {
            roomStatus.save(failOnError: true, flush: true)
        }
    }


    void testOptimisticLock() {
        def roomStatus = newValidForCreateRoomStatus()
        roomStatus.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRMST set STVRMST_VERSION = 999 where STVRMST_SURROGATE_ID = ?", [roomStatus.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        roomStatus.description = u_success_description
        roomStatus.inactiveIndicator = u_success_inactiveIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            roomStatus.save(failOnError: true, flush: true)
        }
    }


    void testDeleteRoomStatus() {
        def roomStatus = newValidForCreateRoomStatus()
        roomStatus.save(failOnError: true, flush: true)
        def id = roomStatus.id
        assertNotNull id
        roomStatus.delete()
        assertNull RoomStatus.get(id)
    }


    void testValidation() {
        def roomStatus = newValidForCreateRoomStatus()
        assertTrue "RoomStatus could not be validated as expected due to ${roomStatus.errors}", roomStatus.validate()
    }


    void testNullValidationFailure() {
        def roomStatus = new RoomStatus()
        assertFalse "RoomStatus should have failed validation", roomStatus.validate()
        assertErrorsFor roomStatus, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor roomStatus,
                [
                        'description',
                        'inactiveIndicator'
                ]
    }


    void testMaxSizeValidationFailures() {
        def roomStatus = new RoomStatus(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                inactiveIndicator: 'XXX')
        assertFalse "RoomStatus should have failed validation", roomStatus.validate()
        assertErrorsFor roomStatus, 'maxSize', ['description', 'inactiveIndicator']
    }


    private def newValidForCreateRoomStatus() {
        def roomStatus = new RoomStatus(
                code: i_success_code,
                description: i_success_description,
                inactiveIndicator: i_success_inactiveIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return roomStatus
    }


    private def newInvalidForCreateRoomStatus() {
        def roomStatus = new RoomStatus(
                code: i_failure_code,
                description: i_failure_description,
                inactiveIndicator: i_failure_inactiveIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return roomStatus
    }


}
