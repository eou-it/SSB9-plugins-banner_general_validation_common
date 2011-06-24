/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ScheduleToolStatusIntegrationTests extends BaseIntegrationTestCase {

    def scheduleToolStatusService


    protected void setUp() {
        formContext = ['GTVSCHS'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateScheduleToolStatus() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus
        //Test if the generated entity now has an id assigned
        assertNotNull scheduleToolStatus.id
    }


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
            scheduleToolStatus.save(flush: true)
        }
    }


    void testDeleteScheduleToolStatus() {
        def scheduleToolStatus = newScheduleToolStatus()
        save scheduleToolStatus
        def id = scheduleToolStatus.id
        assertNotNull id
        scheduleToolStatus.delete()
        assertNull ScheduleToolStatus.get(id)
    }


    void testValidation() {
        def scheduleToolStatus = newScheduleToolStatus()
        assertTrue "ScheduleToolStatus could not be validated as expected due to ${scheduleToolStatus.errors}", scheduleToolStatus.validate()
    }


    void testNullValidationFailure() {
        def scheduleToolStatus = new ScheduleToolStatus()
        assertFalse "ScheduleToolStatus should have failed validation", scheduleToolStatus.validate()
        assertErrorsFor scheduleToolStatus, 'nullable', ['code', 'description', 'systemRequiredIndicator']
        assertNoErrorsFor scheduleToolStatus, []
    }


    void testMaxSizeValidationFailures() {
        def scheduleToolStatus = new ScheduleToolStatus()
        assertFalse "ScheduleToolStatus should have failed validation", scheduleToolStatus.validate()
        assertErrorsFor scheduleToolStatus, 'maxSize', []
    }


    void testValidationMessages() {
        def scheduleToolStatus = newScheduleToolStatus()

        scheduleToolStatus.code = null
        assertFalse scheduleToolStatus.validate()
        assertLocalizedError scheduleToolStatus, 'nullable', /.*Field.*code.*of class.*ScheduleToolStatus.*cannot be null.*/, 'code'

        scheduleToolStatus.description = null
        assertFalse scheduleToolStatus.validate()
        assertLocalizedError scheduleToolStatus, 'nullable', /.*Field.*description.*of class.*ScheduleToolStatus.*cannot be null.*/, 'description'

        scheduleToolStatus.systemRequiredIndicator = null
        assertFalse scheduleToolStatus.validate()
        assertLocalizedError scheduleToolStatus, 'nullable', /.*Field.*systemRequiredIndicator.*of class.*ScheduleToolStatus.*cannot be null.*/, 'systemRequiredIndicator'

    }


    private def newScheduleToolStatus() {
        new ScheduleToolStatus(code: "TTT",
                description: "TTTTT",
                systemRequiredIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(scheduletoolstatus_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
