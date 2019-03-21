/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ScheduleToolStatusIntegrationTests extends BaseIntegrationTestCase {

    def scheduleToolStatusService


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
    void testCreateScheduleToolStatus() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus
        //Test if the generated entity now has an id assigned
        assertNotNull scheduleToolStatus.id
    }


    @Test
    void testUpdateScheduleToolStatus() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus

        assertNotNull scheduleToolStatus.id
        assertEquals 0L, scheduleToolStatus.version
        assertEquals "TTT", scheduleToolStatus.code
        assertEquals "TTTTT", scheduleToolStatus.description
        assertTrue scheduleToolStatus.systemRequiredIndicator

        //Update the entity
        def testDate = new Date()
        scheduleToolStatus.code = "UUU"
        scheduleToolStatus.description = "UUUUU"
        scheduleToolStatus.systemRequiredIndicator = false
        scheduleToolStatus.lastModified = testDate
        scheduleToolStatus.lastModifiedBy = "test"
        scheduleToolStatus.dataOrigin = "Banner"
        save scheduleToolStatus

        scheduleToolStatus = ScheduleToolStatus.get(scheduleToolStatus.id)
        assertEquals 1L, scheduleToolStatus?.version
        assertEquals "UUU", scheduleToolStatus.code
        assertEquals "UUUUU", scheduleToolStatus.description
        assertEquals false, scheduleToolStatus.systemRequiredIndicator
    }


    @Test
    void testOptimisticLock() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSCHS set GTVSCHS_VERSION = 999 where GTVSCHS_SURROGATE_ID = ?", [scheduleToolStatus.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        scheduleToolStatus.code = "UUU"
        scheduleToolStatus.description = "UUUUU"
        scheduleToolStatus.systemRequiredIndicator = false
        scheduleToolStatus.lastModified = new Date()
        scheduleToolStatus.lastModifiedBy = "test"
        scheduleToolStatus.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            scheduleToolStatus.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteScheduleToolStatus() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus
        def id = scheduleToolStatus.id
        assertNotNull id
        scheduleToolStatus.delete()
        assertNull ScheduleToolStatus.get(id)
    }


    @Test
    void testValidation() {
        def scheduleToolStatus = newScheduleToolStatus()
        assertTrue "ScheduleToolStatus could not be validated as expected due to ${scheduleToolStatus.errors}", scheduleToolStatus.validate()
    }


    @Test
    void testNullValidationFailure() {
        def scheduleToolStatus = new ScheduleToolStatus()
        assertFalse "ScheduleToolStatus should have failed validation", scheduleToolStatus.validate()
        assertErrorsFor scheduleToolStatus, 'nullable', ['code', 'description', 'systemRequiredIndicator']
        assertNoErrorsFor scheduleToolStatus, []
    }


    @Test
    void testMaxSizeValidationFailures() {
        def scheduleToolStatus = new ScheduleToolStatus()
        assertFalse "ScheduleToolStatus should have failed validation", scheduleToolStatus.validate()
        assertErrorsFor scheduleToolStatus, 'maxSize', []
    }


    private def newScheduleToolStatus() {
        new ScheduleToolStatus(code: "TTT",
                description: "TTTTT",
                systemRequiredIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner")
    }


}
