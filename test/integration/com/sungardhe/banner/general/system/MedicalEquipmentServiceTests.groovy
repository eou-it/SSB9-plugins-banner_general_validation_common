
/*******************************************************************************

 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/

package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Medical Equipment model.
 **/

class MedicalEquipmentServiceTests extends BaseIntegrationTestCase {

	def MedicalEquipmentService


	protected void setUp() {
        formContext = ['STVMDEQ'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
       super.tearDown()
    }

	void testCreateMedicalEquipment() {
        def medicalEquipment =  new MedicalEquipment( code: "TT", description: "TT",
                    lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner" )
		medicalEquipment = MedicalEquipmentService.create(medicalEquipment)
        assertNotNull "Medical Equipment ID is null in Medical Equipment Create Service Test", medicalEquipment.id
        assertNotNull "Medical Equipment Code is null in MedicalEquipment Create Service Test", medicalEquipment.code
	}

	void testUpdateMedicalEquipment() {
        def medicalEquipment =  new MedicalEquipment( code: "TT", description: "TT",
                  lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner" )
	    medicalEquipment = MedicalEquipmentService.create(medicalEquipment)

        MedicalEquipment medicalEquipmentUpdate = MedicalEquipment.findWhere(code: "TT")
        assertNotNull "Medical Equipment ID is null in Medical Equipment Update Service Test", medicalEquipmentUpdate.id

        medicalEquipmentUpdate.description = "ZZ"
        medicalEquipmentUpdate = MedicalEquipmentService.update(medicalEquipmentUpdate)
        assertEquals "ZZ", medicalEquipmentUpdate.description
	}


	void testDeleteMedicalEquipment() {
         def medicalEquipment =  new MedicalEquipment( code: "TT", description: "TT",
                    lastModified: new Date(), lastModifiedBy: "horizon", dataOrigin: "Banner" )
        medicalEquipment = MedicalEquipmentService.create(medicalEquipment)
        assertNotNull "Medical Equipment ID is null in Medical Equipment Delete Service Test", medicalEquipment.id

        MedicalEquipment medicalEquipmentDelete = MedicalEquipment.findWhere(code: "TT")
        MedicalEquipmentService.delete(medicalEquipmentDelete.id)

        assertNull "Medical Equipment should have been deleted in Medical Equipment Delete Service Test", medicalEquipment.get(medicalEquipmentDelete.id)

	}

}