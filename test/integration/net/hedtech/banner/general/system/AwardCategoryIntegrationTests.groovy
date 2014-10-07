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

class AwardCategoryIntegrationTests extends BaseIntegrationTestCase {

    def awardCategoryService


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
    void testCreateAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory
        //Test if the generated entity now has an id assigned
        assertNotNull awardCategory.id
    }


    @Test
    void testUpdateAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory

        assertNotNull awardCategory.id
        groovy.util.GroovyTestCase.assertEquals(0L, awardCategory.version)
        assertEquals("TT", awardCategory.code)
        assertEquals("TTTTT", awardCategory.description)
        assertEquals("T", awardCategory.systemRequiredIndicator)

        //Update the entity
        awardCategory.code = "UU"
        awardCategory.description = "UUUUU"
        awardCategory.systemRequiredIndicator = "U"
        awardCategory.lastModified = new Date()
        awardCategory.lastModifiedBy = "test"
        awardCategory.dataOrigin = "Banner"

        save awardCategory

        awardCategory = AwardCategory.get(awardCategory.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), awardCategory?.version
        assertEquals("UU", awardCategory.code)
        assertEquals("UUUUU", awardCategory.description)
        assertEquals("U", awardCategory.systemRequiredIndicator)

    }


    @Test
    void testOptimisticLock() {
        def awardCategory = newAwardCategory()
        save awardCategory

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVACAT set STVACAT_VERSION = 999 where STVACAT_SURROGATE_ID = ?", [awardCategory.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        awardCategory.code = "UU"
        awardCategory.description = "UUUUU"
        awardCategory.systemRequiredIndicator = "U"
        awardCategory.lastModified = new Date()
        awardCategory.lastModifiedBy = "test"
        awardCategory.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            awardCategory.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory
        def id = awardCategory.id
        assertNotNull id
        awardCategory.delete()
        assertNull AwardCategory.get(id)
    }


    @Test
    void testValidation() {
        def awardCategory = newAwardCategory()
        assertTrue "AwardCategory could not be validated as expected due to ${awardCategory.errors}", awardCategory.validate()
    }


    @Test
    void testNullValidationFailure() {
        def awardCategory = new AwardCategory()
        assertFalse "AwardCategory should have failed validation", awardCategory.validate()
        assertErrorsFor(awardCategory, 'nullable', ['code', 'description'])
        assertNoErrorsFor(awardCategory, ['systemRequiredIndicator'])
    }


    @Test
    void testMaxSizeValidationFailures() {
        def awardCategory = new AwardCategory(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "AwardCategory should have failed validation", awardCategory.validate()
        assertErrorsFor(awardCategory, 'maxSize', ['code', 'description'])
    }


    private def newAwardCategory() {
        new AwardCategory(code: "TT", description: "TTTTT", systemRequiredIndicator: "T", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }


}
