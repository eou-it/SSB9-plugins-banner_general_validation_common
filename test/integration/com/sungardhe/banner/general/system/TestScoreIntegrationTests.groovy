/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the test score model.
 * */
class TestScoreIntegrationTests extends BaseIntegrationTestCase {

  def testScoreService


  protected void setUp() {
    formContext = ['STVTESC'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }

  void testValidation() {
    def testScore = newTestScore()
    assertTrue "TestScore could not be validated as expected due to ${testScore.errors}", testScore.validate()
  }


  void testNullValidationFailure() {
    def testScore = new TestScore()
    assertFalse "TestScore should have failed validation", testScore.validate()
    assertErrorsFor(testScore, 'nullable', ['code', 'numberPositions', 'dataType'])
    assertNoErrorsFor(testScore, ['description', 'minimumValue', 'maximumValue',
            'systemRequiredIndicator', 'managementInformationSystemCode',
            'assessmentForm', 'voiceResponseMessageNumber',
            'admissionRequest'])

  }


  void testMaxSizeValidationFailures() {
    def testScore = new TestScore(code: "TTXXX", description: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            numberPositions: 22567,
            minimumValue: "XXXXXX", maximumValue: "XXXXXX",
            systemRequiredIndicator: true, managementInformationSystemCode: 'XXXXXXXXXXXXX',
            assessmentForm: "XX", voiceResponseMessageNumber: 1234567)

    assertFalse "TestScore should have failed validation", testScore.validate()
    assertErrorsFor(testScore, 'maxSize', ['code', 'description', 'minimumValue', 'maximumValue',
            'managementInformationSystemCode', 'assessmentForm'
    ])

    assertErrorsFor(testScore, 'max', ['numberPositions', 'voiceResponseMessageNumber'])
  }



  void testCreateTestScore() {
    def entity = newTestScore()
    save entity

    assertNotNull entity.id
  }


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
      entity.save(flush: true)
    }
  }


  void testDeleteTestScore() {
    def testScore = newTestScore()
    save testScore

    def id = testScore.id
    assertNotNull id
    testScore.delete()
    assertNull TestScore.get(id)
  }


  void testValidationMessages() {
    def testScore = newTestScore()
    testScore.code = null
    assertFalse testScore.validate()

    assertLocalizedError(testScore, 'nullable', /.*Field.*code.*of class.*TestScore.*cannot be null.*/, 'code')
  }


  void testFetchBySomeAttribute() {
      def testScore = TestScore.fetchBySomeAttribute()
      def testScoreList = testScore.get("list")

      def testScoreObj1 = TestScore.findWhere(code : "GM")
      def testScoreObj2 = TestScore.findWhere(code : "MB")
      def testScoreObj3 = TestScore.findWhere(code : "AGE")
      def testScoreObj4 = TestScore.findWhere(code : "MATH")
      def testScoreObj5 = newTestScore()

      assertTrue testScoreList.contains(testScoreObj1)
      assertTrue testScoreList.contains(testScoreObj2)
      assertTrue testScoreList.contains(testScoreObj3)
      assertTrue testScoreList.contains(testScoreObj4)
      assertFalse testScoreList.contains(testScoreObj5)

      def filter = "A0"
      testScore = TestScore.fetchBySomeAttribute(filter)
      testScoreList = testScore.get("list")

      testScoreObj1 = TestScore.findWhere(code : "A01")
      testScoreObj2 = TestScore.findWhere(code : "A02")
      testScoreObj3 = TestScore.findWhere(code : "A03")
      testScoreObj4 = TestScore.findWhere(code : "A04")
      testScoreObj5 = TestScore.findWhere(code : "AGE")

      assertTrue testScoreList.contains(testScoreObj1)
      assertTrue testScoreList.contains(testScoreObj2)
      assertTrue testScoreList.contains(testScoreObj3)
      assertTrue testScoreList.contains(testScoreObj4)
      assertFalse testScoreList.contains(testScoreObj5)

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
