/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class GeographicRegionAsCostCenterInformationByDisctirctOrDivisionIntegrationTests extends BaseIntegrationTestCase {

    def geographicRegionAsCostCenterInformationByDisctirctOrDivisionService


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
    void testCreateGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision
        //Test if the generated entity now has an id assigned
        assertNotNull geographicRegionAsCostCenterInformationByDisctirctOrDivision.id
    }


    @Test
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


    @Test
    void testOptimisticLock() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVDICD set GTVDICD_VERSION = 999 where GTVDICD_SURROGATE_ID = ?", [geographicRegionAsCostCenterInformationByDisctirctOrDivision.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.code = "UUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.description = "UUUUU"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModified = new Date()
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.lastModifiedBy = "test"
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            geographicRegionAsCostCenterInformationByDisctirctOrDivision.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteGeographicRegionAsCostCenterInformationByDisctirctOrDivision() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        save geographicRegionAsCostCenterInformationByDisctirctOrDivision
        def id = geographicRegionAsCostCenterInformationByDisctirctOrDivision.id
        assertNotNull id
        geographicRegionAsCostCenterInformationByDisctirctOrDivision.delete()
        assertNull GeographicRegionAsCostCenterInformationByDisctirctOrDivision.get(id)
    }


    @Test
    void testValidation() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = newGeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        assertTrue "GeographicRegionAsCostCenterInformationByDisctirctOrDivision could not be validated as expected due to ${geographicRegionAsCostCenterInformationByDisctirctOrDivision.errors}", geographicRegionAsCostCenterInformationByDisctirctOrDivision.validate()
    }


    @Test
    void testNullValidationFailure() {
        def geographicRegionAsCostCenterInformationByDisctirctOrDivision = new GeographicRegionAsCostCenterInformationByDisctirctOrDivision()
        assertFalse "GeographicRegionAsCostCenterInformationByDisctirctOrDivision should have failed validation", geographicRegionAsCostCenterInformationByDisctirctOrDivision.validate()
        assertErrorsFor geographicRegionAsCostCenterInformationByDisctirctOrDivision, 'nullable',
                [
                        'code',
                        'description',

                ]
    }


    @Test
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


}
