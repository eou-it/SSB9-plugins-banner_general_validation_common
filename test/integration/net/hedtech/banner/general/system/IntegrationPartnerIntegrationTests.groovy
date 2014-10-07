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

class IntegrationPartnerIntegrationTests extends BaseIntegrationTestCase {

    def integrationPartnerService


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
    void testCreateIntegrationPartner() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner
        //Test if the generated entity now has an id assigned
        assertNotNull integrationPartner.id
    }


    @Test
    void testUpdateIntegrationPartner() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner

        assertNotNull integrationPartner.id
        assertEquals 0L, integrationPartner.version
        assertEquals "TTTTT", integrationPartner.code
        assertEquals "TTTTT", integrationPartner.description

        //Update the entity
        integrationPartner.code = "UUUUU"
        integrationPartner.description = "UUUUU"
        integrationPartner.lastModified = new Date()
        integrationPartner.lastModifiedBy = "test"
        integrationPartner.dataOrigin = "Banner"
        save integrationPartner

        integrationPartner = IntegrationPartner.get(integrationPartner.id)
        assertEquals 1L, integrationPartner?.version
        assertEquals "UUUUU", integrationPartner.code
        assertEquals "UUUUU", integrationPartner.description

    }


    @Test
    void testOptimisticLock() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVINTP set GTVINTP_VERSION = 999 where GTVINTP_SURROGATE_ID = ?", [integrationPartner.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

        //Try to update the entity
        integrationPartner.code = "UUUUU"
        integrationPartner.description = "UUUUU"
        integrationPartner.lastModified = new Date()
        integrationPartner.lastModifiedBy = "test"
        integrationPartner.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            integrationPartner.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteIntegrationPartner() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner
        def id = integrationPartner.id
        assertNotNull id
        integrationPartner.delete()
        assertNull IntegrationPartner.get(id)
    }


    @Test
    void testValidation() {
        def integrationPartner = newIntegrationPartner()
        assertTrue "IntegrationPartner could not be validated as expected due to ${integrationPartner.errors}", integrationPartner.validate()
    }


    @Test
    void testNullValidationFailure() {
        def integrationPartner = new IntegrationPartner()
        assertFalse "IntegrationPartner should have failed validation", integrationPartner.validate()
        assertErrorsFor integrationPartner, 'nullable', ['code', 'description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def integrationPartner = new IntegrationPartner(
                code: 'XXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "IntegrationPartner should have failed validation", integrationPartner.validate()
        assertErrorsFor integrationPartner, 'maxSize', ['code', 'description']
    }


    private def newIntegrationPartner() {
        new IntegrationPartner(code: "TTTTT", description: "TTTTT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
