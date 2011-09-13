/** *****************************************************************************

 © 2011 SunGard Higher Education.  All Rights Reserved.

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
 * Integration test for the level model.
 * */
class LevelIntegrationTests extends BaseIntegrationTestCase {

  /*PROTECTED REGION ID(level_domain_integration_test_data) ENABLED START*/
  //Test data for creating new domain instance
  //Valid test data (For success tests)

  def i_success_code = "TT"
  def i_success_description = "TTTTT"
  def i_success_academicIndicator = true
  def i_success_continuingEducationIndicator = true

  def i_success_systemRequiredIndicator = true
  def i_success_voiceResponseMessageNumber = 1

  def i_success_electronicDataInterchangeEquivalent = "TT"

  /*PROTECTED REGION END*/

  def levelService


  protected void setUp() {
    formContext = ['STVLEVL'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }


  void testCreateLevel() {
    def level = newValidForCreateLevel()

    if (!level.save()) {
      fail("Could not save Level; LEVEL ERRORS = " + level.errors);
    }
    assertNotNull(level.id)
  }

  void testUpdateLevel() {
    def level = newValidForCreateLevel()
    if (!level.save(flush:true, failOnError:true)) {
      fail("Could not save Level; LEVEL ERRORS = " + level.errors);
    }
    def id = level.id
    def version = level.version
    assertNotNull(id)
    assertEquals(0L, version)

    level.description = "updated"

    if (!level.save(flush:true, failOnError:true)) {
      fail("Could not update Level; LEVEL ERRORS = " + level.errors);
    }
    level = Level.get(id)

    assertNotNull("found must not be null", level)
    assertEquals("updated", level.description)
    assertEquals(1, level.version)
  }

  void testDeleteLevel() {
    def level = newValidForCreateLevel()
    if (!level.save(flush:true, failOnError:true)) {
      fail("Could not save Level; LEVEL ERRORS = " + level.errors);
    }
    def id = level.id
    assertNotNull(id)
    level.delete();
    def found = Level.get(id)
    assertNull(found)
  }


  void testOptimisticLock() {
    def level = newValidForCreateLevel()
    save level

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update STVLEVL set STVLEVL_VERSION = 999 where STVLEVL_SURROGATE_ID = ?", [level.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    level.description = "Test Description"
    shouldFail(HibernateOptimisticLockingFailureException) {
      level.save(flush:true, failOnError:true)
    }
  }

  void testValidation() {
    def level = new Level()
    //should not pass validation since none of the required values are provided
    assertFalse(level.validate())
    level.code = "TT"
    level.description = "TT"
    level.acadInd = true
    level.ceuInd = true
    level.systemReqInd = true
    level.vrMsgNo = 4321
    level.ediEquiv = "TT"
    level.lastModified = new Date()
    level.lastModifiedBy = "test"
    level.dataOrigin = "banner"

    //should pass this time
    assertTrue(level.validate())
  }

  void testNullValidationFailure() {
    def level = new Level()
    assertFalse "Level should have failed validation", level.validate()
    assertErrorsFor level, 'nullable',
            [
                    'code',
                    'description',
                    'ceuInd'
            ]
    assertNoErrorsFor level,
            [
                    'acadInd',
                    'systemReqInd',
                    'vrMsgNo',
                    'ediEquiv'
            ]
  }

  void testMaxSizeValidationFailures() {
    def level = new Level(
            description: 'This description is longet then allowed, it should throw maxSize error',
            ediEquiv: 'Allowd length is 2, should throw maxSize Error')
    assertFalse "Level should have failed validation", level.validate()
    assertErrorsFor level, 'maxSize', ['description', 'ediEquiv']
  }




  private def newValidForCreateLevel() {
    def level = new Level(
            code: i_success_code,
            description: i_success_description,
            acadInd: i_success_academicIndicator,
            ceuInd: i_success_continuingEducationIndicator,
            systemReqInd: i_success_systemRequiredIndicator,
            vrMsgNo: i_success_voiceResponseMessageNumber,
            ediEquiv: i_success_electronicDataInterchangeEquivalent,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return level
  }
}
