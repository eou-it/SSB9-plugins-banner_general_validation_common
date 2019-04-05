/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the Medical Equipment model.
 * */

class MedicalEquipmentServiceTests extends BaseIntegrationTestCase {

    def MedicalEquipmentService


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
    void testCreateMedicalEquipment() {
        def medicalEquipment = new MedicalEquipment(code: "TT", description: "TT",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
        medicalEquipment = MedicalEquipmentService.create(medicalEquipment)
        assertNotNull "Medical Equipment ID is null in Medical Equipment Create Service Test", medicalEquipment.id
        assertNotNull "Medical Equipment Code is null in MedicalEquipment Create Service Test", medicalEquipment.code
    }

    @Test
    void testUpdateMedicalEquipment() {
        def medicalEquipment = new MedicalEquipment(code: "TT", description: "TT",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
        medicalEquipment = MedicalEquipmentService.create(medicalEquipment)

        MedicalEquipment medicalEquipmentUpdate = MedicalEquipment.findWhere(code: "TT")
        assertNotNull "Medical Equipment ID is null in Medical Equipment Update Service Test", medicalEquipmentUpdate.id

        medicalEquipmentUpdate.description = "ZZ"
        medicalEquipmentUpdate = MedicalEquipmentService.update(medicalEquipmentUpdate)
        assertEquals "ZZ", medicalEquipmentUpdate.description
    }


    @Test
    void testDeleteMedicalEquipment() {
        def medicalEquipment = new MedicalEquipment(code: "TT", description: "TT",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
        medicalEquipment = MedicalEquipmentService.create(medicalEquipment)
        assertNotNull "Medical Equipment ID is null in Medical Equipment Delete Service Test", medicalEquipment.id

        MedicalEquipment medicalEquipmentDelete = MedicalEquipment.findWhere(code: "TT")
        MedicalEquipmentService.delete(medicalEquipmentDelete.id)

        assertNull "Medical Equipment should have been deleted in Medical Equipment Delete Service Test", medicalEquipment.get(medicalEquipmentDelete.id)

    }

}
