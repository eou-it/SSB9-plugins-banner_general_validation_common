
/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Thu May 12 23:12:04 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class AddressTypeIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(addresstype_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)
	def i_success_telephoneType

	def i_success_code = "QQ"
	def i_success_description = "TTTTT"
	def i_success_systemRequiredIndicator = "Y"
	//Invalid test data (For failure tests)
	def i_failure_telephoneType

	def i_failure_code = null
	def i_failure_description = "TTTTT"
	def i_failure_systemRequiredIndicator = "Y"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)
	def u_success_telephoneType

	def u_success_code = "XX"
	def u_success_description = "XXXX"
	def u_success_systemRequiredIndicator = "N"
	//Valid test data (For failure tests)
	def u_failure_telephoneType

	def u_failure_code = "123"
	def u_failure_description = "1234567890123456789012345678901234567890"
	def u_failure_systemRequiredIndicator = "O"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['STVATYP'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(addresstype_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)
    	i_success_telephoneType = TelephoneType.findWhere(code:"GR")

		//Valid test data (For success tests)
	    u_success_telephoneType = TelephoneType.findWhere(code:"CO")

		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidAddressType() {
		def addressType = newValidForCreateAddressType()
		save addressType
		//Test if the generated entity now has an id assigned
        assertNotNull addressType.id
	}



	void testUpdateValidAddressType() {
		def addressType = newValidForCreateAddressType()
		save addressType
        assertNotNull addressType.id
        assertEquals 0L, addressType.version
        assertEquals i_success_code, addressType.code
        assertEquals i_success_description, addressType.description
        assertEquals i_success_systemRequiredIndicator, addressType.systemRequiredIndicator

		//Update the entity
		addressType.description = u_success_description
		addressType.systemRequiredIndicator = u_success_systemRequiredIndicator


		addressType.telephoneType = u_success_telephoneType
        save addressType
		//Asset for sucessful update
        addressType = AddressType.get( addressType.id )
        assertEquals 1L, addressType?.version
        assertEquals u_success_description, addressType.description
        assertEquals u_success_systemRequiredIndicator, addressType.systemRequiredIndicator


		addressType.telephoneType = u_success_telephoneType
	}



    void testOptimisticLock() {
		def addressType = newValidForCreateAddressType()
		save addressType

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVATYP set STVATYP_VERSION = 999 where STVATYP_SURROGATE_ID = ?", [ addressType.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		addressType.description = u_success_description
		addressType.systemRequiredIndicator = u_success_systemRequiredIndicator
        shouldFail( HibernateOptimisticLockingFailureException ) {
            addressType.save( failOnError: true, flush: true )
        }
    }

	void testDeleteAddressType() {
		def addressType = newValidForCreateAddressType()
		save addressType
		def id = addressType.id
		assertNotNull id
		addressType.delete()
		assertNull AddressType.get( id )
	}

    void testValidation() {
       def addressType = newInvalidForCreateAddressType()
       addressType.systemRequiredIndicator = "X"
       assertFalse "AddressType could not be validated as expected due to ${addressType.errors}", addressType.validate()
       assertErrorsFor addressType, 'inList', [ 'systemRequiredIndicator' ]
    }

    void testNullValidationFailure() {
        def addressType = new AddressType()
        assertFalse "AddressType should have failed validation", addressType.validate()
        assertErrorsFor addressType, 'nullable',
                                               [
                                                 'code'
                                               ]
        assertNoErrorsFor addressType,
        									   [
             									 'description',
             									 'systemRequiredIndicator',
                                                 'telephoneType'
											   ]
    }

    void testMaxSizeValidationFailures() {
        def addressType = new AddressType(
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        systemRequiredIndicator:'XXX' )
		assertFalse "AddressType should have failed validation", addressType.validate()
		assertErrorsFor addressType, 'maxSize', [ 'description', 'systemRequiredIndicator' ]
    }



	private def newValidForCreateAddressType() {
		def addressType = new AddressType(
			code: i_success_code,
			description: i_success_description,
			systemRequiredIndicator: i_success_systemRequiredIndicator,
			telephoneType: i_success_telephoneType,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return addressType
	}

	private def newInvalidForCreateAddressType() {
		def addressType = new AddressType(
			code: i_failure_code,
			description: i_failure_description,
			systemRequiredIndicator: i_failure_systemRequiredIndicator,
			telephoneType: null,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return addressType
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(addresstype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
