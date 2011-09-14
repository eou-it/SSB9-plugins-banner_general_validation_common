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
 Generated: Fri Apr 01 15:12:08 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


class SystemIndicatorIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(systemindicator_domain_integration_test_data) ENABLED START*/
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
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['GTVSYSI'] // Since we are not testing a controller, we need to explicitly set this
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
        def systemIndicator = new SystemIndicator(code:u_failure_code, description: u_failure_description)
		assertFalse "SystemIndicator should have failed validation", systemIndicator.validate()
		assertErrorsFor systemIndicator, 'maxSize', [ 'code', 'description' ]
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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(systemindicator_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
