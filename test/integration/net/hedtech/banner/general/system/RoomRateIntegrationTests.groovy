/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class RoomRateIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TEST"
    def i_success_monthlyIndicator = true
    def i_success_dailyIndicator = true
    def i_success_termlyIndicator = true
    def i_success_description = "Test Room Rate"
    //Invalid test data (For failure tests)

    def i_failure_code = "FAILURE"
    def i_failure_monthlyIndicator = true
    def i_failure_dailyIndicator = true
    def i_failure_termlyIndicator = true
    def i_failure_description = "Failure Room Rate Description Too Long"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TEST"
    def u_success_monthlyIndicator = true
    def u_success_dailyIndicator = true
    def u_success_termlyIndicator = true
    def u_success_description = "Test"
    //Valid test data (For failure tests)

    def u_failure_code = "TEST"
    def u_failure_monthlyIndicator = true
    def u_failure_dailyIndicator = true
    def u_failure_termlyIndicator = true
    def u_failure_description = "Failure Room Rate Description Too Long"



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
    void testCreateValidRoomRate() {
        def roomRate = newValidForCreateRoomRate()
        roomRate.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull roomRate.id
    }


    @Test
    void testCreateInvalidRoomRate() {
        def roomRate = newInvalidForCreateRoomRate()
        shouldFail {
            roomRate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidRoomRate() {
        def roomRate = newValidForCreateRoomRate()
        roomRate.save(failOnError: true, flush: true)
        assertNotNull roomRate.id
        assertEquals 0L, roomRate.version
        assertEquals i_success_code, roomRate.code
        assertEquals i_success_monthlyIndicator, roomRate.monthlyIndicator
        assertEquals i_success_dailyIndicator, roomRate.dailyIndicator
        assertEquals i_success_termlyIndicator, roomRate.termlyIndicator
        assertEquals i_success_description, roomRate.description

        //Update the entity
        roomRate.monthlyIndicator = u_success_monthlyIndicator
        roomRate.dailyIndicator = u_success_dailyIndicator
        roomRate.termlyIndicator = u_success_termlyIndicator
        roomRate.description = u_success_description
        roomRate.save(failOnError: true, flush: true)
        //Asset for sucessful update
        roomRate = RoomRate.get(roomRate.id)
        assertEquals 1L, roomRate?.version
        assertEquals u_success_monthlyIndicator, roomRate.monthlyIndicator
        assertEquals u_success_dailyIndicator, roomRate.dailyIndicator
        assertEquals u_success_termlyIndicator, roomRate.termlyIndicator
        assertEquals u_success_description, roomRate.description
    }


    @Test
    void testUpdateInvalidRoomRate() {
        def roomRate = newValidForCreateRoomRate()
        roomRate.save(failOnError: true, flush: true)
        assertNotNull roomRate.id
        assertEquals 0L, roomRate.version
        assertEquals i_success_code, roomRate.code
        assertEquals i_success_monthlyIndicator, roomRate.monthlyIndicator
        assertEquals i_success_dailyIndicator, roomRate.dailyIndicator
        assertEquals i_success_termlyIndicator, roomRate.termlyIndicator
        assertEquals i_success_description, roomRate.description

        //Update the entity with invalid values
        roomRate.monthlyIndicator = u_failure_monthlyIndicator
        roomRate.dailyIndicator = u_failure_dailyIndicator
        roomRate.termlyIndicator = u_failure_termlyIndicator
        roomRate.description = u_failure_description
        shouldFail {
            roomRate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def roomRate = newValidForCreateRoomRate()
        roomRate.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRRCD set STVRRCD_VERSION = 999 where STVRRCD_SURROGATE_ID = ?", [roomRate.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        roomRate.monthlyIndicator = u_success_monthlyIndicator
        roomRate.dailyIndicator = u_success_dailyIndicator
        roomRate.termlyIndicator = u_success_termlyIndicator
        roomRate.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            roomRate.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteRoomRate() {
        def roomRate = newValidForCreateRoomRate()
        roomRate.save(failOnError: true, flush: true)
        def id = roomRate.id
        assertNotNull id
        roomRate.delete()
        assertNull RoomRate.get(id)
    }


    @Test
    void testValidation() {
        def roomRate = newValidForCreateRoomRate()
        assertTrue "RoomRate could not be validated as expected due to ${roomRate.errors}", roomRate.validate()
    }


    @Test
    void testNullValidationFailure() {
        def roomRate = new RoomRate()
        assertFalse "RoomRate should have failed validation", roomRate.validate()
        assertErrorsFor roomRate, 'nullable',
                [
                        'code',
                        'monthlyIndicator',
                        'dailyIndicator',
                        'termlyIndicator',
                        'description'
                ]
    }


    private def newValidForCreateRoomRate() {
        def roomRate = new RoomRate(
                code: i_success_code,
                monthlyIndicator: i_success_monthlyIndicator,
                dailyIndicator: i_success_dailyIndicator,
                termlyIndicator: i_success_termlyIndicator,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return roomRate
    }


    private def newInvalidForCreateRoomRate() {
        def roomRate = new RoomRate(
                code: i_failure_code,
                monthlyIndicator: i_failure_monthlyIndicator,
                dailyIndicator: i_failure_dailyIndicator,
                termlyIndicator: i_failure_termlyIndicator,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return roomRate
    }


}
