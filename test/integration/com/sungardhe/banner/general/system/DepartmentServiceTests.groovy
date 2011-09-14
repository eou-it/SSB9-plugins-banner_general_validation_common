
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