/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class BuildingIntegrationTests extends BaseIntegrationTestCase {

    def buildingService


    protected void setUp() {
        formContext = ['STVBLDG'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateBuilding() {
        def building = newBuilding()
        save building
        //Test if the generated entity now has an id assigned
        assertNotNull building.id
    }


    void testUpdateBuilding() {
        def building = newBuilding()
        save building

        assertNotNull building.id
        assertEquals 0L, building.version
        assertEquals "TTTTT", building.code
        assertEquals "TTTTT", building.description
        assertEquals 1, building.voiceResponseMsgNumber

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
        assertEquals 0, building.voiceResponseMsgNumber

    }


    void testOptimisticLock() {
        def building = newBuilding()
        save building

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVBLDG set STVBLDG_VERSION = 999 where STVBLDG_SURROGATE_ID = ?", [building.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        building.code = "UUUUU"
        building.description = "UUUUU"
        building.voiceResponseMsgNumber = 0
        building.lastModified = new Date()
        building.lastModifiedBy = "test"
        building.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            building.save(flush:true, failOnError:true)
        }
    }


    void testDeleteBuilding() {
        def building = newBuilding()
        save building
        def id = building.id
        assertNotNull id
        building.delete()
        assertNull Building.get(id)
    }


    void testValidation() {
        def building = newBuilding()
        assertTrue "Building could not be validated as expected due to ${building.errors}", building.validate()
    }


    void testNullValidationFailure() {
        def building = new Building()
        assertFalse "Building should have failed validation", building.validate()
        assertErrorsFor building, 'nullable', ['code', 'description']
    }


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(building_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}