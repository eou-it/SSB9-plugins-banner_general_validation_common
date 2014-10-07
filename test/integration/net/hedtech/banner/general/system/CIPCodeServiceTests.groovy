/** *****************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the CIP code model.
 * */

class CIPCodeServiceTests extends BaseIntegrationTestCase {

    def CIPCodeService


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
    void testCreateCIPCode() {
        def cipCode = new CIPCode(code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT", publicationYear: 2010,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")

        cipCode = CIPCodeService.create(cipCode)
        assertNotNull "CIP Code ID is null in CIP Code Create Service Test", cipCode.id
        assertNotNull "CIP Code Code is null in CIP Code Create Service Test", cipCode.code
    }

    @Test
    void testUpdateCIPCode() {
        def cipCode = new CIPCode(code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT", publicationYear: 2010,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        cipCode = CIPCodeService.create(cipCode)

        CIPCode cipCodeUpdate = CIPCode.findWhere(code: "TT")
        assertNotNull "CIP Code ID is null in CIP Code Update Service Test", cipCodeUpdate.id

        cipCodeUpdate.description = "ZZ"
        cipCodeUpdate = CIPCodeService.update(cipCodeUpdate)
        assertEquals "ZZ", cipCodeUpdate.description
    }


    @Test
    void testDeleteCIPCode() {
        def cipCode = new CIPCode(code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT", publicationYear: 2010,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        cipCode = CIPCodeService.create(cipCode)
        assertNotNull "CIP Code ID is null in CIP Code Delete Service Test", cipCode.id

        CIPCode cipCodeDelete = CIPCode.findWhere(code: "TT")
        CIPCodeService.delete(cipCodeDelete.id)

        assertNull "CIP Code should have been deleted in CIP Code Delete Service Test", cipCode.get(cipCodeDelete.id)

    }

}
