/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class DiplomaTypeIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidDiplomaType() {
        def diplomaType = newDiplomaType()
        diplomaType.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull diplomaType.id
    }


    void testUpdateDiplomaType() {
        def diplomaType = newDiplomaType()
        diplomaType.save(failOnError: true, flush: true)
        assertNotNull diplomaType.id
        assertEquals 0L, diplomaType.version
        assertEquals "TT", diplomaType.code
        assertEquals "TTTTTTTTTT", diplomaType.description
        assertEquals "TTT", diplomaType.electronicDataInterchangeEquivalent

        //Update the entity
        diplomaType.description = "UUUUUUUUUU"
        diplomaType.electronicDataInterchangeEquivalent = "UUU"
        diplomaType.save(failOnError: true, flush: true)
        //Assert for sucessful update
        diplomaType = DiplomaType.get(diplomaType.id)
        assertEquals 1L, diplomaType?.version
        assertEquals "UUUUUUUUUU", diplomaType.description
        assertEquals "UUU", diplomaType.electronicDataInterchangeEquivalent
    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def diplomaType = newDiplomaType()

        diplomaType.save(flush: true, failOnError: true)
        diplomaType.refresh()
        assertNotNull "DiplomaType should have been saved", diplomaType.id

        // test date values -
        assertEquals date.format(today), date.format(diplomaType.lastModified)
        assertEquals hour.format(today), hour.format(diplomaType.lastModified)
    }


    void testOptimisticLock() {
        def diplomaType = newDiplomaType()
        save diplomaType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVDPLM set STVDPLM_VERSION = 999 where STVDPLM_SURROGATE_ID = ?", [diplomaType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        diplomaType.description = "UUU"
        diplomaType.electronicDataInterchangeEquivalent = "TTT"
        shouldFail(HibernateOptimisticLockingFailureException) {
            diplomaType.save(failOnError: true, flush: true)
        }
    }


    void testDeleteDiplomaType() {
        def diplomaType = newDiplomaType()
        diplomaType.save(failOnError: true, flush: true)
        def id = diplomaType.id
        assertNotNull id
        diplomaType.delete()
        assertNull DiplomaType.get(id)
    }


    void testValidation() {
        def diplomaType = new DiplomaType()
        assertFalse "DiplomaType could not be validated as expected due to ${diplomaType.errors}", diplomaType.validate()
    }


    void testNullValidationFailure() {
        def diplomaType = new DiplomaType()
        assertFalse "DiplomaType should have failed validation", diplomaType.validate()
        assertErrorsFor diplomaType, 'nullable', ['code', 'description']
        assertNoErrorsFor diplomaType, ['electronicDataInterchangeEquivalent']
    }


    void testMaxSizeValidationFailures() {
        def diplomaType = new DiplomaType(code: "XXXX", description: "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                electronicDataInterchangeEquivalent: 'XXXXX')
        assertFalse "DiplomaType should have failed validation", diplomaType.validate()
        assertErrorsFor diplomaType, 'maxSize', ['code', 'description', 'electronicDataInterchangeEquivalent']
    }


    private def newDiplomaType() {
        def diplomaType = new DiplomaType(
                code: "TT",
                description: "TTTTTTTTTT",
                electronicDataInterchangeEquivalent: "TTT"
        )
        return diplomaType
    }

}
