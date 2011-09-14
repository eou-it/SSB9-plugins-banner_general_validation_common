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

package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class GeographicRegionAsCostCenterInformationByDisctirctOrDivisionIntegrationTests extends BaseIntegrationTestCase {

    def geographicRegionAsCostCenterInformationByDisctirctOrDivisionService


    protected void setUp() {
        formContext = ['GTVDICD'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision
        //Test if the generated entity now has an id assigned
        assertNotNull geographicRegionAsCostCenterInformationByDisctirctOrDivision.id
    }


    void testUpdateGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision

        assertNotNull geographicRegionAsCostCenterInformationByDisctirctOrDivision.id
        assertEquals 0L, geographicRegionAsCostCenterInformationByDisctirctOrDivision.version
        assertEquals "TTT", geographicRegionAsCostCenterInformationByDisctirctOrDivision.code
        assertEquals "TTTTT", geographicRegionAsCostCenterInformationByDisctirctOrDivision.description

        //Update the entity
        def testDate = new Date()
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.code = "UUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.description = "UUUUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModified = testDate
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModifiedBy = "test"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.dataOrigin = "Banner"
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision

        geographicRegionAsCostCenterInformationByDisctirctOrDivision = GeographicRegionAsCostCenterInformationByDisctirctOrDivision.get(geographicRegionAsCostCenterInformationByDisctirctOrDivision.id)
        assertEquals 1L, geographicRegionAsCostCenterInformationByDisctirctOrDivision?.version
        assertEquals "UUU", geographicRegionAsCostCenterInformationByDisctirctOrDivision.code
        assertEquals "UUUUU", geographicRegionAsCostCenterInformationByDisctirctOrDivision.description
    }


    void testOptimisticLock() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVDICD set GTVDICD_VERSION = 999 where GTVDICD_SURROGATE_ID = ?", [geographicRegionAsCostCenterInformationByDisctirctOrDivision.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.code = "UUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.description = "UUUUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModified = new Date()
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModifiedBy = "test"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            geographicRegionAsCostCenterInformationByDisctirctOrDivision.save(flush:true, failOnError:true)
        }
    }


    void testDeleteGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision
        def id = geographicRegionAsCostCenterInformationByDisctirctOrDivision.id
        assertNotNull id
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.delete()
        assertNull GeographicRegionAsCostCenterInformationByDisctirctOrDivision.get(id)
    }


    void testValidation() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        assertTrue "GeographicRegionAsCostCenterInformationByDisctirctOrDivision could not be validated as expected due to ${geographicRegionAsCostCenterInformationByDisctirctOrDivision.errors}", geographicRegionAsCostCenterInformationByDisctirctOrDivision.validate()
    }


    void testNullValidationFailure() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = new GeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        assertFalse "GeographicRegionAsCostCenterInformationByDisctirctOrDivision should have failed validation", geographicRegionAsCostCenterInformationByDisctirctOrDivision.validate()
        assertErrorsFor geographicRegionAsCostCenterInformationByDisctirctOrDivision, 'nullable',
                [
                        'code',
                        'description',

                ]

    }


    void testMaxSizeValidationFailures() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = new GeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        assertFalse "GeographicRegionAsCostCenterInformationByDisctirctOrDivision should have failed validation", geographicRegionAsCostCenterInformationByDisctirctOrDivision.validate()
        assertErrorsFor geographicRegionAsCostCenterInformationByDisctirctOrDivision, 'maxSize', []
    }



    private def newGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {


        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = new GeographicRegionAsCostCenterInformationByDisctirctOrDivision(
                code: "TTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return geographicRegionAsCostCenterInformationByDisctirctOrDivision
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(geographicregionascostcenterinformationbydisctirctordivision_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
