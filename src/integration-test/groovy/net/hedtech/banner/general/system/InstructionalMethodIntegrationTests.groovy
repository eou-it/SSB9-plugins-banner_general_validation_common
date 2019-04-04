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
class InstructionalMethodIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreate() {
        def imc = newInstructionalMethod()
        imc.save()
        assertNotNull imc.id
        assertNotNull imc.lastModified
    }


    @Test
    void testList() {
        def imcs = InstructionalMethod.list()
        assertTrue imcs.size() > 0
    }


    @Test
    void testDelete() {
        def imc = saveInstructionalMethod()
        def id = imc.id
        imc.delete()
        assertNull imc.get(id)
    }


    @Test
    void testUpdate() {
        def imc = saveInstructionalMethod()
        imc.description = "Updated by Dan"
        save imc
        def updatedimc = imc.get(imc.id)
        assertEquals new Long(1), updatedimc.version
        assertEquals "Updated by Dan", updatedimc.description
    }


    @Test
    void testFindAllByCode() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByCode("1111")
        assertTrue imcs.size() > 0
    }


    @Test
    void testFindAllByDescription() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByDescription("unit-test")
        assertTrue imcs.size() > 0
    }


    @Test
    void testFindAllByVoiceResponseMessageNumber() {
        saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByVoiceResponseMessageNumber(new BigDecimal(22))
        assertTrue imcs.size() > 0
    }


    @Test
    void testValidationFail() {
        def imc = new InstructionalMethod(code: "exceeds_length", description: "unit-test",
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
        assertFalse "InstructionalMethod should have failed validation since code exceeds constraint length", imc.validate()
    }


    @Test
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
