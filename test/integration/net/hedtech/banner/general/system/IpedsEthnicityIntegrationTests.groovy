/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class IpedsEthnicityIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateIpedsEthnicity() {
        def ipedsEthnicity = newIpedsEthnicity()
        save ipedsEthnicity
        //Test if the generated entity now has an id assigned
        assertNotNull ipedsEthnicity.id
    }


    void testUpdateIpedsEthnicity() {
        def ipedsEthnicity = newIpedsEthnicity()
        save ipedsEthnicity

        assertNotNull ipedsEthnicity.id
        groovy.util.GroovyTestCase.assertEquals(0L, ipedsEthnicity.version)
        assertEquals("T", ipedsEthnicity.code)
        assertEquals("TTTTT", ipedsEthnicity.description)
        assertEquals("Y", ipedsEthnicity.systemRequiredIndicator)

        //Update the entity
        ipedsEthnicity.code = "U"
        ipedsEthnicity.description = "UUUUU"
        ipedsEthnicity.systemRequiredIndicator = null
        ipedsEthnicity.lastModified = new Date()
        ipedsEthnicity.lastModifiedBy = "test"
        ipedsEthnicity.dataOrigin = "Banner"

        save ipedsEthnicity

        ipedsEthnicity = IpedsEthnicity.get(ipedsEthnicity.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), ipedsEthnicity?.version
        assertEquals("U", ipedsEthnicity.code)
        assertEquals("UUUUU", ipedsEthnicity.description)
        assertNull(ipedsEthnicity.systemRequiredIndicator)

    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def personType = newIpedsEthnicity()

        personType.save(flush: true, failOnError: true)
        personType.refresh()
        assertNotNull "IpedsEthnicity should have been saved", personType.id

        // test date values -
        assertEquals date.format(today), date.format(personType.lastModified)
        assertEquals hour.format(today), hour.format(personType.lastModified)
    }


    void testOptimisticLock() {
        def ipedsEthnicity = newIpedsEthnicity()
        save ipedsEthnicity

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVETCT set STVETCT_VERSION = 999 where STVETCT_SURROGATE_ID = ?", [ipedsEthnicity.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        ipedsEthnicity.code = "U"
        ipedsEthnicity.description = "UUUUU"
        ipedsEthnicity.systemRequiredIndicator = "Y"
        ipedsEthnicity.lastModified = new Date()
        ipedsEthnicity.lastModifiedBy = "test"
        ipedsEthnicity.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            ipedsEthnicity.save(flush: true, failOnError: true)
        }
    }


    void testDeleteIpedsEthnicity() {
        def ipedsEthnicity = newIpedsEthnicity()
        save ipedsEthnicity
        def id = ipedsEthnicity.id
        assertNotNull id
        ipedsEthnicity.delete()
        assertNull IpedsEthnicity.get(id)
    }


    void testValidation() {
        def ipedsEthnicity = new IpedsEthnicity()
        assertFalse "IpedsEthnicity could not be validated as expected due to ${ipedsEthnicity.errors}", ipedsEthnicity.validate()
    }


    void testNullValidationFailure() {
        def ipedsEthnicity = new IpedsEthnicity()
        assertFalse "IpedsEthnicity should be valid", ipedsEthnicity.validate()
        assertErrorsFor(ipedsEthnicity, 'nullable', ['code', 'description'])
        assertNoErrorsFor(ipedsEthnicity, ['systemRequiredIndicator'])
    }


    void testMaxSizeValidationFailures() {
        def ipedsEthnicity = new IpedsEthnicity(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "IpedsEthnicity should have failed validation", ipedsEthnicity.validate()
        assertErrorsFor(ipedsEthnicity, 'maxSize', ['code', 'description'])
    }

    void testInListValidationFailure() {
        def ipedsEthnicity = newIpedsEthnicity()
        ipedsEthnicity.systemRequiredIndicator = "T"
        assertFalse "IpedsEthnicity should have failed validation", ipedsEthnicity.validate()
        assertErrorsFor(ipedsEthnicity, 'inList', ['systemRequiredIndicator'])
    }


    private def newIpedsEthnicity() {
        new IpedsEthnicity(code: "T", description: "TTTTT", systemRequiredIndicator: "Y", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

}
