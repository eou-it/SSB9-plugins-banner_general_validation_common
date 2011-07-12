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

class BuildingAndRoomAttributeIntegrationTests extends BaseIntegrationTestCase {

    def buildingAndRoomAttributeService


    protected void setUp() {
        formContext = ['STVRDEF'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateBuildingAndRoomAttribute() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()
        save buildingAndRoomAttribute
        //Test if the generated entity now has an id assigned
        assertNotNull buildingAndRoomAttribute.id
    }


    void testUpdateBuildingAndRoomAttribute() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()
        save buildingAndRoomAttribute

        assertNotNull buildingAndRoomAttribute.id
        assertEquals 0L, buildingAndRoomAttribute.version
        assertEquals "TTTT", buildingAndRoomAttribute.code
        assertEquals "TTTTT", buildingAndRoomAttribute.description
        assertEquals 1, buildingAndRoomAttribute.schedulerNumber
        assertEquals true, buildingAndRoomAttribute.autoSchedulerIndicator

        //Update the entity
        def testDate = new Date()
        buildingAndRoomAttribute.code = "UUUU"
        buildingAndRoomAttribute.description = "UUUUU"
        buildingAndRoomAttribute.schedulerNumber = 0
        buildingAndRoomAttribute.autoSchedulerIndicator = false
        buildingAndRoomAttribute.lastModified = testDate
        buildingAndRoomAttribute.lastModifiedBy = "test"
        buildingAndRoomAttribute.dataOrigin = "Banner"
        save buildingAndRoomAttribute

        buildingAndRoomAttribute = BuildingAndRoomAttribute.get(buildingAndRoomAttribute.id)
        assertEquals 1L, buildingAndRoomAttribute?.version
        assertEquals "UUUU", buildingAndRoomAttribute.code
        assertEquals "UUUUU", buildingAndRoomAttribute.description
        assertEquals 0, buildingAndRoomAttribute.schedulerNumber
        assertFalse buildingAndRoomAttribute.autoSchedulerIndicator
    }


    void testOptimisticLock() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()
        save buildingAndRoomAttribute

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRDEF set STVRDEF_VERSION = 999 where STVRDEF_SURROGATE_ID = ?", [buildingAndRoomAttribute.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        buildingAndRoomAttribute.code = "UUUU"
        buildingAndRoomAttribute.description = "UUUUU"
        buildingAndRoomAttribute.schedulerNumber = 0
        buildingAndRoomAttribute.autoSchedulerIndicator = false
        buildingAndRoomAttribute.lastModified = new Date()
        buildingAndRoomAttribute.lastModifiedBy = "test"
        buildingAndRoomAttribute.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            buildingAndRoomAttribute.save(flush:true, failOnError:true)
        }
    }


    void testDeleteBuildingAndRoomAttribute() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()
        save buildingAndRoomAttribute
        def id = buildingAndRoomAttribute.id
        assertNotNull id
        buildingAndRoomAttribute.delete()
        assertNull BuildingAndRoomAttribute.get(id)
    }


    void testValidation() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()
        assertTrue "BuildingAndRoomAttribute could not be validated as expected due to ${buildingAndRoomAttribute.errors}", buildingAndRoomAttribute.validate()
    }


    void testNullValidationFailure() {
        def buildingAndRoomAttribute = new BuildingAndRoomAttribute()
        assertFalse "BuildingAndRoomAttribute should have failed validation", buildingAndRoomAttribute.validate()
        assertErrorsFor buildingAndRoomAttribute, 'nullable', ['code', 'autoSchedulerIndicator']
        assertNoErrorsFor buildingAndRoomAttribute, ['description', 'schedulerNumber']
    }


    void testMaxSizeValidationFailures() {
        def buildingAndRoomAttribute = new BuildingAndRoomAttribute(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "BuildingAndRoomAttribute should have failed validation", buildingAndRoomAttribute.validate()
        assertErrorsFor buildingAndRoomAttribute, 'maxSize', ['description']
    }


    void testValidationMessages() {
        def buildingAndRoomAttribute = newBuildingAndRoomAttribute()

        buildingAndRoomAttribute.code = null
        assertFalse buildingAndRoomAttribute.validate()
        assertLocalizedError buildingAndRoomAttribute, 'nullable', /.*Field.*code.*of class.*BuildingAndRoomAttribute.*cannot be null.*/, 'code'

        buildingAndRoomAttribute.autoSchedulerIndicator = null
        assertFalse buildingAndRoomAttribute.validate()
        assertLocalizedError buildingAndRoomAttribute, 'nullable', /.*Field.*autoSchedulerIndicator.*of class.*BuildingAndRoomAttribute.*cannot be null.*/, 'autoSchedulerIndicator'

    }


    private def newBuildingAndRoomAttribute() {
        new BuildingAndRoomAttribute(code: "TTTT",
                description: "TTTTT",
                schedulerNumber: 1,
                autoSchedulerIndicator: true,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(buildingandroomattributes_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
