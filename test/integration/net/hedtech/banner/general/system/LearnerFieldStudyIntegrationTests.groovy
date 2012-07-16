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
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 16:09:29 CST 2011 
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
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
      learnerFieldStudy.save(flush:true, failOnError:true)
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
