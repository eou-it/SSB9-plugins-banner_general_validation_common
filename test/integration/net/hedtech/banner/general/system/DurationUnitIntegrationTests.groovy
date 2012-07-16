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

/**
 * Integration test for the duration unit code model.
 * */
class DurationUnitIntegrationTests extends BaseIntegrationTestCase {

  /*PROTECTED REGION ID(durationunit_domain_integration_test_data) ENABLED START*/
  //Test data for creating new domain instance
  //Valid test data (For success tests)

  def i_success_code = "TTTT"
  def i_success_description = "TTTTT"
  def i_success_numberOfDays = 1
  def i_success_voiceResponseMessageNumber = 1

  //Invalid test data (For failure tests)

  def i_failure_code = "TTTT"
  def i_failure_description = "TTTTT"
  def i_failure_numberOfDays = 1
  def i_failure_voiceResponseMessageNumber = 1

  //Test data for creating updating domain instance
  //Valid test data (For success tests)

  def u_success_code = "TTTT"
  def u_success_description = "TTTTT"
  def u_success_numberOfDays = 1
  def u_success_voiceResponseMessageNumber = 1

  //Valid test data (For failure tests)

  def u_failure_code = "TTTT"
  def u_failure_description = "TTTTT"
  def u_failure_numberOfDays = 1
  def u_failure_voiceResponseMessageNumber = 1

  /*PROTECTED REGION END*/
  def durationUnitCodeService


  protected void setUp() {
    formContext = ['SPAIDEN'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }


  void testCreateDurationUnitCode() {
    def durationUnitCode = newValidForCreateDurationUnit()

    save durationUnitCode
    assertNotNull(durationUnitCode.id)
  }


  void testUpdateDurationUnitCode() {
    def durationUnitCode = newValidForCreateDurationUnit()

    save durationUnitCode
    def id = durationUnitCode.id
    assertNotNull id
    assertEquals 0L, durationUnitCode.version

    durationUnitCode.description = "updated"
    save durationUnitCode

    durationUnitCode = DurationUnit.get(id)
    assertNotNull "found must not be null", durationUnitCode
    assertEquals "updated", durationUnitCode.description
    assertEquals 1, durationUnitCode.version
  }


  void testDeleteDurationUnitCode() {
    def durationUnitCode = newValidForCreateDurationUnit()

    save durationUnitCode
    def id = durationUnitCode.id
    assertNotNull id

    durationUnitCode.delete();
    assertNull DurationUnit.get(id)
  }

  void testOptimisticLock() {
    def durationUnit = newValidForCreateDurationUnit()
    save durationUnit

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update GTVDUNT set GTVDUNT_VERSION = 999 where GTVDUNT_SURROGATE_ID = ?", [durationUnit.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    durationUnit.description = "Update Description"
    durationUnit.numberOfDays = 43.34
    durationUnit.vrMsgNo = 3
    shouldFail(HibernateOptimisticLockingFailureException) {
      durationUnit.save(flush:true, failOnError:true)
    }
  }

  void testValidation() {
    def durationUnitCode = new DurationUnit()
    //should not pass validation since none of the required values are provided
    assertFalse(durationUnitCode.validate())

    durationUnitCode = new DurationUnit(code: "TT", description: "TT", numberOfDays: 1, vrMsgNo: 1,
            lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
    //should pass this time
    assertTrue durationUnitCode.validate()
  }

  void testNullValidationFailure() {
    def durationUnit = new DurationUnit()
    assertFalse "DurationUnit should have failed validation", durationUnit.validate()
    assertErrorsFor durationUnit, 'nullable',
            [
                    'code',
                    'description',
                    'numberOfDays'
            ]
    assertNoErrorsFor durationUnit,
            [
                    'vrMsgNo'
            ]
  }



  private def newValidForCreateDurationUnit() {
    def durationUnit = new DurationUnit(
            code: i_success_code,
            description: i_success_description,
            numberOfDays: i_success_numberOfDays,
            vrMsgNo: i_success_voiceResponseMessageNumber,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return durationUnit
  }

  private def newInvalidForCreateDurationUnit() {
    def durationUnit = new DurationUnit(
            code: i_failure_code,
            description: i_failure_description,
            numberOfDays: i_failure_numberOfDays,
            vrMsgNo: i_failure_voiceResponseMessageNumber,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return durationUnit
  }
}
