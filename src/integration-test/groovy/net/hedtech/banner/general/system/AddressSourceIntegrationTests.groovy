/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class AddressSourceIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateValidAddressSource() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource
        //Test if the generated entity now has an id assigned
        assertNotNull addressSource.id
    }


    @Test
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


    @Test
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


    @Test
    void testDeleteAddressSource() {
        def addressSource = newValidForCreateAddressSource()
        save addressSource
        def id = addressSource.id
        assertNotNull id
        addressSource.delete()
        assertNull AddressSource.get(id)
    }



    @Test
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


}
