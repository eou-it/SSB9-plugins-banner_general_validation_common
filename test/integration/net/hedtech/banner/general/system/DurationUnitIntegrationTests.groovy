/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.apache.commons.lang.StringUtils
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the duration unit code model.
 * */
class DurationUnitIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    def i_success_numberOfDays = 1
    def i_success_voiceResponseMessageNumber = 1

    //Invalid test data (For failure tests)

    def i_failure_code = "TTTT"
    def i_failure_description = "TTTTT"
    def i_failure_numberOfDays = 1
    def i_failure_voiceResponseMessageNumber = 1

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTT"
    def u_success_description = "TTTTT"
    def u_success_numberOfDays = 1
    def u_success_voiceResponseMessageNumber = 1

    //Valid test data (For failure tests)

    def u_failure_code = "TTTT"
    def u_failure_description = "TTTTT"
    def u_failure_numberOfDays = 1
    def u_failure_voiceResponseMessageNumber = 1

    def durationUnitCodeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreateDurationUnitCode() {
        def durationUnitCode = newValidForCreateDurationUnit()

        save durationUnitCode
        assertNotNull(durationUnitCode.id)
    }


    @Test
    void testUpdateDurationUnitCode() {
        def durationUnitCode = newValidForCreateDurationUnit()

        save durationUnitCode
        def id = durationUnitCode.id
        assertNotNull id
        assertEquals 0L, durationUnitCode.version

        durationUnitCode.description = "updated"
        save durationUnitCode

        durationUnitCode = DurationUnit.get(id)
        assertNotNull "found must not be null", durationUnitCode
        assertEquals "updated", durationUnitCode.description
        assertEquals 1, durationUnitCode.version
    }


    @Test
    void testDeleteDurationUnitCode() {
        def durationUnitCode = newValidForCreateDurationUnit()

        save durationUnitCode
        def id = durationUnitCode.id
        assertNotNull id

        durationUnitCode.delete();
        assertNull DurationUnit.get(id)
    }


    @Test
    void testOptimisticLock() {
        def durationUnit = newValidForCreateDurationUnit()
        save durationUnit

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVDUNT set GTVDUNT_VERSION = 999 where GTVDUNT_SURROGATE_ID = ?", [durationUnit.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        durationUnit.description = "Update Description"
        durationUnit.numberOfDays = 43.34
        durationUnit.vrMsgNo = 3
        shouldFail(HibernateOptimisticLockingFailureException) {
            durationUnit.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testValidation() {
        def durationUnitCode = new DurationUnit()
        //should not pass validation since none of the required values are provided
        assertFalse(durationUnitCode.validate())

        durationUnitCode = new DurationUnit(code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        //should pass this time
        assertTrue durationUnitCode.validate()
    }


    @Test
    void testNullValidationFailure() {
        def durationUnit = new DurationUnit()
        assertFalse "DurationUnit should have failed validation", durationUnit.validate()
        assertErrorsFor durationUnit, 'nullable',
                [
                        'code',
                        'description',
                        'numberOfDays'
                ]
        assertNoErrorsFor durationUnit,
                [
                        'vrMsgNo'
                ]
    }


    @Test
    void testFetchByCodeOrDescriptionILike() {

        def filter = "HALF"
        def results = DurationUnit.fetchByCodeOrDescriptionILike(formatWildCard(filter))
        assertEquals 1, results.size()

        results.each {
            assertTrue it.code.contains(filter) || it.code.contains(filter)
        }

        filter = "day"
        results = DurationUnit.fetchByCodeOrDescriptionILike(formatWildCard(filter))
        assertEquals 3, results.size()
    }


    private def newValidForCreateDurationUnit() {
        def durationUnit = new DurationUnit(
                code: i_success_code,
                description: i_success_description,
                numberOfDays: i_success_numberOfDays,
                vrMsgNo: i_success_voiceResponseMessageNumber,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return durationUnit
    }

    private def newInvalidForCreateDurationUnit() {
        def durationUnit = new DurationUnit(
                code: i_failure_code,
                description: i_failure_description,
                numberOfDays: i_failure_numberOfDays,
                vrMsgNo: i_failure_voiceResponseMessageNumber,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return durationUnit
    }


    private String formatWildCard(filter) {
        def wildCard = "%"
        if (StringUtils.isBlank(filter)) {
            filter = wildCard
        } else if (!(filter =~ /%/)) {
            filter = wildCard + filter.toUpperCase() + wildCard
        }
        else filter = filter.toUpperCase()
        return filter
    }

}
