/** *****************************************************************************
 Copyright 2017 - 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the level model.
 * */
class LevelIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TT"
    def i_success_description = "TTTTT"
    def i_success_academicIndicator = true
    def i_success_continuingEducationIndicator = true

    def i_success_systemRequiredIndicator = true
    def i_success_voiceResponseMessageNumber = 1

    def i_success_electronicDataInterchangeEquivalent = "TT"

    def levelService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreateLevel() {
        def level = newValidForCreateLevel()

        if (!level.save(failOnError: true, flush: true)) {
            fail("Could not save Level; LEVEL ERRORS = " + level.errors);
        }
        assertNotNull(level.id)
    }

    @Test
    void testUpdateLevel() {
        def level = newValidForCreateLevel()
        if (!level.save(flush: true, failOnError: true)) {
            fail("Could not save Level; LEVEL ERRORS = " + level.errors);
        }
        def id = level.id
        def version = level.version
        assertNotNull(id)
        Assert.assertEquals(0L, version)

        level.description = "updated"

        if (!level.save(flush: true, failOnError: true)) {
            fail("Could not update Level; LEVEL ERRORS = " + level.errors);
        }
        level = Level.get(id)

        assertNotNull("found must not be null", level)
        assertEquals("updated", level.description)
        Assert.assertEquals(1, level.version)
    }

    @Test
    void testDeleteLevel() {
        def level = newValidForCreateLevel()
        if (!level.save(flush: true, failOnError: true)) {
            fail("Could not save Level; LEVEL ERRORS = " + level.errors);
        }
        def id = level.id
        assertNotNull(id)
        level.delete();
        def found = Level.get(id)
        assertNull(found)
    }


    @Test
    void testOptimisticLock() {
        def level = newValidForCreateLevel()
        level.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVLEVL set STVLEVL_VERSION = 999 where STVLEVL_SURROGATE_ID = ?", [level.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        level.description = "Test Description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            level.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testValidation() {
        def level = new Level()
        //should not pass validation since none of the required values are provided
        assertFalse(level.validate())
        level.code = "TT"
        level.description = "TT"
        level.acadInd = true
        level.ceuInd = true
        level.systemReqInd = true
        level.vrMsgNo = 4321
        level.ediEquiv = "TT"
        level.lastModified = new Date()
        level.lastModifiedBy = "test"
        level.dataOrigin = "banner"

        //should pass this time
        assertTrue(level.validate())
    }

    @Test
    void testNullValidationFailure() {
        def level = new Level()
        assertFalse "Level should have failed validation", level.validate()
        assertErrorsFor level, 'nullable',
                [
                        'code',
                        'description',
                        'ceuInd'
                ]
        assertNoErrorsFor level,
                [
                        'acadInd',
                        'systemReqInd',
                        'vrMsgNo',
                        'ediEquiv'
                ]
    }

    @Test
    void testMaxSizeValidationFailures() {
        def level = new Level(
                description: 'This description is longet then allowed, it should throw maxSize error',
                ediEquiv: 'Allowd length is 2, should throw maxSize Error')
        assertFalse "Level should have failed validation", level.validate()
        assertErrorsFor level, 'maxSize', ['description', 'ediEquiv']
    }

    @Test
    void testFetchByCodeInListNullList() {
        assertEquals([], Level.fetchAllByCodeInList(null))
    }

    @Test
    void testFetchByCodeInListEmptyList() {
        assertEquals([], Level.fetchAllByCodeInList([]))
    }

    @Test
    void testFetchByCodeInList() {
        def newLevel = newValidForCreateLevel()
        newLevel.save(failOnError: true, flush: true)
        List<Level> levelList = Level.fetchAllByCodeInList([i_success_code])
        assertEquals(1, levelList.size())
        assertEquals([i_success_code], levelList.code)
    }

    @Test
    void testFetchByCodeInvalidCode() {
        assertNull(Level.fetchByCode(i_success_code + i_success_code))
    }

    @Test
    void testFetchByCode() {
        def newLevel = newValidForCreateLevel()
        newLevel.save(failOnError: true, flush: true)
        Level level = Level.fetchByCode(i_success_code)
        assertNotNull(level)
        assertEquals(i_success_code, level.code)
    }

    @Test
    void testFetchLevelForCEUNoParams() {
        def newLevel = newValidForCreateLevel()
        newLevel.save(failOnError: true, flush: true)
        Level level = Level.fetchLevelForCEU(i_success_code)
        assertNotNull(level)
        assertEquals(i_success_code, level.code)
    }

    @Test
    void testFetchLevelForCEUWithParamCEUTrue() {
        def newLevel = newValidForCreateLevel()
        newLevel.save(failOnError: true, flush: true)
        Level level = Level.fetchLevelForCEU(i_success_code, [ceu: i_success_continuingEducationIndicator])
        assertNotNull(level)
        assertEquals(i_success_code, level.code)
        assertTrue(level.ceuInd)
    }

    @Test
    void testFetchAllByCeuIndAndCodeLike() {
        Map result = Level.fetchAllByCeuIndAndCodeLike([ceu: true])
        if (result.list) {
            result.list.each {
                assertTrue(it.ceuInd)
            }
        }

        result = Level.fetchAllByCeuIndAndCodeLike([ceu: false])
        if (result.list) {
            result.list.each {
                assertFalse(it.ceuInd)
            }
        }
    }

    private def newValidForCreateLevel() {
        def level = new Level(
                code: i_success_code,
                description: i_success_description,
                acadInd: i_success_academicIndicator,
                ceuInd: i_success_continuingEducationIndicator,
                systemReqInd: i_success_systemRequiredIndicator,
                vrMsgNo: i_success_voiceResponseMessageNumber,
                ediEquiv: i_success_electronicDataInterchangeEquivalent,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return level
    }
}
