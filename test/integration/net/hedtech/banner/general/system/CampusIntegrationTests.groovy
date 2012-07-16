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
 * Integration tests for the Campus model.  
 * */
class CampusIntegrationTests extends BaseIntegrationTestCase {

  /*PROTECTED REGION ID(campus_domain_integration_test_data) ENABLED START*/
  //Test data for creating new domain instance
  //Valid test data (For success tests)

  def i_success_code = "TTT"
  def i_success_description = "TTTTT"

  /*PROTECTED REGION END*/

  def campusService

  protected void setUp() {
    formContext = ['STVCAMP'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }


  protected void tearDown() {
    super.tearDown()
  }


  void testCreateCampus() {
    def campus = newValidForCreateCampus()

    assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
    assertNotNull campus.id
  }


  void testUpdateCampus() {
    def campus = newValidForCreateCampus()

    save campus
    def id = campus.id
    def version = campus.version
    assertNotNull id
    assertEquals 0L, version

    campus.description = "updated"
    save campus

    def found = Campus.get(id)
    assertNotNull "found must not be null", found
    assertEquals "updated", found.description
    assertEquals 1L, found.version
  }


  void testDeleteCampus() {
    def campus = newValidForCreateCampus()

    assertNotNull "Could not save Campus due to: ${campus.errors}", campus.save()
    def id = campus.id
    assertNotNull id
    campus.delete()
    assertNull(Campus.get(id))
  }

  void testOptimisticLock() {
    def campus = newValidForCreateCampus()
    save campus

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update STVCAMP set STVCAMP_VERSION = 999 where STVCAMP_SURROGATE_ID = ?", [campus.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    campus.description = "Updating description"
    shouldFail(HibernateOptimisticLockingFailureException) {
      campus.save(flush:true, failOnError:true)
    }
  }


  void testValidation() {
    def campus = newValidForCreateCampus()
    assertTrue "Campus could not be validated as expected due to ${campus.errors}", campus.validate()
  }

  void testNullValidationFailure() {
    def campus = new Campus()
    assertFalse "Campus should have failed validation", campus.validate()
    assertErrorsFor campus, 'nullable',
            [
                    'code'
            ]
    assertNoErrorsFor campus,
            [
                    'description',
                    'geographicRegionAsCostCenterInformationByDisctirctOrDivision'
            ]
  }

  void testMaxSizeValidationFailures() {
    def campus = new Campus(
            description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
    assertFalse "Campus should have failed validation", campus.validate()
    assertErrorsFor campus, 'maxSize', ['description']
  }



  private def newValidForCreateCampus() {
    def campus = new Campus(
            code: i_success_code,
            description: i_success_description,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return campus
  }

}
