/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class ReligionIntegrationTests extends BaseIntegrationTestCase {


    private String i_failure_religion = 'HINDU'
    private String i_success_description = 'Protestant'
    private String i_success_code = 'PR'


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateReligion() {
        def religion = newReligion()
        religion.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull religion.id
        assertNotNull religion.id
        assertEquals 0L, religion.version
        assertEquals "TT", religion.code
        assertEquals "TTTTTTTTTT", religion.description
    }


    @Test
    void testUpdateReligion() {
        def religion = newReligion()
        religion.save(failOnError: true, flush: true)
        assertNotNull religion.id
        assertEquals 0L, religion.version
        assertEquals "TT", religion.code
        assertEquals "TTTTTTTTTT", religion.description

        //Update the entity
        religion.description = "UUUUUUUUUU"
        religion.save(failOnError: true, flush: true)
        //Assert for successful update
        religion = Religion.get(religion.id)
        assertEquals 1L, religion?.version
        assertEquals "UUUUUUUUUU", religion.description
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def religion = newReligion()

        religion.save(flush: true, failOnError: true)
        religion.refresh()
        assertNotNull "ReligionDecorator should have been saved", religion.id

        // test date values -
        assertEquals date.format(today), date.format(religion.lastModified)
        assertEquals hour.format(today), hour.format(religion.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def religion = newReligion()
        religion.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVRELG set STVRELG_VERSION = 999 where STVRELG_SURROGATE_ID = ?", [religion.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        religion.description = "UUUUUUUUUU"
        shouldFail(HibernateOptimisticLockingFailureException) {
            religion.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteReligion() {
        def religion = newReligion()
        religion.save(failOnError: true, flush: true)
        def id = religion.id
        assertNotNull id
        religion.delete()
        assertNull Religion.get(id)
    }


    @Test
    void testValidation() {
        def religion = new Religion()
        assertFalse "ReligionDecorator could not be validated as expected due to ${religion.errors}", religion.validate()
    }


    @Test
    void testNullValidationFailure() {
        def religion = new Religion()
        assertFalse "ReligionDecorator should have failed validation", religion.validate()
        assertErrorsFor religion, 'nullable', ['code']
        assertNoErrorsFor religion, ['description']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def religion = new Religion(
                code: "TTTTT",
                description: 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT')

        assertFalse "ReligionDecorator should have failed validation", religion.validate()
        assertErrorsFor religion, 'maxSize', ['code', 'description']
    }


    private def newReligion() {
        def religion = new Religion(
                code: "TT",
                description: "TTTTTTTTTT"
        )
        return religion
    }
/**
 * This test case is checking for Religion list
 */
    @Test
    void testList() {
        def params = [max: '500', offset: '0']
        List religionList= Religion.list(params)
        assertNotNull religionList
        assertFalse religionList.isEmpty()
        assertTrue religionList.description.contains(i_success_description)
        assertTrue religionList.code.contains(i_success_code)
        assertFalse religionList.code.contains(i_failure_religion)
    }

}
