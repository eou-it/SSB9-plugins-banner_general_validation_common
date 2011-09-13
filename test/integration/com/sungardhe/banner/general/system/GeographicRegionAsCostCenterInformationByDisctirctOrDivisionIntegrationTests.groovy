/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

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
