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

import net.hedtech.banner.exceptions.*

import net.hedtech.banner.testing.BaseIntegrationTestCase

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
