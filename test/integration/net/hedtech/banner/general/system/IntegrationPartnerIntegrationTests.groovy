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
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class IntegrationPartnerIntegrationTests extends BaseIntegrationTestCase {

    def integrationPartnerService


    protected void setUp() {
        formContext = ['GTVINTP'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateIntegrationPartner() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner
        //Test if the generated entity now has an id assigned
        assertNotNull integrationPartner.id
    }


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
            integrationPartner.save(flush:true, failOnError:true)
        }
    }


    void testDeleteIntegrationPartner() {
        def integrationPartner = newIntegrationPartner()
        save integrationPartner
        def id = integrationPartner.id
        assertNotNull id
        integrationPartner.delete()
        assertNull IntegrationPartner.get(id)
    }


    void testValidation() {
        def integrationPartner = newIntegrationPartner()
        assertTrue "IntegrationPartner could not be validated as expected due to ${integrationPartner.errors}", integrationPartner.validate()
    }


    void testNullValidationFailure() {
        def integrationPartner = new IntegrationPartner()
        assertFalse "IntegrationPartner should have failed validation", integrationPartner.validate()
        assertErrorsFor integrationPartner, 'nullable', ['code', 'description']
    }


    void testMaxSizeValidationFailures() {
        def integrationPartner = new IntegrationPartner(
                code: 'XXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "IntegrationPartner should have failed validation", integrationPartner.validate()
        assertErrorsFor integrationPartner, 'maxSize', ['code', 'description']
    }


    void testValidationMessages() {

    }


    private def newIntegrationPartner() {
        new IntegrationPartner(code: "TTTTT", description: "TTTTT", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(integrationpartner_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
