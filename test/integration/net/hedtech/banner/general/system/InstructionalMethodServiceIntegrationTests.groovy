/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the InstructionalMethod model.
 * */

class InstructionalMethodServiceIntegrationTests extends BaseIntegrationTestCase {

    def instructionalMethodService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateInstructionalMethod() {
        def instructionalMethod = newInstructionalMethod("1111")
        instructionalMethod = instructionalMethodService.create([domainModel: instructionalMethod])

        assertNotNull instructionalMethod
    }


    void testList() {
        assertTrue 0 < instructionalMethodService.list().size()
    }


    void testUpdateInstructionalMethod() {
        def instructionalMethod = newInstructionalMethod("1111")
        instructionalMethod = instructionalMethodService.create([domainModel: instructionalMethod])

        InstructionalMethod instructionalMethodUpdate = InstructionalMethod.findWhere(code: "1111")
        assertNotNull instructionalMethodUpdate

        instructionalMethodUpdate.description = "2222"
        instructionalMethodUpdate = instructionalMethodService.update([domainModel: instructionalMethodUpdate])
        assertEquals "2222", instructionalMethodUpdate.description
    }


    void testDeleteInstructionalMethod() {
        def instructionalMethod = newInstructionalMethod("1111")
        instructionalMethod = instructionalMethodService.create([domainModel: instructionalMethod])
        assertNotNull instructionalMethod

        def id = instructionalMethod.id
        def deleteMe = InstructionalMethod.get(id)

        instructionalMethodService.delete([domainModel: deleteMe])
        assertNull InstructionalMethod.get(id)
    }


    private InstructionalMethod newInstructionalMethod(String code)  {
        new InstructionalMethod(code: code, description: "$code test",
                                          voiceResponseMessageNumber: new BigDecimal(22),
                                          lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
    }

}
