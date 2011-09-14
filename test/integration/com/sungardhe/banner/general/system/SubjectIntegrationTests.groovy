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
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the 'subject' model.
 * */
class SubjectIntegrationTests extends BaseIntegrationTestCase {


  def i_success_code = "TTTT"
  def i_success_description = "TTTTT"
  def i_success_voiceResponseMessageNumber = 1

  def i_success_displayWebIndicator = true

  protected void setUp() {
    formContext = ['STVSUBJ'] // Since we are not testing a controller, we need to explicitly set this
    super.setUp()
  }


  void testCreateSubject() {
    def subject = newValidForCreateSubject()

    if (!subject.save()) {
      fail("Could not save Subject; SUBJECT ERRORS = " + subject.errors);
    }
    assertNotNull(subject.id)
  }

  void testUpdateSubject() {
    def subject = newValidForCreateSubject()
    if (!subject.save(flush:true, failOnError:true)) {
      fail("Could not save Subject; SUBJECT ERRORS = " + subject.errors);
    }
    def id = subject.id
    def version = subject.version
    assertNotNull(id)
    assertEquals(0L, version)

    subject.description = "updated"

    if (!subject.save(flush:true, failOnError:true)) {
      fail("Could not update Subject; SUBJECT ERRORS = " + subject.errors);
    }
    subject = Subject.get(id)

    assertNotNull("found must not be null", subject)
    assertEquals("updated", subject.description)
    assertEquals(1, subject.version)
  }

  void testDeleteSubject() {
    def subject = newValidForCreateSubject()
    if (!subject.save(flush:true, failOnError:true)) {
      fail("Could not save Subject; SUBJECT ERRORS = " + subject.errors);
    }
    def id = subject.id
    assertNotNull(id)
    subject.delete();
    def found = Subject.get(id)
    assertNull(found)
  }

  void testOptimisticLock() {
    def subject = newValidForCreateSubject()
    save subject

    def sql
    try {
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.executeUpdate("update STVSUBJ set STVSUBJ_VERSION = 999 where STVSUBJ_SURROGATE_ID = ?", [subject.id])
    } finally {
      sql?.close() // note that the test will close the connection, since it's our current session's connection
    }
    //Try to update the entity
    //Update the entity
    subject.description = "Update Description"
    subject.vrMsgNo = 21
    subject.dispWebInd = false
    shouldFail(HibernateOptimisticLockingFailureException) {
      subject.save(flush:true, failOnError:true)
    }
  }

  void testValidation() {
    def subject = new Subject()
    //should not pass validation since none of the required values are provided
    assertFalse(subject.validate())

    // Setup the valid subject
    subject = newValidForCreateSubject()

    //should pass this time
    assertTrue(subject.validate())
  }

  void testNullValidationFailure() {
    def subject = new Subject()
    assertFalse "Subject should have failed validation", subject.validate()
    assertErrorsFor subject, 'nullable',
            [
                    'code',
                    'dispWebInd'
            ]
    assertNoErrorsFor subject,
            [
                    'description',
                    'vrMsgNo'
            ]
  }

  void testMaxSizeValidationFailures() {
    def subject = new Subject(
            description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
    assertFalse "Subject should have failed validation", subject.validate()
    assertErrorsFor subject, 'maxSize', ['description']
  }


  private def newValidForCreateSubject() {
    def subject = new Subject(
            code: i_success_code,
            description: i_success_description,
            vrMsgNo: i_success_voiceResponseMessageNumber,
            dispWebInd: i_success_displayWebIndicator,
            lastModified: new Date(),
            lastModifiedBy: "test",
            dataOrigin: "Banner"
    )
    return subject
  }

}
