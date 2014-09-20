/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class HoldTypeIntegrationTests extends BaseIntegrationTestCase {

    def holdTypeService


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
    void testCreateHoldType() {
        def holdType = newHoldType()
        save holdType
        //Test if the generated entity now has an id assigned
        assertNotNull holdType.id
    }


	@Test
    void testUpdateHoldType() {
        def holdType = newHoldType()
        save holdType

        assertNotNull holdType.id
        assertEquals 0L, holdType.version
        assertEquals "TT", holdType.code
        assertTrue holdType.registrationHoldIndicator
        assertTrue holdType.transcriptHoldIndicator
        assertTrue holdType.graduationHoldIndicator
        assertTrue holdType.gradeHoldIndicator
        assertEquals "TTTTT", holdType.description
        assertTrue holdType.accountsReceivableHoldIndicator
        assertTrue holdType.enrollmentVerificationHoldIndicator
        assertEquals 1, holdType.voiceResponseMessageNumber
        assertTrue holdType.displayWebIndicator
        assertTrue holdType.applicationHoldIndicator
        assertTrue  holdType.complianceHoldIndicator

        //Update the entity
        holdType.registrationHoldIndicator = false
        save holdType

        holdType = HoldType.get(holdType.id)
        assertEquals 1L, holdType?.version
        assertFalse holdType.registrationHoldIndicator
    }


	@Test
    void testOptimisticLock() {
        def holdType = newHoldType()
        save holdType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVHLDD set STVHLDD_VERSION = 999 where STVHLDD_SURROGATE_ID = ?", [holdType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        holdType.code = "UU"
        holdType.registrationHoldIndicator = false

        shouldFail(HibernateOptimisticLockingFailureException) {
            holdType.save(flush: true)
        }
    }


	@Test
    void testDeleteHoldType() {
        def holdType = newHoldType()
        save holdType
        def id = holdType.id
        assertNotNull id
        holdType.delete()
        assertNull HoldType.get(id)
    }


	@Test
    void testValidation() {
        def holdType = newHoldType()
        assertTrue "HoldType could not be validated as expected due to ${holdType.errors}", holdType.validate()
    }


	@Test
    void testNullValidationFailure() {
        def holdType = new HoldType()
        assertFalse "HoldType should have failed validation", holdType.validate()
        assertErrorsFor holdType, 'nullable',
                        [
                                'code'
                        ]
        assertNoErrorsFor holdType,
                          [
                                  'registrationHoldIndicator',
                                  'transcriptHoldIndicator',
                                  'graduationHoldIndicator',
                                  'gradeHoldIndicator',
                                  'description',
                                  'accountsreceivablerHoldIndicator',
                                  'enrollmentVerificationHoldIndicator',
                                  'voiceResponseMessageNumber',
                                  'displayWebIndicator',
                                  'applicationHoldIndicator',
                                  'complianceHoldIndicator'
                          ]
    }


	@Test
    void testMaxSizeValidationFailures() {
        def holdType = new HoldType(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "HoldType should have failed validation", holdType.validate()
        assertErrorsFor holdType, 'maxSize', ['description']
    }


    private def newHoldType() {

        def holdType = new HoldType(
                code: "TT",
                registrationHoldIndicator: true,
                transcriptHoldIndicator: true,
                graduationHoldIndicator: true,
                gradeHoldIndicator: true,
                description: "TTTTT",
                accountsReceivableHoldIndicator: true,
                enrollmentVerificationHoldIndicator: true,
                voiceResponseMessageNumber: 1,
                displayWebIndicator: true,
                applicationHoldIndicator: true,
                complianceHoldIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return holdType
    }

    /**
     * A test to exercise 'code' primary key in HoldType
     */
	@Test
    void testPrimaryKeyOnCode() {
        def holdType = newHoldType()
        def duplicateObj = newHoldType()

        save holdType
        assertNotNull holdType.id

        assertEquals holdType.code, duplicateObj.code

        shouldFail() {
            save duplicateObj
        }
    }

    /**
     * A test to exercise the findBy method for HoldType
     */
	@Test
    void testFindHoldType() {
        def holdType = newHoldType()
        save holdType

        def holdType2 = HoldType.findByCode("TT")
        assertNotNull holdType2
        assertEquals holdType2.code, "TT"

    }


}
