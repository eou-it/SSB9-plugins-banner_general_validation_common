/** *****************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
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
class BuildingIntegrationTests extends BaseIntegrationTestCase {

    def buildingService


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
    void testCreateBuilding() {
        def building = newBuilding()
        save building
        //Test if the generated entity now has an id assigned
        assertNotNull building.id
    }


    @Test
    void testUpdateBuilding() {
        def building = newBuilding()
        save building

        assertNotNull building.id
        assertEquals 0L, building.version
        assertEquals "TTTTT", building.code
        assertEquals "TTTTT", building.description
        assertEquals 1, building.voiceResponseMsgNumber, 0

        //Update the entity
        building.code = "UUUUU"
        building.description = "UUUUU"
        building.voiceResponseMsgNumber = 0
        building.lastModified = new Date()
        building.lastModifiedBy = "test"
        building.dataOrigin = "Banner"
        save building

        building = Building.get(building.id)
        assertEquals 1L, building?.version
        assertEquals "UUUUU", building.code
        assertEquals "UUUUU", building.description
        assertEquals 0, building.voiceResponseMsgNumber, 0

    }


    @Test
    void testOptimisticLock() {
        def building = newBuilding()
        save building

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVBLDG set STVBLDG_VERSION = 999 where STVBLDG_SURROGATE_ID = ?", [building.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        building.code = "UUUUU"
        building.description = "UUUUU"
        building.voiceResponseMsgNumber = 0
        building.lastModified = new Date()
        building.lastModifiedBy = "test"
        building.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            building.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteBuilding() {
        def building = newBuilding()
        save building
        def id = building.id
        assertNotNull id
        building.delete()
        assertNull Building.get(id)
    }


    @Test
    void testValidation() {
        def building = newBuilding()
        assertTrue "Building could not be validated as expected due to ${building.errors}", building.validate()
    }


    @Test
    void testNullValidationFailure() {
        def building = new Building()
        assertFalse "Building should have failed validation", building.validate()
        assertErrorsFor building, 'nullable', ['code', 'description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def building = new Building(
                code: 'XXXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "Building should have failed validation", building.validate()
        assertErrorsFor building, 'maxSize', ['code', 'description']
    }



    private def newBuilding() {
        new Building(code: "TTTTT", description: "TTTTT", voiceResponseMsgNumber: 1, lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
