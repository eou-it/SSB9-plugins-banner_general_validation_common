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


class OriginatorIntegrationTests extends BaseIntegrationTestCase {

    def originatorService

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
    void testCreateOriginator() {
        def originator = newOriginator()
        save originator
        //Test if the generated entity now has an id assigned
        assertNotNull originator.id
    }

    @Test
    void testUpdateOriginator() {
        def originator = newOriginator()
        save originator

        assertNotNull originator.id
        assertEquals 0L, originator.version
        assertEquals "TTTT", originator.code
        assertEquals "TTTTT", originator.description

        //Update the entity
        def testDate = new Date()
        originator.code = "UUUU"
        originator.description = "UUUUU"
        originator.lastModified = testDate
        originator.lastModifiedBy = "test"
        originator.dataOrigin = "Banner"
        save originator

        originator = Originator.get(originator.id)
        assertEquals 1L, originator?.version
        assertEquals "UUUU", originator.code
        assertEquals "UUUUU", originator.description
    }

    @Test
    void testOptimisticLock() {
        def originator = newOriginator()
        save originator

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVORIG set STVORIG_VERSION = 999 where STVORIG_SURROGATE_ID = ?", [originator.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        originator.code = "UUUU"
        originator.description = "UUUUU"
        originator.lastModified = new Date()
        originator.lastModifiedBy = "test"
        originator.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            originator.save(flush: true)
        }
    }

    @Test
    void testDeleteOriginator() {
        def originator = newOriginator()
        save originator
        def id = originator.id
        assertNotNull id
        originator.delete()
        assertNull Originator.get(id)
    }

    @Test
    void testValidation() {
        def originator = newOriginator()
        assertTrue "Originator could not be validated as expected due to ${originator.errors}", originator.validate()
    }

    @Test
    void testNullValidationFailure() {
        def originator = new Originator()
        assertFalse "Originator should have failed validation", originator.validate()
        assertErrorsFor originator, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor originator,
                [
                        'description'
                ]
    }

    @Test
    void testMaxSizeValidationFailures() {
        def originator = new Originator(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "Originator should have failed validation", originator.validate()
        assertErrorsFor originator, 'maxSize', ['description']
    }


    private def newOriginator() {

        def originator = new Originator(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return originator
    }

    /**
     * A test to exercise 'code' primary key in HoldType
     */
    @Test
    void testPrimaryKeyOnCode() {
        def originator = newOriginator()
        def duplicateObj = newOriginator()

        save originator
        assertNotNull originator.id

        assertEquals originator.code, duplicateObj.code

        shouldFail() {
            save duplicateObj
        }
    }

    /**
     * A test to exercise the findBy method for Originator
     */
    @Test
    void testFindOriginator() {
        def originator = newOriginator()
        save originator

        def originator2 = Originator.findByCode("TTTT")
        assertNotNull originator2
        assertEquals originator2.code, "TTTT"
    }

}
