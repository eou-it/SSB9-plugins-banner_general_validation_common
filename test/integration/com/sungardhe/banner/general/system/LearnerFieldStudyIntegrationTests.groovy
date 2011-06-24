/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 16:09:29 CST 2011 
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class LearnerFieldStudyIntegrationTests extends BaseIntegrationTestCase {

  /*PROTECTED REGION ID(learnerfieldstudy_domain_integration_test_data) ENABLED START*/
  //Test data for creating new domain instance
  //Valid test data (For success tests)

  def i_success_code = "TTTTT"
  def i_success_description = "TTTTT"
  def i_success_systemRequiredIndicator = true

  // No Invalid Scenario required
  /*PROTECTED REGION END*/

  protected void setUp() {
    formContext = ['GTVLFST'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCreateValidLearnerFieldStudy() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    save learnerFieldStudy
    //Test if the generated entity now has an id assigned
    assertNotNull learnerFieldStudy.id
  }

  void testUpdateValidLearnerFieldStudy() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    save learnerFieldStudy
    assertNotNull learnerFieldStudy.id
    assertEquals 0L, learnerFieldStudy.version
    assertEquals i_success_code, learnerFieldStudy.code
    assertEquals i_success_description, learnerFieldStudy.description
    assertEquals i_success_systemRequiredIndicator, learnerFieldStudy.systemRequiredIndicator

    //Update the entity
    learnerFieldStudy.description = "Updated Description"
    learnerFieldStudy.systemRequiredIndicator = false
    save learnerFieldStudy
    //Asset for sucessful update
    learnerFieldStudy = LearnerFieldStudy.get(learnerFieldStudy.id)
    assertEquals 1L, learnerFieldStudy?.version
    assertEquals "Updated Description", learnerFieldStudy.description
    assertEquals false, learnerFieldStudy.systemRequiredIndicator
  }

  void testOptimisticLock() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    save learnerFieldStudy

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update GTVLFST set GTVLFST_VERSION = 999 where GTVLFST_SURROGATE_ID = ?", [learnerFieldStudy.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    learnerFieldStudy.description = "updated description"
    learnerFieldStudy.systemRequiredIndicator = true
    shouldFail(HibernateOptimisticLockingFailureException) {
      learnerFieldStudy.save(flush: true)
    }
  }

  void testDeleteLearnerFieldStudy() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    save learnerFieldStudy
    def id = learnerFieldStudy.id
    assertNotNull id
    learnerFieldStudy.delete()
    assertNull LearnerFieldStudy.get(id)
  }

  void testValidation() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    assertTrue "LearnerFieldStudy could not be validated as expected due to ${learnerFieldStudy.errors}", learnerFieldStudy.validate()
  }

  void testNullValidationFailure() {
    def learnerFieldStudy = new LearnerFieldStudy()
    assertFalse "LearnerFieldStudy should have failed validation", learnerFieldStudy.validate()
    assertErrorsFor learnerFieldStudy, 'nullable',
            [
                    'code',
                    'description',
                    'systemRequiredIndicator'
            ]
  }


  void testValidationMessages() {
    def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
    learnerFieldStudy.code = null
    assertFalse learnerFieldStudy.validate()
    assertLocalizedError learnerFieldStudy, 'nullable', /.*Field.*code.*of class.*LearnerFieldStudy.*cannot be null.*/, 'code'
    learnerFieldStudy.description = null
    assertFalse learnerFieldStudy.validate()
    assertLocalizedError learnerFieldStudy, 'nullable', /.*Field.*description.*of class.*LearnerFieldStudy.*cannot be null.*/, 'description'
    learnerFieldStudy.systemRequiredIndicator = null
    assertFalse learnerFieldStudy.validate()
    assertLocalizedError learnerFieldStudy, 'nullable', /.*Field.*systemRequiredIndicator.*of class.*LearnerFieldStudy.*cannot be null.*/, 'systemRequiredIndicator'
  }


  private def newValidForCreateLearnerFieldStudy() {
    def learnerFieldStudy = new LearnerFieldStudy(
            code: i_success_code,
            description: i_success_description,
            systemRequiredIndicator: i_success_systemRequiredIndicator,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return learnerFieldStudy
  }

}
