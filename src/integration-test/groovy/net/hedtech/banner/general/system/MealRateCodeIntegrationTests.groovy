/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the mealRateCode model.
 * */
class MealRateCodeIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the ability to create and persist a new MealRateCode instance.
     */
    @Test
    void testCreateMealRateCode() {
        def mealRateCode = createMealRateCode()
        mealRateCode.save(failOnError: true, flush: true)
        assertNotNull mealRateCode.id
    }

    /**
     * Tests the ability to update a Description.
     */
    @Test
    void testUpdateMealRateCode() {
        def mealRateCode = createMealRateCode()
        save mealRateCode

        def id = mealRateCode.id
        def version = mealRateCode.version
        assertNotNull id
        assertEquals 0L, version

        mealRateCode.description = "updated"
        save mealRateCode
        mealRateCode = MealRateCode.get(id)

        assertNotNull "found must not be null", mealRateCode
        assertEquals "updated", mealRateCode.description
        assertEquals 1, mealRateCode.version
    }

    /**
     * Test optimistic locking.
     */
    @Test
    void testOptimisticLock() {
        def mealRateCode = createMealRateCode()
        save mealRateCode

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVMRCD set STVMRCD_VERSION = 999 where STVMRCD_SURROGATE_ID = ?", [mealRateCode.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

        //Try to update the entity
        mealRateCode.code = "UU"
        mealRateCode.description = "UUUU"
        mealRateCode.lastModified = new Date()
        mealRateCode.lastModifiedBy = "test"
        mealRateCode.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            mealRateCode.save(flush: true)
        }
    }

    /**
     * Tests the ability to delete a MealRateCode.
     */
    @Test
    void testDeleteMealRateCode() {
        def mealRateCode = createMealRateCode()
        save mealRateCode

        def id = mealRateCode.id
        assertNotNull id
        mealRateCode.delete()
        assertNull mealRateCode.get(id)
    }


    @Test
    void testValidation() {
        def mealRateCode = new MealRateCode()
        assertFalse "MealRateCode could not be validated as expected due to ${mealRateCode.errors}", mealRateCode.validate()
    }


    @Test
    void testNullValidationFailure() {
        def mealRateCode = new MealRateCode()
        //should not pass validation since none of the required values are provided
        assertFalse "MealRateCode should have failed validation", mealRateCode.validate()
        assertErrorsFor mealRateCode, 'nullable', ['code', 'description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def mealRateCode = new MealRateCode(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT',
                monthlyIndicator: false,
                dailyIndicator: false,
                termIndicator: false,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner",
                )
        assertFalse "MealRateCode should have failed validation", mealRateCode.validate()
        assertErrorsFor mealRateCode, 'maxSize', ['code', 'description']
    }



    private def createMealRateCode() {
        def mealRateCode = new MealRateCode(
                code: "TT",
                description: "TT",
                monthlyIndicator: false,
                dailyIndicator: false,
                termIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return mealRateCode
    }
}
