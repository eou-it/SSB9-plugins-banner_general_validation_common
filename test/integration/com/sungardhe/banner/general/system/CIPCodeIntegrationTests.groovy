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
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the CIP code model.
 **/
class CIPCodeIntegrationTests extends BaseIntegrationTestCase {

	def cipCodeService
	
		
	protected void setUp() {
        formContext = ['STVCIPC'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateCIPCode() {
		def cipCode = newCIPCode()
		save cipCode
        assertNotNull cipCode.id
	}
	

	void testUpdateCIPCode() {
		def cipCode = newCIPCode()
		save cipCode

		def id = cipCode.id
		def version = cipCode.version
		assertNotNull id
		assertEquals 0L, version

		cipCode.description = "updated"
		save cipCode
		cipCode = CIPCode.get( id )
		
		assertNotNull "found must not be null", cipCode
		assertEquals "updated", cipCode.description
		assertEquals 1, cipCode.version
	}
	
     void testOptimisticLock() {
        def cipCode = newCIPCode()
		save cipCode

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVCIPC set STVCIPC_VERSION = 999 where STVCIPC_SURROGATE_ID = ?", [ cipCode.id ] )
        } finally {
          sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

		//Try to update the entity
		cipCode.code="UU"
		cipCode.description="UUUU"
		cipCode.lastModified= new Date()
		cipCode.lastModifiedBy="test"
		cipCode.dataOrigin= "Banner"
        shouldFail( HibernateOptimisticLockingFailureException ) {
            cipCode.save( flush: true )
        }
    }

	void testDeleteCIPCode() {
		def cipCode = newCIPCode()
		save cipCode

		def id = cipCode.id
		assertNotNull id
		cipCode.delete()
		assertNull CIPCode.get( id )
	}	

	void testValidation() {
      def cipCode = newCIPCode()
      //should not pass validation since none of the required values are provided
      assertTrue "CIP Code could not be validated as expected due to ${cipCode.errors}", cipCode.validate()
	}

     void testNullValidationFailure() {
        def cipCode = new CIPCode()
        assertFalse "CIP Code should have failed validation", cipCode.validate()
        assertErrorsFor cipCode, 'nullable', [ 'code', 'description'  ]
    }

    void testMaxSizeValidationFailures() {
        def cipCode = new CIPCode(
        code:'XXXXXXXXXX',
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "CIPCode should have failed validation", cipCode.validate()
		assertErrorsFor cipCode, 'maxSize', [ 'code', 'description' ]
    }


    private def newCIPCode() {
      new CIPCode( code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT" ,
                   lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner" )
    }

}
