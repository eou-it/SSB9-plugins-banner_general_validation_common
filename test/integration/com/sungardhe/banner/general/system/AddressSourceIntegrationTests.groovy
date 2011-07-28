/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:44:46 EDT 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.junit.Ignore
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class AddressSourceIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(addresssource_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "WWWW"
    def i_failure_description = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "WWWW"
    def u_success_description = "WWWWWW"
    //Valid test data (For failure tests)

    def u_failure_code = "WWWW"
    def u_failure_description = "WWWWW"
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['STVASRC'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidAddressSource() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource
        //Test if the generated entity now has an id assigned
        assertNotNull addressSource.id
    }


    @Ignore
    void testCreateInvalidAddressSource() {
        def addressSource = newInvalidForCreateAddressSource()
        addressSource.code = null
        save addressSource
        shouldFail(ApplicationException) {
            addressSource.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidAddressSource() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource
        assertNotNull addressSource.id
        assertEquals 0L, addressSource.version
        assertEquals i_success_code, addressSource.code
        assertEquals i_success_description, addressSource.description

        //Update the entity
        addressSource.description = u_success_description
        save addressSource
        //Asset for sucessful update
        addressSource = AddressSource.get(addressSource.id)
        assertEquals 1L, addressSource?.version
        assertEquals u_success_description, addressSource.description
    }


    @Ignore
    void testUpdateInvalidAddressSource() {
        def addressSource = newInvalidForCreateAddressSource()
        addressSource.code = null
        save addressSource
        assertNotNull addressSource.id
        assertEquals 0L, addressSource.version
        assertEquals i_success_code, addressSource.code
        assertEquals i_success_description, addressSource.description

        //Update the entity with invalid values
        addressSource.code = null
        addressSource.description = u_failure_description
        shouldFail(ApplicationException) {
            addressSource.save(failOnError: true, flush: true)
        }
    }


    void testOptimisticLock() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVASRC set STVASRC_VERSION = 999 where STVASRC_SURROGATE_ID = ?", [addressSource.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        addressSource.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            addressSource.save(failOnError: true, flush: true)
        }
    }


    void testDeleteAddressSource() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource
        def id = addressSource.id
        assertNotNull id
        addressSource.delete()
        assertNull AddressSource.get(id)
    }


    @Ignore
    void testValidation() {
        def addressSource = newInvalidForCreateAddressSource()
        addressSource.code = null
        assertTrue "AddressSource could not be validated as expected due to ${addressSource.errors}", addressSource.validate()
    }


    void testNullValidationFailure() {
        def addressSource = new AddressSource()
        assertFalse "AddressSource should have failed validation", addressSource.validate()
        assertErrorsFor addressSource, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    void testValidationMessages() {
        def addressSource = newInvalidForCreateAddressSource()
        addressSource.code = null
        assertFalse addressSource.validate()
        assertLocalizedError addressSource, 'nullable', /.*Field.*code.*of class.*AddressSource.*cannot be null.*/, 'code'
        addressSource.description = null
        assertFalse addressSource.validate()
        assertLocalizedError addressSource, 'nullable', /.*Field.*description.*of class.*AddressSource.*cannot be null.*/, 'description'
    }


    private def newValidForCreateAddressSource() {
        def addressSource = new AddressSource(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return addressSource
    }


    private def newInvalidForCreateAddressSource() {
        def addressSource = new AddressSource(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return addressSource
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(addresssource_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
