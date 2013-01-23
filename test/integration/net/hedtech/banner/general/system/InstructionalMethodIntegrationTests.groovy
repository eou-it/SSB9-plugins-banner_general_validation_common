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
package net.hedtech.banner.general.system;

import net.hedtech.banner.testing.BaseIntegrationTestCase;

import org.codehaus.groovy.grails.commons.ConfigurationHolder


class InstructionalMethodIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreate() {
        def imc = newInstructionalMethod()
        imc.save()
        assertNotNull imc.id
        assertNotNull imc.lastModified
    }


    void testList() {
        def imcs = InstructionalMethod.list()
        assertTrue imcs.size() > 0
    }


    void testDelete() {
        def imc = saveInstructionalMethod()
        def id = imc.id
        imc.delete()
        assertNull imc.get(id)
    }


    void testUpdate() {
        def imc = saveInstructionalMethod()
        imc.description = "Updated by Dan"
        save imc
        def updatedimc = imc.get(imc.id)
        assertEquals new Long(1), updatedimc.version
        assertEquals "Updated by Dan", updatedimc.description
    }


    void testFindAllByCode() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByCode("1111")
        assertTrue imcs.size() > 0
    }


    void testFindAllByDescription() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByDescription("unit-test")
        assertTrue imcs.size() > 0
    }


    void testFindAllByVoiceResponseMessageNumber() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByVoiceResponseMessageNumber(new BigDecimal(22))
        assertTrue imcs.size() > 0
    }


    void testValidationFail() {
        def imc = new InstructionalMethod(code: "exceeds_length", description: "unit-test",
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
        assertFalse "InstructionalMethod should have failed validation since code exceeds constraint length", imc.validate()
    }


    void testValidationSuccess() {
        def imc = new InstructionalMethod(code: "1111", description: "unit-test",
                voiceResponseMessageNumber: new BigDecimal(22),
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
        assertTrue "InstructionalMethod validation failed due to ${imc.errors}", imc.validate()
    }


    private InstructionalMethod newInstructionalMethod() {
        new InstructionalMethod(code: "1111", description: "unit-test",
                voiceResponseMessageNumber: new BigDecimal(22),
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
    }


    private InstructionalMethod saveInstructionalMethod() {
        save newInstructionalMethod()
    }

}
