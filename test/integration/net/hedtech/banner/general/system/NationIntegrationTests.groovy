/** *****************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class NationIntegrationTests extends BaseIntegrationTestCase {

    def nationService


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
    void testCreateNation() {
        def nation = newNation()
        save nation
        //Test if the generated entity now has an id assigned
        assertNotNull nation.id
    }


    @Test
    void testUpdateNation() {
        def nation = newNation()
        save nation

        assertNotNull nation.id
        assertEquals 0L, nation.version
        assertEquals "TTTTT", nation.code
        assertEquals "TTTTT", nation.nation
        assertEquals "TTTTT", nation.capital
        assertEquals "TT", nation.ediEquiv
        assertEquals "TT", nation.lmsEquiv
        assertEquals "TTTTT", nation.postalMask
        assertEquals "TTTTT", nation.telephoneMask
        assertEquals "TTTTT", nation.statscan
        assertEquals "TTT", nation.scodIso
        assertEquals "TT", nation.ssaReportingEquiv
        assertEquals "TT", nation.sevisEquiv

        //Update the entity
        def testDate = new Date()
        nation.code = "UUUUU"
        nation.nation = "UUUUU"
        nation.capital = "UUUUU"
        nation.lmsEquiv = "UU"
        nation.postalMask = "UUUUU"
        nation.telephoneMask = "UUUUU"
        nation.statscan = "UUUUU"
        nation.scodIso = "UUU"
        nation.ssaReportingEquiv = "UU"
        nation.sevisEquiv = "UU"
        nation.lastModified = testDate
        nation.lastModifiedBy = "test"
        nation.dataOrigin = "Banner"
        save nation

        nation = Nation.get(nation.id)
        assertEquals 1L, nation?.version
        assertEquals "UUUUU", nation.code
        assertEquals "UUUUU", nation.nation
        assertEquals "UUUUU", nation.capital
        assertEquals "UU", nation.lmsEquiv
        assertEquals "UUUUU", nation.postalMask
        assertEquals "UUUUU", nation.telephoneMask
        assertEquals "UUUUU", nation.statscan
        assertEquals "UUU", nation.scodIso
        assertEquals "UU", nation.ssaReportingEquiv
        assertEquals "UU", nation.sevisEquiv
    }


    @Test
    void testOptimisticLock() {
        def nation = newNation()
        save nation

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVNATN set STVNATN_VERSION = 999 where STVNATN_SURROGATE_ID = ?", [nation.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        nation.code = "UUUUU"
        nation.nation = "UUUUU"
        nation.capital = "UUUUU"

        nation.ediEquiv = "UU"
        nation.lmsEquiv = "UU"
        nation.postalMask = "UUUUU"
        nation.telephoneMask = "UUUUU"
        nation.statscan = "UUUUU"
        nation.scodIso = "UUU"
        nation.ssaReportingEquiv = "UU"
        nation.sevisEquiv = "UU"
        nation.lastModified = new Date()
        nation.lastModifiedBy = "test"
        nation.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            nation.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteNation() {
        def nation = newNation()
        save nation
        def id = nation.id
        assertNotNull id
        nation.delete()
        assertNull Nation.get(id)
    }


    @Test
    void testValidation() {
        def nation = newNation()
        assertTrue "Nation could not be validated as expected due to ${nation.errors}", nation.validate()
    }


    @Test
    void testNullValidationFailure() {
        def nation = new Nation()
        assertFalse "Nation should have failed validation", nation.validate()
        assertErrorsFor nation, 'nullable',
                [
                        'code',
                        'nation',

                ]
        assertNoErrorsFor nation,
                [
                        'capital',
                        'area',
                        'population',
                        'ediEquiv',
                        'lmsEquiv',
                        'postalMask',
                        'telephoneMask',
                        'statscan',
                        'scodIso',
                        'ssaReportingEquiv',
                        'sevisEquiv',

                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def nation = new Nation(
                capital: 'XXXXXXXXXXXXXXXXXXXXXX',
                ediEquiv: 'XXXX',
                lmsEquiv: 'XXXX',
                postalMask: 'XXXXXXXXXXXXXXXXX',
                telephoneMask: 'XXXXXXXXXXXXXXXXX',
                statscan: 'XXXXXXX',
                scodIso: 'XXXXX',
                ssaReportingEquiv: 'XXXX',
                sevisEquiv: 'XXXX')
        assertFalse "Nation should have failed validation", nation.validate()
        assertErrorsFor nation, 'maxSize', ['capital', 'ediEquiv', 'lmsEquiv', 'postalMask', 'telephoneMask', 'statscan', 'scodIso', 'ssaReportingEquiv', 'sevisEquiv']
    }


    @Test
    void testFetchByNation() {
        def nationList = Nation.fetchBySomeNation()
        assertTrue nationList.list.size() > 5

        nationList = Nation.fetchBySomeNation("157")
        assertEquals 1, nationList.list.size()
        assertEquals "157", nationList.list[0].code

        nationList = Nation.fetchBySomeNation("Panama")
        assertTrue 1 <= nationList.list.size()
        assertEquals "Panama", nationList.list[0].nation
    }


    private def newNation() {
        def nation = new Nation(
                code: "TTTTT",
                nation: "TTTTT",
                capital: "TTTTT",
                ediEquiv: "TT",
                lmsEquiv: "TT",
                postalMask: "TTTTT",
                telephoneMask: "TTTTT",
                statscan: "TTTTT",
                scodIso: "TTT",
                ssaReportingEquiv: "TT",
                sevisEquiv: "TT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return nation
    }


}
