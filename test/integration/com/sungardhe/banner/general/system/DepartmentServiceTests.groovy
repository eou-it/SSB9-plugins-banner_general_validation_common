
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
 * Integration test for the Department model.
 **/

class DepartmentServiceTests extends BaseIntegrationTestCase {

	def DepartmentService


	protected void setUp() {
        formContext = ['STVDEPT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
       super.tearDown()
    }

	void testCreateDepartment() {
        def department = new Department(code: "TT", description: "TT",  systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
            lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
		department = DepartmentService.create(department)
        assertNotNull "Department ID is null in Department Create Service Test", department.id
        assertNotNull "Department Code is null in Department Create Service Test", department.code
	}

	void testUpdateDepartment() {
        def department = new Department(code: "TT", description: "TT",  systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
            lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
	    department = DepartmentService.create(department)

        Department departmentUpdate = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Update Service Test", departmentUpdate.id

        departmentUpdate.description = "ZZ"
        departmentUpdate = DepartmentService.update(departmentUpdate)
        assertEquals "ZZ", departmentUpdate.description
	}

	void testDeleteDepartment() {
        def department = new Department(code: "TT", description: "TT" , systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
            lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
	    department = DepartmentService.create(department)
        assertNotNull "Department ID is null in Department Delete Service Test", department.id

        Department departmentDelete = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Delete Service Test", departmentDelete.id

        DepartmentService.delete(departmentDelete.id)
        assertNull "Department should have been deleted in Department Delete Service Test", department.get(departmentDelete.id)
	}

}