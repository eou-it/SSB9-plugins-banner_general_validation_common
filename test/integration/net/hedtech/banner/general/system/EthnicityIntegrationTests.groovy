/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class EthnicityIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull ethnicity.id
    }


    @Test
    void testUpdateEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity.save(failOnError: true, flush: true)
        assertNotNull ethnicity.id
        assertEquals 0L, ethnicity.version
        assertEquals "TT", ethnicity.code
        assertEquals "TTTTTTTTTT", ethnicity.description
        assertEquals "T", ethnicity.ethnicCode
        assertEquals "T", ethnicity.electronicDataInterchangeEquivalent
        assertEquals "T", ethnicity.lmsEquivalent
        assertEquals "1", ethnicity.ethnic

        //Update the entity
        ethnicity.description = "UUUUUUUUUU"
        ethnicity.ethnicCode = "U"
        ethnicity.electronicDataInterchangeEquivalent = "U"
        ethnicity.lmsEquivalent = "U"
        ethnicity.ethnic = "2"
        ethnicity.race = Race.findByRace("MOA")
        ethnicity.ipedsEthnicity = IpedsEthnicity.findByCode("5")
        ethnicity.save(failOnError: true, flush: true)
        //Assert for successful update
        ethnicity = Ethnicity.get(ethnicity.id)
        assertEquals 1L, ethnicity?.version
        assertEquals "UUUUUUUUUU", ethnicity.description
        assertEquals "U", ethnicity.ethnicCode
        assertEquals "U", ethnicity.electronicDataInterchangeEquivalent
        assertEquals "U", ethnicity.lmsEquivalent
        assertEquals "2", ethnicity.ethnic
        assertEquals "MOA", ethnicity?.race?.race
        assertEquals "5", ethnicity?.ipedsEthnicity?.code
    }


    @Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def ethnicity = newEthnicity()

        ethnicity.save(flush: true, failOnError: true)
        ethnicity.refresh()
        assertNotNull "Ethnicity should have been saved", ethnicity.id

        // test date values -
        assertEquals date.format(today), date.format(ethnicity.lastModified)
        assertEquals hour.format(today), hour.format(ethnicity.lastModified)
    }


    @Test
    void testOptimisticLock() {
        def ethnicity = newEthnicity()
        ethnicity.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVETHN set STVETHN_VERSION = 999 where STVETHN_SURROGATE_ID = ?", [ethnicity.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        ethnicity.description = "UUUUUUUUUU"
        ethnicity.ethnicCode = "U"
        ethnicity.electronicDataInterchangeEquivalent = "U"
        ethnicity.lmsEquivalent = "U"
        ethnicity.ethnic = "2"
        shouldFail(HibernateOptimisticLockingFailureException) {
            ethnicity.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity.save(failOnError: true, flush: true)
        def id = ethnicity.id
        assertNotNull id
        ethnicity.delete()
        assertNull Ethnicity.get(id)
    }


    @Test
    void testValidation() {
        def ethnicity = new Ethnicity()
        assertFalse "Ethnicity could not be validated as expected due to ${ethnicity.errors}", ethnicity.validate()
    }


    @Test
    void testNullValidationFailure() {
        def ethnicity = new Ethnicity()
        assertFalse "Ethnicity should have failed validation", ethnicity.validate()
        assertErrorsFor ethnicity, 'nullable', ['code']
        assertNoErrorsFor ethnicity, ['description', 'ethnicCode', 'electronicDataInterchangeEquivalent',
                'lmsEquivalent', 'ethnic', 'race', 'ipedsEthnicity']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def ethnicity = new Ethnicity(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                ethnicCode: 'XXX',
                electronicDataInterchangeEquivalent: 'XXX',
                lmsEquivalent: 'XXX',
                ethnic: '111')
        assertFalse "Ethnicity should have failed validation", ethnicity.validate()
        assertErrorsFor ethnicity, 'maxSize', ['description', 'ethnicCode', 'electronicDataInterchangeEquivalent', 'lmsEquivalent', 'ethnic']
    }


    private def newEthnicity() {
        def race = Race.findByRace("IND")
        def ipedsEthnicity = IpedsEthnicity.findByCode("2")

        def ethnicity = new Ethnicity(
                code: "TT",
                description: "TTTTTTTTTT",
                ethnicCode: "T",
                electronicDataInterchangeEquivalent: "T",
                lmsEquivalent: "T",
                ethnic: "1",
                race: race,
                ipedsEthnicity: ipedsEthnicity,
        )
        return ethnicity
    }

}
