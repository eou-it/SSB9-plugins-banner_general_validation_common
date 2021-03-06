/** *****************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration tests for the Campus model.  
 * */
class CampusIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTT"
    def i_success_description = "TTTTT"

    def campusService

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
    void testCreateCampus() {
        def campus = newValidForCreateCampus()

        assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
        assertNotNull campus.id
    }


    @Test
    void testUpdateCampus() {
        def campus = newValidForCreateCampus()

        save campus
        def id = campus.id
        def version = campus.version
        assertNotNull id
        assertEquals 0L, version

        campus.description = "updated"
        save campus

        def found = Campus.get(id)
        assertNotNull "found must not be null", found
        assertEquals "updated", found.description
        assertEquals 1L, found.version
    }

    @Test
    void testUpdateUtcCampus() {
        def campus = newValidForCreateCampus()

        save campus
        def id = campus.id
        def version = campus.version
        assertNotNull id
        assertEquals 0L, version
        assertEquals "+09:00", campus.utcOffset

        campus.utcOffset = "-04:00"
        save campus

        def found = Campus.get(id)
        assertNotNull "found must not be null", found
        assertEquals "-04:00", found.utcOffset
        assertEquals 1L, found.version
    }



    @Test
    void testDeleteCampus() {
        def campus = newValidForCreateCampus()

        assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
        def id = campus.id
        assertNotNull id
        campus.delete()
        assertNull(Campus.get(id))
    }

    @Test
    void testOptimisticLock() {
        def campus = newValidForCreateCampus()
        save campus

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCAMP set STVCAMP_VERSION = 999 where STVCAMP_SURROGATE_ID = ?", [campus.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        campus.description = "Updating description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            campus.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testValidation() {
        def campus = newValidForCreateCampus()
        assertTrue "Campus could not be validated as expected due to ${campus.errors}", campus.validate()
    }

    @Test
    void testNullValidationFailure() {
        def campus = new Campus()
        assertFalse "Campus should have failed validation", campus.validate()
        assertErrorsFor campus, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor campus,
                [
                        'description',
                        'geographicRegionAsCostCenterInformationByDisctirctOrDivision'
                ]
    }

    @Test
    void testMaxSizeValidationFailures() {
        def campus = new Campus(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "Campus should have failed validation", campus.validate()
        assertErrorsFor campus, 'maxSize', ['description']
    }

    @Test
    void testFetchCampusCodeNotInListWithNoCampusCode() {
        List campusList = []
        try {
            //If campusList is empty the in statement will be incomplete and an exception will should occur.
            List campusReturnList = Campus.fetchCampusCodesNotInList(campusList)
            fail "Fetch should fail as the query is incomplete"
        } catch(Exception e) {
            //Hibernate Query exception should occur.
        }
    }

    @Test
    void testFetchCampusCodeNotInListWithWrongCampusCode() {
        List campusList = ['DOES_NOT_EXIST']
        /*
            If campus code is invalid then all the campus codes will be returned as the in statement returns
            all excluding the ones passed.
        */

        List campusReturnList = Campus.fetchCampusCodesNotInList(campusList)

        List campusDomains = Campus.findAll()

        assertNotNull campusReturnList
        assertNotNull campusDomains
        assertTrue campusDomains.size() == campusReturnList.size()
    }

    @Test
    void testFetchCampusCodeNotInListWithSingleCampusCode() {
        List campusList = ['M']

        assertNotNull Campus.findByCode('M')
        List campusReturnList = Campus.fetchCampusCodesNotInList(campusList)

        List campusDomains = Campus.findAll()

        assertNotNull campusReturnList
        assertNotNull campusDomains
        assertTrue campusDomains.size() == (campusReturnList.size() + campusList.size())

        assertNull campusReturnList.find { it == "M" }
    }

    @Test
    void testFetchCampusCodeNotInListWithMultipleCampusCode() {
        List campusList = ['M', 'A', 'C']
        List campusReturnList = Campus.fetchCampusCodesNotInList(campusList)

        List campusDomains = Campus.findAll()

        assertNotNull campusReturnList
        assertNotNull campusDomains
        assertTrue campusDomains.size() == (campusReturnList.size() + campusList.size())

        assertNull campusReturnList.find { it == "M" }
        assertNull campusReturnList.find { it == "A" }
        assertNull campusReturnList.find { it == "C" }
    }



    private def newValidForCreateCampus() {
        def campus = new Campus(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner",
                utcOffset: "+09:00"
        )
        return campus
    }

}
