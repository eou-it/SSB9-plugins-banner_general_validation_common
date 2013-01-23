/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class NationIntegrationTests extends BaseIntegrationTestCase {

    def nationService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateNation() {
        def nation = newNation()
        save nation
        //Test if the generated entity now has an id assigned
        assertNotNull nation.id
    }


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
            nation.save(flush:true, failOnError:true)
        }
    }


    void testDeleteNation() {
        def nation = newNation()
        save nation
        def id = nation.id
        assertNotNull id
        nation.delete()
        assertNull Nation.get(id)
    }


    void testValidation() {
        def nation = newNation()
        assertTrue "Nation could not be validated as expected due to ${nation.errors}", nation.validate()
    }


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(nation_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
