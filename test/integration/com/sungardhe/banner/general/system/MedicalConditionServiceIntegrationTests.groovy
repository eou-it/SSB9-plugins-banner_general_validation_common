/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.exceptions.*

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Medical Condition Service.
 * */
public class MedicalConditionServiceIntegrationTests extends BaseIntegrationTestCase {

    def medicalConditionService      // injected by Spring

    protected void setUp() {
        formContext = ['STVMEDI'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreate() {
        def medicalCondition = new MedicalCondition(code: "zz", description: "integration-test")
        medicalCondition = medicalConditionService.create(medicalCondition)
        assertNotNull medicalCondition.id
    }


    void testCreateWithParams() {
        def medicalCondition = medicalConditionService.create([code: "zz", description: "integration-test"])
        assertNotNull medicalCondition.id
    }


    void testUpdate() {
        def medicalCondition = new MedicalCondition(code: "zz", description: "integration-test")
        medicalCondition = medicalConditionService.create(medicalCondition)
        assertNotNull medicalCondition.id

        medicalCondition.code = "aa"
        medicalCondition = medicalConditionService.update(medicalCondition.properties) // pass params the first time (we'll test passing a model later)
        assertTrue medicalCondition.code == "aa"

        medicalCondition.code = null
        try {
            medicalConditionService.update(medicalCondition) // pass instance of the model this time
            fail "Should of thrown an exception because code is not allowed to be null"
        } catch (ApplicationException ae) {
            assertLocalizedError medicalCondition, 'nullable', /.*Field.*code.*of class.*MedicalCondition.*cannot be null.*/, 'code'
        }
    }


    void testDelete() {
        def medicalCondition = new MedicalCondition(code: "zz", description: "integration-test")
        medicalCondition = medicalConditionService.create(medicalCondition)
        assertNotNull medicalCondition.id

        medicalConditionService.delete(medicalCondition.id)
    }

}