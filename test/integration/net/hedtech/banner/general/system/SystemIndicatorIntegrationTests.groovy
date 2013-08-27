/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class SystemIndicatorIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = null
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "XX"
    def u_success_description = "XXXXXX"
    //Valid test data (For failure tests)

    def u_failure_code = "1234567890"
    def u_failure_description = "1234567890123456789012345678901234567890"



    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }



    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidSystemIndicator() {
        def systemIndicator = newValidForCreateSystemIndicator()
        save systemIndicator
        //Test if the generated entity now has an id assigned
        assertNotNull systemIndicator.id
    }


    void testUpdateValidSystemIndicator() {
        def systemIndicator = newValidForCreateSystemIndicator()
        save systemIndicator
        assertNotNull systemIndicator.id
        assertEquals 0L, systemIndicator.version
        assertEquals i_success_code, systemIndicator.code
        assertEquals i_success_description, systemIndicator.description

        //Update the entity
        systemIndicator.description = u_success_description
        save systemIndicator
        //Asset for sucessful update
        systemIndicator = SystemIndicator.get(systemIndicator.id)
        assertEquals 1L, systemIndicator?.version
        assertEquals u_success_description, systemIndicator.description
    }


    void testOptimisticLock() {
        def systemIndicator = newValidForCreateSystemIndicator()
        save systemIndicator

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSYSI set GTVSYSI_VERSION = 999 where GTVSYSI_SURROGATE_ID = ?", [systemIndicator.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        systemIndicator.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            systemIndicator.save(failOnError: true, flush: true)
        }
    }


    void testDeleteSystemIndicator() {
        def systemIndicator = newValidForCreateSystemIndicator()
        save systemIndicator
        def id = systemIndicator.id
        assertNotNull id
        systemIndicator.delete()
        assertNull SystemIndicator.get(id)
    }



    void testNullValidationFailure() {
        def systemIndicator = new SystemIndicator()
        assertFalse "SystemIndicator should have failed validation", systemIndicator.validate()
        assertErrorsFor systemIndicator, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    void testMaxSizeValidationFailures() {
        def systemIndicator = new SystemIndicator(code: u_failure_code, description: u_failure_description)
        assertFalse "SystemIndicator should have failed validation", systemIndicator.validate()
        assertErrorsFor systemIndicator, 'maxSize', ['code', 'description']
    }


    private def newValidForCreateSystemIndicator() {
        def systemIndicator = new SystemIndicator(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return systemIndicator
    }


    private def newInvalidForCreateSystemIndicator() {
        def systemIndicator = new SystemIndicator(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return systemIndicator
    }

}
