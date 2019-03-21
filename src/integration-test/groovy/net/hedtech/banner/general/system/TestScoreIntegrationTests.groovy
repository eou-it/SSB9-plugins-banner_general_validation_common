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
 * Integration test for the test score model.
 * */
class TestScoreIntegrationTests extends BaseIntegrationTestCase {

    def testScoreService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testValidation() {
        def testScore = newTestScore()
        assertTrue "TestScore could not be validated as expected due to ${testScore.errors}", testScore.validate()
    }


    @Test
    void testNullValidationFailure() {
        def testScore = new TestScore()
        assertFalse "TestScore should have failed validation", testScore.validate()
        assertErrorsFor(testScore, 'nullable', ['code', 'numberPositions', 'dataType'])
        assertNoErrorsFor(testScore, ['description', 'minimumValue', 'maximumValue',
                'systemRequiredIndicator', 'managementInformationSystemCode',
                'assessmentForm', 'voiceResponseMessageNumber',
                'admissionRequest'])

    }


    @Test
    void testMaxSizeValidationFailures() {
        def testScore = new TestScore(code: "TTXXXXX", description: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                numberPositions: 22567,
                minimumValue: "XXXXXXXXXXXXXXXX", maximumValue: "XXXXXXXXXXXXXXXX",
                systemRequiredIndicator: true, managementInformationSystemCode: 'XXXXXXXXXXXXX',
                assessmentForm: "XX", voiceResponseMessageNumber: 1234567)

        assertFalse "TestScore should have failed validation", testScore.validate()
        assertErrorsFor(testScore, 'maxSize', ['code', 'description', 'minimumValue', 'maximumValue',
                'managementInformationSystemCode', 'assessmentForm'
        ])

        assertErrorsFor(testScore, 'max', ['numberPositions', 'voiceResponseMessageNumber'])
    }



    @Test
    void testCreateTestScore() {
        def entity = newTestScore()
        save entity

        assertNotNull entity.id
    }


    @Test
    void testUpdateTestScore() {
        def entity = newTestScore()
        save entity

        assertNotNull entity.id
        assertEquals(0L, entity.version)
        assertEquals('TT', entity.description)

        entity.description = "Updated TT"
        save entity

        entity = TestScore.get(entity.id)
        assertEquals new Long(1), entity?.version
        assertEquals "Updated TT", entity?.description
    }


    @Test
    void testOptimisticLock() {
        def entity = newTestScore()
        save entity

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVTESC set STVTESC_VERSION = 999 where stvtesc_surrogate_id = ?", [entity.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        entity.description = "This better fail"
        shouldFail(HibernateOptimisticLockingFailureException) {
            entity.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteTestScore() {
        def testScore = newTestScore()
        save testScore

        def id = testScore.id
        assertNotNull id
        testScore.delete()
        assertNull TestScore.get(id)
    }


    private def newTestScore() {
        new TestScore(code: "TT", description: "TT",
                numberPositions: 1, dataType: "Z",
                minimumValue: "A", maximumValue: "Z",
                systemRequiredIndicator: true, managementInformationSystemCode: "A",
                assessmentForm: "A", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon')
    }

}
