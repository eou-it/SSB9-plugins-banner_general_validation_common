
/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 1.20
 Generated: Mon Jun 13 15:40:29 IST 2011
 */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import grails.validation.ValidationException
import groovy.sql.Sql
import org.junit.Ignore
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class SubjectIndexIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(subjectindex_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTTT"
	def i_success_description = "TTTTT"
	//Invalid test data (For failure tests)

	def i_failure_code = "JJJJJ"
	def i_failure_description = "TTTTT TTTTT TTTTT TTTTT TTTTT TTTTT"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "TTTTT"
	def u_success_description = "UUUUU"
	//Valid test data (For failure tests)

	def u_failure_code = "TTTTT"
	def u_failure_description = "TTTTT TTTTT TTTTT TTTTT TTTTT TTTTT"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GTVSUBJ'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		//initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(subjectindex_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)

		//Invalid test data (For failure tests)

		//Valid test data (For success tests)

		//Valid test data (For failure tests)

		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidSubjectIndex() {
		def subjectIndex = newValidForCreateSubjectIndex()
		save subjectIndex
		//Test if the generated entity now has an id assigned
        assertNotNull subjectIndex.id
	}

	void testCreateInvalidSubjectIndex() {
		def subjectIndex = newInvalidForCreateSubjectIndex()
		shouldFail(ValidationException) {
            subjectIndex.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidSubjectIndex() {
		def subjectIndex = newValidForCreateSubjectIndex()
		save subjectIndex
        assertNotNull subjectIndex.id
        assertEquals 0L, subjectIndex.version
        assertEquals i_success_code, subjectIndex.code
        assertEquals i_success_description, subjectIndex.description

		//Update the entity
		subjectIndex.description = u_success_description
        save subjectIndex
		//Asset for sucessful update
        subjectIndex = SubjectIndex.get( subjectIndex.id )
        assertEquals 1L, subjectIndex?.version
        assertEquals u_success_description, subjectIndex.description
	}

	void testUpdateInvalidSubjectIndex() {
		def subjectIndex = newValidForCreateSubjectIndex()
		save subjectIndex
        assertNotNull subjectIndex.id
        assertEquals 0L, subjectIndex.version
        assertEquals i_success_code, subjectIndex.code
        assertEquals i_success_description, subjectIndex.description

		//Update the entity with invalid values
		subjectIndex.description = u_failure_description
		shouldFail(ValidationException) {
            subjectIndex.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def subjectIndex = newValidForCreateSubjectIndex()
		save subjectIndex

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVSUBJ set GTVSUBJ_VERSION = 999 where GTVSUBJ_SURROGATE_ID = ?", [ subjectIndex.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		subjectIndex.description = u_success_description
        shouldFail( HibernateOptimisticLockingFailureException ) {
            subjectIndex.save( failOnError: true, flush: true )
        }
    }

	void testDeleteSubjectIndex() {
		def subjectIndex = newValidForCreateSubjectIndex()
		save subjectIndex
		def id = subjectIndex.id
		assertNotNull id
		subjectIndex.delete()
		assertNull SubjectIndex.get( id )
	}


    void testNullValidationFailure() {
        def subjectIndex = new SubjectIndex()
        assertFalse "SubjectIndex should have failed validation", subjectIndex.validate()
        assertErrorsFor subjectIndex, 'nullable',
                                               [
                                                 'code',
                                                 'description'
                                               ]
    }


	void testValidationMessages() {
	    def subjectIndex = newInvalidForCreateSubjectIndex()
	    subjectIndex.code = null
	    assertFalse subjectIndex.validate()
	    assertLocalizedError subjectIndex, 'nullable', /.*Field.*code.*of class.*SubjectIndex.*cannot be null.*/, 'code'
	    subjectIndex.description = null
	    assertFalse subjectIndex.validate()
	    assertLocalizedError subjectIndex, 'nullable', /.*Field.*description.*of class.*SubjectIndex.*cannot be null.*/, 'description'
	}


	private def newValidForCreateSubjectIndex() {
		def subjectIndex = new SubjectIndex(
			code: i_success_code,
			description: i_success_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return subjectIndex
	}

	private def newInvalidForCreateSubjectIndex() {
		def subjectIndex = new SubjectIndex(
			code: i_failure_code,
			description: i_failure_description,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return subjectIndex
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(subjectindex_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
