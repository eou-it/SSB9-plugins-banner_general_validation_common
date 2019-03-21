/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the college model.
 * */
class CollegeIntegrationTests extends BaseIntegrationTestCase {

    def collegeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreateCollege() {
        def entity = newCollege()
        save entity

        assertNotNull entity.id
    }


    @Test
    void testUpdateCollege() {
        def entity = newCollege()
        save entity

        assertNotNull entity.id
        assertEquals(0L, entity.version)
        assertEquals('TT', entity.description)

        entity.description = "Updated TT"
        save entity

        entity = College.get(entity.id)
        assertEquals new Long(1), entity?.version
        assertEquals "Updated TT", entity?.description
    }


    @Test
    void testOptimisticLock() {
        def entity = newCollege()
        save entity

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCOLL set STVCOLL_VERSION = 999 where STVCOLL_SURROGATE_ID = ?", [entity.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        entity.description = "This better fail"
        shouldFail(HibernateOptimisticLockingFailureException) {
            entity.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteCollege() {
        def college = newCollege()
        save college

        def id = college.id
        assertNotNull id
        college.delete()
        assertNull College.get(id)
    }


    @Test
    void testValidation() {
        def college = newCollege()
        assertTrue "College could not be validated as expected due to ${college.errors}", college.validate()
    }


    @Test
    void testNullValidationFailure() {
        def college = new College()
        assertFalse "College should have failed validation", college.validate()
        assertErrorsFor(college, 'nullable', ['code', 'description'])
        assertNoErrorsFor(college, ['addressStreetLine1', 'addressStreetLine2', 'addressStreetLine3', 'addressStreetLine4',
                'addressCity', 'addressState', 'addressCountry', 'addressZipCode', 'systemRequiredIndicator', 'voiceResponseMessageNumber',
                'statisticsCanadianInstitution', 'districtDivision', 'houseNumber'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def college = new College(code: "TTXX", description: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                addressStreetLine1: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                addressStreetLine2: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                addressStreetLine3: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                addressCity: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                addressState: "TTX", addressCountry: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", addressZipCode: "TTXXXXXXXXXXXXXXXXX",
                systemRequiredIndicator: "N", statisticsCanadianInstitution: "TTXXXXX",
                districtDivision: "XXXXXXXXXX", houseNumber: "TTXXXXXXXXX",
                addressStreetLine4: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")

        assertFalse "College should have failed validation", college.validate()
        assertErrorsFor(college, 'maxSize', ['code', 'description', 'addressStreetLine1', 'addressStreetLine2', 'addressStreetLine3',
                'addressStreetLine4', 'addressCity', 'addressState', 'addressCountry', 'addressZipCode',
                'statisticsCanadianInstitution', 'districtDivision', 'houseNumber'])
    }




    @Test
    void testFetchBySomeAttribute() {
        def college = College.fetchBySomeAttribute()
        def collegeList = college.get("list")

        def collegeObj1 = College.findWhere(code: "AG")
        def collegeObj2 = College.findWhere(code: "BU")
        def collegeObj3 = College.findWhere(code: "CE")
        def collegeObj4 = College.findWhere(code: "DN")
        def collegeObj5 = newCollege()

        assertTrue collegeList.contains(collegeObj1)
        assertTrue collegeList.contains(collegeObj2)
        assertTrue collegeList.contains(collegeObj3)
        assertTrue collegeList.contains(collegeObj4)
        assertFalse collegeList.contains(collegeObj5)

        def filter = "E"
        college = College.fetchBySomeAttribute(filter)
        collegeList = college.get("list")

        collegeObj1 = College.findWhere(code: "ED")
        collegeObj2 = College.findWhere(code: "EN")
        collegeObj3 = College.findWhere(code: "ES")
        collegeObj4 = College.findWhere(code: "ET")
        collegeObj5 = College.findWhere(code: "FD")

        assertTrue collegeList.contains(collegeObj1)
        assertTrue collegeList.contains(collegeObj2)
        assertTrue collegeList.contains(collegeObj3)
        assertTrue collegeList.contains(collegeObj4)
        assertFalse collegeList.contains(collegeObj5)

    }


    private def newCollege() {
        new College(code: "TT", description: "TT", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
                addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, statisticsCanadianInstitution: "TT",
                districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
                lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon') // audit trail normally set in service
    }
}
