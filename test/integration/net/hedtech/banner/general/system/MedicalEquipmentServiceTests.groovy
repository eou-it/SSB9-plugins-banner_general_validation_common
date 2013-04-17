/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Medical Equipment model.
 * */

class MedicalEquipmentServiceTests extends BaseIntegrationTestCase {

    def MedicalEquipmentService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateMedicalEquipment() {
        def medicalEquipment = new MedicalEquipment(code: "TT", description: "TT",
                lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner")
        medicalEquipment = MedicalEquipmentService.create(medicalEquipment)
        assertNotNull "Medical Equipment ID is null in Medical Equipment Create Service Test", medicalEquipment.id
        assertNotNull "Medical Equipment Code is null in MedicalEquipment Create Service Test", medicalEquipment.code
    }

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
