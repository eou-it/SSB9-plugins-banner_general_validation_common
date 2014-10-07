/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the Department model.
 * */

class DepartmentServiceTests extends BaseIntegrationTestCase {

    def DepartmentService


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
    void testCreateDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = DepartmentService.create(department)
        assertNotNull "Department ID is null in Department Create Service Test", department.id
        assertNotNull "Department Code is null in Department Create Service Test", department.code
    }

    @Test
    void testUpdateDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = DepartmentService.create(department)

        Department departmentUpdate = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Update Service Test", departmentUpdate.id

        departmentUpdate.description = "ZZ"
        departmentUpdate = DepartmentService.update(departmentUpdate)
        assertEquals "ZZ", departmentUpdate.description
    }

    @Test
    void testDeleteDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = DepartmentService.create(department)
        assertNotNull "Department ID is null in Department Delete Service Test", department.id

        Department departmentDelete = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Delete Service Test", departmentDelete.id

        DepartmentService.delete(departmentDelete.id)
        assertNull "Department should have been deleted in Department Delete Service Test", department.get(departmentDelete.id)
    }

}
