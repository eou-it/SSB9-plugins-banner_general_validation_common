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
/**
 Banner Automator Version: 0.1.1
 Generated: Thu May 12 23:12:04 IST 2011
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class TelephoneTypeIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(telephonetype_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTT"
	def i_success_description = "TTTTT"
	//Invalid test data (For failure tests)

	def i_failure_code = null
	def i_failure_description = null

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "XXXX"
	def u_success_description = "XXXX"
	//Valid test data (For failure tests)

    def u_failure_code = "1234567890"
	def u_failure_description = "1234567890123456789012345678901234567890"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}


	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidTelephoneType() {
		def telephoneType = newValidForCreateTelephoneType()
		save telephoneType
		//Test if the generated entity now has an id assigned
        assertNotNull telephoneType.id
	}


	void testUpdateValidTelephoneType() {
		def telephoneType = newValidForCreateTelephoneType()
		save telephoneType
        assertNotNull telephoneType.id
        assertEquals 0L, telephoneType.version
        assertEquals i_success_code, telephoneType.code
        assertEquals i_success_description, telephoneType.description

		//Update the entity
		telephoneType.description = u_success_description
        save telephoneType
		//Asset for sucessful update
        telephoneType = TelephoneType.get( telephoneType.id )
        assertEquals 1L, telephoneType?.version
        assertEquals u_success_description, telephoneType.description
	}


    void testOptimisticLock() {
		def telephoneType = newValidForCreateTelephoneType()
		save telephoneType

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVTELE set STVTELE_VERSION = 999 where STVTELE_SURROGATE_ID = ?", [ telephoneType.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		telephoneType.description = u_success_description
        shouldFail( HibernateOptimisticLockingFailureException ) {
            telephoneType.save( failOnError: true, flush: true )
        }
    }

	void testDeleteTelephoneType() {
		def telephoneType = newValidForCreateTelephoneType()
		save telephoneType
		def id = telephoneType.id
		assertNotNull id
		telephoneType.delete()
		assertNull TelephoneType.get( id )
	}


    void testNullValidationFailure() {
        def telephoneType = new TelephoneType()
        assertFalse "TelephoneType should have failed validation", telephoneType.validate()
        assertErrorsFor telephoneType, 'nullable',
                                               [
                                                 'code',
                                                 'description'
                                               ]
    }





	private def newValidForCreateTelephoneType() {
		def telephoneType = new TelephoneType(
			code: i_success_code,
			description: i_success_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return telephoneType
	}

	private def newInvalidForCreateTelephoneType() {
		def telephoneType = new TelephoneType(
			code: i_failure_code,
			description: i_failure_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return telephoneType
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(telephonetype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
