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
 * Integration test for academic year model.
 * */
class AcademicYearIntegrationTests extends BaseIntegrationTestCase {

  /*PROTECTED REGION ID(academicyear_domain_integration_test_data) ENABLED START*/
  //Test data for creating new domain instance
  //Valid test data (For success tests)

  def i_success_code = "TTTT"
  def i_success_description = "TTTTT"
  def i_success_systemRequestIndicator = true

  /*PROTECTED REGION END*/

  def academicYearService


  protected void setUp() {
    formContext = ['STVACYR'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }


  void testCreateAcademicYear() {
    def academicYear = newValidForCreateAcademicYear()

    save academicYear
    assertNotNull academicYear.id
  }

  void testUpdateAcademicYear() {
    def academicYear = newValidForCreateAcademicYear()
    save academicYear

    def id = academicYear.id
    def version = academicYear.version
    assertNotNull id
    assertEquals 0L, version

    academicYear.description = "updated"
    save academicYear

    academicYear = AcademicYear.get(id)
    assertNotNull "found must not be null", academicYear
    assertEquals "updated", academicYear.description
    assertEquals 1L, academicYear.version
  }


  void testDeleteAcademicYear() {
    def academicYear = newValidForCreateAcademicYear()
    save academicYear

    def id = academicYear.id
    assertNotNull id
    academicYear.delete()
    assertNull AcademicYear.get(id)
  }



  void testOptimisticLock() {
    def academicYear = newValidForCreateAcademicYear()
    save academicYear

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update STVACYR set STVACYR_VERSION = 999 where STVACYR_SURROGATE_ID = ?", [academicYear.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    academicYear.description = "Test Description"
    academicYear.sysreqInd = false
    shouldFail(HibernateOptimisticLockingFailureException) {
      academicYear.save(flush: true)
    }
  }

  void testValidation() {
    def academicYear = new AcademicYear()
    //should not pass validation since none of the required values are provided
    assertFalse academicYear.validate()

    academicYear.code = "TT"
    academicYear.description = "TT"
    academicYear.sysreqInd = true
    academicYear.lastModified = new Date()
    academicYear.lastModifiedBy = "test"
    academicYear.dataOrigin = "banner"

    assertTrue academicYear.validate()
  }

  void testNullValidationFailure() {
    def academicYear = new AcademicYear()
    assertFalse "AcademicYear should have failed validation", academicYear.validate()
    assertErrorsFor academicYear, 'nullable',
            [
                    'code'
            ]
    assertNoErrorsFor academicYear,
            [
                    'description',
                    'sysreqInd'
            ]
  }

  void testMaxSizeValidationFailures() {
    def academicYear = new AcademicYear(
            description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
            sysreqInd: 'XXX')
    assertFalse "AcademicYear should have failed validation", academicYear.validate()
    assertErrorsFor(academicYear, 'maxSize', ['description'])
  }

  void testValidationMessages() {
    def academicYear = newValidForCreateAcademicYear()
    academicYear.code = null
    assertFalse academicYear.validate()
    assertLocalizedError academicYear, 'nullable', /.*Field.*code.*of class.*AcademicYear.*cannot be null.*/, 'code'
  }


  private def newValidForCreateAcademicYear() {
    def academicYear = new AcademicYear(
            code: i_success_code,
            description: i_success_description,
            sysreqInd: i_success_systemRequestIndicator,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return academicYear
  }


}
