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

class SiteIntegrationTests extends BaseIntegrationTestCase {

    def siteService


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
    void testCreateSite() {
        def site = newSite()
        save site
        //Test if the generated entity now has an id assigned
        assertNotNull site.id
    }


	@Test
    void testUpdateSite() {
        def site = newSite()
        save site

        assertNotNull site.id
        assertEquals 0L, site.version
        assertEquals "TTT", site.code
        assertEquals "TTTTT", site.description
        assertEquals "TTTTT", site.streetAddress1
        assertEquals "TTTTT", site.streetAddress2
        assertEquals "TTTTT", site.streetAddress3
        assertEquals "TTTTT", site.city
        assertEquals "TTT", site.state
        assertEquals "TTTTT", site.foreignCountry
        assertEquals "TTTTT", site.zip
        assertEquals "TTTTT", site.streetAddress4
        assertEquals "TTTTT", site.houseNumber

        //Update the entity
        def testDate = new Date()
        site.code = "UUU"
        site.description = "UUUUU"
        site.streetAddress1 = "UUUUU"
        site.streetAddress2 = "UUUUU"
        site.streetAddress3 = "UUUUU"
        site.city = "UUUUU"
        site.state = "UUU"
        site.foreignCountry = "UUUUU"
        site.zip = "UUUUU"
        site.streetAddress4 = "UUUUU"
        site.houseNumber = "UUUUU"
        site.lastModified = testDate
        site.lastModifiedBy = "test"
        site.dataOrigin = "Banner"
        save site

        site = Site.get(site.id)
        assertEquals 1L, site?.version
        assertEquals "UUU", site.code
        assertEquals "UUUUU", site.description
        assertEquals "UUUUU", site.streetAddress1
        assertEquals "UUUUU", site.streetAddress2
        assertEquals "UUUUU", site.streetAddress3
        assertEquals "UUUUU", site.city
        assertEquals "UUU", site.state
        assertEquals "UUUUU", site.foreignCountry
        assertEquals "UUUUU", site.zip
        assertEquals "UUUUU", site.streetAddress4
        assertEquals "UUUUU", site.houseNumber
    }


	@Test
    void testOptimisticLock() {
        def site = newSite()
        save site

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVSITE set STVSITE_VERSION = 999 where STVSITE_SURROGATE_ID = ?", [site.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        site.code = "UUU"
        site.description = "UUUUU"
        site.streetAddress1 = "UUUUU"
        site.streetAddress2 = "UUUUU"
        site.streetAddress3 = "UUUUU"
        site.city = "UUUUU"
        site.state = "UUU"
        site.foreignCountry = "UUUUU"
        site.zip = "UUUUU"
        site.streetAddress4 = "UUUUU"
        site.houseNumber = "UUUUU"
        site.lastModified = new Date()
        site.lastModifiedBy = "test"
        site.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            site.save(flush: true, failOnError: true)
        }
    }


	@Test
    void testDeleteSite() {
        def site = newSite()
        save site
        def id = site.id
        assertNotNull id
        site.delete()
        assertNull Site.get(id)
    }


	@Test
    void testValidation() {
        def site = newSite()
        assertTrue "Site could not be validated as expected due to ${site.errors}", site.validate()
    }


	@Test
    void testNullValidationFailure() {
        def site = new Site()
        assertFalse "Site should have failed validation", site.validate()
        assertErrorsFor site, 'nullable',
                [
                        'code',

                ]
        assertNoErrorsFor site,
                [
                        'description',
                        'streetAddress1',
                        'streetAddress2',
                        'streetAddress3',
                        'city',
                        'state',
                        'foreignCountry',
                        'zip',
                        'streetAddress4',
                        'houseNumber',

                        'nation'
                ]
    }


	@Test
    void testMaxSizeValidationFailures() {
        def site = new Site(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetAddress1: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetAddress2: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetAddress3: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                city: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                state: 'XXXXX',
                foreignCountry: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                zip: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetAddress4: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                houseNumber: 'XXXXXXXXXXXX')
        assertFalse "Site should have failed validation", site.validate()
        assertErrorsFor site, 'maxSize', ['description', 'streetAddress1', 'streetAddress2', 'streetAddress3', 'city', 'state', 'foreignCountry', 'zip', 'streetAddress4', 'houseNumber']
    }



    private def newSite() {
        def ination = Nation.findWhere(code: "") //TODO: fill in this code

        def site = new Site(
                code: "TTT",
                description: "TTTTT",
                streetAddress1: "TTTTT",
                streetAddress2: "TTTTT",
                streetAddress3: "TTTTT",
                city: "TTTTT",
                state: "TTT",
                foreignCountry: "TTTTT",
                zip: "TTTTT",
                streetAddress4: "TTTTT",
                houseNumber: "TTTTT",
                nation: ination,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return site
    }

}
