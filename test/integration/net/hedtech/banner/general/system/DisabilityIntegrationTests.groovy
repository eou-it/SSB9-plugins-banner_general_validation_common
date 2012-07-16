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

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


/**
 * Integration test for the disability model.
 **/
class DisabilityIntegrationTests extends BaseIntegrationTestCase {
    
    def disabilityService
    def medicalEquipmentService
    
	protected void setUp() {
        formContext = [ 'STVDISA' ] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    /**
     * Tests the ability to create and persist a new Disability instance.
     */
     void testCreateDisability() {
	    def disability = newDisability()
		save disability
        assertNotNull disability.id
	 }

     /**
     * Tests the ability to update a Description.
     */
     void testUpdateDisability() {
        def disability = newDisability()
        save disability

        def id = disability.id
        def version = disability.version
        assertNotNull id
        assertEquals 0L, version

        disability.description = "updated"
        save disability
        disability = Disability.get(id)

        assertNotNull "found must not be null", disability
        assertEquals "updated", disability.description
        assertEquals 1, disability.version
     }

     /**
     * Test optimistic locking.
     */
     void testOptimisticLock() {
        def disability = newDisability()
		save disability

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVDISA set STVDISA_VERSION = 999 where STVDISA_SURROGATE_ID = ?", [ disability.id ] )
        } finally {
          sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

		//Try to update the entity
		disability.code="UU"
		disability.description="UUUU"
		disability.lastModified= new Date()
		disability.lastModifiedBy="test"
		disability.dataOrigin= "Banner"
        shouldFail( HibernateOptimisticLockingFailureException ) {
            disability.save( flush: true )
        }
    }

    /**
     * Tests the ability to delete a Disability.
     */
    void testDeleteDisability() {
      def disability = newDisability()
      save disability

      def id = disability.id
      assertNotNull id
      disability.delete()
      assertNull disability.get( id )
    }

    void testValidation() {
      def disability = newDisability()
      assertTrue "Disability could not be validated as expected due to ${disability.errors}", disability.validate()
    }

    void testNullValidationFailure() {
       def disability = new Disability()
      //should not pass validation since none of the required values are provided
       assertFalse "Disability should have failed validation", disability.validate()
       assertErrorsFor disability, 'nullable', [ 'code', 'description'  ]
    }

    void testMaxSizeValidationFailures() {
       def disability = new Disability(
           code:'XXXXXXXXXX',
           description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
       assertFalse "Disability should have failed validation", disability.validate()
       assertErrorsFor disability, 'maxSize', [ 'code', 'description' ]
    }



     private def newDisability() {
       new Disability(code: "TT", description: "TT" , lastModified: new Date(),
		   lastModifiedBy: "test", dataOrigin: "Banner" )
     }
}
