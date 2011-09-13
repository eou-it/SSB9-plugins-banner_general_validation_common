
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
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the department model.
 **/
class DepartmentIntegrationTests extends BaseIntegrationTestCase {

	def departmentService
	
	
	protected void setUp() {
        formContext = ['STVDEPT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateDepartment() {
	    def department = newDepartment()
		save department
        assertNotNull department.id
	}
	

	void testUpdateDepartment() {
        def department = newDepartment()
		save department

		def id = department.id
		def version = department.version
		assertNotNull id
		assertEquals 0L, version

		department.description = "updated"
	    save department
		department = Department.get(id)

        assertNotNull "found must not be null", department
		assertEquals "updated", department.description
		assertEquals 1, department.version			
	}


    void testOptimisticLock() {
        def department = newDepartment()
		save department

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVDEPT set STVDEPT_VERSION = 999 where STVDEPT_SURROGATE_ID = ?", [ department.id ] )
        } finally {
          sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

		//Try to update the entity
		department.code="UU"
		department.description="UUUU"
		department.lastModified= new Date()
		department.lastModifiedBy="test"
		department.dataOrigin= "Banner"
        shouldFail( HibernateOptimisticLockingFailureException ) {
            department.save( flush: true )
        }
    }


	void testDeleteDepartment() {
        def department = newDepartment()
	    save department

		def id = department.id
		assertNotNull id
		department.delete()
		assertNull department.get( id )
	}

    void testValidation() {
        def department = newDepartment()
        //should not pass validation since none of the required values are provided
        assertTrue "Department could not be validated as expected due to ${department.errors}", department.validate()
	}

     void testNullValidationFailure() {
       def department = new Department()
      //should not pass validation since none of the required values are provided
       assertFalse "Department should have failed validation", department.validate()
       assertErrorsFor department, 'nullable', [ 'code', 'description'  ]
    }


    void testMaxSizeValidationFailures() {
       def department = new Department(
           code:'XXXXXXXXXX',
           description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
       assertFalse "Department should have failed validation", department.validate()
       assertErrorsFor department, 'maxSize', [ 'code', 'description' ]
    }



    void testFetchBySomeAttribute() {
      def department = Department.fetchBySomeAttribute()
      def departmentList = department.get("list")

      def departmentObj1 = Department.findWhere(code : "ACCT")
      def departmentObj2 = Department.findWhere(code : "BIOL")
      def departmentObj3 = Department.findWhere(code : "ECON")
      def departmentObj4 = Department.findWhere(code : "MGMT")
      def departmentObj5 = newDepartment()

      assertTrue departmentList.contains(departmentObj1)
      assertTrue departmentList.contains(departmentObj2)
      assertTrue departmentList.contains(departmentObj3)
      assertTrue departmentList.contains(departmentObj4)
      assertFalse departmentList.contains(departmentObj5)

      def filter = "ENG"
      department = Department.fetchBySomeAttribute(filter)
      departmentList = department.get("list")

      departmentObj1 = Department.findWhere(code : "ENGE")
      departmentObj2 = Department.findWhere(code : "ENGL")
      departmentObj3 = Department.findWhere(code : "ENGR")
      departmentObj4 = Department.findWhere(code : "ENGT")
      departmentObj5 = Department.findWhere(code : "ANSC")

      assertTrue departmentList.contains(departmentObj1)
      assertTrue departmentList.contains(departmentObj2)
      assertTrue departmentList.contains(departmentObj3)
      assertTrue departmentList.contains(departmentObj4)
      assertFalse departmentList.contains(departmentObj5)

  }


    private def newDepartment() {
       new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, lastModified: new Date(),
		   lastModifiedBy: "test", dataOrigin: "Banner" )
    }
}
