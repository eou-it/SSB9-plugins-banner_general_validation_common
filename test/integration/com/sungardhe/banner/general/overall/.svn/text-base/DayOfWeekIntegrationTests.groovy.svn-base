/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
package com.sungardhe.banner.general.system

import grails.test.GrailsUnitTestCase
import com.sungardhe.banner.testing.BaseIntegrationTestCase 
import groovy.sql.Sql 
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException as OptimisticLock

class DayOfWeekIntegrationTests extends BaseIntegrationTestCase {

	def dayOfWeekService
	
	protected void setUp() {
		formContext = ['SSAEXCL', 'SSASECT', 'STVDAYS'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateDayOfWeek() {
		def dayOfWeek = newDayOfWeek()
		save dayOfWeek
		//Test if the generated entity now has an id assigned		
        assertNotNull dayOfWeek.id
	}

	void testUpdateDayOfWeek() {
		def dayOfWeek = newDayOfWeek()
		save dayOfWeek
       
        assertNotNull dayOfWeek.id
        assertEquals 0L, dayOfWeek.version
        assertEquals "Z", dayOfWeek.code
        assertEquals "TTTTT", dayOfWeek.description
        assertEquals "8", dayOfWeek.number
        assertEquals "Y", dayOfWeek.sysreqIndicator
        
		//Update the entity
		def testDate = new Date()
		dayOfWeek.code = "Y"
		dayOfWeek.description = "UUUUU"
		dayOfWeek.number = "8"
		dayOfWeek.sysreqIndicator = "Y"
		dayOfWeek.lastModified = testDate
		dayOfWeek.lastModifiedBy = "test"
		dayOfWeek.dataOrigin = "Banner" 
        save dayOfWeek
        
        dayOfWeek = DayOfWeek.get( dayOfWeek.id )
        assertEquals 1L, dayOfWeek?.version
        assertEquals "Y", dayOfWeek.code
        assertEquals "UUUUU", dayOfWeek.description
        assertEquals "8", dayOfWeek.number
        assertEquals "Y", dayOfWeek.sysreqIndicator
	}

    void testOptimisticLock() { 
		def dayOfWeek = newDayOfWeek()
		save dayOfWeek
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVDAYS set STVDAYS_VERSION = 999 where STVDAYS_SURROGATE_ID = ?", [ dayOfWeek.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		dayOfWeek.code="Y"
		dayOfWeek.description="UUUUU"
		dayOfWeek.number="8"
		dayOfWeek.sysreqIndicator="Y"
		dayOfWeek.lastModified= new Date()
		dayOfWeek.lastModifiedBy="test"
		dayOfWeek.dataOrigin= "Banner" 
        shouldFail( OptimisticLock ) {
            dayOfWeek.save( flush: true )
        }
    }
	
	void testDeleteDayOfWeek() {
		def dayOfWeek = newDayOfWeek()
		save dayOfWeek
		def id = dayOfWeek.id
		assertNotNull id
		dayOfWeek.delete()
		assertNull DayOfWeek.get( id )
	}
	
    void testValidation() {
       def dayOfWeek = newDayOfWeek()
       assertTrue "DayOfWeek could not be validated as expected due to ${dayOfWeek.errors}", dayOfWeek.validate()
    }

    void testNullValidationFailure() {
        def dayOfWeek = new DayOfWeek()
        assertFalse "DayOfWeek should have failed validation", dayOfWeek.validate()
        assertErrorsFor dayOfWeek, 'nullable', [ 'code', 'number' ]
        assertNoErrorsFor dayOfWeek, [ 'description', 'sysreqIndicator'  ]
    }
    
    void testMaxSizeValidationFailures() {
        def dayOfWeek = new DayOfWeek( 
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        sysreqIndicator:'XXX' )
		assertFalse "DayOfWeek should have failed validation", dayOfWeek.validate()
		assertErrorsFor dayOfWeek, 'maxSize', [ 'description', 'sysreqIndicator' ]    
    }
    
	void testValidationMessages() {
	    def dayOfWeek = newDayOfWeek()
	    
	    dayOfWeek.code = null
	    assertFalse dayOfWeek.validate()
	    assertLocalizedError dayOfWeek, 'nullable', /.*Field.*code.*of class.*DayOfWeek.*cannot be null.*/, 'code'
	    
	    dayOfWeek.number = null
	    assertFalse dayOfWeek.validate()
	    assertLocalizedError dayOfWeek, 'nullable', /.*Field.*number.*of class.*DayOfWeek.*cannot be null.*/, 'number'
	    
	}

    private def newDayOfWeek() {
        new DayOfWeek(
            code: "Z",
            description: "TTTTT",
            number: "8",
            sysreqIndicator: "Y",
            lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner" ) 
    }
    
    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(dayofweek_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
