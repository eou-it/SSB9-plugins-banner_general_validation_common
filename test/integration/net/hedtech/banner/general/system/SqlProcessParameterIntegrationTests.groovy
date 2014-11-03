/*******************************************************************************
Copyright 2014 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class SqlProcessParameterIntegrationTests extends BaseIntegrationTestCase {

	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_code = "TTTTT"
	def i_success_description = "TTTTT"
	def i_success_dataType = "C"
	def i_success_startDate = new Date()
	def i_success_endDate = new Date()
	//Invalid test data (For failure tests)

	def i_failure_code = "TTTTT"
	def i_failure_description = "TTTTT"
	def i_failure_dataType = "X"
	def i_failure_startDate = new Date()
	def i_failure_endDate = new Date()

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_code = "TTTTT"
	def u_success_description = "TTTTTTTTTT"
	def u_success_dataType = "C"
	def u_success_startDate = new Date()
	def u_success_endDate = new Date()
	//Valid test data (For failure tests)

	def u_failure_code = "TTTTT"
	def u_failure_description = "TTTTTTTTTT"
	def u_failure_dataType = "X"
	def u_failure_startDate = new Date()
	def u_failure_endDate = new Date()



	protected void setUp() {
		formContext = ['GUAGMNU']
		super.setUp()
	}


	protected void tearDown() {
		super.tearDown()
	}
	
	
	void testCreateValidSqlProcessParameter() {
		def sqlProcessParameter = newValidForCreateSqlProcessParameter()
		sqlProcessParameter.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull sqlProcessParameter.id
	}
	
	
	void testCreateInvalidSqlProcessParameter() {
		def sqlProcessParameter = newInvalidForCreateSqlProcessParameter()
		shouldFail(ValidationException) {
            sqlProcessParameter.save( failOnError: true, flush: true )
		}
	}
	
	
	void testUpdateValidSqlProcessParameter() {
		def sqlProcessParameter = newValidForCreateSqlProcessParameter()
		sqlProcessParameter.save( failOnError: true, flush: true )
        assertNotNull sqlProcessParameter.id
        assertEquals 0L, sqlProcessParameter.version
        assertEquals i_success_code, sqlProcessParameter.code
        assertEquals i_success_description, sqlProcessParameter.description
        assertEquals i_success_dataType, sqlProcessParameter.dataType
        assertEquals i_success_startDate, sqlProcessParameter.startDate
        assertEquals i_success_endDate, sqlProcessParameter.endDate

		//Update the entity
		sqlProcessParameter.description = u_success_description
		sqlProcessParameter.dataType = u_success_dataType
		sqlProcessParameter.startDate = u_success_startDate
		sqlProcessParameter.endDate = u_success_endDate
		sqlProcessParameter.save( failOnError: true, flush: true )
		//Assert for sucessful update
        sqlProcessParameter = SqlProcessParameter.get( sqlProcessParameter.id )
        assertEquals 1L, sqlProcessParameter?.version
        assertEquals u_success_description, sqlProcessParameter.description
        assertEquals u_success_dataType, sqlProcessParameter.dataType
        assertEquals u_success_startDate, sqlProcessParameter.startDate
        assertEquals u_success_endDate, sqlProcessParameter.endDate
	}
	
	
	void testUpdateInvalidSqlProcessParameter() {
		def sqlProcessParameter = newValidForCreateSqlProcessParameter()
		sqlProcessParameter.save( failOnError: true, flush: true )
        assertNotNull sqlProcessParameter.id
        assertEquals 0L, sqlProcessParameter.version
        assertEquals i_success_code, sqlProcessParameter.code
        assertEquals i_success_description, sqlProcessParameter.description
        assertEquals i_success_dataType, sqlProcessParameter.dataType
        assertEquals i_success_startDate, sqlProcessParameter.startDate
        assertEquals i_success_endDate, sqlProcessParameter.endDate

		//Update the entity with invalid values
		sqlProcessParameter.description = u_failure_description
		sqlProcessParameter.dataType = u_failure_dataType
		sqlProcessParameter.startDate = u_failure_startDate
		sqlProcessParameter.endDate = u_failure_endDate
		shouldFail(ValidationException) {
            sqlProcessParameter.save( failOnError: true, flush: true )
		}
	}
	
	
    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

    	def sqlProcessParameter = newValidForCreateSqlProcessParameter()

    	// TODO review the arbitrary use of "Date()" as a date value in the test below and choose better values

    	sqlProcessParameter.startDate = new Date()
    	sqlProcessParameter.endDate = new Date()

    	sqlProcessParameter.save(flush: true, failOnError: true)
    	sqlProcessParameter.refresh()
    	assertNotNull "SqlProcessParameter should have been saved", sqlProcessParameter.id

    	// test date values -
    	assertEquals date.format(today), date.format(sqlProcessParameter.lastModified)
    	assertEquals hour.format(today), hour.format(sqlProcessParameter.lastModified)

    	assertEquals time.format(sqlProcessParameter.startDate), "000000"
    	assertEquals time.format(sqlProcessParameter.endDate), "000000"

    }
	
	
    void testOptimisticLock() {
		def sqlProcessParameter = newValidForCreateSqlProcessParameter()
		sqlProcessParameter.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GTVSQPA set GTVSQPA_VERSION = 999 where GTVSQPA_SURROGATE_ID = ?", [ sqlProcessParameter.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		sqlProcessParameter.description = u_success_description
		sqlProcessParameter.dataType = u_success_dataType
		sqlProcessParameter.startDate = u_success_startDate
		sqlProcessParameter.endDate = u_success_endDate
        shouldFail( HibernateOptimisticLockingFailureException ) {
            sqlProcessParameter.save( failOnError: true, flush: true )
        }
    }
	
	
	void testDeleteSqlProcessParameter() {
		def sqlProcessParameter = newValidForCreateSqlProcessParameter()
		sqlProcessParameter.save( failOnError: true, flush: true )
		def id = sqlProcessParameter.id
		assertNotNull id
		sqlProcessParameter.delete()
		assertNull SqlProcessParameter.get( id )
	}
	
	
    void testValidation() {
       def sqlProcessParameter = newInvalidForCreateSqlProcessParameter()
       assertFalse "SqlProcessParameter could not be validated as expected due to ${sqlProcessParameter.errors}", sqlProcessParameter.validate()
    }
	
	
    void testNullValidationFailure() {
        def sqlProcessParameter = new SqlProcessParameter()
        assertFalse "SqlProcessParameter should have failed validation", sqlProcessParameter.validate()
        assertErrorsFor sqlProcessParameter, 'nullable',
                                               [
                                                 'code',
                                                 'description',
                                                 'dataType',
                                                 'startDate'
                                               ]
        assertNoErrorsFor sqlProcessParameter,
        									   [
             									 'endDate'
											   ]
    }


	private def newValidForCreateSqlProcessParameter() {
		def sqlProcessParameter = new SqlProcessParameter(
			code: i_success_code,
			description: i_success_description,
			dataType: i_success_dataType,
			startDate: i_success_startDate,
			endDate: i_success_endDate,
		)
		return sqlProcessParameter
	}
	
	
	private def newInvalidForCreateSqlProcessParameter() {
		def sqlProcessParameter = new SqlProcessParameter(
			code: i_failure_code,
			description: i_failure_description,
			dataType: i_failure_dataType,
			startDate: i_failure_startDate,
			endDate: i_failure_endDate,
		)
		return sqlProcessParameter
	}

}
