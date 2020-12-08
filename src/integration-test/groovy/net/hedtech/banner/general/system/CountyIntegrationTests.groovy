/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
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
class CountyIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "270"
    def i_success_description = "Otsego County"

    //Invalid test data (For failure tests)
    def i_failure_code = "INVALID"
    def i_failure_description = "Albany County"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_code = "270"
    def u_success_description = "Otsego"

    //Valid test data (For failure tests)
    def u_failure_code = "270"
    def u_failure_description = "Otsego County Description Too Long for Field"

    final String COUNTY_ISO_CODE_GB_BAS = 'GB-BAS'
    final String COUNTY_ISO_CODE_GB_CAM = 'GB-CAM'


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
    void testCreateValidCounty() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull county.id
    }


    @Test
    void testCreateValidCountyWithIsoCode() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull county.id
        assertNotNull county.isoCode
        assertTrue COUNTY_ISO_CODE_GB_BAS.equalsIgnoreCase(county.isoCode)
    }


    @Test
    void testCreateInvalidCounty() {
        def county = newInvalidForCreateCounty()
        shouldFail {
            county.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidCounty() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)
        assertNotNull county.id
        assertEquals 0L, county.version
        assertEquals i_success_code, county.code
        assertEquals i_success_description, county.description
        assertNotNull county.isoCode
        assertTrue COUNTY_ISO_CODE_GB_BAS.equalsIgnoreCase(county.isoCode)

        //Update the entity
        county.description = u_success_description
        county.isoCode = COUNTY_ISO_CODE_GB_CAM
        county.save(failOnError: true, flush: true)
        //Assert for successful update
        county = County.get(county.id)
        assertEquals 1L, county?.version
        assertEquals u_success_description, county.description
        assertNotNull county.isoCode
        assertTrue COUNTY_ISO_CODE_GB_CAM.equalsIgnoreCase(county.isoCode)
    }


    @Test
    void testUpdateInvalidCounty() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)
        assertNotNull county.id
        assertEquals 0L, county.version
        assertEquals i_success_code, county.code
        assertEquals i_success_description, county.description

        //Update the entity with invalid values
        county.description = u_failure_description
        shouldFail {
            county.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCNTY set STVCNTY_VERSION = 999 where STVCNTY_SURROGATE_ID = ?", [county.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        county.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            county.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteCounty() {
        def county = newValidForCreateCounty()
        county.save(failOnError: true, flush: true)
        def id = county.id
        assertNotNull id
        county.delete()
        assertNull County.get(id)
    }


    @Test
    void testValidation() {
        def county = newValidForCreateCounty()
        assertTrue "County could not be validated as expected due to ${county.errors}", county.validate()
    }


    @Test
    void testNullValidationFailure() {
        def county = new County()
        assertFalse "County should have failed validation", county.validate()
        assertErrorsFor county, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor county,
                [
                        'description'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def county = new County(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "County should have failed validation", county.validate()
        assertErrorsFor county, 'maxSize', ['description']
    }



    private def newValidForCreateCounty() {
        def county = new County(
                code: i_success_code,
                description: i_success_description,
                isoCode: COUNTY_ISO_CODE_GB_BAS,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return county
    }


    private def newInvalidForCreateCounty() {
        def county = new County(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return county
    }

    @Test
    void testfetchAllCodes() {
        def countryList = County.fetchAllCodes()
        assertTrue countryList.size() > 0
    }

}
