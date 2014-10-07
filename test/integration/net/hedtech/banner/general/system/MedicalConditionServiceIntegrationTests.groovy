/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.*

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Medical Condition Service.
 * */
public class MedicalConditionServiceIntegrationTests extends BaseIntegrationTestCase {

    def medicalConditionService      // injected by Spring

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreate() {
        def medicalCondition = new MedicalCondition(code: "zz", description: "integration-test")
        medicalCondition = medicalConditionService.create(medicalCondition)
        assertNotNull medicalCondition.id
    }


    @Test
    void testCreateWithParams() {
        def medicalCondition = medicalConditionService.create([code: "zz", description: "integration-test"])
        assertNotNull medicalCondition.id
    }


    @Test
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
            assertTrue ae.wrappedException.fullMessage.contains('rejected value [null]')
        }
    }


    @Test
    void testDelete() {
        def medicalCondition = new MedicalCondition(code: "zz", description: "integration-test")
        medicalCondition = medicalConditionService.create(medicalCondition)
        assertNotNull medicalCondition.id

        medicalConditionService.delete(medicalCondition.id)
    }

}
