/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system;

import com.sungardhe.banner.testing.BaseIntegrationTestCase;

import org.codehaus.groovy.grails.commons.ConfigurationHolder


class InstructionalMethodIntegrationTests extends BaseIntegrationTestCase {
    
    protected void setUp() {
        formContext = [ 'GTVINSM' ] // Since we are not testing a controller, we need to explicitly set this
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
        assertNull imc.get( id )
    }
    
    void testUpdate() {
        def imc = saveInstructionalMethod()
        imc.description = "Updated by Dan"
        save imc
        def updatedimc = imc.get( imc.id )
        assertEquals new Long ( 1 ), updatedimc.version
        assertEquals "Updated by Dan", updatedimc.description
    }
    
    void testFindAllByCode() {
        def imc = saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByCode( "1111")
        assertTrue imcs.size > 0
    }
    
    void testFindAllByDescription() {
        def imc = saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByDescription("unit-test")
        assertTrue imcs.size() > 0
    }
    
    void testFindAllByVoiceResponseMessageNumber() {
        def imc = saveInstructionalMethod()
        def imcs = InstructionalMethod.findAllByVoiceResponseMessageNumber(new BigDecimal(22))
        assertTrue imcs.size > 0
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
        def imc = new InstructionalMethod(code: "1111", description: "unit-test",
                voiceResponseMessageNumber: new BigDecimal(22),
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
    }
    
    private InstructionalMethod saveInstructionalMethod() {
        save newInstructionalMethod()
    }
    
}
