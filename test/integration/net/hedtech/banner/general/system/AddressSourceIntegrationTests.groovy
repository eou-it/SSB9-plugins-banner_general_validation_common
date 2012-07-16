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
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:44:46 EDT 2011
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
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



    void testNullValidationFailure() {
        def addressSource = new AddressSource()
        assertFalse "AddressSource should have failed validation", addressSource.validate()
        assertErrorsFor addressSource, 'nullable',
                [
                        'code',
                        'description'
                ]
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
