/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration tests for net.hedtech.banner.student.MedicalEquipment.
 */
class MedicalEquipmentIntegrationTests extends BaseIntegrationTestCase {

    def medicalEquipmentService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreateMedicalEquipment() {
        def medicalEquipment = newMedicalEquipment()
        save medicalEquipment
        assertNotNull medicalEquipment.id
    }


    void testUpdateMedicalEquipment() {
        def medicalEquipment = newMedicalEquipment()
        save medicalEquipment

        def id = medicalEquipment.id
        def version = medicalEquipment.version
        assertNotNull id
        assertEquals 0L, version

        medicalEquipment.description = "updated"
        save medicalEquipment
        medicalEquipment = MedicalEquipment.get(id)

        assertNotNull "found must not be null", medicalEquipment
        assertEquals "updated", medicalEquipment.description
        assertEquals 1, medicalEquipment.version
    }


    void testOptimisticLock() {
        def medicalEquipment = newMedicalEquipment()
        save medicalEquipment

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVMDEQ set STVMDEQ_VERSION = 999 where STVMDEQ_SURROGATE_ID = ?", [medicalEquipment.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

        //Try to update the entity
        medicalEquipment.code = "UU"
        medicalEquipment.description = "UUUU"
        medicalEquipment.lastModified = new Date()
        medicalEquipment.lastModifiedBy = "test"
        medicalEquipment.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            medicalEquipment.save(flush: true)
        }
    }


    void testDeleteMedicalEquipment() {
        def medicalEquipment = newMedicalEquipment()
        save medicalEquipment

        def id = medicalEquipment.id
        assertNotNull id
        medicalEquipment.delete()
        assertNull medicalEquipment.get(id)
    }

    void testValidation() {
        def medicalEquipment = newMedicalEquipment()
        //should not pass validation since none of the required values are provided
        assertTrue "Medical Equipment could not be validated as expected due to ${medicalEquipment.errors}", medicalEquipment.validate()
    }

    void testNullValidationFailure() {
        def medicalEquipment = new MedicalEquipment()
        assertFalse "Medical Equipment should have failed validation", medicalEquipment.validate()
        assertErrorsFor medicalEquipment, 'nullable', ['code', 'description']
    }

    void testMaxSizeValidationFailures() {
        def medicalEquipment = new MedicalEquipment(
                code: 'XXXXXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "MedicalEquipment should have failed validation", medicalEquipment.validate()
        assertErrorsFor medicalEquipment, 'maxSize', ['code', 'description']
    }



    private def newMedicalEquipment() {
        new MedicalEquipment(code: "zz", description: "zz",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
    }


}
